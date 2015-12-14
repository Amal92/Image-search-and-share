package com.amal.imageshare.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amal.imageshare.Models.SearchEngineResults;
import com.amal.imageshare.R;
import com.amal.imageshare.ViewHolders.singleColumnViewHolder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.List;

/**
 * Created by amal on 13/12/15.
 */
public class StaggeredGridAdapter extends RecyclerView.Adapter<com.amal.imageshare.ViewHolders.singleColumnViewHolder> {

    List<SearchEngineResults> searchEngineResultsList;
    private Context context;

    public StaggeredGridAdapter(Context context, List<SearchEngineResults> searchEngineResultsList) {
        this.context = context;
        this.searchEngineResultsList = searchEngineResultsList;
    }

    @Override
    public singleColumnViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_column, null);
        singleColumnViewHolder singlecolumnviewholder = new singleColumnViewHolder(layoutView);
        return singlecolumnviewholder;
    }

    @Override
    public void onBindViewHolder(final singleColumnViewHolder holder, final int position) {

        holder.searchEngineResults = searchEngineResultsList.get(position);
        Glide.with(context).load(searchEngineResultsList.get(position).getThumbnail()).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                holder.imageView.setImageBitmap(bitmap);
            }
        });

    }

    @Override
    public int getItemCount() {
        return this.searchEngineResultsList.size();
    }
}