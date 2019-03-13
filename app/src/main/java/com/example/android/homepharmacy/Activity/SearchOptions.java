package com.example.android.homepharmacy.Activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.Toast;

import com.example.android.homepharmacy.Database.DB;
import com.example.android.homepharmacy.Database.DataContract;
import com.example.android.homepharmacy.R;
import com.example.android.homepharmacy.Setting.SettingsActivity;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.Locale;

public class SearchOptions  extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    EditText et;
    SQLiteDatabase mDb;
    DB dbHelper;
    Button button;
    String query = "";
    ArrayList drugsResult;
    Spinner spinner;
    int spinnerValue;
    Cursor c;
    int memberId;
    Intent intent;
    final Activity activity = this;
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

        setContentView(R.layout.activity_search_options);

        //////CREATE DATABASE
        dbHelper = new DB(this);
        mDb = dbHelper.getWritableDatabase();


        intent = this.getIntent();
        memberId = intent.getIntExtra(Intent.EXTRA_TEXT,0);

        Intent intent1 = this.getIntent();
        userId = intent1.getIntExtra("userId",0);

        ////NAV DRAWER /////
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.search_options);

        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


            button = (Button) findViewById(R.id.btnSearch);
            et = (EditText) findViewById(R.id.etSearch);
            spinner = (Spinner) findViewById(R.id.spinner);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    //  spinnerValue =adapterView.getItemAtPosition(i).;
                    spinnerValue = spinner.getSelectedItemPosition();
                }

                public void onNothingSelected(AdapterView<?> adapterView) {
                    return;
                }
            });

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    query = et.getText().toString();
                    drugsResult = new ArrayList();
                    boolean isEnglish = Locale.getDefault().getLanguage().equals("en");

                    switch (spinnerValue){
                        case 0:
                            Toast.makeText(getApplicationContext(),"Please select search option!", Toast.LENGTH_LONG).show();
                            break;
                        case 1:
                            if(isEnglish) {
                                c = getDrugNameMatches(query, null);
                            }
                            else {
                                c = getDrugNameMatchesArabic(query, null);
                            }
                            break;
                        case 2:
                            if(isEnglish)
                                c = getDrugIndicationMatches(query,null);
                            else
                                c = getDrugIndicationMatchesArabic(query, null);
                            break;
                        case 3:

                            startScan();
                            break;

                        default:
                            Intent intent = new Intent(getApplicationContext(), DrugsActivity.class)
                                    .putExtra("userId", userId)
                                    .putExtra("lan", languageToLoad);
                            startActivity(intent);
                            break;
                    }

                    if(c != null){
                        if(c.moveToFirst()){
                            while (!c.isAfterLast()) {
                                String id = String.valueOf(c.getInt(c.getColumnIndex("_id")));
                                    drugsResult.add(id);

                                c.moveToNext();
                            }

                            Intent intent = new Intent(getApplicationContext(), DrugsActivity.class);
                            Bundle args = new Bundle();
                            intent.putExtra(Intent.EXTRA_TEXT, memberId);
                            intent.putExtra("userId", userId);
                            intent.putExtra("lan", languageToLoad);
                            args.putSerializable("my_array", drugsResult);
                            intent.putExtra("BUNDLE", args);
                            startActivity(intent);

                        }
                        else {
                            Toast.makeText(getApplicationContext(),"No Result!",Toast.LENGTH_LONG).show();
                        }
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
        Intent intent = new Intent(this, HomeActivity.class)
                .putExtra("userId",userId)
                .putExtra("lan", languageToLoad);
        startActivity(intent);
    }

    public Cursor getDrugNameMatches(String query, String[] columns) {
        String[] selectionArgs = new String[] {"%"+query+"%"};

        return getContentResolver().query(DataContract.DrugsEntry.CONTENT_URI,
                null,
                DataContract.DrugsEntry.COLUMN_DRUG_COMMERCIAL_NAME + " LIKE ? ",
                selectionArgs,
                DataContract.DrugsEntry.COLUMN_DRUG_COMMERCIAL_NAME);
    }

    public Cursor getDrugNameMatchesArabic(String query, String[] columns) {
        String[] selectionArgs = new String[] {"%"+query+"%"};

        return getContentResolver().query(DataContract.DrugsEntry.CONTENT_URI,
                null,
                DataContract.DrugsEntry.COLUMN_DRUG_COMMERCIAL_NAME_ARABIC + " LIKE ? ",
                selectionArgs,
                DataContract.DrugsEntry.COLUMN_DRUG_COMMERCIAL_NAME_ARABIC);
    }

    public Cursor getDrugIndicationMatches(String query, String[] columns) {
        String[] selectionArgs = new String[] {"%"+query+"%"};

        return getContentResolver().query(DataContract.DrugsEntry.CONTENT_URI,
                null,
                DataContract.DrugsEntry.COLUMN_DRUG_INDICATION + " LIKE ? ",
                selectionArgs,
                DataContract.DrugsEntry.COLUMN_DRUG_COMMERCIAL_NAME);
    }

    public Cursor getDrugIndicationMatchesArabic(String query, String[] columns) {
        String[] selectionArgs = new String[] {"%"+query+"%"};

        return getContentResolver().query(DataContract.DrugsEntry.CONTENT_URI,
                null,
                DataContract.DrugsEntry.COLUMN_DRUG_INDICATION_ARABIC + " LIKE ? ",
                selectionArgs,
                DataContract.DrugsEntry.COLUMN_DRUG_COMMERCIAL_NAME_ARABIC);
    }

    public void startScan(){
        IntentIntegrator integrator = new IntentIntegrator(activity);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan Drug Barcode/ QR Code");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       // setupSharedPreferences();
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){
            if(result.getContents()==null){
                Toast.makeText(this, "You cancelled the scanning", Toast.LENGTH_LONG).show();
            }
            else {
                String _code = ""+ String.valueOf(result.getContents());
                if(_code.contains(""+"\u001D"))
                _code = _code.replace("\u001D", "");

                c = getDrugScannedCodeMatches(_code, null);

                if(c != null){
                    if(c.moveToFirst()){
                        while (!c.isAfterLast()) {
                            String id = String.valueOf(c.getInt(c.getColumnIndex("_id")));
                            drugsResult.add(id);
                            c.moveToNext();
                        }
                        Intent intent = new Intent(getApplicationContext(), DrugsActivity.class);
                        Bundle args = new Bundle();
                        intent.putExtra(Intent.EXTRA_TEXT, memberId);
                        intent.putExtra("userId", userId);
                        intent.putExtra("lan", languageToLoad);
                        args.putSerializable("my_array", drugsResult);
                        intent.putExtra("BUNDLE", args);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"No Result!",Toast.LENGTH_LONG).show();
                    }
                }

            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public Cursor getDrugScannedCodeMatches(String query, String[] columns) {
        String[] selectionArgs = new String[] {query};

        return getContentResolver().query(DataContract.DrugsEntry.CONTENT_URI,
                null,
                DataContract.DrugsEntry.COLUMN_DRUG_BARCODE + " LIKE ? ",
                selectionArgs,
                null);
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
