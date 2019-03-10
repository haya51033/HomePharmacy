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
import com.example.android.homepharmacy.R;

import java.util.Locale;

public class ForgetPassword2Activity extends BaseActivity {

    boolean english;
    String languageToLoad;
    SQLiteDatabase mDb;
    DB dbHelper;
    EditText et;
    TextView tv;
    Button button;
    Cursor cursor;

    String _reminder_question;
    int _reminder_question_num;
    int _userId;
    String enteredAnswer;



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


        setContentView(R.layout.activity_fp_step2);
        //////CREATE DATABASE
        dbHelper = new DB(this);
        mDb = dbHelper.getWritableDatabase();

        Intent intent = getIntent();
        _reminder_question_num = intent.getIntExtra("RQN",0);

        Intent intent1 = getIntent();
        _reminder_question = intent1.getStringExtra("RQA");

        Intent intent2 = getIntent();
        _userId = intent2.getIntExtra("_userId",0);

        String[] some_array = getResources().getStringArray(R.array.array_question);
        tv = (TextView) findViewById(R.id.tvRQ);
        tv.setText(some_array[_reminder_question_num]);

        et = (EditText) findViewById(R.id.etRQA);
        button = (Button) findViewById(R.id.nextButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et.getText().toString().trim().length()>0){
                    enteredAnswer = et.getText().toString();
                    if(enteredAnswer.equals(_reminder_question)){
                        Intent intent3 =  new Intent(getApplicationContext(), ForgetPassword3Activity.class)
                                .putExtra("_userId", _userId)
                                .putExtra("lan",languageToLoad);
                        startActivity(intent3);
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Your Answer is not correct!",Toast.LENGTH_LONG).show();

                    }
                }
                else {
                    Toast.makeText(getApplicationContext(),"Please enter your Answer",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void onBackPressed() {
        Intent intent = new Intent(this, LoginActivity.class)
                .putExtra("lan", languageToLoad);
        startActivity(intent);
    }
}
