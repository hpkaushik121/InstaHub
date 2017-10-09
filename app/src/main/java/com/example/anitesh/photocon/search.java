package com.example.anitesh.photocon;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Switch;
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


public class search extends AppCompatActivity {
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    TextView data_err;
    ListView grid;
    String str;
    RecyclerView.LayoutManager layoutManager;
    private PopupMenu popupMenu;
    public  static List<String> ImageUrls,captions,fileids,dpurls,id,ext,filename;
    boolean image=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        this.setTitle("searching...");
        Bundle b = getIntent().getExtras();
        str = b.getString("search");
        grid=(ListView) findViewById(R.id.profile_grid8);
        data_err=(TextView) findViewById(R.id.no_data3);
        ImageUrls=new ArrayList<>();
        captions=new ArrayList<>();
        fileids=new ArrayList<>();
        dpurls = new ArrayList<>();
        id= new ArrayList<>();
        ext = new ArrayList<>();
        filename = new ArrayList<>();
        new search.AsyncGetinfo().execute(String.valueOf(str));
    }
    private class AsyncGetinfo extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(search.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }
        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL("http://photocone.com/searching2.php");
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("search", params[0]);
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
                    return(result.toString());

                }else{

                    return("unsuccessful");
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

            pdLoading.dismiss();

            if(!result.equalsIgnoreCase("false"))
            {
                try {
                    JSONArray jsonArray=new JSONArray(result);
                    if(jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            if (!jsonObject.getString("file_ext").equalsIgnoreCase("pdf") && !jsonObject.getString("file_ext").equalsIgnoreCase("docx") && !jsonObject.getString("file_ext").equalsIgnoreCase("pptx") && !jsonObject.getString("file_ext").equalsIgnoreCase("ppt")) {
                                ImageUrls.add("http://photocone.com/pictures/" + jsonObject.getString("file_id") + "." + jsonObject.getString("file_ext"));
                                captions.add(jsonObject.getString("username"));
                                id.add(jsonObject.getString("id"));
                                ext.add("httP://photocone.com/pictures/"+jsonObject.getString("file_id")+"."+jsonObject.getString("file_ext"));
                                filename.add(jsonObject.getString("name"));
                                fileids.add(jsonObject.getString("caption"));
                                dpurls.add("http://photocone.com/photos/"+jsonObject.getString("dp"));

                            }
                            if (jsonObject.getString("file_ext").equalsIgnoreCase("pdf")) {
                                ImageUrls.add("http://www.photocone.com/PDF.png");
                                fileids.add(jsonObject.getString("caption"));
                                id.add(jsonObject.getString("id"));
                                ext.add("httP://photocone.com/pictures/"+jsonObject.getString("file_id")+"."+jsonObject.getString("file_ext"));
                                filename.add(jsonObject.getString("name"));
                                dpurls.add("http://photocone.com/photos/"+jsonObject.getString("dp"));
                                captions.add(jsonObject.getString("username"));

                            }
                            if (jsonObject.getString("file_ext").equalsIgnoreCase("docx")) {
                                ImageUrls.add("http://www.photocone.com/DOCX.png");
                                fileids.add(jsonObject.getString("caption"));
                                id.add(jsonObject.getString("id"));
                                ext.add(jsonObject.getString("httP://photocone.com/pictures/"+jsonObject.getString("file_id")+"."+jsonObject.getString("file_ext")));
                                filename.add(jsonObject.getString("name"));
                                dpurls.add("http://photocone.com/photos/"+jsonObject.getString("dp"));
                                captions.add(jsonObject.getString("username"));

                            }
                            if (jsonObject.getString("file_ext").equalsIgnoreCase("pptx")) {
                                ImageUrls.add("http://www.photocone.com/PPTX.png");
                                fileids.add(jsonObject.getString("caption"));
                                id.add(jsonObject.getString("id"));
                                ext.add("httP://photocone.com/pictures/"+jsonObject.getString("file_id")+"."+jsonObject.getString("file_ext"));
                                filename.add(jsonObject.getString("name"));
                                dpurls.add("http://photocone.com/photos/"+jsonObject.getString("dp"));
                                captions.add(jsonObject.getString("username"));

                            }
                            if (jsonObject.getString("file_ext").equalsIgnoreCase("ppt")) {
                                ImageUrls.add("http://www.photocone.com/PPTX.png");
                                fileids.add(jsonObject.getString("caption"));
                                id.add(jsonObject.getString("id"));
                                ext.add("httP://photocone.com/pictures/"+jsonObject.getString("file_id")+"."+jsonObject.getString("file_ext"));
                                filename.add(jsonObject.getString("name"));
                                dpurls.add("http://photocone.com/photos/"+jsonObject.getString("dp"));
                                captions.add(jsonObject.getString("username"));

                            }
                        }
                    }
                    grid.setAdapter(new listadapter(search.this,captions,fileids,ImageUrls,dpurls,id,ext,filename));
                } catch (JSONException e7){
                    data_err.setVisibility(View.VISIBLE);
                }
            } else if (result.equalsIgnoreCase("false")){

                // If username and password does not match display a error message
                data_err.setVisibility(View.VISIBLE);

            }else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(search.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();

            }
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
