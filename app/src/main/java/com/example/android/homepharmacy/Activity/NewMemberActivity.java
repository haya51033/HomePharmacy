package com.example.android.homepharmacy.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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
import com.example.android.homepharmacy.Setting.SettingsActivity;

import java.util.Locale;

public class NewMemberActivity extends BaseActivity implements
        NavigationView.OnNavigationItemSelectedListener{

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

    int userId;
    boolean english;
    String languageToLoad;


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


        setContentView(R.layout.activity_new_member);

        ///
        Intent intent1 = this.getIntent();
        userId = intent1.getIntExtra("userId",0);

        dbHelper = new DB(this);
        mDb = dbHelper.getWritableDatabase();

        ////NAV DRAWER /////
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.new_member);

        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


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
                            Intent intent = new Intent(getApplicationContext(), MembersActivity.class)
                                    .putExtra("userId", userId)
                                    .putExtra("lan", languageToLoad);
                            startActivity(intent);
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please fill in all fields ! ", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        Intent intent = new Intent(this, MembersActivity.class)
                .putExtra("userId", userId)
                .putExtra("lan", languageToLoad);
        startActivity(intent);
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
        String isLogged = "1";
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_drugs) {
            Intent intent = new Intent(this, SearchOptions.class)
                    .putExtra("userId", userId)
                    .putExtra("lan", languageToLoad);
            startActivity(intent);
        } else if (id == R.id.nav_members) {
            Intent intent = new Intent(this, MembersActivity.class)
                    .putExtra("userId", userId)
                    .putExtra("lan", languageToLoad);
            startActivity(intent);
        } else if (id == R.id.nav_FAid) {
            Intent intent = new Intent(this, FirstAidListActivity.class)
                    .putExtra("userId", userId)
                    .putExtra("lan", languageToLoad);
            startActivity(intent);
        } else if (id == R.id.nav_manage) {
            Intent intent = new Intent(this, SettingsActivity.class)
                    .putExtra("userId", userId)
                    .putExtra("lan", languageToLoad);
            startActivity(intent);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_logOut) {
            if(checkLoginData()){
                Toast.makeText(getApplicationContext(), "You are logged out..",Toast.LENGTH_LONG).show();
                Intent intent =  new Intent(getApplicationContext(), StartActivity.class)
                        .putExtra("lan", languageToLoad);
                startActivity(intent);
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public boolean checkLoginData() {
        String query = "SELECT *" + " FROM " + DataContract.UserEntry.TABLE_NAME
                + " WHERE " + DataContract.UserEntry.COLUMN_IS_LOGGED + " =? AND " + DataContract.UserEntry._ID + " =?";

        String isLogged = "1";
        String id = String.valueOf(userId);
        Cursor cursor = mDb.rawQuery(query, new String[]{isLogged, id});
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                boolean userUpdated = UpdateUser(userId);
                if (userUpdated) return true;
            }
            return false;
        }
        return false;
    }
    private boolean UpdateUser(int _id) {
        ContentValues cv = new ContentValues();
        cv.put(DataContract.UserEntry.COLUMN_IS_LOGGED, 0);
        int x = getContentResolver().update(DataContract.UserEntry.CONTENT_URI,cv, "_id=" + _id, null);
        finish();
        if(x > 0){
            return true;
        }
        return false;
    }
}
