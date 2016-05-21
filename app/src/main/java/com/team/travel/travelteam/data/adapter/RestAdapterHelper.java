package com.team.travel.travelteam.data.adapter;

import com.team.travel.travelteam.data.api_interfaces.ApiClientMethods;

import retrofit.RestAdapter;

/**
 * Created by Jehison on 18/05/2016.
 */
public class RestAdapterHelper {

    private static final String API_URL = "https://travel-team-api.herokuapp.com";

    private static RestAdapter restAdapter;

    private static ApiClientMethods apiClientMethods;

    public static RestAdapter getRestAdapter(){
        if(restAdapter == null){
            restAdapter = new RestAdapter.Builder().setEndpoint(API_URL).build();
        }
        return restAdapter;
    }

    public static ApiClientMethods getApiClientMethods(){
        if(apiClientMethods == null){
            apiClientMethods = getRestAdapter().create(ApiClientMethods.class);
        }
        return apiClientMethods;
    }

}
