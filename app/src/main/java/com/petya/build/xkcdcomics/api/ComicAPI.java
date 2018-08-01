package com.petya.build.xkcdcomics.api;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Petya Marinova on 7/24/2018.
 */
public interface ComicAPI {

    String BASE_URL = "http://xkcd.com/";

    @GET("{comic_id}/info.0.json")
    Observable<ComicResponse> getComicById(
            @Path("comic_id") Integer comic_id);

    @GET("info.0.json")
    Observable<ComicResponse> getCurrentComic();
}
