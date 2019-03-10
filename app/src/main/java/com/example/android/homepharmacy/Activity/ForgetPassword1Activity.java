package com.example.android.homepharmacy.Activity;

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

public class ForgetPassword1Activity extends BaseActivity {

    boolean english;
    String languageToLoad;
    SQLiteDatabase mDb;
    DB dbHelper;
    EditText et;
    Button button;
    Cursor cursor1;

    String _reminder_question;
    int _reminder_question_num;
    int _userId;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupSharedPreferences();
        Intent intent4 = this.getIntent();
        if( intent4.getStringExtra("lan") != null){
            languageToLoad = intent4.getStringExtra("lan");

        }
        else {
            english = super.english;
            if(english){
                languageToLoad="en";
            }
            else {
                languageToLoad="ar";
            }
        }

        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config,getResources().getDisplayMetrics());


        setContentView(R.layout.activity_fp_step1);
        //////CREATE DATABASE
        dbHelper = new DB(this);
        mDb = dbHelper.getWritableDatabase();

        et = (EditText) findViewById(R.id.etEmail);
        button = (Button) findViewById(R.id.nextButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et.getText().toString().trim().length()>0) {

                    Cursor cursor = checkAlreadyExist(et.getText().toString());
                        if (cursor.getCount() > 0) {
                            if (cursor.moveToFirst()) {
                                _userId = cursor.getInt(cursor.getColumnIndex(DataContract.UserEntry._ID));
                                _reminder_question_num = cursor.getInt(cursor.getColumnIndex(DataContract.UserEntry.COLUMN_REMINDER_QUESTION_NUM));
                                _reminder_question = cursor.getString(cursor.getColumnIndex(DataContract.UserEntry.COLUMN_REMINDER_QUESTION));

                                Intent intent = new Intent(getApplicationContext(), ForgetPassword2Activity.class)
                                        .putExtra("lan", languageToLoad)
                                        .putExtra("RQA", _reminder_question)
                                        .putExtra("RQN", _reminder_question_num)
                                        .putExtra("_userId", _userId);
                                startActivity(intent);
                            }


                    } else {
                        Toast.makeText(getApplicationContext(), "The email is not Exist!!", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Please Enter Your Email!", Toast.LENGTH_LONG).show();

                }
            }
        });

    }

    public Cursor checkAlreadyExist(String email) {
        String query = "SELECT * " + " FROM " + DataContract.UserEntry.TABLE_NAME
                +" WHERE "+ DataContract.UserEntry.COLUMN_EMAIL + " =?";

         cursor1 = mDb.rawQuery(query, new String[]{email});
        return cursor1;
    }

    public void onBackPressed() {
        Intent intent = new Intent(this, LoginActivity.class)
                .putExtra("lan", languageToLoad);
        startActivity(intent);
    }
}
