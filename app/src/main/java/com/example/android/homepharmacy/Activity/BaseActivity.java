package com.example.android.homepharmacy.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.android.homepharmacy.Database.DataContract;
import com.example.android.homepharmacy.R;

import java.util.Locale;


public class BaseActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{


    public boolean english = true;
    public boolean lang;
    public String languageToLoad;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     //   setupSharedPreferences();

    }



    public void setupSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        english = sharedPreferences.getBoolean(getString(R.string.pref_language_key), getResources().getBoolean(R.bool.pref_lang_default));

        if(english){
            languageToLoad="en";
        }
        else {
            languageToLoad="ar";
        }


        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config,getResources().getDisplayMetrics());


        // Register the listener
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }



    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Toast ar = Toast.makeText(getApplicationContext(), "اللغة العربية", Toast.LENGTH_SHORT);
        Toast en = Toast.makeText(getApplicationContext(), "English Language", Toast.LENGTH_SHORT);
        if (key.equals(getString(R.string.pref_language_key))) {
            lang=sharedPreferences.getBoolean(key, getResources().getBoolean(R.bool.pref_lang_default));
            if(lang){
                languageToLoad="en";
                en.show();
            }
            else {
                languageToLoad="ar";
                ar.show();
            }

        }
    }



}
