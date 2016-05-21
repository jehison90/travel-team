package com.team.travel.travelteam.data.api_interfaces;

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

    @GET("/position/{routeId}")
    void findActiveUsersByRouteId(@Path("routeId") Integer routeId, Callback<List> positions);

    @POST("/user/add")
    void addUser(@Header("Content-Type") String content_type, @Body User user, Callback<User> cb);

}
