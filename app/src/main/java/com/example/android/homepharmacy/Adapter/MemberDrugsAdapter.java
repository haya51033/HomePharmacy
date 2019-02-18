package com.example.android.homepharmacy.Adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.homepharmacy.Activity.CourseActivity;
import com.example.android.homepharmacy.Activity.DrugActivity;
import com.example.android.homepharmacy.Database.DB;
import com.example.android.homepharmacy.Database.DataContract;
import com.example.android.homepharmacy.R;

public class MemberDrugsAdapter extends RecyclerView.Adapter<MemberDrugsAdapter.DrugsViewHolder> {

    // Class variables for the Cursor that holds task data and the Context
    private Cursor mCursor;
    private Context mContext;
    private MemberDrugsAdapter.DrugsnOnClickHandler mDrugsOnClichkHandler;

    SQLiteDatabase mDb;
    DB dbHelper;
    Cursor cur;

    int drug_ID;
    int member_ID;
    Cursor cursor;
    Cursor cursor1;

    private static final String[] DRUG_COLUMNS = {
            DataContract.DrugsEntry._ID,
            DataContract.DrugsEntry.COLUMN_DRUG_COMMERCIAL_NAME,
            DataContract.DrugsEntry.COLUMN_DRUG_COMMERCIAL_NAME_ARABIC,
            DataContract.DrugsEntry.COLUMN_DRUG_SCIENTIFIC_NAME,
            DataContract.DrugsEntry.COLUMN_DRUG_SCIENTIFIC_NAME_ARABIC,
            DataContract.DrugsEntry.COLUMN_DRUG_INDICATION,
            DataContract.DrugsEntry.COLUMN_DRUG_INDICATION_ARABIC,
            DataContract.DrugsEntry.COLUMN_EXPIRY_DATE,
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

    private static final String[] MEMBER_COLUMNS = {
            DataContract.MemberEntry._ID,
            DataContract.MemberEntry.COLUMN_MEMBER_NAME,
            DataContract.MemberEntry.COLUMN_AGE,
            DataContract.MemberEntry.COLUMN_EMAIL,
            DataContract.MemberEntry.COLUMN_GENDER,
            DataContract.MemberEntry.COLUMN_PREGNANT
    };
   /*Intent intent=new Intent(getApplicationContext(), MyHotelReservationActivity.class)
                .putExtra(Intent.EXTRA_TEXT,reservationId);
        startActivity(intent);*/

    /**
     * Constructor for the CustomCursorAdapter that initializes the Context.
     *
     * @param mContext the current Context
     */
    public MemberDrugsAdapter(Context mContext) {
        this.mContext = mContext;
    }


    /**
     * Called when ViewHolders are created to fill a RecyclerView.
     *
     * @return A new TaskViewHolder that holds the view for each task
     */
    @Override
    public DrugsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Inflate the task_layout to a view
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.row_drug, parent, false);

        return new DrugsViewHolder(view);
    }

    public Cursor getSingleDrug(){
        cur = mContext.getContentResolver().query(DataContract.DrugsEntry.CONTENT_URI, DRUG_COLUMNS,"_id='"+drug_ID+"'",null,null,null);
        return cur;
    }
    public Cursor getSingleMember(){
        cur = mContext.getContentResolver().query(DataContract.MemberEntry.CONTENT_URI, MEMBER_COLUMNS,"_id='"+member_ID+"'",null,null,null);
        return cur;
    }
    /**
     * Called by the RecyclerView to display data at a specified position in the Cursor.
     *
     * @param holder   The ViewHolder to bind Cursor data to
     * @param position The position of the data in the Cursor
     */

    @Override
    public void onBindViewHolder(MemberDrugsAdapter.DrugsViewHolder holder, int position) {

        mCursor.moveToPosition(position); // get to the right location in the cursor

        drug_ID = mCursor.getInt(mCursor.getColumnIndex(DataContract.DrugListEntry.COLUMN_DRUG_L_ID));
        member_ID = mCursor.getInt(mCursor.getColumnIndex(DataContract.DrugListEntry.COLUMN_MEMBER_L_ID));

        cursor = getSingleDrug();
        cursor1 = getSingleMember();
        int drug_c_name, drug_s_name;

        cursor.moveToFirst();
        cursor1.moveToFirst();

        // Indices for the _id, drug_c_name, drug_s_name and drug_concentration columns
        int idIndex = cursor.getColumnIndex(DataContract.DrugsEntry._ID);
        drug_c_name = cursor.getColumnIndex(DataContract.DrugsEntry.COLUMN_DRUG_COMMERCIAL_NAME);
        drug_s_name = cursor.getColumnIndex(DataContract.DrugsEntry.COLUMN_DRUG_SCIENTIFIC_NAME);
        int concentration = cursor.getColumnIndex(DataContract.DrugsEntry.COLUMN_DRUG_CONCENTRATION);
        /** **** ARABIC *****
         * drug_c_name = mCursor.getColumnIndex(DataContract.DrugsEntry.COLUMN_DRUG_COMMERCIAL_NAME_ARABIC);
         * drug_s_name = mCursor.getColumnIndex(DataContract.DrugsEntry.COLUMN_DRUG_SCIENTIFIC_NAME_ARABIC);
         * */



        // Determine the values of the wanted data
        final int id = cursor.getInt(idIndex);
        String _CName = cursor.getString(drug_c_name);
        String _SName = cursor.getString(drug_s_name);
        String _DC = cursor.getString(concentration);

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

            int courseId = mCursor.getInt(mCursor.getColumnIndex(
                    DataContract.DrugListEntry._ID));

            Intent intent = new Intent(mContext, CourseActivity.class)
                    .putExtra(Intent.EXTRA_TEXT, courseId);
            mContext.startActivity(intent);

        }
    }

    public interface DrugsnOnClickHandler {
        void onClickDrug(Cursor cursor);
    }
}