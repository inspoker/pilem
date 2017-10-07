package com.belajar.posma.retrofitposma.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.belajar.posma.retrofitposma.R;
import com.belajar.posma.retrofitposma.activity.Detail;
import com.belajar.posma.retrofitposma.model.Movie;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

/**
 * Created by Posma on 9/21/2017.
 */

public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Movie> movies;
    private int rowLayout;
    private Context context;
    String Base_URL ="http://image.tmdb.org/t/p/w300";
    String Base_URL2 ="http://image.tmdb.org/t/p/w500";

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_movie, parent, false);
        return new RecyclerView.ViewHolder(view) {
        };

    }

    public void setArray(List<Movie> dList) {
        this.movies = dList;
    }

    public SearchAdapter(Context c) {
        context = c;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        TextView movieTitle         = (TextView) holder.itemView.findViewById(R.id.title);
        TextView movieDescription   = (TextView) holder.itemView.findViewById(R.id.description);
        ImageView movieThumb        = (ImageView) holder.itemView.findViewById(R.id.thumb);
        ImageView movieThumbs        = (ImageView) holder.itemView.findViewById(R.id.imageDrop);
        TextView rating             = (TextView) holder.itemView.findViewById(R.id.rating);

        final RelativeLayout moviesLayout = (RelativeLayout) holder.itemView.findViewById(R.id.movies_layout);


        movieTitle.setText(movies.get(position).getTitle());
        movieDescription.setText(movies.get(position).getOverview());
        rating.setText(" "+movies.get(position).getVoteAverage()+"/10");

        String imageBackdrop = movies.get(position).getBackdropPath();
        String imagePoster = movies.get(position).getPosterPath();

        if (imageBackdrop == null){
            //Sengaja dikosongkan
        }else{
            Glide.with(context).load(Base_URL + movies.get(position).getBackdropPath().toString())
                    .into(movieThumbs);
        }

        if (imagePoster == null){
            //Sengaja Dikosongkan
        }else{
            Glide.with(context).load(Base_URL + movies.get(position).getPosterPath().toString())
                    .into(movieThumb);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id_movies = movies.get(position).getId();
                Bundle bundle = new Bundle();
                Movie movie = movies.get(position);
                bundle.putString("title_mov", ""+movie.getTitle());
                bundle.putString("id_movie", ""+id_movies);
                bundle.putString("background", ""+movie.getBackdropPath());
                bundle.putString("overview", ""+movie.getOverview());
                bundle.putString("budget", ""+movie.getBudget());
                bundle.putString("release_date", ""+movie.getReleaseDate());
                bundle.putString("vote", ""+movie.getVoteAverage());
                bundle.putString("status", ""+movie.getStatus());
                bundle.putString("poster", ""+movie.getPosterPath());

                Intent kirim = new Intent(context, Detail.class);
                kirim.putExtras(bundle);
                context.startActivity(kirim);

                Toast.makeText(context, "Hasil .. ."+id_movies, Toast.LENGTH_SHORT).show();


            }
        });


    }

    @Override
    public int getItemCount() {
        return movies.size();
    }



}
