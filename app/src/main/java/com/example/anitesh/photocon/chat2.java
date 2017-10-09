package com.example.anitesh.photocon;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import static com.example.anitesh.photocon.R.layout.activity_chat;
import static com.example.anitesh.photocon.R.layout.fragment_search;

public class chat2 extends Fragment {
    View v;
    ListView cht;
    ActionBar act1;
    List<String> chatmsg,usern,userimage;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.activity_chat2, container, false);
        act1=((AppCompatActivity)getActivity()).getSupportActionBar();
        act1.setDisplayShowCustomEnabled(false);
        cht =(ListView) v.findViewById(R.id.chats);
        chatmsg =new ArrayList<>();
        usern = new ArrayList<>();
        userimage = new ArrayList<>();


        return v;
    }
}
