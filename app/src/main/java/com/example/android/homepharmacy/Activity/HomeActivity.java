package com.example.android.homepharmacy.Activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.homepharmacy.Adapter.ImageAdapter;
import com.example.android.homepharmacy.Database.DB;
import com.example.android.homepharmacy.Database.DataContract;
import com.example.android.homepharmacy.R;
import com.example.android.homepharmacy.Setting.SettingsActivity;

public class HomeActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener{


    Context context;
    int userId;
    SQLiteDatabase mDb;
    DB dbHelper;
    ImageView iv, iv1,iv2,iv3;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupSharedPreferences();

        setContentView(R.layout.activity_home);
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


        iv = (ImageView) findViewById(R.id.ivDrug);
        iv1 = (ImageView) findViewById(R.id.ivMember);
        iv2 = (ImageView) findViewById(R.id.ivSearch);
        iv3 = (ImageView) findViewById(R.id.ivFAid);

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchOptions.class)
                        .putExtra("userId", userId);
                startActivity(intent);
            }
        });
        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MembersActivity.class)
                        .putExtra("userId", userId);
                startActivity(intent);
            }
        });
        iv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FirstAidListActivity.class)
                        .putExtra("userId", userId);
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
            Intent intent = new Intent(this, SearchOptions.class);
            startActivity(intent);
        } else if (id == R.id.nav_members) {
            Intent intent = new Intent(this, MembersActivity.class)
                    .putExtra("userId", userId);
            startActivity(intent);
        } else if (id == R.id.nav_FAid) {
            Intent intent = new Intent(this, FirstAidListActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_manage) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_logOut) {
            if(checkLoginData()){
                Toast.makeText(getApplicationContext(), "You are logged out..",Toast.LENGTH_LONG).show();
                Intent intent =  new Intent(getApplicationContext(), StartActivity.class);
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
