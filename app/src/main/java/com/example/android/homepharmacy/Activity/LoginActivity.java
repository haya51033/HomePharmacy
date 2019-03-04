package com.example.android.homepharmacy.Activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.homepharmacy.Database.DB;
import com.example.android.homepharmacy.Database.DataContract;
import com.example.android.homepharmacy.R;

import java.util.Locale;

public class LoginActivity extends BaseActivity {

    EditText et, et1;
    Button button;

    SQLiteDatabase mDb;
    DB dbHelper;

    String _Username;
    String _Password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        //////CREATE DATABASE
        dbHelper = new DB(this);
        mDb = dbHelper.getWritableDatabase();

        et = (EditText) findViewById(R.id.etLUsername);
        et1 = (EditText) findViewById(R.id.etLPassword);
        button = (Button) findViewById(R.id.etLButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(et.getText().toString().trim().length() != 0 && et1.getText().toString().trim().length() != 0){
                    _Username = et.getText().toString();
                    _Password = et1.getText().toString();

                  if(checkLoginData(_Username,_Password)){
                      Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                      startActivity(intent);
                  }
                }
                else {
                    Toast.makeText(getApplicationContext(),"Please fill in all fields ! ",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public boolean checkLoginData(String user_name, String pass_word) {
        String query = "SELECT " + DataContract.UserEntry.COLUMN_USER_NAME + " FROM " + DataContract.UserEntry.TABLE_NAME
                +" WHERE "+ DataContract.UserEntry.COLUMN_USER_NAME + " =? AND "+ DataContract.UserEntry.COLUMN_PASSWORD+ " =?";

        Cursor cursor = mDb.rawQuery(query, new String[]{user_name,pass_word});

        if (cursor.getCount() > 0)
        {
            Toast.makeText(getApplicationContext(),"Welcome " + user_name ,Toast.LENGTH_LONG).show();
                return true;
        }
        else {
            String query1 = "SELECT " + DataContract.UserEntry.COLUMN_USER_NAME + " FROM " + DataContract.UserEntry.TABLE_NAME
                    +" WHERE "+ DataContract.UserEntry.COLUMN_USER_NAME + " =?";
            Cursor cursor1 = mDb.rawQuery(query1, new String[]{user_name});
            if (cursor1.getCount() > 0)
            {
                Toast.makeText(getApplicationContext(),"The Password is not correct" ,Toast.LENGTH_LONG).show();
                return false;
            }
            else {

                Toast.makeText(getApplicationContext(),"Username or Password is not correct !",Toast.LENGTH_LONG).show();
                return false;
            }
        }
    }

}