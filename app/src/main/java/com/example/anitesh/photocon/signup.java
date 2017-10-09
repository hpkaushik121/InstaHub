package com.example.anitesh.photocon;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class signup extends AppCompatActivity {
    Button button;
    EditText email,password,pnumber,biodata,username,conpass;
    private RequestQueue requestQ;
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    String pass;
    private StringRequest request;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_signup);
        button = (Button) findViewById(R.id.bRegister);
          email = (EditText) findViewById(R.id.email1);
         password = (EditText) findViewById(R.id.etPassword);
         pnumber = (EditText) findViewById(R.id.pnumber);
         conpass = (EditText) findViewById(R.id.confirm_password);
        biodata = (EditText) findViewById(R.id.biodata);
       username = (EditText) findViewById(R.id.etUsername);
        requestQ = Volley.newRequestQueue(this);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                    pass = password.getText().toString();
                    String Cpass= conpass.getText().toString();
                    if(email.getText().toString().trim().equals("")) {
                        email.setError("enter email");
                        return;
                    }else if(username.getText().toString().trim().equals("")) {
                        username.setError("enter username");
                        return;
                    }else if(password.getText().toString().trim().equals("")) {
                        password.setError("enter password");
                        return;
                }else if( pass.length() < 8) {
                    password.setError("password should be of min. 8 characters");
                    return;
                }else if(pnumber.getText().toString().trim().equals("")) {
                        pnumber.setError("enter phone no.");
                        return;
                    }else if(pnumber.length() < 10)
                    {
                        pnumber.setError("enter correct no.");
                        return;
                    }else  if(pnumber.length() > 10)
                    {
                        pnumber.setError("enter correct no.");
                        return;
                    }else if(biodata.getText().toString().trim().equals("")) {
                        biodata.setError("enter something about yourslef");
                        return;
                    }else {
                        if (!pass.equals(Cpass)) {
                            Toast.makeText(signup.this, "password don't match", Toast.LENGTH_SHORT).show();
                        }else {
                            new Asyncsignup().execute(email.getText().toString());


                        }
                    }
                }
            });
        }
    private class Asyncsignup extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(signup.this);
        HttpURLConnection conn;
        java.net.URL url = null;

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
                url = new URL("http://photocone.com/check.php");

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
                        .appendQueryParameter("username", params[0]);
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
        protected void onPostExecute(String result) {

            //this method will be running on UI thread
            pdLoading.dismiss();

            if(result.toString().trim().equals("true"))
            {
                Intent intent = new Intent(signup.this, biodata.class);
                intent.putExtra("pnumber", pnumber.getText().toString());
                intent.putExtra("biod", biodata.getText().toString());
                intent.putExtra("email", email.getText().toString());
                intent.putExtra("username", username.getText().toString());
                intent.putExtra("password", pass);
                startActivity(intent);

            }else if (result.toString().trim().equals("false")){

                // If username and password does not match display a error message
                Toast.makeText(signup.this, "email already exist", Toast.LENGTH_LONG).show();

            } else if (result.toString().trim().equalsIgnoreCase("exception") || result.toString().trim().equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(signup.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();

            }
        }

    }
    }
