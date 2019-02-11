package com.example.android.homepharmacy.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.android.homepharmacy.Adapter.DrugsAdapter;
import com.example.android.homepharmacy.Database.DataContract;
import com.example.android.homepharmacy.R;

public class DrugsActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>,
        DrugsAdapter.DrugsnOnClickHandler {
    // Constants for logging and referring to a unique loader
    private static final String TAG = "ANY";
    private static final int TASK_LOADER_ID = 0;

    // Member variables for the adapter and RecyclerView
    private DrugsAdapter mAdapter;
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drugs);
        // Set the RecyclerView to its corresponding view
        mRecyclerView = (RecyclerView) findViewById(R.id.listDrugs);

        // Set the layout for the RecyclerView to be a linear layout, which measures and
        // positions items within a RecyclerView into a linear list
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        // Initialize the adapter and attach it to the RecyclerView
        mAdapter = new DrugsAdapter(this);
        mRecyclerView.setAdapter(mAdapter);


        /*
         Add a touch helper to the RecyclerView to recognize when a user swipes to delete an item.
         An ItemTouchHelper enables touch behavior (like swipe and move) on each ViewHolder,
         and uses callbacks to signal when a user is performing these actions.
         */
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                /**
                 * // Here is where you'll implement swipe to delete
                 *
                   // Construct the URI for the item to delete
                   // Use getTag (from the adapter code) to get the id of the swiped item
                   // Retrieve the id of the drug to delete
                      int id = (int) viewHolder.itemView.getTag();

                 * // Build appropriate uri with String row id appended
                    String stringId = Integer.toString(id);
                    Uri uri = DataContract.DrugsEntry.CONTENT_URI;
                    uri = uri.buildUpon().appendPath(stringId).build();
                    //Delete a single row of data using a ContentResolver
                 *  getContentResolver().delete(uri, null, null);
                 *   // Restart the loader to re-query for all drugs after a deletion
                 * **/
                 getSupportLoaderManager().restartLoader(TASK_LOADER_ID, null, DrugsActivity.this);

            }
        }).attachToRecyclerView(mRecyclerView);

        /*
         Set the Floating Action Button (FAB) to its corresponding View.
         Attach an OnClickListener to it, so that when it's clicked, a new intent will be created
         to launch the AddTaskActivity.
         */
        FloatingActionButton fabButton = (FloatingActionButton) findViewById(R.id.fab);

     /*   fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a new intent to start an AddTaskActivity
                Intent addTaskIntent = new Intent(getApplicationContext(), DrugActivity.class);
                startActivity(addTaskIntent);
            }
        });*/

        /*
         Ensure a loader is initialized and active. If the loader doesn't already exist, one is
         created, otherwise the last created loader is re-used.
         */
        getSupportLoaderManager().initLoader(TASK_LOADER_ID, null, this);

    }



    @Override
    public void onClickDrug(Cursor cursor) {
        int drugId = cursor.getInt(0);
        int x=0;
        Intent intent=new Intent(getApplicationContext(), DrugActivity.class)
                .putExtra(Intent.EXTRA_TEXT,drugId);
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

                try {
                    return getContentResolver().query(DataContract.DrugsEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            DataContract.DrugsEntry.COLUMN_DRUG_COMMERCIAL_NAME);

                } catch (Exception e) {
                    Log.e(TAG, "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            }

            // deliverResult sends the result of the load, a Cursor, to the registered listener
            public void deliverResult(Cursor data) {
                mTaskData = data;
                super.deliverResult(data);
            }
        };

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



}