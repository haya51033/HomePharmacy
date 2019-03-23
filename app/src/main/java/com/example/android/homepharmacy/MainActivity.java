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
import android.widget.Toast;

import com.example.android.homepharmacy.Activity.BaseActivity;
import com.example.android.homepharmacy.Activity.HomeActivity;
import com.example.android.homepharmacy.Activity.LoginActivity;
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


public class MainActivity extends BaseActivity {

    SQLiteDatabase mDb;
    DB dbHelper;
    int userId = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //////CREATE DATABASE
        dbHelper = new DB(this);
        mDb = dbHelper.getWritableDatabase();

        // Set Up Notifications:
        NotificationEventReceiver.setupAlarm(getApplicationContext());
        NotificationEventReceiver2.setupAlarm2(getApplicationContext());


        boolean isLogged = checkLoginData();

        if(isLogged){
            Intent intent = new Intent(this, HomeActivity.class)
                    .putExtra("userId", userId);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(this, StartActivity.class);
            startActivity(intent);
        }

    }




    public boolean checkLoginData() {
        String query = "SELECT *" + " FROM " + DataContract.UserEntry.TABLE_NAME
                + " WHERE " + DataContract.UserEntry.COLUMN_IS_LOGGED + " =? ";

        String isLogged = "1";
        Cursor cursor = mDb.rawQuery(query, new String[]{isLogged});

        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {

                userId = cursor.getInt(cursor.getColumnIndex(DataContract.UserEntry._ID));

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




}
