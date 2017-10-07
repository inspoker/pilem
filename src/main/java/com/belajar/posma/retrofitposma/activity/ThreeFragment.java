package com.belajar.posma.retrofitposma.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.belajar.posma.retrofitposma.R;
import com.belajar.posma.retrofitposma.adapter.MoviesAdapter;
import com.belajar.posma.retrofitposma.model.Movie;
import com.belajar.posma.retrofitposma.model.MoviesResponse;
import com.belajar.posma.retrofitposma.model.OneFragmentResponse;
import com.belajar.posma.retrofitposma.rest.ApiClient;
import com.belajar.posma.retrofitposma.rest.ApiInterface;
import com.belajar.posma.retrofitposma.util.EndlessRecyclerViewScrollListener;
import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;
import com.github.johnpersano.supertoasts.library.utils.PaletteUtils;
import com.google.gson.Gson;
import com.pnikosis.materialishprogress.ProgressWheel;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ThreeFragment extends Fragment {
    private static final String TAG = ThreeFragment.class.getSimpleName();
    View view;
    LinearLayoutManager linearLayoutManager;
    ProgressWheel progressWheel;
    protected Context context;
    private List<Movie> mList = new ArrayList<Movie>();
    private List<Movie> mList2 = new ArrayList<Movie>();
    private MoviesAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private EndlessRecyclerViewScrollListener scrollListener;
    RecyclerView recyclerView;
    private final static String API_KEY = "0f37e9e0c9d8b63697b69e9bbe09b564";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);

        if (API_KEY.isEmpty()) {
            Toast.makeText(getContext(), "Please obtain your API KEY from themoviedb.org first!", Toast.LENGTH_LONG).show();
            return view;
        }
        progressWheel       = (ProgressWheel) view.findViewById(R.id.progress_wheel);
        recyclerView        = (RecyclerView) view.findViewById(R.id.movies_recycler_view);
        mAdapter            = new MoviesAdapter(getActivity());
        linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);

        PopularMovies();

        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {


            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                PopularMoviesEndless(page);
            }
        };

        // Adds the scroll listener to RecyclerView
        recyclerView.addOnScrollListener(scrollListener);
        return  view;
    }

    private void PopularMovies(){
        try {
            progressWheel.setVisibility(View.VISIBLE);
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);

            Call<ResponseBody> call = apiService.getUpcoming(API_KEY, "en-US", "1");
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        JSONObject jsonObj = (JSONObject) new JSONTokener(new String(response.body().bytes())).nextValue();

                        Gson gson = new Gson();
                        OneFragmentResponse responseSuccess = gson.fromJson(jsonObj.toString(), OneFragmentResponse.class);
                        mList2 = responseSuccess.getResults();
                        mList.addAll(mList2);

                        if (mList2 != null && mList2.size() > 0) {
                            mAdapter.setArray(mList);
                            recyclerView.setAdapter(mAdapter);

                            progressWheel.setVisibility(View.GONE);

                        } else {

                            progressWheel.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), "Tidak ada data", Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception e2) {
                        progressWheel.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), e2.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                    progressWheel.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Fetch data gagal!", Toast.LENGTH_LONG).show();
                }
            });
        }catch (Exception e){
            progressWheel.setVisibility(View.GONE);
            e.printStackTrace();
        }

    }

    private void PopularMoviesEndless(int page){
        SuperActivityToast.create(getActivity(), new Style(), Style.TYPE_PROGRESS_CIRCLE)
                .setProgressBarColor(Color.WHITE)
                .setText("Loading . . .")
                .setDuration(Style.DURATION_LONG)
                .setFrame(Style.FRAME_STANDARD)
                .setColor(PaletteUtils.getSolidColor(PaletteUtils.BLACK))
                .setAnimations(Style.ANIMATIONS_POP).show();
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<ResponseBody> call = apiService.getUpcoming(API_KEY,"en-US",page+1+ "");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject jsonObj = (JSONObject) new JSONTokener(new String(response.body().bytes())).nextValue();

                    Gson gson = new Gson();
                    OneFragmentResponse responseSuccess = gson.fromJson(jsonObj.toString(), OneFragmentResponse.class);
                    mList2 =  responseSuccess.getResults();
                    mList.addAll(mList2);

                    if(mList2 != null && mList2.size() > 0) {
                        mAdapter.setArray(mList);
                        mAdapter.notifyDataSetChanged();

                    }else {
                        Toast.makeText(getActivity(),"Tidak ada data",Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e2) {
                    Toast.makeText(getActivity(), e2.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Log error here since request failed
                Toast.makeText(getActivity(),"Fetch data gagal!",Toast.LENGTH_LONG).show();
            }
        });

    }

    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }

    // Showing the status in Snackbar
    private void showSnack(boolean isConnected) {
        String message;
        Snackbar snackbar;
        int color;
        if (isConnected) {

        } else {
            message = "Sorry! Not connected to internet";
            color = Color.WHITE;
            snackbar = Snackbar
                    .make(view.findViewById(R.id.fab), message, Snackbar.LENGTH_INDEFINITE);
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(color);
            snackbar.show();
        }
    }

}
