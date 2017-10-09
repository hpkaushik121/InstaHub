package com.example.anitesh.photocon;

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
import com.example.anitesh.photocon.FullImageAcitivity;
import com.example.anitesh.photocon.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class chatadapter2 extends BaseAdapter {

    private Context mContext;
    List<String> chatuser,image,chatm;

    public chatadapter2(Context mContext, List chatu, List img, List msg) {
        this.mContext = mContext;
        this.chatuser = chatu;
        this.image =img;
        chatm = msg;

    }

    @Override
    public int getCount() {
        return chatm.size();
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
            grid = inflater.inflate(R.layout.activity_chatadapter2, null);
            TextView user = (TextView) grid.findViewById(R.id.textView10);
            CircleImageView imgee = (CircleImageView) grid.findViewById(R.id.view);
            TextView message = (TextView) grid.findViewById(R.id.textView11);
            Glide
                    .with(mContext)
                    .load(image.get(position))
                    .into(imgee);

            user.setText(chatuser.get(position));

            message.setText(chatm.get(position));
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