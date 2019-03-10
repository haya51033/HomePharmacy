package com.example.android.homepharmacy.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.homepharmacy.Database.DB;
import com.example.android.homepharmacy.Database.DataContract;
import com.example.android.homepharmacy.R;

import java.util.Locale;

public class ForgetPassword3Activity extends BaseActivity {

    boolean english;
    String languageToLoad;
    SQLiteDatabase mDb;
    DB dbHelper;
    EditText et;
    Button button;
    Cursor cursor;

    int _userId;
    String enteredPassword;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupSharedPreferences();
        Intent intent4 = this.getIntent();
        if (intent4.getStringExtra("lan") != null) {
            languageToLoad = intent4.getStringExtra("lan");

        } else {
            english = super.english;
            if (english) {
                languageToLoad = "en";
            } else {
                languageToLoad = "ar";
            }
        }

        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());


        setContentView(R.layout.activity_fp_step3);
        //////CREATE DATABASE
        dbHelper = new DB(this);
        mDb = dbHelper.getWritableDatabase();

        Intent intent = getIntent();
        _userId = intent.getIntExtra("_userId", 0);

        et = (EditText) findViewById(R.id.etNewPassword);
        button = (Button) findViewById(R.id.btnUpdatePassword);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et.getText().toString().trim().length()>0){
                    enteredPassword = et.getText().toString();

                    if(UpdateUser(_userId, enteredPassword)){
                        Toast.makeText(getApplicationContext(),"Your Password Updated Successfully, Please login to continue :) ",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class)
                                .putExtra("lan", languageToLoad);
                        startActivity(intent);
                    }
                    else {

                    }
                }
                else {
                    Toast.makeText(getApplicationContext(),"Enter New Password Please!",Toast.LENGTH_LONG).show();
                }
            }
        });


    }



    private boolean UpdateUser(int _id, String newPassword) {
        ContentValues cv = new ContentValues();
        cv.put(DataContract.UserEntry.COLUMN_PASSWORD, newPassword);
        int x = getContentResolver().update(DataContract.UserEntry.CONTENT_URI,cv, "_id=" + _id, null);
        finish();
        if(x > 0){
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(getApplicationContext(), LoginActivity.class)
                .putExtra("lan", languageToLoad);
        startActivity(intent);
    }
}