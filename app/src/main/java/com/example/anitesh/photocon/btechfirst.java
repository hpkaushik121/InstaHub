package com.example.anitesh.photocon;

import android.content.Intent;;
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
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class btechfirst extends AppCompatActivity {
    int year2;
    Spinner spin, spin2, spin3;
    Button but;
    String dateInString,biodata2,pnumber2,branch2,email2,username2,password2;
    private RequestQueue requestQueue;
    private static final String URL = "http://photocone.com/user_control.php";
    private StringRequest request;
    ArrayAdapter<CharSequence> adapter, adapter1, adapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_btechfirst);

        spin = (Spinner) findViewById(R.id.spinner);
        spin2 = (Spinner) findViewById(R.id.spinner2);
        spin3 = (Spinner) findViewById(R.id.spinner3);
        but = (Button) findViewById(R.id.button8);
        Bundle b = getIntent().getExtras();
        biodata2 = b.getString("biodata");
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
        year2=1;
        requestQueue = Volley.newRequestQueue(this);
        but.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String date = spin.getSelectedItem().toString();
                String month = spin2.getSelectedItem().toString();
                String year = spin3.getSelectedItem().toString();
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                dateInString = date + "/" + month + "/" + year;
                Toast.makeText(btechfirst.this, dateInString, Toast.LENGTH_SHORT).show();
                request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.names().get(0).equals("success")) {
                                Toast.makeText(getApplicationContext(), "SUCCESS " + jsonObject.getString("success"), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(btechfirst.this,login.class);
                                startActivity(intent);
                                btechfirst.this.finish();
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
                        hashMap.put("year", String.valueOf(year2));
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
}