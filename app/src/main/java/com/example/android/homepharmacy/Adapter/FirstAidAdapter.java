package com.example.android.homepharmacy.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.homepharmacy.Activity.DrugActivity;
import com.example.android.homepharmacy.Database.DataContract;
import com.example.android.homepharmacy.R;

import java.util.Locale;


public class FirstAidAdapter extends RecyclerView.Adapter<FirstAidAdapter.FirstAidViewHolder> {

    // Class variables for the Cursor that holds task data and the Context
    private Cursor mCursor;
    private Context mContext;
    private FirstAidOnClickHandler mFirstAidOnClickHandler;
    boolean english = true;
    String languageToLoad = "en";

    public FirstAidAdapter(FirstAidOnClickHandler firstAidOnClickHandler) {
        mFirstAidOnClickHandler = firstAidOnClickHandler;
    }


    /**
     * Called when ViewHolders are created to fill a RecyclerView.
     *
     * @return A new TaskViewHolder that holds the view for each task
     */
    @Override
    public FirstAidViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();

        // Inflate the task_layout to a view
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.row_first_aid, parent, false);

        return new FirstAidViewHolder(view);
    }


    /**
     * Called by the RecyclerView to display data at a specified position in the Cursor.
     *
     * @param holder The ViewHolder to bind Cursor data to
     * @param position The position of the data in the Cursor
     */
    @Override
    public void onBindViewHolder(FirstAidViewHolder holder, int position) {
        boolean isEnglish = Locale.getDefault().getLanguage().equals("en");
        int firstAidTitle;
        // Indices for the _id, drug_c_name, drug_s_name and drug_concentration columns
        int idIndex = mCursor.getColumnIndex(DataContract.FirstAidEntry._ID);
        if(isEnglish){
            firstAidTitle = mCursor.getColumnIndex(DataContract.FirstAidEntry.COLUMN_FIRST_AID_TITLE);
        }
        else {
         firstAidTitle = mCursor.getColumnIndex(DataContract.FirstAidEntry.COLUMN_FIRST_AID_TITLE_ARABIC);
        }


        mCursor.moveToPosition(position); // get to the right location in the cursor

        // Determine the values of the wanted data
        final int id = mCursor.getInt(idIndex);
        String _Title = mCursor.getString(firstAidTitle);


        //Set values
        holder.itemView.setTag(id);
        holder.tv_first_aid_title.setText(_Title);
    }


    /**
     * Returns the number of items to display.
     */
    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }


    /**
     * When data changes and a re-query occurs, this function swaps the old Cursor
     * with a newly updated Cursor (Cursor c) that is passed in.
     */
    public Cursor swapCursor(Cursor c) {
        // check if this cursor is the same as the previous cursor (mCursor)
        if (mCursor == c) {
            return null; // bc nothing has changed
        }
        Cursor temp = mCursor;
        this.mCursor = c; // new cursor value assigned

        //check if this is a valid cursor, then update the cursor
        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }


    // Inner class for creating ViewHolders
    class FirstAidViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv_first_aid_title;


        /**
         * Constructor for the TaskViewHolders.
         *
         * @param itemView The view inflated in onCreateViewHolder
         */
        public FirstAidViewHolder(View itemView) {
            super(itemView);

            tv_first_aid_title = (TextView) itemView.findViewById(R.id.tvFirstAidTitle);


            itemView.setOnClickListener(this);

        }
        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mCursor.moveToPosition(position);
            mFirstAidOnClickHandler.onClickFirstAid(mCursor);
        }

    }

    public interface FirstAidOnClickHandler {
        void onClickFirstAid(Cursor cursor);
    }
}