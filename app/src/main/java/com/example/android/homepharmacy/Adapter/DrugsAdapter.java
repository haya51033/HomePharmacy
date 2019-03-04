package com.example.android.homepharmacy.Adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.homepharmacy.Activity.DrugActivity;
import com.example.android.homepharmacy.Database.DataContract;
import com.example.android.homepharmacy.R;

import java.util.Locale;


public class DrugsAdapter extends RecyclerView.Adapter<DrugsAdapter.DrugsViewHolder> {

    // Class variables for the Cursor that holds task data and the Context
    private Cursor mCursor;
    private Context mContext;
    private DrugsnOnClickHandler mDrugsOnClichkHandler;




    public DrugsAdapter(DrugsnOnClickHandler drugsnOnClickHandler) {
        mDrugsOnClichkHandler = drugsnOnClickHandler;
    }


    /**
     * Called when ViewHolders are created to fill a RecyclerView.
     *
     * @return A new TaskViewHolder that holds the view for each task
     */
    @Override
    public DrugsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();

        // Inflate the task_layout to a view
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.row_drug, parent, false);

        return new DrugsViewHolder(view);
    }


    /**
     * Called by the RecyclerView to display data at a specified position in the Cursor.
     *
     * @param holder The ViewHolder to bind Cursor data to
     * @param position The position of the data in the Cursor
     */
    @Override
    public void onBindViewHolder(DrugsViewHolder holder, int position) {

        boolean isEnglish = Locale.getDefault().getLanguage().equals("en");

        int drug_c_name, drug_s_name;

        // Indices for the _id, drug_c_name, drug_s_name and drug_concentration columns
      int idIndex = mCursor.getColumnIndex(DataContract.DrugsEntry._ID);
      if(isEnglish){
          drug_c_name = mCursor.getColumnIndex(DataContract.DrugsEntry.COLUMN_DRUG_COMMERCIAL_NAME);
          drug_s_name = mCursor.getColumnIndex(DataContract.DrugsEntry.COLUMN_DRUG_SCIENTIFIC_NAME);
      }
      else {
          drug_c_name = mCursor.getColumnIndex(DataContract.DrugsEntry.COLUMN_DRUG_COMMERCIAL_NAME_ARABIC);
          drug_s_name = mCursor.getColumnIndex(DataContract.DrugsEntry.COLUMN_DRUG_SCIENTIFIC_NAME_ARABIC);
      }

      int concentration = mCursor.getColumnIndex(DataContract.DrugsEntry.COLUMN_DRUG_CONCENTRATION);

        mCursor.moveToPosition(position); // get to the right location in the cursor

        // Determine the values of the wanted data
        final int id = mCursor.getInt(idIndex);
        String _CName = mCursor.getString(drug_c_name);
        String _SName = mCursor.getString(drug_s_name);
        String _DC = mCursor.getString(concentration);

        //Set values
       holder.itemView.setTag(id);
       holder.tv_drug_c_name.setText(_CName);
       holder.tv_drug_s_name.setText(_SName);
       holder.tv_concentration.setText(_DC);


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
    class DrugsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv_drug_c_name;
        TextView tv_drug_s_name;
        TextView tv_concentration;

        /**
         * Constructor for the TaskViewHolders.
         *
         * @param itemView The view inflated in onCreateViewHolder
         */
        public DrugsViewHolder(View itemView) {
            super(itemView);

            tv_drug_c_name = (TextView) itemView.findViewById(R.id.tvDrugCName);
            tv_drug_s_name = (TextView) itemView.findViewById(R.id.tvDrugSName);
            tv_concentration = (TextView) itemView.findViewById(R.id.tvDrugC);

            itemView.setOnClickListener(this);

        }
       @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
           mCursor.moveToPosition(position);
           mDrugsOnClichkHandler.onClickDrug(mCursor);

        }

    }

    public interface DrugsnOnClickHandler {
        void onClickDrug(Cursor cursor);
    }
}