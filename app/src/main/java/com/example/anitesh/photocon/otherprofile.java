package com.example.anitesh.photocon;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

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
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.R.attr.data;
import static android.R.attr.theme;
import static android.app.Activity.RESULT_OK;
import static com.example.anitesh.photocon.R.id.pnumber;
import static java.security.AccessController.getContext;


/**
 * A simple {@link Fragment} subclass.
 */
public class otherprofile extends AppCompatActivity {

    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    CircleImageView profileImg;
    TextView username,data_err,editText,txt;
    private int PICK_IMAGE_REQUEST = 1;

    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;

    //Bitmap to get image from gallery
    private Bitmap bitmap;

    //Uri to store the image uri
    private Uri filePath;
    RecyclerView grid;
    GridLayoutManager layoutManager;
    public  static List<String> ImageUrls,ext,file;
    boolean image=false,test = false;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu2,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.scnd:
                Intent i = (new Intent(otherprofile.this,chat.class));
                i.putExtra("id",String.valueOf(UserInfo.id2));
                startActivity(i);
                return true;
        }
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otherprofile);
        profileImg=(CircleImageView)findViewById(R.id.profile_image1);
        this.setTitle(String.valueOf(UserInfo.username2));
        username=(TextView)findViewById(R.id.username1);
        editText =(TextView)findViewById(R.id.textView5);
        txt = (TextView) findViewById(R.id.textView8);
        data_err=(TextView) findViewById(R.id.no_data1);
        grid=(RecyclerView) findViewById(R.id.profile_dt);
        layoutManager = new GridLayoutManager(this,3);
        grid.setLayoutManager(layoutManager);
        grid.setHasFixedSize(true);
        txt.setText(String.valueOf(UserInfo.biodata2));

        ImageUrls=new ArrayList<>();
        ext = new ArrayList<>();
        file = new ArrayList<>();
        new AsyncGetinfo().execute(String.valueOf(UserInfo.id2));
        username.setText(UserInfo.username2);

    }

    private class AsyncGetinfo extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(otherprofile.this);
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
                url = new URL("http://photocone.com/app_file.php");
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
                test =true;
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
                    try {
                        connection = new URL("http://photocone.com/photos/"+UserInfo.dp2).openConnection();
                        String contentType = connection.getHeaderField("Content-Type");
                        image = contentType.startsWith("image/");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

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

            if(image){
                Glide
                        .with(otherprofile.this)
                        .load("http://photocone.com/photos/"+UserInfo.dp2)
                        .crossFade()
                        .into(profileImg);
            }

            if(!result.equalsIgnoreCase("false") && test ==true)
            {
                try {
                    JSONArray jsonArray=new JSONArray(result);
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        if(!jsonObject.getString("file_ext").equalsIgnoreCase("pdf")&& !jsonObject.getString("file_ext").equalsIgnoreCase("docx") && !jsonObject.getString("file_ext").equalsIgnoreCase("pptx") && !jsonObject.getString("file_ext").equalsIgnoreCase("ppt")){
                            ImageUrls.add("http://photocone.com/pictures/"+jsonObject.getString("file_id")+"."+jsonObject.getString("file_ext"));
                            ext.add("addd");
                            file.add("asdfasd");

                        }if (jsonObject.getString("file_ext").equalsIgnoreCase("pdf")) {
                            ImageUrls.add("http://www.photocone.com/PDF.png");
                            ext.add("httP://photocone.com/pictures/"+jsonObject.getString("file_id")+"."+jsonObject.getString("file_ext"));
                            file.add(jsonObject.getString("name"));


                        }
                        if (jsonObject.getString("file_ext").equalsIgnoreCase("docx")) {
                            ImageUrls.add("http://www.photocone.com/DOCX.png");
                            ext.add("httP://photocone.com/pictures/"+jsonObject.getString("file_id")+"."+jsonObject.getString("file_ext"));
                            file.add(jsonObject.getString("name"));


                        }
                        if (jsonObject.getString("file_ext").equalsIgnoreCase("pptx")) {
                            ImageUrls.add("http://www.photocone.com/PPTX.png");
                            ext.add("httP://photocone.com/pictures/"+jsonObject.getString("file_id")+"."+jsonObject.getString("file_ext"));
                            file.add(jsonObject.getString("name"));


                        }
                        if (jsonObject.getString("file_ext").equalsIgnoreCase("ppt")) {
                            ImageUrls.add("http://www.photocone.com/PPTX.png");
                            ext.add("httP://photocone.com/pictures/"+jsonObject.getString("file_id")+"."+jsonObject.getString("file_ext"));
                            file.add(jsonObject.getString("name"));


                        }
                    }
                    grid.setAdapter(new adapter3(otherprofile.this,ImageUrls,ext,file));
                } catch (JSONException e) {
                    e.printStackTrace();
                    data_err.setVisibility(View.VISIBLE);
                }
            } else if (result.equalsIgnoreCase("false")){

                // If username and password does not match display a error message
                data_err.setVisibility(View.VISIBLE);

            }else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(otherprofile.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();

            }
        }

    }


    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
    }

}
