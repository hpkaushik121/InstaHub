package com.example.anitesh.photocon;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

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
import java.util.Arrays;
import java.util.List;

public class welcome extends AppCompatActivity {

    ProgressBar pgr;
    int progress = 0;

    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    String email,password;
    int doub = 0;
    Handler h = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        pgr = (ProgressBar) findViewById(R.id.progressBar);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                for(int i=0;i<=100;i++)
//                {
//                    progress+=1;
//                    h.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            pgr.setProgress(progress);
//                            if(progress == pgr.getMax()){
        SharedPreferences spf = getSharedPreferences("photocone", MODE_PRIVATE);
        email = (spf.getString("email", null));
        password = (spf.getString("password", null));
        if (password == null) {
            Intent in = new Intent(welcome.this, login.class);
            startActivity(in);
            finish();

        } else {
            String logout = (spf.getString("logout", ""));
            if (logout.equalsIgnoreCase("logout")) {
                Intent inte = new Intent(welcome.this, onetaplogin.class);
                startActivity(inte);
                finish();
            } else {
                String email1 = (spf.getString("email", ""));
                String password1 = (spf.getString("password", ""));
                new welcome.AsyncLogin().execute(email1, password1);
            }
//
//
//                                }
//
//                            }
//                        }
//                    });
//                    try{
//                        Thread.sleep(30);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
            }
//        }).start();

    }
    private class AsyncLogin extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pgr.setMax(100);

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL("http://photocone.com/app_login.php");

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
                        .appendQueryParameter("username", params[0])
                        .appendQueryParameter("password", params[1]);
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
                for(int i=0;i<50;i++){
                    progress+=1;
                    pgr.setProgress(progress);
                }

            }
            catch (IOException e4){
                e4.printStackTrace();
                return "exception";

            }
            try {
                int response_code = conn.getResponseCode();
                for(int j = 50;j<80;j++){
                    progress+=1;
                    pgr.setProgress(progress);
                }
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
            }
            finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {


            if (!result.equalsIgnoreCase("false") && !result.equalsIgnoreCase("exception") && !result.equalsIgnoreCase("unsuccessful")) {
                /* Here launching another activity when login successful. If you persist login state
                use sharedPreferences of Android. and logout button to clear sharedPreferences.
                 */
                for(int j = 80;j<100;j++){
                    progress+=1;
                    pgr.setProgress(progress);
                }
                UserInfo.email = String.valueOf(email);
                UserInfo.password = String.valueOf(password);
                List<String> res = Arrays.asList(result.split(";"));
                UserInfo.id = Integer.parseInt(res.get(0));
                UserInfo.username = res.get(1);
                UserInfo.dp = res.get(2);
                UserInfo.biodata = res.get(3);
                Intent intent = new Intent(welcome.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                welcome.this.finish();

            } else if (result.equalsIgnoreCase("false")||result.equalsIgnoreCase("exception")||result.equalsIgnoreCase("unsuccessful")) {
                SharedPreferences spf = getSharedPreferences("photocone", MODE_PRIVATE);
                String email1 = (spf.getString("email", ""));
                String password1 = (spf.getString("password", ""));
                new welcome.AsyncLogin().execute(email1, password1);
                if(doub == 0) {
                    // If username and password does not match display a error message
                    doub++;
                    Toast.makeText(welcome.this, "OOps! someyhing went wronge", Toast.LENGTH_LONG).show();
                }

            }
        }

    }
}

