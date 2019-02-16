package com.example.android.homepharmacy.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.android.homepharmacy.Database.DataContract;
import com.example.android.homepharmacy.R;

public class CourseActivity extends AppCompatActivity {

    Intent intent;
    int courseId;
    Cursor cur;
    String _DRUG_DOSE_DESCRIPTION,
     _DRUG_END_DATE,DRUG_FIRST_TIME, _DRUG_START_DATE;

    int _DRUG_DOSE_QUANTITY, _DRUG_DOSE_REPEAT,
            _DRUG_L_ID, _MEMBER_L_ID;

    private static final String[] DRUG_LIST_COLUMNS = {
            DataContract.DrugListEntry._ID,
            DataContract.DrugListEntry.COLUMN_DRUG_DOSE_DESCRIPTION,
            DataContract.DrugListEntry.COLUMN_DRUG_DOSE_QUANTITY,
            DataContract.DrugListEntry.COLUMN_DRUG_DOSE_REPEAT,
            DataContract.DrugListEntry.COLUMN_DRUG_END_DATE,
            DataContract.DrugListEntry.COLUMN_DRUG_FIRST_TIME,
            DataContract.DrugListEntry.COLUMN_DRUG_L_ID,
            DataContract.DrugListEntry.COLUMN_DRUG_START_DATE,
            DataContract.DrugListEntry.COLUMN_MEMBER_L_ID
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        intent = this.getIntent();
        courseId = intent.getIntExtra(Intent.EXTRA_TEXT,0);
        Cursor cursor = getSingleCourse();

        if (cursor.moveToFirst()){
            _DRUG_DOSE_DESCRIPTION = cursor.getString(cursor.getColumnIndex("drug_dose_description"));
            _DRUG_DOSE_QUANTITY = cursor.getInt(cursor.getColumnIndex("drug_dose_quantity"));
            _DRUG_DOSE_REPEAT =cursor.getInt(cursor.getColumnIndex("drug_dose_repeat"));
            _DRUG_END_DATE = cursor.getString(cursor.getColumnIndex("end_date"));
            _DRUG_L_ID = cursor.getInt(cursor.getColumnIndex("drug_list_id"));
            _DRUG_START_DATE = cursor.getString(cursor.getColumnIndex("start_date"));
            _MEMBER_L_ID = cursor.getInt(cursor.getColumnIndex("member_list_id"));
            DRUG_FIRST_TIME = cursor.getString(cursor.getColumnIndex("drug_first_time"));

            cursor.close();


        }


    }


    public Cursor getSingleCourse(){
        cur = getContentResolver().query(DataContract.DrugListEntry.CONTENT_URI, DRUG_LIST_COLUMNS,"_id='"+courseId+"'",null,null,null);
        return cur;
    }
}

