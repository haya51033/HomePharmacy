package com.example.android.homepharmacy.Setting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.homepharmacy.Activity.BaseActivity;
import com.example.android.homepharmacy.Activity.HomeActivity;
import com.example.android.homepharmacy.Activity.MemberActivity;
import com.example.android.homepharmacy.Activity.StartActivity;
import com.example.android.homepharmacy.R;

import java.util.Locale;


public class SettingsActivity extends BaseActivity {

   int userId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupSharedPreferences();



        setContentView(R.layout.activity_setting);
        Intent intent = this.getIntent();
        userId = intent.getIntExtra("userId",0);

        ActionBar actionBar = this.getSupportActionBar();

        // Set the action bar back button to look like an up button
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        // When the home button is pressed, take the user back t
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
       startActivity(new Intent(this, HomeActivity.class)
               .putExtra("userId", userId));

    }
    @Override
    public void onResume() {
        super.onResume();

    }



}