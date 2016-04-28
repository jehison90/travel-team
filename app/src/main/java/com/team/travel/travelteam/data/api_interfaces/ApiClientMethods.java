package com.team.travel.travelteam.data.api_interfaces;

import android.content.pm.LauncherApps;

import com.team.travel.travelteam.data.entities.User;

import retrofit.Callback;

import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by Jehison on 26/04/2016.
 */
public interface ApiClientMethods {

    @GET("/user/{userName}")
    void findUser(@Path("userName") String userName, Callback<User> cb);

}
