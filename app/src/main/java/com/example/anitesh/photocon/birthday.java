package com.example.anitesh.photocon;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
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
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class birthday extends AppCompatActivity {

    SharedPreferences spf;
    Spinner spin, spin2, spin3;
    Button but;
    String dateInString,biodata2,pnumber2,branch2,email2,username2,password2,year3;
    private RequestQueue requestQueue;
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    private static final String URL = "http://photocone.com/user_control.php";
    private StringRequest request;
    ArrayAdapter<CharSequence> adapter, adapter1, adapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birthday);

        spin = (Spinner) findViewById(R.id.spinner);
        spin2 = (Spinner) findViewById(R.id.spinner2);
        spin3 = (Spinner) findViewById(R.id.spinner3);
        but = (Button) findViewById(R.id.button8);
        Bundle b = getIntent().getExtras();
        biodata2 = b.getString("biodata");
        year3 = b.getString("year");
        pnumber2 = b.getString("pnumber");
        branch2 = b.getString("branch");
        email2 = b.getString("email");
        username2 = b.getString("username");
         password2 = b.getString("password");
        adapter = ArrayAdapter.createFromResource(this, R.array.days, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter1 = ArrayAdapter.createFromResource(this, R.array.month1, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2 = ArrayAdapter.createFromResource(this, R.array.year, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
        spin2.setAdapter(adapter1);
        spin3.setAdapter(adapter2);
        requestQueue = Volley.newRequestQueue(this);
        but.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                spf = getSharedPreferences("photocone", Context.MODE_PRIVATE);
                SharedPreferences.Editor  edt = spf.edit();

                edt.putString("email",email2);
                edt.putString("password",password2);
                edt.commit();
                String date = spin.getSelectedItem().toString();
                String month = spin2.getSelectedItem().toString();
                String year = spin3.getSelectedItem().toString();
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                dateInString = date + "/" + month + "/" + year;
                Toast.makeText(birthday.this, dateInString, Toast.LENGTH_SHORT).show();
                request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.names().get(0).equals("success")) {
                                Toast.makeText(getApplicationContext(), "SUCCESS " + jsonObject.getString("success"), Toast.LENGTH_SHORT).show();
                                new birthday.AsyncLogin().execute(email2,password2);
                            } else if (jsonObject.names().get(0).equals("error")){
                                Toast.makeText(getApplicationContext(), "Error" + jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> hashMap = new HashMap<String, String>();
                        hashMap.put("email", email2);
                        hashMap.put("username", username2);
                        hashMap.put("password", password2);
                        hashMap.put("biodata", biodata2);
                        hashMap.put("year", year3);
                        hashMap.put("branch", branch2);
                        hashMap.put("pnumber", pnumber2);
                        hashMap.put("birthday",dateInString);
                        return hashMap;
                    }
                };

                requestQueue.add(request);
            }
        });
    }
    //Server login
    private class AsyncLogin extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(birthday.this);
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
                url = new URL("http://photocone.com/app_login.php");

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

            if(!result.equalsIgnoreCase("false"))
            {
                /* Here launching another activity when login successful. If you persist login state
                use sharedPreferences of Android. and logout button to clear sharedPreferences.
                 */
                UserInfo.email=email2;
                UserInfo.password=password2;
                List<String> res = Arrays.asList(result.split(";"));
                UserInfo.id= Integer.parseInt(res.get(0));
                UserInfo.username=res.get(1);
                UserInfo.dp= res.get(2);
                Intent intent = new Intent(birthday.this,MainActivity.class);
                startActivity(intent);
                overridePendingTransition( R.anim.fadein, R.anim.fadeout );
                birthday.this.finish();

            }else if (result.equalsIgnoreCase("false")){

                // If username and password does not match display a error message
                Toast.makeText(birthday.this, "Invalid email or password", Toast.LENGTH_LONG).show();

            } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(birthday.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();

            }
        }

    }

}