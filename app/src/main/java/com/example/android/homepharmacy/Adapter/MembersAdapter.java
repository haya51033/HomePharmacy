package com.example.android.homepharmacy.Adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.homepharmacy.Activity.MemberActivity;
import com.example.android.homepharmacy.Database.DataContract;
import com.example.android.homepharmacy.R;
import com.squareup.picasso.Picasso;

public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.MembersViewHolder> {

    // Class variables for the Cursor that holds task data and the Context
    private Cursor mCursor;
    private Context mContext;
    private MembersAdapter.MembersOnClickHandler mMembersOnClichkHandler;


   /*Intent intent=new Intent(getApplicationContext(), MyHotelReservationActivity.class)
                .putExtra(Intent.EXTRA_TEXT,reservationId);
        startActivity(intent);*/

    /**
     * Constructor for the CustomCursorAdapter that initializes the Context.
     *
     * @param mContext the current Context
     */
    public MembersAdapter(Context mContext) {
        this.mContext = mContext;
    }


    /**
     * Called when ViewHolders are created to fill a RecyclerView.
     *
     * @return A new TaskViewHolder that holds the view for each task
     */
    @Override
    public MembersAdapter.MembersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Inflate the task_layout to a view
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.row_member, parent, false);

        return new MembersAdapter.MembersViewHolder(view);
    }


    /**
     * Called by the RecyclerView to display data at a specified position in the Cursor.
     *
     * @param holder The ViewHolder to bind Cursor data to
     * @param position The position of the data in the Cursor
     */
    @Override
    public void onBindViewHolder(MembersAdapter.MembersViewHolder holder, int position) {

        int member_name, member_gender;

        // Indices for the _id, drug_c_name, drug_s_name and drug_concentration columns
        int idIndex = mCursor.getColumnIndex(DataContract.MemberEntry._ID);
        member_name = mCursor.getColumnIndex(DataContract.MemberEntry.COLUMN_MEMBER_NAME);
        member_gender = mCursor.getColumnIndex(DataContract.MemberEntry.COLUMN_GENDER);

        mCursor.moveToPosition(position); // get to the right location in the cursor

        // Determine the values of the wanted data
        final int id = mCursor.getInt(idIndex);
        String _MName = mCursor.getString(member_name);
        String gender = mCursor.getString(member_gender);

        //Set values
        holder.itemView.setTag(id);
        holder.tv_member_name.setText(_MName);
        ImageView iv = holder.iv_member_icon;

        if(gender.equals("Female")){
            Picasso.with(mContext).load(R.drawable.women_icon).resize(300,300).into(iv);
        }
        else {
            Picasso.with(mContext).load(R.drawable.man_icon).resize(300,300).into(iv);
        }



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
    class MembersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tv_member_name;
        ImageView iv_member_icon;

        /**
         * Constructor for the TaskViewHolders.
         *
         * @param itemView The view inflated in onCreateViewHolder
         */
        public MembersViewHolder(View itemView) {
            super(itemView);

            tv_member_name = (TextView) itemView.findViewById(R.id.tvMemberName);
            iv_member_icon = (ImageView) itemView.findViewById(R.id.imageViewMember);

            itemView.setOnClickListener(this);

        }
        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mCursor.moveToPosition(position);

            int memberId = mCursor.getInt(mCursor.getColumnIndex(
                    DataContract.MemberEntry._ID));

            Intent intent=new Intent(mContext, MemberActivity.class)
                    .putExtra("memberId", memberId);
            mContext.startActivity(intent);

        }
    }

    public interface MembersOnClickHandler {
        void onClickMember(Cursor cursor);
    }
}