package com.example.alertapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class AddNumber extends AppCompatActivity {
Button btnAddNUmber;
EditText editText;
    Connection con;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_number);
        btnAddNUmber=findViewById(R.id.button2);
        editText=findViewById(R.id.editTextPhone);
        con=new databaseConnect().ConnectDB();
        if (con != null) {
            Toast.makeText(AddNumber.this, "Connection valid", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(AddNumber.this, "Connection invalid", Toast.LENGTH_SHORT).show();
        }
        btnAddNUmber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    NumberUpload numberUpload =new NumberUpload();
                    numberUpload.execute("");
                }catch (Exception e)
                {
                    Toast.makeText(AddNumber.this, "Error : "+e, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    public class NumberUpload extends AsyncTask<String, String, String> {

        String z = "";
        Boolean isSuccess = false;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String r) {
            Toast.makeText(AddNumber.this, r, Toast.LENGTH_SHORT).show();
            if (isSuccess) {
                Toast.makeText(AddNumber.this, "Number added Successfully", Toast.LENGTH_LONG).show();

            }else{
                Toast.makeText(AddNumber.this, "Number NOT added Successfully", Toast.LENGTH_LONG).show();

            }
        }

        @Override
        protected String doInBackground(String... params) {

            try
            {

                String number=editText.getText().toString();
               if (number.equals("")){
                   Toast.makeText(AddNumber.this, "Empty value for phone number passed !", Toast.LENGTH_SHORT).show();
               }

                String query = "insert into AlertPhone (pNumber) values ('" + number + "');";

                Statement stmt = con.createStatement();


                stmt.executeQuery(query);
            }
            catch (SQLException se)
            {
                Log.e("ERROR", Objects.requireNonNull(se.getMessage()));
            }
            return "Done";
        }
    }
}