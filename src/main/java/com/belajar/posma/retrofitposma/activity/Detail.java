package com.belajar.posma.retrofitposma.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.ToolbarWidgetWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.belajar.posma.retrofitposma.R;
import com.belajar.posma.retrofitposma.adapter.CastAdapter;
import com.belajar.posma.retrofitposma.adapter.MoviesAdapter;
import com.belajar.posma.retrofitposma.model.Credits;
import com.belajar.posma.retrofitposma.model.CreditsResponse;
import com.belajar.posma.retrofitposma.model.DetailMovieResponse;
import com.belajar.posma.retrofitposma.model.Movie;
import com.belajar.posma.retrofitposma.model.OneFragmentResponse;
import com.belajar.posma.retrofitposma.rest.ApiClient;
import com.belajar.posma.retrofitposma.rest.ApiInterface;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.google.gson.Gson;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class Detail extends AppCompatActivity {


    LinearLayoutManager linearLayoutManager;
    private Context context;
    TextView textHompage;

    private CastAdapter mAdapter;
    private List<Credits> mList = new ArrayList<Credits>();
    private List<Credits> mList2 = new ArrayList<Credits>();
    String Base_URL ="http://image.tmdb.org/t/p/w500";
    String Base_URL2 ="http://image.tmdb.org/t/p/w300";
    ImageView MovieImage, PosterImage;
    TextView Title_txt, Tagline_txt, Overeview_txt, Kategori_txt, Rate_txt, Release_txt, Runtime_txt_,Tahun_txt;
    private ProgressDialog mProgress;
    boolean mIsLoading = false;
    private DetailMovieResponse responseDetail;
    private final static String API_KEY = "0f37e9e0c9d8b63697b69e9bbe09b564";
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);
        MovieImage = (ImageView) findViewById(R.id.imageMovie);
        PosterImage = (ImageView) findViewById(R.id.imagePoster);
        textHompage = (TextView) findViewById(R.id.txtHompage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Title_txt       = (TextView) findViewById(R.id.txtTitle);
        Tahun_txt       = (TextView) findViewById(R.id.tahun);
        Tagline_txt     = (TextView) findViewById(R.id.txtTagline);
        Overeview_txt   = (TextView) findViewById(R.id.txt_Overview);
        Kategori_txt    = (TextView) findViewById(R.id.txtKategori);
        Rate_txt        = (TextView) findViewById(R.id.txtRating);
        Release_txt     = (TextView) findViewById(R.id.txtRelease);
        Runtime_txt_    = (TextView) findViewById(R.id.txtRuntime);
        mProgress	    = new ProgressDialog(Detail.this);
        mAdapter        = new CastAdapter(this);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        recyclerView    =  (RecyclerView) findViewById(R.id.recCredits);
        recyclerView.setLayoutManager(linearLayoutManager);



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getBundle();

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    private void getBundle(){

        if(getIntent().getExtras()!=null){
            /**
             * Jika Bundle ada, ambil data dari Bundle
             */
            Bundle bundle = getIntent().getExtras();
            String title = bundle.getString("title_mov");
            String id_movies = bundle.getString("id_movie");
            String tagline = bundle.getString("tagline");
            String overview = bundle.getString("overview");
            String kategori = bundle.getString("overview");
            String rate = bundle.getString("vote");
            String release = bundle.getString("release_date");

            Tagline_txt.setText(""+tagline);
            Title_txt.setText(""+title);
            Overeview_txt.setText(""+overview);
            Rate_txt.setText(""+rate);
            Release_txt.setText(""+release);

           // DateFormat dateFormat = new SimpleDateFormat("yyyy");

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
            try {
                Date tgl = sdf.parse(release);
                Tahun_txt.setText(""+ sdf.format(tgl));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            MovieImage = (ImageView) findViewById(R.id.imageMovie);
            getSupportActionBar().setTitle(title);
            Glide.with(this).load(Base_URL +  bundle.getString("background"))
                    .thumbnail(0.5f)
                    .into(MovieImage);

            try{

                mIsLoading = true;
                mProgress.setMessage("Load Data ...");
                mProgress.setCanceledOnTouchOutside(false);
                mProgress.show();

                ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

                //Get Detail Movie
                Call<ResponseBody> call = apiService.getDetailMovie(id_movies, API_KEY);
                call.enqueue(new Callback<ResponseBody>() {


                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        mProgress.dismiss();
                        try{
                            JSONObject jsonObject = (JSONObject) new JSONTokener(new String(response.body().bytes())).nextValue();
                            Gson gson = new Gson();
                            responseDetail = gson.fromJson(jsonObject.toString(),DetailMovieResponse.class);
                            Runtime_txt_.setText(""+responseDetail.getRuntime()+"/mins");
                            Tagline_txt.setText(""+responseDetail.getTagline());
                            String hompage = responseDetail.getHomepage().toString();
                            if(hompage.equals("")){
                                textHompage.setText("Not availabel web page . . .");
                            }else{
                                textHompage.setText(""+responseDetail.getHomepage());
                                textHompage.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Uri uriUrl = Uri.parse(responseDetail.getHomepage().toString());
                                        Intent browse_intent = new Intent(Intent.ACTION_VIEW, uriUrl);
                                        startActivity(browse_intent);

                                    }
                                });
                            }

                            StringBuilder genre = new StringBuilder();

                            for (int i = 0; i < responseDetail.getGenres().size(); i++) {
                                if (i == 0){
                                    genre.append(responseDetail.getGenres().get(i).getName());
                                }else{
                                    genre.append(", "+responseDetail.getGenres().get(i).getName());
                                }
                            }



                            Kategori_txt.setText(genre);

                            PosterImage = (ImageView) findViewById(R.id.imagePoster);
                            Glide.with(Detail.this).load(Base_URL +responseDetail.getPoster_path())
                                    .thumbnail(0.5f)
                                    .apply(bitmapTransform(new RoundedCornersTransformation(10, 0,
                                            RoundedCornersTransformation.CornerType.LEFT)))
                                    .into(PosterImage);

                        }catch(Exception e2) {
                            Toast.makeText(getApplicationContext(), e2.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        mProgress.dismiss();
                        Toast.makeText(getApplicationContext(),"Fetch data gagal!",Toast.LENGTH_LONG).show();
                    }
                });

                /*======================== = Mengambil Data Cast = =======================*/

                Call<ResponseBody> callCredit = apiService.getCredits(id_movies, API_KEY);
                callCredit.enqueue(new Callback<ResponseBody>() {


                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        mProgress.dismiss();
                        try{
                            JSONObject jsonObject = (JSONObject) new JSONTokener(new String(response.body().bytes())).nextValue();
                            Gson gson = new Gson();
                            CreditsResponse responseSuccess = gson.fromJson(jsonObject.toString(), CreditsResponse.class);

                            mList2 = responseSuccess.getCast();
                            mList.addAll(mList2);

                            if (mList2 != null && mList2.size() > 0) {
                                mAdapter.setArray(mList);
                                recyclerView.setAdapter(mAdapter);
                            } else {
                                Toast.makeText(getBaseContext(), "Tidak ada data Cast", Toast.LENGTH_LONG).show();
                            }

                        }catch(Exception e2) {
                            Toast.makeText(getApplicationContext(), e2.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        mProgress.dismiss();
                        Toast.makeText(getApplicationContext(),"Fetch Credit Gagal!",Toast.LENGTH_LONG).show();
                    }
                });

                    }catch (Exception e) {
                        mProgress.dismiss();
                        mIsLoading=false;
                        e.printStackTrace();
            }




        }else{

        }
    }

}
