package com.example.android.homepharmacy.Activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.android.homepharmacy.R;
import com.example.android.homepharmacy.Setting.SettingsActivity;

import java.util.Locale;

public class StartActivity extends BaseActivity {

    LinearLayout layout1;
    LinearLayout layout2;
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

        setContentView(R.layout.activity_start);

        layout1 = (LinearLayout) findViewById(R.id.layout_login);
        layout2 = (LinearLayout) findViewById(R.id.layout_register);


        //String local = Locale.getDefault().getLanguage();

        layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class)
                        .putExtra("lan", languageToLoad);
                startActivity(intent);
            }
        });

        layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class)
                        .putExtra("lan", languageToLoad);
                startActivity(intent);
            }
        });
    }
}
