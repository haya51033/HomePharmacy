package com.example.android.homepharmacy.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.homepharmacy.Database.DataContract;
import com.example.android.homepharmacy.R;

public class CourseActivity extends AppCompatActivity {

    Intent intent;
    int courseId;
    int memberId;
    int drugId;

    Cursor cur;
    Cursor cur1;
    Cursor cur2;

    String _DRUG_DOSE_DESCRIPTION,
     _DRUG_END_DATE,DRUG_FIRST_TIME,
     _DRUG_START_DATE, drug_s_name,
     drug_c_name, drug_c, memberName;

    int _DRUG_DOSE_QUANTITY, _DRUG_DOSE_REPEAT;

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

    TextView tv, tv1, tv2, tv3, tv4, tv5, tv6, tv7;

    Button button1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        intent = this.getIntent();
        courseId = intent.getIntExtra(Intent.EXTRA_TEXT,0);

        button1 =(Button) findViewById(R.id.btnDelete);
        Cursor cursor = getSingleCourse();

        if (cursor.moveToFirst()){
            _DRUG_DOSE_DESCRIPTION = cursor.getString(cursor.getColumnIndex("drug_dose_description"));
            _DRUG_DOSE_QUANTITY = cursor.getInt(cursor.getColumnIndex("drug_dose_quantity"));
            _DRUG_DOSE_REPEAT =cursor.getInt(cursor.getColumnIndex("drug_dose_repeat"));
            _DRUG_END_DATE = cursor.getString(cursor.getColumnIndex("end_date"));
            _DRUG_START_DATE = cursor.getString(cursor.getColumnIndex("start_date"));
            DRUG_FIRST_TIME = cursor.getString(cursor.getColumnIndex("drug_first_time"));

            memberId = cursor.getInt(cursor.getColumnIndex("member_list_id"));
            drugId = cursor.getInt(cursor.getColumnIndex("drug_list_id"));
            cursor.close();

            tv4 = (TextView) findViewById(R.id.tv_drug_start_date);
            tv4.setText(_DRUG_START_DATE);
            tv5 = (TextView) findViewById(R.id.tv_drug_end_date);
            tv5.setText(_DRUG_END_DATE);
            tv6 = (TextView) findViewById(R.id.tv_drug_dose);
            tv6.setText(_DRUG_DOSE_QUANTITY+ getResources().getString(R.string.slash) + _DRUG_DOSE_REPEAT);
            tv7 = (TextView) findViewById(R.id.tv_drug_description);
            tv7.setText(_DRUG_DOSE_DESCRIPTION);

            Cursor cursor1 = getSingleMember();
            if(cursor1.moveToFirst()){
                memberName = cursor1.getString(cursor1.getColumnIndex("member_name"));
                cursor1.close();
                tv3 = (TextView) findViewById(R.id.tv_drug_for);
                tv3.setText(memberName);
            }

            Cursor cursor2 = getSingleDrug();
            if(cursor2.moveToFirst()){
                drug_c_name = cursor2.getString(cursor2.getColumnIndex("commercial_name"));
                drug_s_name = cursor2.getString(cursor2.getColumnIndex("scientific_name"));
                drug_c = cursor2.getString(cursor2.getColumnIndex("concentration"));

                cursor2.close();
                tv = (TextView) findViewById(R.id.tvDrugCName);
                tv1 = (TextView) findViewById(R.id.tvDrugSName);
                tv2 = (TextView) findViewById(R.id.tvDrugC);
                tv.setText(drug_c_name);
                tv1.setText(drug_s_name);
                tv2.setText(drug_c);

                /** Arabic
                 * drug_c_name = cursor.getString(cursor.getColumnIndex("commercial_name_arabic"));
                 * drug_s_name = cursor.getString(cursor.getColumnIndex("scientific_name_arabic"));
                 */
            }
        }


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
                        .putExtra("memberId" ,memberId);
                startActivity(intent);

            }
        });

    }


    public Cursor getSingleCourse(){
        cur = getContentResolver().query(DataContract.DrugListEntry.CONTENT_URI, DRUG_LIST_COLUMNS,"_id='"+courseId+"'",null,null,null);
        return cur;
    }
    public Cursor getSingleMember(){
        cur1 = getContentResolver().query(DataContract.MemberEntry.CONTENT_URI, MEMBER_COLUMNS,"_id='"+memberId+"'",null,null,null);
        return cur1;
    }
    public Cursor getSingleDrug(){
        cur2 = getContentResolver().query(DataContract.DrugsEntry.CONTENT_URI, DRUG_COLUMNS,"_id='"+drugId+"'",null,null,null);
        return cur2;
    }

}

