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
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.homepharmacy.Database.DB;
import com.example.android.homepharmacy.Database.DataContract;
import com.example.android.homepharmacy.R;

public class NewMemberActivity extends BaseActivity {

    EditText et, et1, et2;
    Spinner spinner, spinner1;
    Button button;
    String spinnerValue;
    String spinnerValue1;
    String pregnant = "false";
    SQLiteDatabase mDb;
    DB dbHelper;
    Cursor cur;
    TextView tv;
    int loggedUser;

    private static final String[] MEMBER_COLUMNS = {
            DataContract.MemberEntry._ID,
            DataContract.MemberEntry.COLUMN_MEMBER_NAME,
            DataContract.MemberEntry.COLUMN_AGE,
            DataContract.MemberEntry.COLUMN_EMAIL,
            DataContract.MemberEntry.COLUMN_GENDER,
            DataContract.MemberEntry.COLUMN_PREGNANT,
            DataContract.MemberEntry.COLUMN_USER_ID
    };

    public void onBackPressed() {
        Intent intent = new Intent(this, MembersActivity.class);
        startActivity(intent);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupSharedPreferences();

        setContentView(R.layout.activity_new_member);

        dbHelper = new DB(this);
        mDb = dbHelper.getWritableDatabase();

        et = (EditText) findViewById(R.id.etRFN);
        et1 = (EditText) findViewById(R.id.etRAge);
        et2 = (EditText) findViewById(R.id.etREmail);
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        tv = (TextView) findViewById(R.id.tvPregnant);
        button = (Button) findViewById(R.id.btnAdd);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinnerValue = adapterView.getItemAtPosition(i).toString();
                if (spinnerValue.equals("Female")) {
                    spinner1.setVisibility(View.VISIBLE);
                    tv.setVisibility(View.VISIBLE);
                    spinnerValue1 = adapterView.getItemAtPosition(i).toString();
                    if (spinnerValue1.equals("Yes")) {
                        pregnant = "true";
                    } else {
                        pregnant = "false";
                    }
                } else {
                    spinner1.setVisibility(View.GONE);
                    tv.setVisibility(View.GONE);
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });
        loggedUser = checkLoggedUser();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et.getText().toString().trim().length() != 0 && et1.getText().toString().trim().length() != 0
                        && spinnerValue.trim().length() != 0) {
                    et = (EditText) findViewById(R.id.etRFN);
                    et1 = (EditText) findViewById(R.id.etRAge);
                    et2 = (EditText) findViewById(R.id.etREmail);

                    if (checkAlreadyExist(et.getText().toString())) {
                        addMember(et.getText().toString(),
                                Integer.parseInt(et1.getText().toString()),
                                spinnerValue,
                                et2.getText().toString(),
                                pregnant, loggedUser);
                        Cursor cursor = getMembers();
                        if (cursor == null || cursor.getColumnCount() == 0) {

                            Toast.makeText(getApplicationContext(), "Some thing wrong! please try again.", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Member Added Successfully! ", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), MembersActivity.class);
                            startActivity(intent);
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please fill in all fields ! ", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void addMember(String _Name, int _Age, String _Gender,
                           String _Email, String _Pregnant, int userId) {
        ContentValues cv = new ContentValues();
        cv.put(DataContract.MemberEntry.COLUMN_MEMBER_NAME, _Name);
        cv.put(DataContract.MemberEntry.COLUMN_AGE, _Age);
        cv.put(DataContract.MemberEntry.COLUMN_GENDER, _Gender);
        cv.put(DataContract.MemberEntry.COLUMN_EMAIL, _Email);
        cv.put(DataContract.MemberEntry.COLUMN_PREGNANT, _Pregnant);
        cv.put(DataContract.MemberEntry.COLUMN_USER_ID, userId);

        Uri uri = getContentResolver().insert(DataContract.MemberEntry.CONTENT_URI, cv);

        finish();
    }


    public boolean checkAlreadyExist(String name) {
        String query = "SELECT " + DataContract.MemberEntry.COLUMN_MEMBER_NAME + " FROM " + DataContract.MemberEntry.TABLE_NAME
                + " WHERE " + DataContract.MemberEntry.COLUMN_MEMBER_NAME + " =?";

        Cursor cursor = mDb.rawQuery(query, new String[]{name});
        if (cursor.getCount() > 0) {
            Toast.makeText(getApplicationContext(), "This Member is already Exist!!", Toast.LENGTH_LONG).show();
            return false;
        } else
            return true;
    }

    public Cursor getMembers() {
        cur = getContentResolver().query(DataContract.MemberEntry.CONTENT_URI, MEMBER_COLUMNS, null, null, null);
        return cur;
    }

    public int checkLoggedUser() {
        String query = "SELECT " + DataContract.UserEntry._ID + " FROM " + DataContract.UserEntry.TABLE_NAME
                + " WHERE " + DataContract.UserEntry.COLUMN_IS_LOGGED + " =?";
        String isLogged = "0";
        int userId;
        Cursor cursor = mDb.rawQuery(query, new String[]{isLogged});
        if (cursor.getCount() > 0) {
            if (cursor.moveToNext()) {
                userId = cursor.getInt(cursor.getColumnIndex(DataContract.UserEntry._ID));
                return userId;
            }
        }
            return 0;
    }


}
