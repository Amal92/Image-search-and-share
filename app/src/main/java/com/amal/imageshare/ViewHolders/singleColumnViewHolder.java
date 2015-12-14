package com.amal.imageshare.ViewHolders;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.amal.imageshare.Models.SearchEngineResults;
import com.amal.imageshare.R;
import com.amal.imageshare.Utils.Const;
import com.amal.imageshare.app.AppController;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by amal on 13/12/15.
 */
public class singleColumnViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView imageView;
    public SearchEngineResults searchEngineResults;
    private Context context = AppController.getInstance();

    public singleColumnViewHolder(View itemView) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.share_image);
        imageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.share_image:
                Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                share_image(bitmap, Const.CACHED_DIRECTORY_NAME);
                break;
        }
    }

    private void share_image(Bitmap bitmap, String fileName) {
        try {
            File file = new File(context.getCacheDir(), fileName + ".png");
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true, false);
            final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.setType("image/png");
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
