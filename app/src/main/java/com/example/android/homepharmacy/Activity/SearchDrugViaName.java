package com.example.android.homepharmacy.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import java.util.ArrayList;

public class SearchDrugViaName extends AppCompatActivity {

    EditText et;
    SQLiteDatabase mDb;
    DB dbHelper;
    Button button;
    String query;
    ArrayList drugsResult;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_drug_via_name);
        //////CREATE DATABASE
        dbHelper = new DB(this);
        mDb = dbHelper.getWritableDatabase();

        button = (Button) findViewById(R.id.btnSearch);
        et = (EditText) findViewById(R.id.etSearch);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                query = et.getText().toString();
                drugsResult = new ArrayList();
                Cursor c = getDrugNameMatches(query, null);
                if(c.moveToFirst()){
                    while (!c.isAfterLast()) {
                        String id = String.valueOf(c.getInt(c.getColumnIndex("_id")));
                        drugsResult.add(id);
                        c.moveToNext();
                    }

                    Intent intent = new Intent(getApplicationContext(), DrugsActivity.class);
                    Bundle args = new Bundle();
                    args.putSerializable("my_array", drugsResult);
                    intent.putExtra("BUNDLE", args);
                    startActivity(intent);

                }
                else {
                    Toast.makeText(getApplicationContext(),"No Result!",Toast.LENGTH_LONG).show();
                }

            }
        });

    }



    public Cursor getDrugNameMatches(String query, String[] columns) {
        String[] selectionArgs = new String[] {"%"+query+"%"};

        return getContentResolver().query(DataContract.DrugsEntry.CONTENT_URI,
                null,
                DataContract.DrugsEntry.COLUMN_DRUG_COMMERCIAL_NAME + " LIKE ? ",
                selectionArgs,
                DataContract.DrugsEntry.COLUMN_DRUG_COMMERCIAL_NAME);
    }

    /**
     *  ARABIC
     *  **/
    public Cursor getDrugNameMatchesArabic(String query, String[] columns) {
        String[] selectionArgs = new String[] {"%"+query+"%"};

        return getContentResolver().query(DataContract.DrugsEntry.CONTENT_URI,
                null,
                DataContract.DrugsEntry.COLUMN_DRUG_COMMERCIAL_NAME_ARABIC + " LIKE ? ",
                selectionArgs,
                DataContract.DrugsEntry.COLUMN_DRUG_COMMERCIAL_NAME_ARABIC);
    }
}
