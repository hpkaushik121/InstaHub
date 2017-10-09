package com.example.anitesh.photocon;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;


public class MainActivity extends AppCompatActivity implements AHBottomNavigation.OnTabSelectedListener{

    AHBottomNavigation bottomNavigation;
    String logout;
    SharedPreferences spf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigation=(AHBottomNavigation)findViewById(R.id.bottom_navigation_bar);
        bottomNavigation.setOnTabSelectedListener(this);
        this.createNavItems();
        spf = getSharedPreferences("photocone", Context.MODE_PRIVATE);
        spf.edit().remove("logout").commit();




    }

    private void createNavItems(){
        AHBottomNavigationItem chat = new AHBottomNavigationItem("chat",R.drawable.chat);
        AHBottomNavigationItem searchItem=new AHBottomNavigationItem("Search",android.R.drawable.ic_menu_search);
        AHBottomNavigationItem UploadItem=new AHBottomNavigationItem("Upload",android.R.drawable.ic_menu_upload);
        AHBottomNavigationItem profileItem=new AHBottomNavigationItem("Profile",R.drawable.user);
        AHBottomNavigationItem logout=new AHBottomNavigationItem("logout",android.R.drawable.ic_lock_power_off);
        bottomNavigation.addItem(chat);
        bottomNavigation.addItem(searchItem);
        bottomNavigation.addItem(UploadItem);
        bottomNavigation.addItem(profileItem);
        bottomNavigation.addItem(logout);
        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#FEFEFE"));
        bottomNavigation.setCurrentItem(3);
    }

    @Override
    public void onTabSelected(int position, boolean wasSelected) {
        if(position==0){
            chat2 chat2=new chat2();
            getSupportFragmentManager().beginTransaction().replace(R.id.content_id,chat2).commit();
        }
            else if(position==1){
                searchFragment Search_Fragment=new searchFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.content_id,Search_Fragment).commit();
            }
        else if(position==2){
                UploadFragment Camera_Fragment=new UploadFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.content_id,Camera_Fragment).commit();
            }
            else if(position==3){
                ProfileFragment Profile_Fragment=new ProfileFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.content_id,Profile_Fragment).commit();
            }else if(position==4){
                AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                        MainActivity.this);

// Setting Dialog Title
                alertDialog2.setTitle("Logout");

// Setting Dialog Message
                alertDialog2.setMessage("Are you sure you want logout");

// Setting Icon to Dialog
                alertDialog2.setIcon(android.R.drawable.ic_lock_power_off);

// Setting Positive "Yes" Btn
                alertDialog2.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(MainActivity.this, onetaplogin.class));
                                logout="logout";
                                spf = getSharedPreferences("photocone", Context.MODE_PRIVATE);
                                SharedPreferences.Editor edt = spf.edit();

                                edt.putString("logout", String.valueOf(logout));
                                edt.commit();
                                Toast.makeText(getApplicationContext(),
                                        "You are logged out", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        });

// Setting Negative "NO" Btn
                alertDialog2.setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                bottomNavigation.setCurrentItem(2);
                                dialog.cancel();
                            }
                        });

// Showing Alert Dialog
                alertDialog2.show();
            }
    }




}


