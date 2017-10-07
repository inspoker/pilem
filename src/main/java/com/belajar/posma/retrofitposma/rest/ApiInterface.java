package com.belajar.posma.retrofitposma.rest;

import com.belajar.posma.retrofitposma.model.MoviesResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Posma on 8/1/2017.
 */

public interface ApiInterface {
    @GET("movie/popular")
    Call<ResponseBody> getTopRatedMovies(@Query("api_key") String apiKey, @Query("language") String language, @Query("page") String page);

    @GET("movie/upcoming")
    Call<ResponseBody> getUpcoming(@Query("api_key") String apiKey, @Query("language") String language, @Query("page") String page);

    @GET("movie/top_rated ")
    Call<ResponseBody> getToprated(@Query("api_key") String apiKey, @Query("language") String language, @Query("page") String page);

    @GET("movie/now_playing")
    Call<ResponseBody> getNowplaying(@Query("api_key") String apiKey, @Query("language") String language, @Query("page") String page);

    @GET("movie/latest")
    Call<ResponseBody> getLast(@Query("api_key") String apiKey, @Query("language") String language);

    @GET("search/movie")
    Call<ResponseBody> getSearch(@Query("api_key") String apiKey,
                                 @Query("language") String language,
                                 @Query("query") String query,
                                 @Query("page") String page,
                                 @Query("include_adult") String include_adult
    );

    @GET("movie/{id_movie}/credits")
    Call<ResponseBody> getCredits(@Path("id_movie") String id_movie, @Query("api_key") String api_key);

    @GET("movie/{id}")
    Call<ResponseBody> getDetailMovie(@Path("id") String id,
                                      @Query("api_key") String api_key);
}