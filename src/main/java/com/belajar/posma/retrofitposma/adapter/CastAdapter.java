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
import com.belajar.posma.retrofitposma.model.Credits;
import com.belajar.posma.retrofitposma.model.DetailMovieResponse;
import com.belajar.posma.retrofitposma.model.Movie;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * Created by Posma on 9/22/2017.
 */

public class CastAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Credits> cast;
    private int rowLayout;
    private Context context;

    String Base_URL ="http://image.tmdb.org/t/p/w300";
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cast, parent, false);
        return new RecyclerView.ViewHolder(view) {
        };

    }

    public void setArray(List<Credits> dList) {
        this.cast = dList;
    }

    public CastAdapter(Context c) {
        context = c;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        TextView name = (TextView) holder.itemView.findViewById(R.id.textName);
        TextView mCast = (TextView) holder.itemView.findViewById(R.id.textCharacter);
        ImageView profile = (ImageView) holder.itemView.findViewById(R.id.imageProfile);
        mCast.setText(cast.get(position).getCharacter().toString());
        name.setText(""+cast.get(position).getName());
        Glide.with(context)
                .load(Base_URL + cast.get(position).getProfile_path())
                .apply(bitmapTransform(new CropCircleTransformation()))
                .into(profile);



    }

    @Override
    public int getItemCount() {
        return cast.size();
    }

}
