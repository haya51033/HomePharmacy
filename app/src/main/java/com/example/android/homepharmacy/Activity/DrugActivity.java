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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.homepharmacy.Database.DB;
import com.example.android.homepharmacy.Database.DataContract;
import com.example.android.homepharmacy.R;
import com.example.android.homepharmacy.Setting.SettingsActivity;

import java.util.Locale;

public class DrugActivity extends BaseActivity implements
        NavigationView.OnNavigationItemSelectedListener{
    Intent intent;
    int drugId;
    Cursor cur;
    Cursor cursor;
    private static final String[] DRUG_LIST_COLUMNS = {
            DataContract.DrugListEntry._ID,
            DataContract.DrugListEntry.COLUMN_DRUG_DOSE_DESCRIPTION,
            DataContract.DrugListEntry.COLUMN_DRUG_DOSE_QUANTITY,
            DataContract.DrugListEntry.COLUMN_DRUG_DOSE_REPEAT,
            DataContract.DrugListEntry.COLUMN_DRUG_END_DATE,
            DataContract.DrugListEntry.COLUMN_DRUG_FIRST_TIME,
            DataContract.DrugListEntry.COLUMN_DRUG_L_ID,
            DataContract.DrugListEntry.COLUMN_DRUG_START_DATE,
            DataContract.DrugListEntry.COLUMN_MEMBER_L_ID,
            DataContract.DrugListEntry.COLUMN_EXPIRY_DATE,
            DataContract.DrugListEntry.COLUMN_DRUG_LOCATION
    };
    private static final String[] DRUG_COLUMNS = {
            DataContract.DrugsEntry._ID,
            DataContract.DrugsEntry.COLUMN_DRUG_COMMERCIAL_NAME,
            DataContract.DrugsEntry.COLUMN_DRUG_COMMERCIAL_NAME_ARABIC,
            DataContract.DrugsEntry.COLUMN_DRUG_SCIENTIFIC_NAME,
            DataContract.DrugsEntry.COLUMN_DRUG_SCIENTIFIC_NAME_ARABIC,
            DataContract.DrugsEntry.COLUMN_DRUG_INDICATION,
            DataContract.DrugsEntry.COLUMN_DRUG_INDICATION_ARABIC,
            DataContract.DrugsEntry.COLUMN_DRUG_CONCENTRATION,
            DataContract.DrugsEntry.COLUMN_DRUG_TYPE,
            DataContract.DrugsEntry.COLUMN_DRUG_TYPE_ARABIC,
            DataContract.DrugsEntry.COLUMN_DRUG_WARNINGS,
            DataContract.DrugsEntry.COLUMN_DRUG_WARNINGS_ARABIC,
            DataContract.DrugsEntry.COLUMN_SIDE_EFFECTS,
            DataContract.DrugsEntry.COLUMN_SIDE_EFFECTS_ARABIC,
            DataContract.DrugsEntry.COLUMN_PREGNENT_ALLOWED,
            DataContract.DrugsEntry.COLUMN_DRUG_DESCRIPTION,
            DataContract.DrugsEntry.COLUMN_DRUG_DESCRIPTION_ARABIC,
            DataContract.DrugsEntry.COLUMN_DRUG_BARCODE
    };
    String _DRUG_COMMERCIAL_NAME, _DRUG_COMMERCIAL_NAME_ARABIC, _DRUG_SCIENTIFIC_NAME,
   _DRUG_SCIENTIFIC_NAME_ARABIC, _DRUG_INDICATION, _DRUG_INDICATION_ARABIC,
     _DRUG_CONCENTRATION, _DRUG_TYPE, _DRUG_TYPE_ARABIC,
    _DRUG_WARNINGS, _DRUG_WARNINGS_ARABIC, _SIDE_EFFECTS,
   _SIDE_EFFECTS_ARABIC, _PREGNENT_ALLOWED, _DRUG_DESCRIPTION,
    _DRUG_DESCRIPTION_ARABIC, _DRUG_BARCODE;

    TextView tv, tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8, tv9;
    Button button;
    Button button1;
    int memberId;
    int courseId;
    Intent intent1;
    int userId;
    SQLiteDatabase mDb;
    DB dbHelper;
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

        setContentView(R.layout.activity_drug);

        intent = this.getIntent();
        drugId = intent.getIntExtra(Intent.EXTRA_TEXT,0);

        intent1 = this.getIntent();
        memberId = intent1.getIntExtra("memberId",0);

        Intent intent2 = this.getIntent();
        userId = intent2.getIntExtra("userId",0);

        //////CREATE DATABASE
        dbHelper = new DB(this);
        mDb = dbHelper.getWritableDatabase();

        ////NAV DRAWER /////
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.drug_information);

        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        cursor = getSingleDrug();

        if (cursor.moveToFirst()){// data?
            _DRUG_COMMERCIAL_NAME =  cursor.getString(cursor.getColumnIndex("commercial_name"));
            _DRUG_SCIENTIFIC_NAME =  cursor.getString(cursor.getColumnIndex("scientific_name"));
            _DRUG_INDICATION =  cursor.getString(cursor.getColumnIndex("indication"));
            _DRUG_TYPE =  cursor.getString(cursor.getColumnIndex("type"));
            _DRUG_WARNINGS =  cursor.getString(cursor.getColumnIndex("warnings"));
            _SIDE_EFFECTS =  cursor.getString(cursor.getColumnIndex("side_effects"));
            _DRUG_DESCRIPTION =  cursor.getString(cursor.getColumnIndex("drug_description"));

            _DRUG_COMMERCIAL_NAME_ARABIC = cursor.getString(cursor.getColumnIndex("commercial_name_arabic"));
            _DRUG_SCIENTIFIC_NAME_ARABIC = cursor.getString(cursor.getColumnIndex("scientific_name_arabic"));
            _DRUG_INDICATION_ARABIC = cursor.getString(cursor.getColumnIndex("indication_arabic"));
            _DRUG_TYPE_ARABIC =  cursor.getString(cursor.getColumnIndex("type_arabic"));
            _DRUG_WARNINGS_ARABIC =  cursor.getString(cursor.getColumnIndex("warnings_arabic"));
            _SIDE_EFFECTS_ARABIC =  cursor.getString(cursor.getColumnIndex("side_effects_arabic"));
            _DRUG_DESCRIPTION_ARABIC =  cursor.getString(cursor.getColumnIndex("drug_description_arabic"));

            _DRUG_CONCENTRATION = cursor.getString(cursor.getColumnIndex("concentration"));
            _PREGNENT_ALLOWED =  cursor.getString(cursor.getColumnIndex("pregnant_allowed"));
            _DRUG_BARCODE =  cursor.getString(cursor.getColumnIndex("drug_barcode"));

            cursor.close();
            boolean isEnglish = Locale.getDefault().getLanguage().equals("en");


            tv = (TextView) findViewById(R.id.tvDrugCName);
            tv1 = (TextView) findViewById(R.id.tvDrugSName);

            tv2 = (TextView) findViewById(R.id.tv_drug_type);


            tv3 = (TextView) findViewById(R.id.tv_drug_indication);

            tv4 = (TextView) findViewById(R.id.tv_drug_side_effects);

            tv5 = (TextView) findViewById(R.id.tv_pregnant_allowed);
            if(_PREGNENT_ALLOWED!=null){
                if(_PREGNENT_ALLOWED.equals("0")){
                    _PREGNENT_ALLOWED = "NO";
                }
                else if(_PREGNENT_ALLOWED.equals("1")){
                    _PREGNENT_ALLOWED = "YES";
                }
            }
            tv5.setText(_PREGNENT_ALLOWED);

            tv6 = (TextView) findViewById(R.id.tv_drug_description);

            tv8 = (TextView) findViewById(R.id.tvDrugC);
            tv8.setText(_DRUG_CONCENTRATION);

            tv9 = (TextView) findViewById(R.id.tv_drug_warnings);

            if(isEnglish){
                tv.setText(_DRUG_COMMERCIAL_NAME);
                tv1.setText(_DRUG_SCIENTIFIC_NAME);
                tv3.setText(_DRUG_INDICATION);
                tv4.setText(_SIDE_EFFECTS);
                tv6.setText(_DRUG_DESCRIPTION);
                tv9.setText(_DRUG_WARNINGS);
                tv2.setText(_DRUG_TYPE);

            }
            else {      /** ARABIC*/
                tv.setText(_DRUG_COMMERCIAL_NAME_ARABIC);
                tv1.setText(_DRUG_SCIENTIFIC_NAME_ARABIC);
                tv3.setText(_DRUG_INDICATION_ARABIC);
                tv4.setText(_SIDE_EFFECTS_ARABIC);
                tv6.setText(_DRUG_DESCRIPTION_ARABIC);
                tv9.setText(_DRUG_WARNINGS_ARABIC);
                tv2.setText(_DRUG_TYPE_ARABIC);
            }
            button = (Button) findViewById(R.id.btnAddToMyList);
            button1 = (Button) findViewById(R.id.btnDeleteToMyList);

            if(memberId != 0) {
                Cursor cursor2 = checkIfExist();
                if(cursor2.moveToFirst()){
                    Toast.makeText(getApplicationContext(),"This Drug is Already in your List!",Toast.LENGTH_LONG).show();
                    courseId = cursor2.getInt(cursor2.getColumnIndex("_id"));
                    button.setVisibility(View.GONE);
                    button1.setVisibility(View.VISIBLE);
                    button1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Construct the URI for the item to delete
                            // Build appropriate uri with String row id appended
                            String stringId = Integer.toString(courseId);
                            Uri uri = DataContract.DrugListEntry.CONTENT_URI;
                            uri = uri.buildUpon().appendPath(stringId).build();
                            //Delete a single row of data using a ContentResolver
                            getContentResolver().delete(uri, null, null);

                            Intent intent = new Intent(getApplicationContext(), MemberActivity.class)
                                    .putExtra(Intent.EXTRA_TEXT,drugId)
                                    .putExtra("memberId",memberId)
                                    .putExtra("userId", userId)
                                    .putExtra("lan", languageToLoad);
                            startActivity(intent);


                        }
                    });

                }
                else {
                    button1.setVisibility(View.GONE);
                    button.setVisibility(View.VISIBLE);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent=new Intent(getApplicationContext(), NewCourseActivity.class)
                                    .putExtra(Intent.EXTRA_TEXT,drugId)
                                    .putExtra("memberId",memberId)
                                    .putExtra("userId", userId);
                            startActivity(intent);
                        }
                    });
                }
            }
        }
    }

    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        Intent intent = new Intent(this, DrugsActivity.class)
                .putExtra("userId", userId)
                .putExtra("lan", languageToLoad);
        startActivity(intent);
    }

    public Cursor getSingleDrug(){
        cur = getContentResolver().query(DataContract.DrugsEntry.CONTENT_URI, DRUG_COLUMNS,"_id='"+drugId+"'",null,null,null);
        return cur;
    }
    public Cursor checkIfExist(){
        cur = getContentResolver().query(DataContract.DrugListEntry.CONTENT_URI, DRUG_LIST_COLUMNS,"drug_list_id='"+drugId+"'" + " AND member_list_id='"+memberId+"'",null,null,null);
        return cur;
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
                    .putExtra("userId", userId);
            startActivity(intent);
        } else if (id == R.id.nav_members) {
            Intent intent = new Intent(this, MembersActivity.class)
                    .putExtra("userId", userId);
            startActivity(intent);
        } else if (id == R.id.nav_FAid) {
            Intent intent = new Intent(this, FirstAidListActivity.class)
                    .putExtra("userId", userId);
            startActivity(intent);
        } else if (id == R.id.nav_manage) {
            Intent intent = new Intent(this, SettingsActivity.class)
                    .putExtra("userId", userId);
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
