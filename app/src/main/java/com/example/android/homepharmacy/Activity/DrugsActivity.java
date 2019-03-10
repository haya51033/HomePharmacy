package com.example.android.homepharmacy.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.homepharmacy.Adapter.DrugsAdapter;
import com.example.android.homepharmacy.Database.DB;
import com.example.android.homepharmacy.Database.DataContract;
import com.example.android.homepharmacy.R;
import com.example.android.homepharmacy.Setting.SettingsActivity;

import java.util.ArrayList;
import java.util.Locale;

public class DrugsActivity extends BaseActivity implements
        LoaderManager.LoaderCallbacks<Cursor>,
        DrugsAdapter.DrugsnOnClickHandler,
        NavigationView.OnNavigationItemSelectedListener{
    // Constants for logging and referring to a unique loader
    private static final String TAG = "ANY";
    private static final int TASK_LOADER_ID = 0;

    // Member variables for the adapter and RecyclerView
    private DrugsAdapter mAdapter;
    RecyclerView mRecyclerView;
    int userId;
    SQLiteDatabase mDb;
    DB dbHelper;

    ArrayList<String> drugsSearchResult;
    String[] selectionArgs1;
    String[] selectionArgs;
    String selection;
    int memberId;
    boolean isEnglish;

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

        isEnglish = Locale.getDefault().getLanguage().equals("en");


        setContentView(R.layout.activity_drugs);


        Intent intent1 = getIntent();
        Bundle args;
        if((intent1.getBundleExtra("BUNDLE")) != null){
            args = intent1.getBundleExtra("BUNDLE");
            if(((ArrayList) args.getSerializable("my_array")) != null)
                drugsSearchResult =  (ArrayList) args.getSerializable("my_array");

        }


        Intent intent = this.getIntent();
        if((intent.getIntExtra(Intent.EXTRA_TEXT, 0)) != 0){
            memberId = intent.getIntExtra(Intent.EXTRA_TEXT, 0);
        }

        //////CREATE DATABASE
        dbHelper = new DB(this);
        mDb = dbHelper.getWritableDatabase();

        Intent intent2 = this.getIntent();
        userId = intent2.getIntExtra("userId",0);

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


        if(drugsSearchResult != null && !drugsSearchResult.isEmpty()){
            selectionArgs1 = drugsSearchResult.toArray(new String[drugsSearchResult.size()]);
            selection = DataContract.DrugsEntry._ID + " IN (" + makePlaceholders(selectionArgs1.length) + ")";
            selectionArgs = new String[selectionArgs1.length];
            for (int i = 0; i < selectionArgs1.length; i++) {
                selectionArgs[i] = selectionArgs1[i];
            }
        }



        // Set the RecyclerView to its corresponding view
        mRecyclerView = (RecyclerView) findViewById(R.id.listDrugs);

        // Set the layout for the RecyclerView to be a linear layout, which measures and
        // positions items within a RecyclerView into a linear list
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the adapter and attach it to the RecyclerView
        mAdapter = new DrugsAdapter(this);

        mRecyclerView.setAdapter(mAdapter);

      /* Add a touch helper to the RecyclerView to recognize when a user swipes to delete an item.
         An ItemTouchHelper enables touch behavior (like swipe and move) on each ViewHolder,
         and uses callbacks to signal when a user is performing these actions.*/
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                 getSupportLoaderManager().restartLoader(TASK_LOADER_ID, null, DrugsActivity.this);
            }
        }).attachToRecyclerView(mRecyclerView);

        /*Ensure a loader is initialized and active.
          If the loader doesn't already exist, one is
          created, otherwise the last created loader is re-used.*/
        getSupportLoaderManager().initLoader(TASK_LOADER_ID, null, this);
    }

    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        Intent intent = new Intent(this, HomeActivity.class)
                .putExtra("userId", userId)
                .putExtra("lan", languageToLoad);
        startActivity(intent);
    }
    @Override
    public void onClickDrug(Cursor cursor) {
        int drugId = cursor.getInt(0);
        Intent intent=new Intent(getApplicationContext(), DrugActivity.class)
                .putExtra(Intent.EXTRA_TEXT,drugId)
                .putExtra("memberId",memberId)
                .putExtra("userId", userId)
                .putExtra("lan", languageToLoad);
        startActivity(intent);

    }

    /**
     * This method is called after this activity has been paused or restarted.
     * Often, this is after new data has been inserted through an AddTaskActivity,
     * so this restarts the loader to re-query the underlying data for any changes.
     */
    @Override
    protected void onResume() {
        super.onResume();

        // re-queries for all drugs
        getSupportLoaderManager().restartLoader(TASK_LOADER_ID, null, this);
    }

    /**
     * Instantiates and returns a new AsyncTaskLoader with the given ID.
     * This loader will return drugs data as a Cursor or null if an error occurs.
     *
     * Implements the required callbacks to take care of loading data at all stages of loading.
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, final Bundle loaderArgs) {
        setupSharedPreferences();
        return new AsyncTaskLoader<Cursor>(this) {
            // Initialize a Cursor, this will hold all the drugs data
            Cursor mTaskData = null;
            // onStartLoading() is called when a loader first starts loading data
            @Override
            protected void onStartLoading() {
                if (mTaskData != null) {
                    // Delivers any previously loaded data immediately
                    deliverResult(mTaskData);
                } else {
                    // Force a new load
                    forceLoad();
                }
            }
            // loadInBackground() performs asynchronous loading of data
            @Override
            public Cursor loadInBackground() {
                // Will implement to load data
                // Query and load all drug data in the background; sort by priority
                // use a try/catch block to catch any errors in loading data

                if(drugsSearchResult == null){
                    try {
                        if(isEnglish)
                        return getContentResolver().query(DataContract.DrugsEntry.CONTENT_URI,
                                null,
                                null,
                                null,
                                DataContract.DrugsEntry.COLUMN_DRUG_COMMERCIAL_NAME);
                        else
                            return getContentResolver().query(DataContract.DrugsEntry.CONTENT_URI,
                                    null,
                                    null,
                                    null,
                                    DataContract.DrugsEntry.COLUMN_DRUG_COMMERCIAL_NAME_ARABIC);
                    } catch (Exception e) {
                        Log.e(TAG, "Failed to asynchronously load data.");
                        e.printStackTrace();
                        return null;
                    }
                }
                else {
                    try {
                        if(isEnglish)
                           return getContentResolver().query(DataContract.DrugsEntry.CONTENT_URI,
                                   null,
                                   selection,
                                   selectionArgs,
                                   DataContract.DrugsEntry.COLUMN_DRUG_COMMERCIAL_NAME);

                        else return getContentResolver().query(DataContract.DrugsEntry.CONTENT_URI,
                                null,
                                selection,
                                selectionArgs,
                                DataContract.DrugsEntry.COLUMN_DRUG_COMMERCIAL_NAME_ARABIC);
                    } catch (Exception e) {
                        Log.e(TAG, "Failed to asynchronously load data 1111111111.");
                        e.printStackTrace();
                        return null;
                    }
                }
            }
            // deliverResult sends the result of the load, a Cursor, to the registered listener
            public void deliverResult(Cursor data) {
                mTaskData = data;
                super.deliverResult(data);
            }
        };


    }
    String makePlaceholders(int len) {
        StringBuilder sb = new StringBuilder(len * 2 - 1);
        sb.append("?");
        for (int i = 1; i < len; i++)
            sb.append(",?");
        return sb.toString();
    }

    /**
     * Called when a previously created loader has finished its load.
     *
     * @param loader The Loader that has finished.
     * @param data The data generated by the Loader.
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Update the data that the adapter uses to create ViewHolders
        mAdapter.swapCursor(data);
    }


    /**
     * Called when a previously created loader is being reset, and thus
     * making its data unavailable.
     * onLoaderReset removes any references this activity had to the loader's data.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
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
