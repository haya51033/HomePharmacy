package com.example.android.homepharmacy.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.homepharmacy.Database.DB;
import com.example.android.homepharmacy.Database.DataContract;
import com.example.android.homepharmacy.R;

import java.sql.Timestamp;

public class NewCourseActivity extends AppCompatActivity {

    Intent intent;
    int memberId;

    EditText  et1, et2, et3, et4, et5, et6;
    Button button;

    SQLiteDatabase mDb;
    DB dbHelper;

    int drugId;
    Intent intent1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_course);



        intent = this.getIntent();
        drugId = intent.getIntExtra(Intent.EXTRA_TEXT,0);

        intent1 = this.getIntent();
        memberId = intent1.getIntExtra("memberId",0);


        dbHelper = new DB(this);
        mDb = dbHelper.getWritableDatabase();

     //   et = (EditText) findViewById(R.id.etDrugID);
        et1 = (EditText) findViewById(R.id.etStartDate);
        et2 = (EditText) findViewById(R.id.etEndDate);
        et3 = (EditText) findViewById(R.id.etFirstTime);
        et4 = (EditText) findViewById(R.id.etDoseQ);
        et5 = (EditText) findViewById(R.id.etDoseR);
        et6 = (EditText) findViewById(R.id.etDescription);

        button = (Button) findViewById(R.id.btnAdd);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(drugId != 0 && et1.getText().toString().trim().length() != 0
                        && et2.getText().toString().trim().length() != 0 && et3.getText().toString().trim().length() != 0
                        && et4.getText().toString().trim().length() != 0 && et5.getText().toString().trim().length() != 0
                        && et6.getText().toString().trim().length() != 0  ) {

                    addCourse(drugId ,memberId, et1.getText().toString(),
                            et2.getText().toString(), et4.getText().toString(),
                            et5.getText().toString(), et6.getText().toString());

                    Toast.makeText(getApplicationContext(),"Course Added Successfully! "  ,Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), MemberActivity.class)
                            .putExtra("memberId",memberId);

                    startActivity(intent);

                }
                else {
                    Toast.makeText(getApplicationContext(),"Please fill in all fields ! ",Toast.LENGTH_LONG).show();
                }
            }
        });

    }




    private void addCourse(int _drugId, int _memberId, String _start,
                           String _end,  String _doseQ,
                           String _doseR, String _description) {
        ContentValues cv = new ContentValues();
        cv.put(DataContract.DrugListEntry.COLUMN_DRUG_L_ID, _drugId);
        cv.put(DataContract.DrugListEntry.COLUMN_MEMBER_L_ID, _memberId);
        cv.put(DataContract.DrugListEntry.COLUMN_DRUG_START_DATE, _start);
        cv.put(DataContract.DrugListEntry.COLUMN_DRUG_END_DATE, _end);
        cv.put(DataContract.DrugListEntry.COLUMN_DRUG_DOSE_QUANTITY, _doseQ);
        cv.put(DataContract.DrugListEntry.COLUMN_DRUG_DOSE_REPEAT, _doseR);
        cv.put(DataContract.DrugListEntry.COLUMN_DRUG_DOSE_DESCRIPTION, _description);


        Uri uri = getContentResolver().insert(DataContract.DrugListEntry.CONTENT_URI, cv);

        finish();
    }
}
