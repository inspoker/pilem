package com.belajar.posma.retrofitposma.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.belajar.posma.retrofitposma.R;
import com.belajar.posma.retrofitposma.adapter.MoviesAdapter;
import com.belajar.posma.retrofitposma.adapter.SearchAdapter;
import com.belajar.posma.retrofitposma.model.Movie;
import com.belajar.posma.retrofitposma.model.OneFragmentResponse;
import com.belajar.posma.retrofitposma.model.SearchResponse;
import com.belajar.posma.retrofitposma.rest.ApiClient;
import com.belajar.posma.retrofitposma.rest.ApiInterface;
import com.belajar.posma.retrofitposma.util.EndlessRecyclerViewScrollListener;
import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;
import com.github.johnpersano.supertoasts.library.utils.PaletteUtils;
import com.google.gson.Gson;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {
    private static final String TAG = OneFragment.class.getSimpleName();
    View view;
    ProgressDialog pDialog;
    LinearLayoutManager linearLayoutManager;
    Toolbar toolbar;
    protected Context context;
    private List<Movie> mList = new ArrayList<Movie>();
    private List<Movie> mList2 = new ArrayList<Movie>();
    private SearchAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private EndlessRecyclerViewScrollListener scrollListener;
    String QuerY = null;
    RecyclerView recyclerView;
    MaterialSearchView searchView;

    private final static String API_KEY = "0f37e9e0c9d8b63697b69e9bbe09b564";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (API_KEY.isEmpty()) {
            Toast.makeText(getBaseContext(), "Please obtain your API KEY from themoviedb.org first!", Toast.LENGTH_LONG).show();

        }

        recyclerView = (RecyclerView) findViewById(R.id.movies_recycler_view_search);
        mAdapter            = new SearchAdapter(this);
        linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        if(getIntent().getExtras()!=null) {
            /**
             * Jika Bundle ada, ambil data dari Bundle
             */
            Bundle bundle = getIntent().getExtras();
            QuerY = bundle.getString("query");

            getSupportActionBar().setTitle("Result ''"+QuerY+"''");


        }else{
            Toast.makeText(context, "Tidak Ada Bungs.;", Toast.LENGTH_SHORT).show();
        }

        SearchData(QuerY);
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {


            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                EndelssSearch(QuerY, page);
            }
        };

        // Adds the scroll listener to RecyclerView

        recyclerView.setItemAnimator(new SlideInUpAnimator());
        recyclerView.addOnScrollListener(scrollListener);

    }

    private void SearchData(final String m_query){
        class Load extends AsyncTask<String, String, String> {

            String success;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pDialog = new ProgressDialog(SearchActivity.this);
                pDialog.setMessage("Please Wait...");
                pDialog.setIndeterminate(false);
                pDialog.show();
            }

            @Override
            protected String doInBackground(String... params) {
                try {
                    ApiInterface apiService =
                            ApiClient.getClient().create(ApiInterface.class);

                    Call<ResponseBody> call = apiService.getSearch(API_KEY, "en-US", m_query, "1", "false");
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try {
                                JSONObject jsonObj = (JSONObject) new JSONTokener(new String(response.body().bytes())).nextValue();

                                Gson gson = new Gson();
                                SearchResponse responseSuccess = gson.fromJson(jsonObj.toString(), SearchResponse.class);
                                mList2 = responseSuccess.getResults();
                                mList.addAll(mList2);

                                if (mList2 != null && mList2.size() > 0) {
                                    mAdapter.setArray(mList);
                                    recyclerView.setAdapter(mAdapter);

                                } else {
                                    Toast.makeText(getBaseContext(), "Tidak ada data", Toast.LENGTH_LONG).show();
                                }

                            } catch (Exception e2) {
                                Toast.makeText(getBaseContext(), e2.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            // Log error here since request failed
                            Toast.makeText(getBaseContext(), "Fetch data gagal!", Toast.LENGTH_LONG).show();
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
                return null;
            }

            protected void onPostExecute(String file_url) {
                pDialog.dismiss();
            }
        }
        new Load().execute();
    }

    private void EndelssSearch(String m_query, int page){
                try {
                    SuperActivityToast.create(this, new Style(), Style.TYPE_PROGRESS_CIRCLE)
                            .setProgressBarColor(Color.WHITE)
                            .setText("Loading . . .")
                            .setDuration(Style.DURATION_LONG)
                            .setFrame(Style.FRAME_STANDARD)
                            .setColor(PaletteUtils.getSolidColor(PaletteUtils.BLACK))
                            .setAnimations(Style.ANIMATIONS_POP).show();

                    ApiInterface apiService =
                            ApiClient.getClient().create(ApiInterface.class);

                    Call<ResponseBody> call = apiService.getSearch(API_KEY, "en-US", m_query, page+1+"", "false");
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try {
                                JSONObject jsonObj = (JSONObject) new JSONTokener(new String(response.body().bytes())).nextValue();

                                Gson gson = new Gson();
                                SearchResponse responseSuccess = gson.fromJson(jsonObj.toString(), SearchResponse.class);
                                mList2 = responseSuccess.getResults();
                                mList.addAll(mList2);

                                if (mList2 != null && mList2.size() > 0) {
                                    mAdapter.setArray(mList);
                                    mAdapter.notifyDataSetChanged();

                                } else {
                                    Toast.makeText(getBaseContext(), "Tidak ada data", Toast.LENGTH_LONG).show();
                                }

                            } catch (Exception e2) {
                                Toast.makeText(getBaseContext(), e2.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            // Log error here since request failed
                            Toast.makeText(getBaseContext(), "Fetch data gagal!", Toast.LENGTH_LONG).show();
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }

    }





}
