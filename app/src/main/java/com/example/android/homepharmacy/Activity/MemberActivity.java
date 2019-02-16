package com.example.android.homepharmacy.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.homepharmacy.Adapter.DrugsAdapter;
import com.example.android.homepharmacy.Adapter.MemberDrugsAdapter;
import com.example.android.homepharmacy.Adapter.MembersAdapter;
import com.example.android.homepharmacy.Database.DataContract;
import com.example.android.homepharmacy.R;
import com.squareup.picasso.Picasso;

public class MemberActivity extends AppCompatActivity  implements
        LoaderManager.LoaderCallbacks<Cursor>,
        MemberDrugsAdapter.DrugsnOnClickHandler {

    // Constants for logging and referring to a unique loader
    private static final String TAG = "ANY";
    private static final int TASK_LOADER_ID = 0;

    // Member variables for the adapter and RecyclerView
    private MemberDrugsAdapter mAdapter;
    RecyclerView mRecyclerView;
    Uri uri;
    Context context;
    Intent intent;
    int memberId;
    Cursor cursor;
    Cursor cur;

    private static final String[] MEMBER_COLUMN = {
            DataContract.MemberEntry._ID,
            DataContract.MemberEntry.COLUMN_PREGNANT,
            DataContract.MemberEntry.COLUMN_GENDER,
            DataContract.MemberEntry.COLUMN_EMAIL,
            DataContract.MemberEntry.COLUMN_AGE,
            DataContract.MemberEntry.COLUMN_MEMBER_NAME
    };

    TextView tv, tv1, tv2;
    ImageView iv, iv2;

    String memName, memEmail, memGender;
    int memAge;
    String[] selectionArgs;
    String memId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);

        intent = this.getIntent();
        memberId = intent.getIntExtra(Intent.EXTRA_TEXT,0);
        cursor = getSingleMember();
    //    memId = String.valueOf(memberId);
        selectionArgs = new String[]{ String.valueOf(memberId) };
        iv = (ImageView) findViewById(R.id.imageViewMember1);
        iv2 = (ImageView) findViewById(R.id.imageViewMember);

        tv =(TextView) findViewById(R.id.tvMemberName);
        tv1 =(TextView) findViewById(R.id.tvEmail);
        tv2 =(TextView) findViewById(R.id.tvAge);


        if (cursor.moveToFirst()){// data?
            memName =  cursor.getString(cursor.getColumnIndex("member_name"));
            memEmail =  cursor.getString(cursor.getColumnIndex("email"));
            memAge =  cursor.getInt(cursor.getColumnIndex("age"));
            memGender =  cursor.getString(cursor.getColumnIndex("gender"));

            cursor.close();

          //  String _memAge =

            tv.setText(memName);
            tv1.setText(memEmail);
            tv2.setText(String.valueOf(memAge));

            if(memGender.equals("Female")){
                iv.setVisibility(View.VISIBLE);
                iv2.setVisibility(View.GONE);
                Picasso.with(getApplicationContext()).load(R.drawable.women_icon).resize(300,300).into(iv);

            }
            else {
                iv2.setVisibility(View.VISIBLE);
                iv.setVisibility(View.GONE);
                Picasso.with(getApplicationContext()).load(R.drawable.man_icon).resize(300,300).into(iv2);
            }

        }

        // Set the RecyclerView to its corresponding view
        mRecyclerView = (RecyclerView) findViewById(R.id.listMemberDrugs);

        // Set the layout for the RecyclerView to be a linear layout, which measures and
        // positions items within a RecyclerView into a linear list
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        // Initialize the adapter and attach it to the RecyclerView
        mAdapter = new MemberDrugsAdapter(this);
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

                getSupportLoaderManager().restartLoader(TASK_LOADER_ID, null, MemberActivity.this);

            }
        }).attachToRecyclerView(mRecyclerView);

        /*
         Set the Floating Action Button (FAB) to its corresponding View.
         Attach an OnClickListener to it, so that when it's clicked, a new intent will be created
         to launch the AddTaskActivity.
         */
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabNewCourse);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent = new Intent(getApplicationContext(), NewCourseActivity.class)
                        .putExtra(Intent.EXTRA_TEXT,memberId);
                startActivity(intent);

            }
        });

        /*
         Ensure a loader is initialized and active. If the loader doesn't already exist, one is
         created, otherwise the last created loader is re-used.
         */
        getSupportLoaderManager().initLoader(TASK_LOADER_ID, null, MemberActivity.this);
    }


    @Override
    public void onClickDrug(Cursor cursor) {
        int drugListId = cursor.getInt(0);
        Intent intent=new Intent(getApplicationContext(), CourseActivity.class)
                .putExtra(Intent.EXTRA_TEXT,drugListId);
        startActivity(intent);
    }

    public Cursor getSingleMember(){
        cur = getContentResolver().query(DataContract.MemberEntry.CONTENT_URI, MEMBER_COLUMN,"_id='"+memberId+"'",null,null,null);
        return cur;
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
                    return getContentResolver().query(DataContract.DrugListEntry.CONTENT_URI,
                            null,
                            DataContract.DrugListEntry.COLUMN_MEMBER_L_ID + "=?",
                            selectionArgs,
                            DataContract.DrugListEntry.COLUMN_DRUG_L_ID);

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
