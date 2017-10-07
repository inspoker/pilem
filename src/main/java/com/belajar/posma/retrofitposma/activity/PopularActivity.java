package com.belajar.posma.retrofitposma.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.belajar.posma.retrofitposma.R;
import com.belajar.posma.retrofitposma.adapter.MoviesAdapter;
import com.belajar.posma.retrofitposma.model.Movie;
import com.belajar.posma.retrofitposma.model.MoviesResponse;
import com.belajar.posma.retrofitposma.rest.ApiClient;
import com.belajar.posma.retrofitposma.rest.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Posma on 9/18/2017.
 */

public class PopularActivity extends Fragment {
    private static final String TAG = PopularActivity.class.getSimpleName();
    LinearLayoutManager linearLayoutManager;
    protected Context context;

    // TODO - insert your themoviedb.org API KEY here
    private final static String API_KEY = "0f37e9e0c9d8b63697b69e9bbe09b564";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_main, container, false);
        if (API_KEY.isEmpty()) {
            Toast.makeText(getContext(), "Please obtain your API KEY from themoviedb.org first!", Toast.LENGTH_LONG).show();
            return view;
        }

        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.movies_recycler_view);
        linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager );

/*
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<MoviesResponse> call = apiService.getTopRatedMovies(API_KEY, "en-US", "1");
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                int statusCode = response.code();  List<Movie> movies = response.body().getResults();
                recyclerView.setAdapter(new MoviesAdapter(movies, R.layout.list_item_movie, getContext()));
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });
*/
        return  view;

    }

}
