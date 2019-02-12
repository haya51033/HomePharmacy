package com.example.android.homepharmacy.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.homepharmacy.Database.DataContract;
import com.example.android.homepharmacy.R;

public class DrugActivity extends AppCompatActivity {
    Intent intent;
    int drugId;
    Cursor cur;
    Cursor cursor;
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
    String _DRUG_COMMERCIAL_NAME, _DRUG_COMMERCIAL_NAME_ARABIC, _DRUG_SCIENTIFIC_NAME,
   _DRUG_SCIENTIFIC_NAME_ARABIC, _DRUG_INDICATION, _DRUG_INDICATION_ARABIC,
    _EXPIRY_DATE, _DRUG_CONCENTRATION, _DRUG_TYPE, _DRUG_TYPE_ARABIC,
    _DRUG_WARNINGS, _DRUG_WARNINGS_ARABIC, _SIDE_EFFECTS,
   _SIDE_EFFECTS_ARABIC, _PREGNENT_ALLOWED, _DRUG_DESCRIPTION,
    _DRUG_DESCRIPTION_ARABIC, _DRUG_BARCODE;

    TextView tv, tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8, tv9;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drug);

        intent = this.getIntent();
        drugId = intent.getIntExtra(Intent.EXTRA_TEXT,0);
        cursor = getSingleDrug();

        if (cursor.moveToFirst()){// data?
            _DRUG_COMMERCIAL_NAME =  cursor.getString(cursor.getColumnIndex("commercial_name"));
            _DRUG_SCIENTIFIC_NAME =  cursor.getString(cursor.getColumnIndex("scientific_name"));
            _DRUG_INDICATION =  cursor.getString(cursor.getColumnIndex("indication"));
            _DRUG_TYPE =  cursor.getString(cursor.getColumnIndex("type"));
            _DRUG_WARNINGS =  cursor.getString(cursor.getColumnIndex("warnings"));
            _SIDE_EFFECTS =  cursor.getString(cursor.getColumnIndex("side_effects"));
            _DRUG_DESCRIPTION =  cursor.getString(cursor.getColumnIndex("drug_description"));

            _DRUG_COMMERCIAL_NAME_ARABIC = cursor.getString(cursor.getColumnIndex("commercial_name_arabic"));
            _DRUG_SCIENTIFIC_NAME_ARABIC = cursor.getString(cursor.getColumnIndex("scientific_name_arabic"));
            _DRUG_INDICATION_ARABIC = cursor.getString(cursor.getColumnIndex("indication_arabic"));
            _DRUG_TYPE_ARABIC =  cursor.getString(cursor.getColumnIndex("type_arabic"));
            _DRUG_WARNINGS_ARABIC =  cursor.getString(cursor.getColumnIndex("warnings_arabic"));
            _SIDE_EFFECTS_ARABIC =  cursor.getString(cursor.getColumnIndex("side_effects_arabic"));
            _DRUG_DESCRIPTION_ARABIC =  cursor.getString(cursor.getColumnIndex("drug_description_arabic"));

            _EXPIRY_DATE =  cursor.getString(cursor.getColumnIndex("expiry_date"));
            _DRUG_CONCENTRATION = cursor.getString(cursor.getColumnIndex("concentration"));
            _PREGNENT_ALLOWED =  cursor.getString(cursor.getColumnIndex("pregnant_allowed"));
            _DRUG_BARCODE =  cursor.getString(cursor.getColumnIndex("drug_barcode"));

            cursor.close();

            tv = (TextView) findViewById(R.id.tvDrugCName);
            tv.setText(_DRUG_COMMERCIAL_NAME);

            tv1 = (TextView) findViewById(R.id.tvDrugSName);
            tv1.setText(_DRUG_SCIENTIFIC_NAME);

            tv2 = (TextView) findViewById(R.id.tv_drug_type);
            tv2.setText(_DRUG_TYPE);

            tv3 = (TextView) findViewById(R.id.tv_drug_indication);
            tv3.setText(_DRUG_INDICATION);

            tv4 = (TextView) findViewById(R.id.tv_drug_side_effects);
            tv4.setText(_SIDE_EFFECTS);

            tv5 = (TextView) findViewById(R.id.tv_pregnant_allowed);
            tv5.setText(_PREGNENT_ALLOWED);

            tv6 = (TextView) findViewById(R.id.tv_drug_description);
            tv6.setText(_DRUG_DESCRIPTION);

            tv7 = (TextView) findViewById(R.id.tv_expiry_date);
            tv7.setText(_EXPIRY_DATE);

            tv8 = (TextView) findViewById(R.id.tvDrugC);
            tv8.setText(_DRUG_CONCENTRATION);

            tv9 = (TextView) findViewById(R.id.tv_drug_warnings);
            tv9.setText(_DRUG_WARNINGS);

            /** ARABIC
             *  tv.setText(_DRUG_COMMERCIAL_NAME_ARABIC);
             *  tv1.setText(_DRUG_SCIENTIFIC_NAME_ARABIC);
             *  tv2.setText(_DRUG_TYPE_ARABIC);
             *  tv3.setText(_DRUG_INDICATION_ARABIC);
             *  tv4.setText(_SIDE_EFFECTS_ARABIC);
             *  tv6.setText(_DRUG_DESCRIPTION_ARABIC);
             *  tv9.setText(_DRUG_WARNINGS_ARABIC);*/
        }
    }

    public Cursor getSingleDrug(){
        cur = getContentResolver().query(DataContract.DrugsEntry.CONTENT_URI, DRUG_COLUMNS,"_id='"+drugId+"'",null,null,null);
        return cur;
    }
}
