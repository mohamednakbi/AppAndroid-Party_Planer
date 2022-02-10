package com.example.partyplanner.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class retrofit {
    private static Retrofit retrofit;

    public static  Retrofit getInstance() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.38.47:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit ;
    }
}