package com.example.anitesh.photocon;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class searchFragment extends Fragment {

    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    CircleImageView profileImg;
    EditText edt;
    ActionBar actionBar;
    int l,next =0,i;
    static  int j ;
    AutoCompleteTextView atextv;
    MultiAutoCompleteTextView mtxtv;
    ArrayAdapter<String> adapt;
    int a=0;
    LinearLayoutManager layoutManager;
    ActionBar act;
    List array;
    RecyclerView grid;
    private boolean isLoading;
    private Activity activity;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    View v;
    boolean flag_loading=false;
    boolean load =false;
    public  static List<String> ImageUrls,captions,fileIDS,dpurl,id,arr,ext,fname;
    public  static hi adapter;
    boolean image=false;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.fragment_search, container, false);
        act=((AppCompatActivity)getActivity()).getSupportActionBar();
        act.hide();
        grid=(RecyclerView) v.findViewById(R.id.profile_grid);
        atextv = (AutoCompleteTextView) v.findViewById(R.id.auto1);
        layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        grid.setLayoutManager(layoutManager);
        grid.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction()==MotionEvent.ACTION_DOWN){
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    atextv.setText("");
                }
                return false;
            }
        });

       atextv.setOnKeyListener(new View.OnKeyListener() {
           @Override
           public boolean onKey(View v, int keyCode, KeyEvent event) {
               if(event.getAction()==KeyEvent.ACTION_DOWN){
                   switch (keyCode){
                       case KeyEvent.ACTION_DOWN:


                       case KeyEvent.KEYCODE_ENTER:
                           String st= atextv.getText().toString();
                           atextv.setText("");
                           Intent inte= new Intent(getContext(),search.class);
                           inte.putExtra("search",st);
                           startActivity(inte);

                           return  true;
                       default:
                           break;
                   }
               }
               return false;
           }
       });


        ImageUrls=new ArrayList<>();
        captions=new ArrayList<>();
        fileIDS=new ArrayList<>();
        dpurl=new ArrayList<>();
        id = new ArrayList<>();
        array = new ArrayList();
        ext =new ArrayList<>();
        fname = new ArrayList<>();
        adapter = new hi(getActivity(), captions, fileIDS, ImageUrls, dpurl,id,ext,fname);
        new AsyncGetinfo().execute(String.valueOf(UserInfo.id));
        return v;
    }



    private class AsyncGetinfo extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL("http://photocone.com/searching.php");
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("id", params[0]);
                String query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return "exception";
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    URLConnection connection = null;

                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {

                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(final String result) {

            //this method will be running on UI thread

            if (!result.equalsIgnoreCase("false") &&!result.equalsIgnoreCase("exception")&& ! result.equalsIgnoreCase("unsuccessful")) {

                try {
                    JSONArray jsonArray = new JSONArray(result);
                        j = jsonArray.length();
                        for ( i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            array.add(jsonObject.getString("caption"));
                            if (!jsonObject.getString("file_ext").equalsIgnoreCase("pdf") && !jsonObject.getString("file_ext").equalsIgnoreCase("docx") && !jsonObject.getString("file_ext").equalsIgnoreCase("pptx") && !jsonObject.getString("file_ext").equalsIgnoreCase("ppt")) {
                                ImageUrls.add("http://photocone.com/pictures/" + jsonObject.getString("file_id") + "." + jsonObject.getString("file_ext"));
                                fileIDS.add(jsonObject.getString("caption"));
                                ext.add("httP://photocone.com/pictures/" + jsonObject.getString("file_id") + "." + jsonObject.getString("file_ext"));
                                fname.add(jsonObject.getString("name"));
                                captions.add(jsonObject.getString("username"));
                                dpurl.add("http://photocone.com/photos/" + jsonObject.getString("dp"));
                                id.add(jsonObject.getString("id"));
                            }
                            if (jsonObject.getString("file_ext").equalsIgnoreCase("pdf")) {
                                ImageUrls.add("http://www.photocone.com/PDF.png");
                                fileIDS.add(jsonObject.getString("caption"));
                                id.add(jsonObject.getString("id"));
                                fname.add(jsonObject.getString("name"));
                                ext.add("httP://photocone.com/pictures/" + jsonObject.getString("file_id") + "." + jsonObject.getString("file_ext"));
                                captions.add(jsonObject.getString("username"));
                                dpurl.add("http://photocone.com/photos/" + jsonObject.getString("dp"));

                            }
                            if (jsonObject.getString("file_ext").equalsIgnoreCase("docx")) {
                                ImageUrls.add("http://www.photocone.com/DOCX.png");
                                fileIDS.add(jsonObject.getString("caption"));
                                id.add(jsonObject.getString("id"));
                                ext.add("httP://photocone.com/pictures/" + jsonObject.getString("file_id") + "." + jsonObject.getString("file_ext"));
                                fname.add(jsonObject.getString("name"));
                                captions.add(jsonObject.getString("username"));
                                dpurl.add("http://photocone.com/photos/" + jsonObject.getString("dp"));

                            }
                            if (jsonObject.getString("file_ext").equalsIgnoreCase("pptx")) {
                                ImageUrls.add("http://www.photocone.com/PPTX.png");
                                fileIDS.add(jsonObject.getString("caption"));
                                id.add(jsonObject.getString("id"));
                                fname.add(jsonObject.getString("name"));
                                ext.add("httP://photocone.com/pictures/" + jsonObject.getString("file_id") + "." + jsonObject.getString("file_ext"));
                                captions.add(jsonObject.getString("username"));
                                dpurl.add("http://photocone.com/photos/" + jsonObject.getString("dp"));

                            }
                            if (jsonObject.getString("file_ext").equalsIgnoreCase("ppt")) {
                                ImageUrls.add("http://www.photocone.com/PPTX.png");
                                fileIDS.add(jsonObject.getString("caption"));
                                id.add(jsonObject.getString("id"));
                                fname.add(jsonObject.getString("name"));
                                ext.add("httP://photocone.com/pictures/" + jsonObject.getString("file_id") + "." + jsonObject.getString("file_ext"));
                                captions.add(jsonObject.getString("username"));
                                dpurl.add("http://photocone.com/photos/" + jsonObject.getString("dp"));
                            }
                        }


                    adapt = new ArrayAdapter<String>(getActivity(),R.layout.support_simple_spinner_dropdown_item,array);
                    atextv.setAdapter(adapt);
                    grid.setAdapter(adapter);

                    } catch (JSONException e) {

                        Toast.makeText(getActivity(), "unable to read ", Toast.LENGTH_LONG).show();
                    }

            } else if (result.equalsIgnoreCase("false")) {

                // If username and password does not match display a error message
                Toast.makeText(getActivity(), "unable  ", Toast.LENGTH_LONG).show();

            } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(getActivity(), "OOPs! Something went wrong. ", Toast.LENGTH_LONG).show();
            }
        }
    }


    private class loadmore extends AsyncTask<String, String, String> {
        HttpURLConnection con;
        URL url1 = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url1 = new URL("http://photocone.com/searching3.php");
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                con = (HttpURLConnection) url1.openConnection();
                con.setReadTimeout(READ_TIMEOUT);
                con.setConnectTimeout(CONNECTION_TIMEOUT);
                con.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                con.setDoInput(true);
                con.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("id", params[0]);
                String query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = con.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                con.connect();

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return "exception";
            }

            try {

                int response_code = con.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = con.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    URLConnection connection = null;

                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {

                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            } finally {
                con.disconnect();
            }


        }

        @Override
        protected void onPostExecute(final String result) {

            //this method will be running on UI thread


            if (!result.equalsIgnoreCase("false") &&!result.equalsIgnoreCase("exception")&& ! result.equalsIgnoreCase("unsuccessful")) {
                try {
                    JSONArray jsonArray = new JSONArray(result);
                        for (int i = 0 ;i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                        }

                    atextv.setThreshold(1);
                    } catch (JSONException e) {

                        Toast.makeText(getActivity(), "un ", Toast.LENGTH_LONG).show();
                    }

            } else if (result.equalsIgnoreCase("false")) {

                // If username and password does not match display a error message
                Toast.makeText(getActivity(), "unable to  ", Toast.LENGTH_LONG).show();

            } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {
                if(a==0){
                    Toast.makeText(getActivity(), " Connection Problem.", Toast.LENGTH_LONG).show();
                    a++;
                }


            }
        }
    }




    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
    }

}
