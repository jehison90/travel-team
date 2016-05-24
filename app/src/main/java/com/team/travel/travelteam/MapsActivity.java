package com.team.travel.travelteam;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.team.travel.travelteam.data.adapter.RestAdapterHelper;
import com.team.travel.travelteam.data.entities.Position;
import com.team.travel.travelteam.data.entities.PositionPk;
import com.team.travel.travelteam.data.entities.Route;
import com.team.travel.travelteam.data.entities.User;
import com.team.travel.travelteam.dialogs.CreateRouteDialog;
import com.team.travel.travelteam.dialogs.JoinRouteDialog;
import com.team.travel.travelteam.dialogs.ProgressDialogUtility;

import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    double latitud = 0;
    double longitud = 0;

    public final static String LOGGED_USER_KEY = "loggedUser";
    private User loggerUser = null;

    private FloatingActionButton fabAddRoute;
    private FloatingActionButton fabJoinRoute;

    private TextView tvRouteId;

    private Route actualRoute = null;

    private List<Position> usersInRoute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        tvRouteId = (TextView) findViewById(R.id.tvRouteId);

        fabAddRoute = (FloatingActionButton) findViewById(R.id.fab_add_route);
        fabJoinRoute = (FloatingActionButton) findViewById(R.id.fab_join_route);

        fabAddRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                renderCreateRouteDialog();
            }
        });

        fabJoinRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                renderJoinRouteDialog();
            }
        });

        loggerUser = (User) getIntent().getExtras().getSerializable(LOGGED_USER_KEY);

        //Forcing reinitialization of loading Dialog
        ProgressDialogUtility.setContext(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void renderCreateRouteDialog(){
        final CreateRouteDialog createRouteDialog = new CreateRouteDialog(this);
        createRouteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        createRouteDialog.show();
        final Button createButton = (Button) createRouteDialog.findViewById(R.id.createRoute);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialogUtility.showProgressDialog();
                final String initialPlace = ((EditText) createRouteDialog.findViewById(R.id.etRouteInitialPlace)).getText().toString();
                final String finalPlace = ((EditText) createRouteDialog.findViewById(R.id.etRouteFinalPlace)).getText().toString();
                Route route = new Route();
                route.setInitialDateTime(new Date());
                route.setInitialPlaceDescription(initialPlace);
                route.setFinalPlaceDescription(finalPlace);
                createRoute(route, createRouteDialog);
            }
        });
    }

    private void createRoute(Route routeToSave, final CreateRouteDialog parentView){
        RestAdapterHelper.getApiClientMethods().addRoute(routeToSave, new Callback<Route>() {
            @Override
            public void success(Route route, Response response) {
                actualRoute = route;
                tvRouteId.setText("In Route " + actualRoute.getId());
                tvRouteId.setVisibility(View.VISIBLE);

                saveOrUpdatePosition();

                ProgressDialogUtility.dismissProgressDialog();
                parentView.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                ProgressDialogUtility.dismissProgressDialog();
                Toast.makeText(getApplicationContext(), "Error creating a route " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void renderJoinRouteDialog(){
        final JoinRouteDialog joinRouteDialog = new JoinRouteDialog(this);
        joinRouteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        joinRouteDialog.show();
        final Button joinRouteButton = (Button) joinRouteDialog.findViewById(R.id.joinRouteButton);
        joinRouteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialogUtility.showProgressDialog();
                Integer routeId = Integer.parseInt(((EditText) joinRouteDialog.findViewById(R.id.etRouteId)).getText().toString());
                RestAdapterHelper.getApiClientMethods().findRoute(routeId, new CallBackFindRoute(routeId, joinRouteDialog));
            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        CameraUpdate ZoomCam = CameraUpdateFactory.zoomTo(15);
        mMap.animateCamera(ZoomCam);

        try {
            mMap.setMyLocationEnabled(true);
            mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                @Override
                public void onMyLocationChange(Location location) {

                    //consumir el servicio para guardar la posicion actual.
                    //Consultar los demas usuarios en ruta.
                    // graficar posicion actual y usuarios.


                    latitud = location.getLatitude();
                    longitud = location.getLongitude();

                    saveOrUpdatePosition();

                    CameraUpdate cam = CameraUpdateFactory.newLatLng(new LatLng(latitud, longitud));
                    mMap.moveCamera(cam);

                    mMap.clear();

                    if(actualRoute != null){
                        RestAdapterHelper.getApiClientMethods().findActiveUsersByRouteId(actualRoute.getId(), new Callback<List<Position>>() {
                            @Override
                            public void success(List<Position> positions, Response response) {
                                usersInRoute = positions;
                            }

                            @Override
                            public void failure(RetrofitError error) {

                            }
                        });
                    }

                    DrawPositionByUser();

                }
            });
        }
        catch (SecurityException e)
        {

        }
    }

    private void DrawPositionByUser() {
        if(usersInRoute != null && !usersInRoute.isEmpty()){
            for (Position item : usersInRoute) {
                if(item.isNormalPosition()) {
                    mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(item.getLastX(), item.getLastY()))
                            .title(item.getUser().getUser())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.moto_icon)));
                }
                else {
                    mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(item.getLastX(), item.getLastY()))
                            .title(item.getUser().getUser())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.moto_icon_alerta)));
                }
            }
        }
    }

    private void saveOrUpdatePosition(){
        if(actualRoute != null && loggerUser != null){
            Position position = new Position();

            PositionPk positionPk = new PositionPk();
            positionPk.setRouteId(actualRoute.getId());
            positionPk.setUserName(loggerUser.getUser());
            position.setPositionPk(positionPk);

            position.setUser(loggerUser);
            position.setRoute(actualRoute);

            position.setActive(true);
            position.setLastUpdated(new Date());
            position.setNormalPosition(true);

            position.setLastX(latitud);
            position.setLastY(longitud);

            RestAdapterHelper.getApiClientMethods().addPosition(position, new Callback<Position>() {
                @Override
                public void success(Position position, Response response) {

                }

                @Override
                public void failure(RetrofitError error) {

                }
            });
        }
    }

    private class CallBackFindRoute implements Callback<Route>{

        Integer routeId;
        JoinRouteDialog parentView;

        public CallBackFindRoute(Integer routeId, JoinRouteDialog parentView) {
            this.routeId = routeId;
            this.parentView = parentView;
        }


        @Override
        public void success(Route route, Response response) {
            if(route != null){
                actualRoute = route;
                saveOrUpdatePosition();
                ProgressDialogUtility.dismissProgressDialog();
                parentView.dismiss();
            } else{
                Toast.makeText(getApplicationContext(), "Route " + routeId + " wasn't found", Toast.LENGTH_LONG).show();
                ProgressDialogUtility.dismissProgressDialog();
            }
        }

        @Override
        public void failure(RetrofitError error) {
            Toast.makeText(getApplicationContext(), "Error trying to find the specified route " + routeId + " " + error.getMessage(), Toast.LENGTH_LONG).show();
            ProgressDialogUtility.dismissProgressDialog();
        }
    }

}
