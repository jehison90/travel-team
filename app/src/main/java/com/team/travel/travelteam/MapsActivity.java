package com.team.travel.travelteam;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.team.travel.travelteam.dialogs.CreateRouteDialog;
import com.team.travel.travelteam.dialogs.JoinRouteDialog;
import com.team.travel.travelteam.dialogs.RegisterDialog;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    double latitud = 0;
    double longitud = 0;

    private FloatingActionButton fabAddRoute;
    private FloatingActionButton fabJoinRoute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

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

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void renderCreateRouteDialog(){
        final CreateRouteDialog createRouteDialog = new CreateRouteDialog(this);
        createRouteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        createRouteDialog.show();
    }

    private void renderJoinRouteDialog(){
        final JoinRouteDialog joinRouteDialog = new JoinRouteDialog(this);
        joinRouteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        joinRouteDialog.show();
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


                    double lat = location.getLatitude();
                    double lon = location.getLongitude();

                    CameraUpdate cam = CameraUpdateFactory.newLatLng(new LatLng(lat, lon));
                    mMap.moveCamera(cam);

                    mMap.clear();

                    mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(lat + latitud + 0.001, lon + longitud + 0.001))
                            .title("Andres Guataquira")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.moto_icon)));

                    mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(lat + latitud + 0.002, lon + longitud + 0.002))
                            .title("Jehison Prada")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.moto_icon_alerta)));

                    latitud =+ 0.003;
                    longitud =+ 0.0003;
                }
            });
        }
        catch (SecurityException e)
        {

        }
        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("You are here"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
