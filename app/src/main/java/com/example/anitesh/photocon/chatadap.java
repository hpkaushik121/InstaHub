package com.example.anitesh.photocon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.github.library.bubbleview.BubbleTextView;

import java.util.List;

/**
 * Created by sourabh on 8/20/2017.
 */

public class chatadap extends BaseAdapter {
    private List<chatmodal> chats;
    private Context cont;
    private LayoutInflater lay;

    public chatadap(List<chatmodal> chats, Context cont) {
        this.chats = chats;
        this.cont = cont;
        lay=(LayoutInflater)cont.getSystemService(cont.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return chats.size();
    }

    @Override
    public Object getItem(int position) {
        return chats.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v= convertView;
        if(v==null){
            if(chats.get(position).Send1()) {
                v = lay.inflate(R.layout.massege_recieve, null);
            }
            else {
                v = lay.inflate(R.layout.massege_send, null);
            }
            BubbleTextView txt = (BubbleTextView) v.findViewById(R.id.msgrcv);
            txt.setText(chats.get(position).msg);
        }
        return v;
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
