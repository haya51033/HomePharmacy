package com.example.android.homepharmacy.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
    int userId;
    boolean english;
    String languageToLoad;
    TextView tv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
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

        setContentView(R.layout.activity_login);

        //////CREATE DATABASE
        dbHelper = new DB(this);
        mDb = dbHelper.getWritableDatabase();

        et = (EditText) findViewById(R.id.etLUsername);
        et1 = (EditText) findViewById(R.id.etLPassword);
        button = (Button) findViewById(R.id.etLButton);
        tv = (TextView) findViewById(R.id.tvForgetPassword);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(et.getText().toString().trim().length() != 0 && et1.getText().toString().trim().length() != 0){
                    _Username = et.getText().toString();
                    _Password = et1.getText().toString();

                  if(checkLoginData(_Username,_Password)){
                      Intent intent = new Intent(getApplicationContext(), HomeActivity.class)
                              .putExtra("userId", userId)
                              .putExtra("lan", languageToLoad);
                      startActivity(intent);
                  }
                }
                else {
                    Toast.makeText(getApplicationContext(),"Please fill in all fields ! ",Toast.LENGTH_LONG).show();
                }
            }
        });


        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ForgetPassword1Activity.class)
                        .putExtra("lan", languageToLoad);
                startActivity(intent);
            }
        });
    }

    public boolean checkLoginData(String user_name, String pass_word) {
        String query = "SELECT *"  + " FROM " + DataContract.UserEntry.TABLE_NAME
                +" WHERE "+ DataContract.UserEntry.COLUMN_USER_NAME + " =? AND "+ DataContract.UserEntry.COLUMN_PASSWORD+ " =?";

        Cursor cursor = mDb.rawQuery(query, new String[]{user_name,pass_word});

        if (cursor.getCount() > 0)
        {
            if (cursor.moveToFirst()) {
                userId = cursor.getInt(cursor.getColumnIndex(DataContract.UserEntry._ID));
                boolean userUpdated = UpdateUser(userId);
                if(userUpdated) {
                    Toast.makeText(getApplicationContext(), "Welcome " + user_name, Toast.LENGTH_LONG).show();
                    return true;
                }
            }
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
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }



    private boolean UpdateUser(int _id) {
        ContentValues cv = new ContentValues();
        cv.put(DataContract.UserEntry.COLUMN_IS_LOGGED, 1);
        int x = getContentResolver().update(DataContract.UserEntry.CONTENT_URI,cv, "_id=" + _id, null);
        finish();
        if(x > 0){
            return true;
        }
        return false;
    }
}