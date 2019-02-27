package com.example.android.homepharmacy;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.homepharmacy.Activity.StartActivity;
import com.example.android.homepharmacy.DataModel.DrugAlert;
import com.example.android.homepharmacy.Database.DB;
import com.example.android.homepharmacy.Database.DataContract;
import com.example.android.homepharmacy.broadcast_receivers.NotificationEventReceiver;
import com.example.android.homepharmacy.broadcast_receivers.NotificationEventReceiver2;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SQLiteDatabase mDb;
    DB dbHelper;

    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //////CREATE DATABASE
        dbHelper = new DB(this);
        mDb = dbHelper.getWritableDatabase();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


            if(setTimeAlarm()){
                NotificationEventReceiver.setupAlarm(getApplicationContext());
            }
        if(setTimeAlarm2()){
            NotificationEventReceiver2.setupAlarm2(getApplicationContext());
        }

        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);



    }

    public boolean setTimeAlarm(){
       cursor =  getContentResolver().query(DataContract.DrugListEntry.CONTENT_URI,
                null,
                null,
                null,
                DataContract.DrugListEntry._ID);
        if(cursor != null){
            if (cursor.moveToFirst()){// data?
               return true;
                }
            }
            return false;
        }

    public boolean setTimeAlarm2(){
        cursor =  getContentResolver().query(DataContract.DrugsEntry.CONTENT_URI,
                null,
                null,
                null,
                DataContract.DrugsEntry._ID);
        if(cursor != null){
            if (cursor.moveToFirst()){// data?
                return true;
            }
        }
        return false;
    }





    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }


    // To prevent crash on resuming activity  : interaction with fragments allowed only after Fragments Resumed or in OnCreate
    // http://www.androiddesignpatterns.com/2013/08/fragment-transaction-commit-state-loss.html
    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        // handleIntent();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void insertDrugs(){

    }
}
