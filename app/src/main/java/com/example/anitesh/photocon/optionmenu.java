package com.example.anitesh.photocon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class optionmenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_optionmenu);
        ImageView img = (ImageView) findViewById(R.id.imageView);
        TextView txtvw = (TextView) findViewById(R.id.username9);
        TextView tv = (TextView) findViewById(R.id.textView55);
        TextView txtv =  (TextView) findViewById(R.id.textView6);
        txtvw.setText(UserInfo.username);
        tv.setText(UserInfo.email);
        Glide
                .with(optionmenu.this)
                .load("http://photocone.com/photos/"+UserInfo.dp)
                .thumbnail(Glide.with(optionmenu.this).load(R.drawable.ring))
                .fitCenter()
                .into(img);
        txtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inte = new Intent(optionmenu.this,editprofile.class);
                startActivity(inte);
            }
        });
    }
}
