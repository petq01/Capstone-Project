package com.petya.build.xkcdcomics.api;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.petya.build.xkcdcomics.api.ComicAPI;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Petya Marinova on 8/1/2018.
 */
public class RetrofitRequest {
    public static ComicAPI createAPI() {
        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ComicAPI.BASE_URL)
                .build();

        return retrofit.create(ComicAPI.class);
    }
}
