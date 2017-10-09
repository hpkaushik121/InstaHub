package com.example.anitesh.photocon;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class editprofile extends AppCompatActivity {
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    EditText usern,eml,passw,conpass,bio;
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);
        usern = (EditText) findViewById(R.id.EditText8);
        eml = (EditText) findViewById(R.id.editText5);
        passw= (EditText) findViewById(R.id.EditText11);
        conpass =(EditText) findViewById(R.id.editText4);
        bio = (EditText) findViewById(R.id.editText6);
        submit = (Button) findViewById(R.id.button9);
        usern.setText(String.valueOf(UserInfo.username));
        eml.setText(String.valueOf(UserInfo.email));
        passw.setText(String.valueOf(UserInfo.password));
        bio.setText(String.valueOf(UserInfo.biodata2));
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(usern.getText().toString().equals("")){
                    usern.setError("field cannot be empty");
                } else if(eml.getText().toString().equals("")){
                    eml.setError("field cannot be empty");
                } else if(bio.getText().toString().equals("")){
                    bio.setError("field cannot be empty");
                }else{
                    if(passw.getText().toString().equals(conpass.getText().toString())){
                        Toast.makeText(editprofile.this,"confirm",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(editprofile.this,"passwords didn't match",Toast.LENGTH_SHORT).show();
                        passw.setText("");
                        conpass.setText("");
                    }
                }


            }
        });

    }

    private  class editprf extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        URL url = null;



        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL("http://photocone.com/editprf.php");
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
        protected void onPostExecute(final String result) {

            if (!result.equalsIgnoreCase("false")) {
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                    }


                } catch (JSONException e) {
                    Toast.makeText(editprofile.this, "OOPs! ", Toast.LENGTH_LONG).show();
                }
            } else if (result.equalsIgnoreCase("false")) {

                // If username and password does not match display a error message
                Toast.makeText(editprofile.this, "Something went wrong. ", Toast.LENGTH_LONG).show();

            } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {
                Toast.makeText(editprofile.this, "Connection Problem.", Toast.LENGTH_LONG).show();


            }
        }
    }

}
