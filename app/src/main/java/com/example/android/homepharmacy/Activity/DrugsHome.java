package com.example.android.homepharmacy.Activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.android.homepharmacy.Database.DB;
import com.example.android.homepharmacy.Database.DataContract;
import com.example.android.homepharmacy.R;
import com.example.android.homepharmacy.Setting.SettingsActivity;

import java.util.Locale;

public class DrugsHome extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener{


    Context context;
    int userId;
    SQLiteDatabase mDb;
    DB dbHelper;
    // ImageView iv, iv1,iv2,iv3;

    Button layout, layout2, layout3;

    boolean english;
    String languageToLoad;

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


        setContentView(R.layout.activity_drugs_home);
        //////CREATE DATABASE
        dbHelper = new DB(this);
        mDb = dbHelper.getWritableDatabase();

        context = getApplicationContext();
        Intent intent = this.getIntent();
        userId = intent.getIntExtra("userId",0);

        ////NAV DRAWER /////
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);

        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        layout = (Button) findViewById(R.id.btnMyDrug);
        layout2 = (Button) findViewById(R.id.btnaddDrug);
        layout3 = (Button) findViewById(R.id.btnSearchDrug);

        layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NewDrugActivity.class)
                        .putExtra("userId", userId)
                        .putExtra("lan", languageToLoad);
                startActivity(intent);
            }
        });
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DrugsActivity.class)
                        .putExtra("userId", userId)
                        .putExtra("lan", languageToLoad);
                startActivity(intent);
            }
        });
        layout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchOptions.class)
                        .putExtra("userId", userId)
                        .putExtra("lan", languageToLoad);
                startActivity(intent);
            }
        });

    }


    ///NAV
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
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
            Intent intent = new Intent(this, SettingsActivity.class)
                    .putExtra("userId", userId)
                    .putExtra("lan", languageToLoad);
            startActivity(intent);

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

            try {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, getResources().getText(R.string.app_name));
                String sAux = "\n "+getResources().getText(R.string.msgReco) + "\n\n";
                sAux = sAux + getResources().getText(R.string.webSite)+" \n\n";
                i.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(i, getResources().getText(R.string.choose_one)));
            } catch(Exception e) {
                //
            }

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
