package com.example.anitesh.photocon;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
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
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class chat extends AppCompatActivity {

    ListView lst;
    EditText edt;
    public static List<chatmodal> chat1 = new ArrayList<>();
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    List type = new ArrayList();
    public static chatadap chatadap;
    boolean adapt=false;
    SharedPreferences spf;
    String id,chat;
    SharedPreferences.Editor edt1;
    int num;
    FloatingActionButton msend;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        lst = (ListView) findViewById(R.id.chatlist);
        edt = (EditText) findViewById(R.id.msg21);
        spf = getSharedPreferences(String.valueOf(UserInfo.id2),MODE_PRIVATE);
        int spfnum = spf.getInt("num",0);
        num=spfnum;
        if(spfnum!=0) {
            for (int i = 1; i < spfnum+1; i++) {
                for (int j = 1; j < 3; j++) {
                    Boolean sen;
                        String st = spf.getString("chat" + String.valueOf(i) + String.valueOf(j), null);
                    if(j==1&& st!=null) {
                        sen = true;
                        chatmodal model = new chatmodal(st, sen);
                        chat1.add(model);
                        List<chatmodal> mode;
                        mode = chat1;
                        chatadap = new chatadap(mode, getApplicationContext());
                        lst.setAdapter(chatadap);
                    }
                    if(j==2 && st !=null){
                        sen=false;
                        chatmodal model = new chatmodal(st, sen);
                        chat1.add(model);
                        List<chatmodal> mode;
                        mode = chat1;
                        chatadap = new chatadap(mode, getApplicationContext());
                        lst.setAdapter(chatadap);
                    }


                }
            }
        }
        edt1= spf.edit();
        Bundle b = getIntent().getExtras();
        id = b.getString("id").toString();
        new rcvmsg().execute(String.valueOf(UserInfo.id));
        msend = (FloatingActionButton) findViewById(R.id.fab6);
        msend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = edt.getText().toString();
                String text1 = text;
                chatmodal model = new chatmodal(text, true);
                chat1.add(model);
                num++;
                chat=("chat"+num+1).toString();
                edt1.putString(chat,text);
                edt1.putInt("num",num);
                edt1.commit();
                new msg().execute(chat1);
                new sendmsg().execute(text, id, String.valueOf(UserInfo.id));

                edt.setText("");
            }
        });


    }

    public class msg extends AsyncTask<List<chatmodal>, Void, String> {
        String stream = null;
        List<chatmodal> mode;
        String text = edt.getText().toString();

        @Override
        protected String doInBackground(List<chatmodal>... params) {
            mode = params[0];

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            chatadap = new chatadap(mode, getApplicationContext());

                lst.setAdapter(chatadap);

        }


    }

    public class sendmsg extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL("http://photocone.com/chat.php");

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
                        .appendQueryParameter("massege", params[0])
                        .appendQueryParameter("sender", params[2])
                        .appendQueryParameter("receiver", params[1]);
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
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(chat.this, String.valueOf(s)+"ghvh", Toast.LENGTH_SHORT).show();
        }
    }

    public class rcvmsg extends AsyncTask<String, String, String> {
        List<chatmodal> mode;
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL("http://photocone.com/recieve.php");

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
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (!s.equalsIgnoreCase("false") &&!s.equalsIgnoreCase("exception")&& ! s.equalsIgnoreCase("unsuccessful")) {
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    for ( int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String text = jsonObject.getString("massege");
                        String text1 = text;
                        Toast.makeText(chat.this,text,Toast.LENGTH_SHORT).show();
                        chatmodal model = new chatmodal(text,false);
                        chat1.add(model);
                        num++;
                        String chat2=("chat"+num+2).toString();
                        edt1.putString(chat2,text1);
                        edt1.putInt("num",num);
                        edt1.commit();


                    }
                    mode=chat1;
                    chatadap = new chatadap(mode,getApplicationContext());
                        lst.setAdapter(chatadap);



                } catch (JSONException e) {

                    Toast.makeText(chat.this, "unable to read ", Toast.LENGTH_LONG).show();
                }

            } else if (s.equalsIgnoreCase("false")) {

                // If username and password does not match display a error message
                Toast.makeText(chat.this, "unable  ", Toast.LENGTH_LONG).show();

            } else if (s.equalsIgnoreCase("exception") || s.equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(chat.this, "OOPs! Something went wrong. ", Toast.LENGTH_LONG).show();
            }
        }
            }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}



