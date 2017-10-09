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
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
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
import static android.app.Activity.RESULT_OK;
import static com.example.anitesh.photocon.R.id.imageView;
import static com.example.anitesh.photocon.R.id.pnumber;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private FragmentManager manager;
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    CircleImageView profileImg;
    FloatingActionButton f1, f2;
    Animation fabopen,fabclose;
    boolean isopen = false;
    TextView username,data_err,editText;
    private int PICK_IMAGE_REQUEST = 1;

    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;

    //Bitmap to get image from gallery
    static Bitmap bitmap;

    ActionBar act1;
    //Uri to store the image uri
    private Uri filePath;
    RecyclerView grid;
    GridLayoutManager layoutManager;
    public  static List<String> ImageUrls,ext,file;
    boolean image=false,test = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_profile, container, false);
        act1=((AppCompatActivity)getActivity()).getSupportActionBar();
        act1.show();
        act1.setDisplayShowCustomEnabled(false);
        profileImg=(CircleImageView)v.findViewById(R.id.profile_image);
        f1 = (FloatingActionButton) v.findViewById(R.id.f2);
        f2 = (FloatingActionButton) v.findViewById(R.id.f1);
        fabclose = AnimationUtils.loadAnimation(getActivity(),R.anim.fabclose);
        fabopen = AnimationUtils.loadAnimation(getActivity(),R.anim.fabopen);
        setHasOptionsMenu(true);
        getActivity().setTitle("Profile");
        username=(TextView)v.findViewById(R.id.username);
        editText =(TextView)v.findViewById(R.id.textView3);
        data_err=(TextView)v.findViewById(R.id.no_data);
        grid=(RecyclerView) v.findViewById(R.id.profile_d);
        layoutManager = new GridLayoutManager(getContext(),3);
        grid.setLayoutManager(layoutManager);
        grid.setHasFixedSize(true);
       grid.setOnScrollChangeListener(new View.OnScrollChangeListener() {
           @Override
           public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
               f1.setVisibility(View.GONE);
               f2.setVisibility(View.GONE);
               isopen =false;
           }
       });
        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isopen == false){
                    f1.setVisibility(View.VISIBLE);
                    f2.setVisibility(View.VISIBLE);

                    f1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent pictureActionIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(pictureActionIntent, PICK_IMAGE_REQUEST);
                        }
                    });
                    f2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent pictureActionIntent = new Intent(Intent.ACTION_GET_CONTENT, null);
                            pictureActionIntent.setType("image/*");
                            pictureActionIntent.putExtra("return-data", true);
                            startActivityForResult(pictureActionIntent, PICK_IMAGE_REQUEST);
                        }
                    });
                    isopen=true;
                }else{

                    f1.setVisibility(View.GONE);
                    f2.setVisibility(View.GONE);
                    isopen =false;
                }
            }

        });
        ImageUrls=new ArrayList<>();
        ext = new ArrayList<>();
        file= new ArrayList<>();
        new AsyncGetinfo().execute(String.valueOf(UserInfo.id));
        username.setText(UserInfo.username);
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.main2,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                Intent i = (new Intent(getActivity(),optionmenu.class));
                startActivity(i);
                return true;
        }
        return true;
    }

    private class AsyncGetinfo extends AsyncTask<String, String, String>
    {
        HttpURLConnection conn;
        URL url = null;


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
                        connection = new URL("http://photocone.com/photos/"+UserInfo.dp).openConnection();
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



            if(image){
                Glide
                        .with(getActivity())
                        .load("http://photocone.com/photos/"+UserInfo.dp)
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
                            file.add("asdfa");

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
                    grid.setAdapter(new adapter3(getActivity(),ImageUrls,ext,file));
                } catch (JSONException e) {
                    data_err.setVisibility(View.VISIBLE);
                }
            } else if (result.equalsIgnoreCase("false")){

                // If username and password does not match display a error message
                data_err.setVisibility(View.VISIBLE);

            }else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(getActivity(), "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();

            }
        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                String path2 = getPath(filePath);
                Intent inte =new Intent(getActivity(),fffff.class);
                inte.putExtra("path",path2);
                getActivity().startActivity(inte);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //method to get the file path from uri
    public String getPath(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getActivity().getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
    }

}
