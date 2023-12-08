package com.example.project2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EnterUserInfo extends AppCompatActivity {

    EditText txtNameNew, txtPhoneNew, txtEmailNew, txtAboutMe,txtAddressNew;
    Button btnSubmitNew;
    SharedPreferences sp;
    TextView lblnewError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_user_info);
        txtNameNew = findViewById(R.id.txtNameNew);
        txtPhoneNew = findViewById(R.id.txtPhoneNew);
        txtEmailNew = findViewById(R.id.txtEmailNew);
        txtAboutMe = findViewById(R.id.txtAboutMe);
        btnSubmitNew = findViewById(R.id.btnSubmitNew);
        txtAddressNew = findViewById(R.id.txtAddressNew);
        sp = getSharedPreferences("user", MODE_PRIVATE);
        lblnewError = findViewById(R.id.lblnewError);

        UseServer server = new UseServer(this, sp.getString("session",""));
        btnSubmitNew.setOnClickListener(e->{
            if(txtNameNew.getText().toString().equals(""))
            {
                lblnewError.setVisibility(View.VISIBLE);
                return;
            }

            if(txtEmailNew.getText().toString().equals("") && txtPhoneNew.getText().toString().equals(""))
            {
                lblnewError.setVisibility(View.VISIBLE);
                return;
            }
            server.insertUserInfo(new HandleResponse() {
                @Override
                public void response(String response) {
                    Log.d("test", "account created part 2: ");

                }
            },sp.getInt("id", -1),
                    txtAddressNew.getText().toString(),
                    txtAboutMe.getText().toString(),
                    txtNameNew.getText().toString(),
                    txtPhoneNew.getText().toString(),
                    "",
                    "",
                    txtEmailNew.getText().toString()
            );
            Intent i = new Intent(this, activity_jobs.class);
            startActivity(i);
        });


    }
}