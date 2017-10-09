package com.example.anitesh.photocon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import static android.support.v4.app.ActivityCompat.startActivity;
import static com.example.anitesh.photocon.searchFragment.ImageUrls;

/**
 * Created by Anitesh on 1/15/2017.
 */
public class CustomGridAdapter extends BaseAdapter {

    private Context mContext;
    List<String> ImageUrl,caption,fileid,dpurl;

    public CustomGridAdapter(Context mContext, List web,List fileids, List imageid) {
        this.mContext = mContext;
        this.caption = web;
        this.fileid =fileids;
        ImageUrl = imageid;

    }

    @Override
    public int getCount() {
        return ImageUrl.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            grid = new View(mContext);
            grid = inflater.inflate(R.layout.grid_image, null);
            TextView textView = (TextView) grid.findViewById(R.id.grid_text);
            ImageView imageView = (ImageView)grid.findViewById(R.id.grid_image);
            Glide
                    .with(mContext)
                    .load(ImageUrl.get(position))
                    .error(R.drawable.camera)
                    .thumbnail(Glide.with(mContext).load(R.drawable.ring))
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
            textView.setText(caption.get(position));
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(mContext, FullImageAcitivity.class);
                    i.putExtra("id",ImageUrl.get(position));
                    mContext.startActivity(i);
                }
            });
        } else {
            grid = convertView;
        }

        return grid;
    }



    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
