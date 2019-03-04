package com.example.android.homepharmacy.Activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.homepharmacy.Database.DB;
import com.example.android.homepharmacy.Database.DataContract;
import com.example.android.homepharmacy.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

public class SearchOptions  extends BaseActivity {

    EditText et;
    SQLiteDatabase mDb;
    DB dbHelper;
    Button button;
    String query = "";
    ArrayList drugsResult;
    Spinner spinner;
    int spinnerValue;
    Cursor c;
    int memberId;
    Intent intent;
    final Activity activity = this;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupSharedPreferences();

        setContentView(R.layout.activity_search_options);

        //////CREATE DATABASE
        dbHelper = new DB(this);
        mDb = dbHelper.getWritableDatabase();

        intent = this.getIntent();
        memberId = intent.getIntExtra(Intent.EXTRA_TEXT,0);

            button = (Button) findViewById(R.id.btnSearch);
            et = (EditText) findViewById(R.id.etSearch);
            spinner = (Spinner) findViewById(R.id.spinner);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    //  spinnerValue =adapterView.getItemAtPosition(i).;
                    spinnerValue = spinner.getSelectedItemPosition();
                }

                public void onNothingSelected(AdapterView<?> adapterView) {
                    return;
                }
            });

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    query = et.getText().toString();
                    drugsResult = new ArrayList();

                    switch (spinnerValue){
                        case 0:
                            Toast.makeText(getApplicationContext(),"Please select search option!", Toast.LENGTH_LONG).show();
                            break;
                        case 1:
                            c = getDrugNameMatches(query, null);
                            break;

                        case 2:
                            c = getDrugIndicationMatches(query,null);
                            break;
                        case 3:
                            startScan();
                            break;

                        default:
                            Intent intent = new Intent(getApplicationContext(), DrugsActivity.class);
                            startActivity(intent);
                            break;
                    }

                    if(c != null){
                        if(c.moveToFirst()){
                            while (!c.isAfterLast()) {
                                String id = String.valueOf(c.getInt(c.getColumnIndex("_id")));
                                drugsResult.add(id);
                                c.moveToNext();
                            }

                            Intent intent = new Intent(getApplicationContext(), DrugsActivity.class);
                            Bundle args = new Bundle();
                            intent.putExtra(Intent.EXTRA_TEXT, memberId);
                            args.putSerializable("my_array", drugsResult);
                            intent.putExtra("BUNDLE", args);
                            startActivity(intent);

                        }
                        else {
                            Toast.makeText(getApplicationContext(),"No Result!",Toast.LENGTH_LONG).show();
                        }
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

    public Cursor getDrugIndicationMatches(String query, String[] columns) {
        String[] selectionArgs = new String[] {"%"+query+"%"};

        return getContentResolver().query(DataContract.DrugsEntry.CONTENT_URI,
                null,
                DataContract.DrugsEntry.COLUMN_DRUG_INDICATION + " LIKE ? ",
                selectionArgs,
                DataContract.DrugsEntry.COLUMN_DRUG_COMMERCIAL_NAME);
    }

    /**
     *  ARABIC
     *  **/
    public Cursor getDrugIndicationMatchesArabic(String query, String[] columns) {
        String[] selectionArgs = new String[] {"%"+query+"%"};

        return getContentResolver().query(DataContract.DrugsEntry.CONTENT_URI,
                null,
                DataContract.DrugsEntry.COLUMN_DRUG_INDICATION_ARABIC + " LIKE ? ",
                selectionArgs,
                DataContract.DrugsEntry.COLUMN_DRUG_INDICATION_ARABIC);
    }

    public void startScan(){
        IntentIntegrator integrator = new IntentIntegrator(activity);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan Drug Barcode/ QR Code");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){
            if(result.getContents()==null){
                Toast.makeText(this, "You cancelled the scanning", Toast.LENGTH_LONG).show();
            }
            else {
                c = getDrugScannedCodeMatches(String.valueOf(result.getContents()), null);

                if(c != null){
                    if(c.moveToFirst()){
                        while (!c.isAfterLast()) {
                            String id = String.valueOf(c.getInt(c.getColumnIndex("_id")));
                            drugsResult.add(id);
                            c.moveToNext();
                        }

                        Intent intent = new Intent(getApplicationContext(), DrugsActivity.class);
                        Bundle args = new Bundle();
                        intent.putExtra(Intent.EXTRA_TEXT, memberId);
                        args.putSerializable("my_array", drugsResult);
                        intent.putExtra("BUNDLE", args);
                        startActivity(intent);

                    }
                    else {
                        Toast.makeText(getApplicationContext(),"No Result!",Toast.LENGTH_LONG).show();
                    }
                }

            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public Cursor getDrugScannedCodeMatches(String query, String[] columns) {
        String[] selectionArgs = new String[] {query};

        return getContentResolver().query(DataContract.DrugsEntry.CONTENT_URI,
                null,
                DataContract.DrugsEntry.COLUMN_DRUG_BARCODE + " LIKE ? ",
                selectionArgs,
                DataContract.DrugsEntry.COLUMN_DRUG_COMMERCIAL_NAME);
    }
}
