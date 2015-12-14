package com.amal.imageshare.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.amal.imageshare.Models.SearchEngineResults;
import com.amal.imageshare.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by amal on 12/12/15.
 */
public class GridAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    List<SearchEngineResults> searchEngineResultsList;

    public GridAdapter(Activity activity, List<SearchEngineResults> searchEngineResultsList) {
        this.activity = activity;
        this.searchEngineResultsList = searchEngineResultsList;
    }

    public static class ViewHolder {
        ImageView image;
    }

    @Override
    public int getCount() {
        return searchEngineResultsList.size();
    }

    @Override
    public Object getItem(int position) {
        return searchEngineResultsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = new ViewHolder();
        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.single_column, null);
            holder.image = (ImageView) convertView.findViewById(R.id.share_image);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        Picasso.with(activity).load(searchEngineResultsList.get(position).getThumbnail()).into(holder.image);


        return convertView;
    }
}
