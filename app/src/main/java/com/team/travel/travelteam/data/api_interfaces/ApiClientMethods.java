package com.team.travel.travelteam.data.api_interfaces;

import com.team.travel.travelteam.data.entities.Position;
import com.team.travel.travelteam.data.entities.Route;
import com.team.travel.travelteam.data.entities.User;

import java.util.List;

import retrofit.Callback;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by Jehison on 26/04/2016.
 */
public interface ApiClientMethods {

    @GET("/user/{userName}")
    void findUser(@Path("userName") String userName, Callback<User> cb);

    @GET("/route/{routeId}")
    void findRoute(@Path("routeId") Integer routeId, Callback<Route> cb);

    @GET("/position/{routeId}")
    void findActiveUsersByRouteId(@Path("routeId") Integer routeId, Callback<List<Position>> positions);

    @POST("/user/add")
    void addUser(@Body User user, Callback<User> cb);

    @POST("/route/add")
    void addRoute(@Body Route route, Callback<Route> cb);

    @POST("/position/add")
    void addPosition(@Body Position position, Callback<Position> cb);

    @GET("/position/user/{userName}")
    void findUserActiveRoute(@Path("userName") String userName, Callback<Route> cb);

}
