package com.example.anitesh.photocon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class biodata extends AppCompatActivity {

    private RadioGroup radio;
    private Button sign_in_register;
    private RadioButton BBA,Barch,btechfirstyear,chemicalE,ECE,EEE,MAE,CSE,CE,IT,RandP;
    String branch;
TextView email,username,password,biodata,pnumber;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biodata);
        radio = (RadioGroup) findViewById(R.id.radioGroup2);
        sign_in_register = (Button) findViewById(R.id.button4);
        BBA = (RadioButton) findViewById(R.id.bradio1);
        Barch = (RadioButton) findViewById(R.id.bradio2);
        btechfirstyear = (RadioButton) findViewById(R.id.bradio3);
        chemicalE = (RadioButton) findViewById(R.id.bradio4);
        ECE = (RadioButton) findViewById(R.id.bradio5);
        EEE = (RadioButton) findViewById(R.id.bradio6);
        MAE = (RadioButton) findViewById(R.id.bradio7);
        CSE = (RadioButton) findViewById(R.id.bradio8);
        CE = (RadioButton) findViewById(R.id.bradio9);
        IT = (RadioButton) findViewById(R.id.bradio11);
        RandP = (RadioButton) findViewById(R.id.bradio10);

        email = (TextView) findViewById(R.id.email22);
        username = (TextView) findViewById(R.id.username18);
        password = (TextView) findViewById(R.id.password19);
        biodata = (TextView) findViewById(R.id.pnumber26);
        pnumber = (TextView) findViewById(R.id.pnumber21);
        Bundle b = getIntent().getExtras();
        String biodata1 =b.getString("biod");
        String pnumber1 =b.getString("pnumber");
        String email1 =b.getString("email");
        String username1 =b.getString("username");
        String password1 =b.getString("password");
        biodata.setText(biodata1);
        pnumber.setText(pnumber1);
        email.setText(email1);
        username.setText(username1);
        password.setText(password1);

        sign_in_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BBA.isChecked()) {
                    branch = BBA.getText().toString();
                    Intent intent = new Intent(getApplicationContext(), com.example.anitesh.photocon.BBA.class);
                    intent.putExtra("branch",branch);
                    intent.putExtra("biodata",biodata.getText().toString());
                    intent.putExtra("pnumber",pnumber.getText().toString());
                    intent.putExtra("email",email.getText().toString());
                    intent.putExtra("username",username.getText().toString());
                    intent.putExtra("password",password.getText().toString());
                    startActivity(intent);
                } else if (Barch.isChecked()) {
                    branch = Barch.getText().toString();
                    Intent intent = new Intent(getApplicationContext(),BARCH.class);
                    intent.putExtra("branch",branch);
                    intent.putExtra("biodata",biodata.getText().toString());
                    intent.putExtra("pnumber",pnumber.getText().toString());
                    intent.putExtra("email",email.getText().toString());
                    intent.putExtra("username",username.getText().toString());
                    intent.putExtra("password",password.getText().toString());
                    startActivity(intent);
                } else if (btechfirstyear.isChecked()) {
                    branch = btechfirstyear.getText().toString();
                    Intent intent = new Intent(getApplicationContext(), btechfirst.class);
                    intent.putExtra("branch",branch);
                    intent.putExtra("biodata",biodata.getText().toString());
                    intent.putExtra("pnumber",pnumber.getText().toString());
                    intent.putExtra("email",email.getText().toString());
                    intent.putExtra("username",username.getText().toString());
                    intent.putExtra("password",password.getText().toString());
                    startActivity(intent);
                } else if (chemicalE.isChecked()) {
                    branch = chemicalE.getText().toString();
                    Intent intent = new Intent(getApplicationContext(),chemical.class);
                    intent.putExtra("branch",branch);
                    intent.putExtra("biodata",biodata.getText().toString());
                    intent.putExtra("pnumber",pnumber.getText().toString());
                    intent.putExtra("email",email.getText().toString());
                    intent.putExtra("username",username.getText().toString());
                    intent.putExtra("password",password.getText().toString());
                    startActivity(intent);
                } else if (ECE.isChecked()) {
                    branch = ECE.getText().toString();
                    Intent intent = new Intent(getApplicationContext(),ECE.class);
                    intent.putExtra("branch",branch);
                    intent.putExtra("biodata",biodata.getText().toString());
                    intent.putExtra("pnumber",pnumber.getText().toString());
                    intent.putExtra("email",email.getText().toString());
                    intent.putExtra("username",username.getText().toString());
                    intent.putExtra("password",password.getText().toString());
                    startActivity(intent);
                }else if (EEE.isChecked()) {
                    branch = EEE.getText().toString();
                    Intent intent = new Intent(getApplicationContext(),EEE.class);
                    intent.putExtra("branch",branch);
                    intent.putExtra("biodata",biodata.getText().toString());
                    intent.putExtra("pnumber",pnumber.getText().toString());
                    intent.putExtra("email",email.getText().toString());
                    intent.putExtra("username",username.getText().toString());
                    intent.putExtra("password",password.getText().toString());
                    startActivity(intent);
                } else if (MAE.isChecked()) {
                    branch = MAE.getText().toString();
                    Intent intent = new Intent(getApplicationContext(),MAE.class);
                    intent.putExtra("branch",branch);
                    intent.putExtra("biodata",biodata.getText().toString());
                    intent.putExtra("pnumber",pnumber.getText().toString());
                    intent.putExtra("email",email.getText().toString());
                    intent.putExtra("username",username.getText().toString());
                    intent.putExtra("password",password.getText().toString());
                    startActivity(intent);
                } else if (CSE.isChecked()) {
                    branch = CSE.getText().toString();
                    Intent intent = new Intent(getApplicationContext(),CSE.class);
                    intent.putExtra("branch",branch);
                    intent.putExtra("biodata",biodata.getText().toString());
                    intent.putExtra("pnumber",pnumber.getText().toString());
                    intent.putExtra("email",email.getText().toString());
                    intent.putExtra("username",username.getText().toString());
                    intent.putExtra("password",password.getText().toString());
                    startActivity(intent);
                } else if (CE.isChecked()) {
                    branch = CE.getText().toString();
                    Intent intent = new Intent(getApplicationContext(),CE.class);
                    intent.putExtra("branch",branch);
                    intent.putExtra("email",email.getText().toString());
                    intent.putExtra("biodata",biodata.getText().toString());
                    intent.putExtra("pnumber",pnumber.getText().toString());
                    intent.putExtra("username",username.getText().toString());
                    intent.putExtra("password",password.getText().toString());
                    startActivity(intent);
                }else if (IT.isChecked()) {
                    branch = IT.getText().toString();
                    Intent intent = new Intent(getApplicationContext(),IT.class);
                    intent.putExtra("branch",branch);
                    intent.putExtra("email",email.getText().toString());
                    intent.putExtra("biodata",biodata.getText().toString());
                    intent.putExtra("pnumber",pnumber.getText().toString());
                    intent.putExtra("username",username.getText().toString());
                    intent.putExtra("password",password.getText().toString());
                    startActivity(intent);
                } else if (RandP.isChecked()) {
                    branch = RandP.getText().toString();
                    Intent intent = new Intent(getApplicationContext(),research.class);
                    intent.putExtra("branch",branch);
                    intent.putExtra("email",email.getText().toString());
                    intent.putExtra("pnumber",pnumber.getText().toString());
                    intent.putExtra("biodata",biodata.getText().toString());
                    intent.putExtra("username",username.getText().toString());
                    intent.putExtra("password",password.getText().toString());
                    startActivity(intent);
                }

            }
        });
    }
}


