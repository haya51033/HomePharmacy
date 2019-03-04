package com.example.android.homepharmacy.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.homepharmacy.Database.DB;
import com.example.android.homepharmacy.Database.DataContract;
import com.example.android.homepharmacy.R;

public class RegistrationActivity extends BaseActivity {

    EditText et, et1, et2, et3, et4;
    Spinner spinner;
    Button button;
    int spinnerValue;

    SQLiteDatabase mDb;
    DB dbHelper;
    Cursor cur;

    private static final String[] USER_COLUMNS = {
            DataContract.UserEntry._ID,
            DataContract.UserEntry.COLUMN_FULL_NAME,
            DataContract.UserEntry.COLUMN_USER_NAME,
            DataContract.UserEntry.COLUMN_EMAIL,
            DataContract.UserEntry.COLUMN_PASSWORD,
            DataContract.UserEntry.COLUMN_REMINDER_QUESTION,
            DataContract.UserEntry.COLUMN_REMINDER_QUESTION_NUM
    };

    String _email;
    String _user_Name;
    String _full_Name;
    String _password;
    String _reminder_question;
    int _reminder_question_num;


    public void onBackPressed() {
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupSharedPreferences();

        setContentView(R.layout.activity_register);
        //////CREATE DATABASE
        dbHelper = new DB(this);
        mDb = dbHelper.getWritableDatabase();


        et  = (EditText) findViewById(R.id.etRFN);
        et1 = (EditText) findViewById(R.id.etREmail);
        et2 = (EditText) findViewById(R.id.etRUsername);
        et3 = (EditText) findViewById(R.id.etRPassword);
        et4 = (EditText) findViewById(R.id.etRAnswer);

        spinner = (Spinner) findViewById(R.id.spinner);
        button = (Button) findViewById(R.id.btnRegister);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinnerValue = adapterView.getSelectedItemPosition();
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(et.getText().toString().trim().length() != 0
                        && et1.getText().toString().trim().length() != 0
                        && et3.getText().toString().trim().length() != 0
                        && et4.getText().toString().trim().length() != 0
                        && spinnerValue != 0 ){

                    _email = et1.getText().toString();
                    _full_Name = et.getText().toString();
                    _user_Name = et2.getText().toString();
                    _password = et3.getText().toString();
                    _reminder_question = et4.getText().toString();
                    _reminder_question_num = spinnerValue;
               if(checkAlreadyExist(_email)){

                     ///////INSERT NEW USER///////
                    addUser( _user_Name, _email, _password, _full_Name,  _reminder_question, _reminder_question_num);

                   Cursor cursor = getUsers();

                     if (cursor == null || cursor.getColumnCount() == 0 ) {

                         Toast.makeText(getApplicationContext(),"Some thing wrong! please try again.",Toast.LENGTH_LONG).show();
                     }
                     else {
                         cursor.moveToLast();
                         String id = cur.getString(0); //id
                         String name = cur.getString(1); // name
                         String username = cur.getString(2); //username
                         String email_ = cur.getString(3);// email
                         String password = cur.getString(4); //pass
                         String RQuestion = cur.getString(5);//r_q

                         Toast.makeText(getApplicationContext(),"Please login to continue "  ,Toast.LENGTH_LONG).show();

                         Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                         startActivity(intent);
                     }
                }
                }
                else {
                    Toast.makeText(getApplicationContext(),"Please fill in all fields ! ",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public boolean checkAlreadyExist(String email) {
        String query = "SELECT " + DataContract.UserEntry.COLUMN_EMAIL + " FROM " + DataContract.UserEntry.TABLE_NAME
                +" WHERE "+ DataContract.UserEntry.COLUMN_EMAIL + " =?";

        Cursor cursor = mDb.rawQuery(query, new String[]{email});
        if (cursor.getCount() > 0)
        {
            Toast.makeText(getApplicationContext(),"The email is already Exist!!", Toast.LENGTH_LONG).show();
            return false;
        }
        else
        return true;
    }

    private void addUser(String _user_Name, String _email, String _password,
                         String _full_Name, String _reminder_question, int _reminder_question_num) {
        ContentValues cv = new ContentValues();
        cv.put(DataContract.UserEntry.COLUMN_USER_NAME,_user_Name);
        cv.put(DataContract.UserEntry.COLUMN_EMAIL, _email);
        cv.put(DataContract.UserEntry.COLUMN_PASSWORD,_password);
        cv.put(DataContract.UserEntry.COLUMN_FULL_NAME, _full_Name);
        cv.put(DataContract.UserEntry.COLUMN_REMINDER_QUESTION, _reminder_question);
        cv.put(DataContract.UserEntry.COLUMN_REMINDER_QUESTION_NUM, _reminder_question_num);

        Uri uri = getContentResolver().insert(DataContract.UserEntry.CONTENT_URI, cv);

        finish();
    }


    public Cursor getUsers() {
        cur =   getContentResolver().query(DataContract.UserEntry.CONTENT_URI, USER_COLUMNS, null, null, null);
        return cur;
    }
}