package com.example.anitesh.photocon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MAE extends AppCompatActivity {
    int year2;
    private RadioGroup radio2;
    private Button button1;
    String biodata2,pnumber2,branch2,email2,username2,password2;
    private RadioButton fstyr,scndyr,thrdyr,frthyr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mae);
        radio2 = (RadioGroup) findViewById(R.id.radioGroup4);
        button1 = (Button) findViewById(R.id.button1);
        fstyr = (RadioButton) findViewById(R.id.radiob1);
        scndyr = (RadioButton) findViewById(R.id.radiob2);
        thrdyr = (RadioButton) findViewById(R.id.radioButton10);
        frthyr = (RadioButton) findViewById(R.id.radiob3);
        Bundle b = getIntent().getExtras();
        biodata2 =b.getString("biodata");
        pnumber2 =b.getString("pnumber");
        branch2 =b.getString("branch");
        email2 =b.getString("email");
        username2 =b.getString("username");
        password2 =b.getString("password");
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fstyr.isChecked()) {
                    year2=1;
                    Intent intent = new Intent(getApplicationContext(), birthday.class);
                    intent.putExtra("branch", branch2);
                    intent.putExtra("biodata", biodata2);
                    intent.putExtra("pnumber", pnumber2);
                    intent.putExtra("email", email2);
                    intent.putExtra("year", String.valueOf(year2));
                    intent.putExtra("username", username2);
                    intent.putExtra("password", password2);
                    startActivity(intent);
                } else if (scndyr.isChecked()) {
                    year2=2;
                    Intent intent = new Intent(getApplicationContext(), birthday.class);
                    intent.putExtra("branch", branch2);
                    intent.putExtra("biodata", biodata2);
                    intent.putExtra("pnumber", pnumber2);
                    intent.putExtra("email", email2);
                    intent.putExtra("year", String.valueOf(year2));
                    intent.putExtra("username", username2);
                    intent.putExtra("password", password2);
                    startActivity(intent);
                } else if (thrdyr.isChecked()) {
                    year2=3;
                    Intent intent = new Intent(getApplicationContext(), birthday.class);
                    intent.putExtra("branch", branch2);
                    intent.putExtra("biodata", biodata2);
                    intent.putExtra("pnumber", pnumber2);
                    intent.putExtra("email", email2);
                    intent.putExtra("year", String.valueOf(year2));
                    intent.putExtra("username", username2);
                    intent.putExtra("password", password2);
                    startActivity(intent);
                }else if (frthyr.isChecked()) {
                    year2=4;
                    Intent intent = new Intent(getApplicationContext(), birthday.class);
                    intent.putExtra("branch", branch2);
                    intent.putExtra("biodata", biodata2);
                    intent.putExtra("pnumber", pnumber2);
                    intent.putExtra("email", email2);
                    intent.putExtra("year", String.valueOf(year2));
                    intent.putExtra("username", username2);
                    intent.putExtra("password", password2);
                    startActivity(intent);
                }
            }
        });
    }
}