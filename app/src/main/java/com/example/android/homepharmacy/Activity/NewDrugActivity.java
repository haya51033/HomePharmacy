package com.example.android.homepharmacy.Activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.homepharmacy.Database.DB;
import com.example.android.homepharmacy.Database.DataContract;
import com.example.android.homepharmacy.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.Locale;

public class NewDrugActivity extends BaseActivity {
    final Activity activity = this;
    int userId;
    boolean english;
    String languageToLoad;
    Intent intent;
    int memberId;
    SQLiteDatabase mDb;
    DB dbHelper;
    Intent intent1;
    EditText et, et1, et2, et3, et4, et5, et6,
    et0, et11, et22, et33, et44, et55, et66,
    et7;
    String en_s_name, en_c_name, en_indications, en_warnning, en_side_effect, en_type, en_description,
     ar_s_name, ar_c_name, ar_indications, ar_warnning, ar_side_effect, ar_type, ar_description,
            concentration,  barcode;
    boolean forPragnant = false;
    Spinner spinner;
    Button button, button2, button3, button4, button41n;
    int spinnerValue = 0;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupSharedPreferences();
        Intent intent4 = this.getIntent();
        if( intent4.getStringExtra("lan") != null){
            languageToLoad = intent4.getStringExtra("lan");

        }
        else {
            english = super.english;
            if(english){
                languageToLoad="en";
            }
            else {
                languageToLoad="ar";
            }
        }

        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config,getResources().getDisplayMetrics());

        setContentView(R.layout.activity_new_drug);

        intent1 = this.getIntent();
        memberId = intent1.getIntExtra("memberId",0);
        Intent intent2 = this.getIntent();
        userId = intent2.getIntExtra("userId",0);


        dbHelper = new DB(this);
        mDb = dbHelper.getWritableDatabase();

        ////NAV DRAWER /////
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.new_drug);
        setSupportActionBar(toolbar);

        et = (EditText) findViewById(R.id.et_en_drug_c_name);
        et1 = (EditText) findViewById(R.id.et_en_drug_s_name);
        et2 = (EditText) findViewById(R.id.et_en_drug_indication);
        et3 = (EditText) findViewById(R.id.et_en_drug_warnings);
        et4 = (EditText) findViewById(R.id.et_en_drug_side_effects);
        et5 = (EditText) findViewById(R.id.et_en_drug_description);
        et6 = (EditText) findViewById(R.id.et_en_drug_type);

       button = (Button) findViewById(R.id.btnNewDrug2);
       button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if(et.getText().toString().trim().length() != 0 &&
                       et1.getText().toString().trim().length() != 0 &&
                       et2.getText().toString().trim().length() != 0 &&
                       et3.getText().toString().trim().length() != 0 &&
                       et4.getText().toString().trim().length() != 0 &&
                       et5.getText().toString().trim().length() != 0 &&
                       et6.getText().toString().trim().length() != 0 ){

                   en_c_name = et.getText().toString();
                   en_s_name = et1.getText().toString();
                   en_indications = et2.getText().toString();
                   en_warnning = et3.getText().toString();
                   en_side_effect = et4.getText().toString();
                   en_description = et5.getText().toString();
                   en_type = et6.getText().toString();

                   setContentView(R.layout.activity_new_drug_2);

                   et0 = (EditText) findViewById(R.id.et_ar_drug_c_name);
                   et11 = (EditText) findViewById(R.id.et_ar_drug_s_name);
                   et22 = (EditText) findViewById(R.id.et_ar_drug_indication);
                   et33 = (EditText) findViewById(R.id.et_ar_drug_warnings);
                   et44 = (EditText) findViewById(R.id.et_ar_drug_side_effects);
                   et55 = (EditText) findViewById(R.id.et_ar_drug_description);
                   et66 = (EditText) findViewById(R.id.et_ar_drug_type);

                   ar_c_name = et0.getText().toString();
                   ar_s_name = et11.getText().toString();
                   ar_indications = et22.getText().toString();
                   ar_warnning = et33.getText().toString();
                   ar_side_effect = et44.getText().toString();
                   ar_description = et55.getText().toString();
                   ar_type = et66.getText().toString();

                   button2 = (Button) findViewById(R.id.btnNewDrug2);
                   button2.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {

                           if(et0.getText().toString().trim().length() != 0 &&
                                   et11.getText().toString().trim().length() != 0 &&
                                   et22.getText().toString().trim().length() != 0 &&
                                   et33.getText().toString().trim().length() != 0 &&
                                   et44.getText().toString().trim().length() != 0 &&
                                   et55.getText().toString().trim().length() != 0 &&
                                   et66.getText().toString().trim().length() != 0 ) {
                               ar_c_name = et0.getText().toString();
                               ar_s_name = et11.getText().toString();
                               ar_indications = et22.getText().toString();
                               ar_warnning = et33.getText().toString();
                               ar_side_effect = et44.getText().toString();
                               ar_description = et55.getText().toString();
                               ar_type = et66.getText().toString();

                               setContentView(R.layout.activity_new_drug_3);

                               et7 = (EditText) findViewById(R.id.et_concentration);
                               spinner = (Spinner) findViewById(R.id.spinner1);
                               spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                   @Override
                                   public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                       spinnerValue = adapterView.getSelectedItemPosition();
                                       if(spinnerValue == 0){
                                           forPragnant = true;
                                       } else {
                                           forPragnant = false;
                                       }
                                   }
                                   @Override
                                   public void onNothingSelected(AdapterView<?> adapterView) { }
                               });
                               button3=  findViewById(R.id.btnNewDrug3);
                               button3.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View view) {
                                       if(et7.getText().toString().trim().length() != 0){
                                           concentration = et7.getText().toString();

                                           setContentView(R.layout.activity_new_drug_4);
                                           button4 = (Button) findViewById(R.id.btnNewDrug4);
                                           button41n = (Button) findViewById(R.id.btnNewDrug4no);
                                           // SCAN
                                           button4.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View view) {
                                                   startScan();
                                               }
                                           });

                                           // NO SCAN
                                           button41n.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View view) {
                                                   AddNewDrug(en_c_name, en_s_name, en_indications,
                                                           en_side_effect, en_warnning,
                                                           en_description, en_type,
                                                           ar_c_name, ar_s_name,
                                                           ar_indications,
                                                           ar_side_effect, ar_warnning,
                                                           ar_description, ar_type,
                                                           concentration, forPragnant,  barcode);

                                                   Toast.makeText(getApplicationContext(),en_c_name + " Drug added Successfully!",Toast.LENGTH_LONG).show();

                                                   Intent intent = new Intent(getApplicationContext(), DrugsActivity.class);
                                                   intent.putExtra("userId", userId);
                                                   intent.putExtra("lan", languageToLoad);
                                                   startActivity(intent);
                                               }
                                           });
                                       }
                                   }
                               });

                           }
                           else {
                               Toast.makeText(getApplicationContext(),"Please fill in all fields ! ",Toast.LENGTH_LONG).show();
                           }
                       }
                   });
               }
               else {
                   Toast.makeText(getApplicationContext(),"Please fill in all fields ! ",Toast.LENGTH_LONG).show();
               }
           }
       });



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
                String _code = ""+ String.valueOf(result.getContents());
                if(_code.contains(""+"\u001D"))
                    _code = _code.replace("\u001D", "");

                if(!_code.isEmpty()){
                    barcode = _code;
                    AddNewDrug(en_c_name, en_s_name, en_indications,
                             en_side_effect, en_warnning,
                             en_description, en_type,
                             ar_c_name, ar_s_name,
                             ar_indications,
                             ar_side_effect, ar_warnning,
                             ar_description, ar_type,
                             concentration, forPragnant,  barcode);
                    Toast.makeText(getApplicationContext(),en_c_name + " Drug added Successfully!",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), DrugsActivity.class);
                    intent.putExtra("userId", userId);
                    intent.putExtra("lan", languageToLoad);
                    startActivity(intent);
                }
            }
        }
        else {
            Toast.makeText(this, "Error while Scanning, Please try again.", Toast.LENGTH_LONG).show();
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    public void AddNewDrug(String en_c_name, String en_s_name, String en_indications,
                           String en_side_effect, String en_warnning,
                           String en_description, String en_type,
                           String ar_c_name, String ar_s_name,
                           String ar_indications,
                           String ar_side_effect, String ar_warnning,
                           String ar_description, String ar_type,
                           String concentration, boolean forPragnant, String barcode){

        ContentValues cv = new ContentValues();
        cv.put(DataContract.DrugsEntry.COLUMN_DRUG_COMMERCIAL_NAME, en_c_name);
        cv.put(DataContract.DrugsEntry.COLUMN_DRUG_SCIENTIFIC_NAME, en_s_name);
        cv.put(DataContract.DrugsEntry.COLUMN_DRUG_INDICATION, en_indications);
        cv.put(DataContract.DrugsEntry.COLUMN_SIDE_EFFECTS, en_side_effect);
        cv.put(DataContract.DrugsEntry.COLUMN_DRUG_WARNINGS, en_warnning);
        cv.put(DataContract.DrugsEntry.COLUMN_DRUG_DESCRIPTION, en_description);
        cv.put(DataContract.DrugsEntry.COLUMN_DRUG_TYPE, en_type);

        cv.put(DataContract.DrugsEntry.COLUMN_DRUG_COMMERCIAL_NAME_ARABIC, ar_c_name);
        cv.put(DataContract.DrugsEntry.COLUMN_DRUG_SCIENTIFIC_NAME_ARABIC, ar_s_name);
        cv.put(DataContract.DrugsEntry.COLUMN_DRUG_INDICATION_ARABIC, ar_indications);
        cv.put(DataContract.DrugsEntry.COLUMN_SIDE_EFFECTS_ARABIC, ar_side_effect);
        cv.put(DataContract.DrugsEntry.COLUMN_DRUG_WARNINGS_ARABIC, ar_warnning);
        cv.put(DataContract.DrugsEntry.COLUMN_DRUG_DESCRIPTION_ARABIC, ar_description);
        cv.put(DataContract.DrugsEntry.COLUMN_DRUG_TYPE_ARABIC, ar_type);

        cv.put(DataContract.DrugsEntry.COLUMN_DRUG_CONCENTRATION, concentration);
        cv.put(DataContract.DrugsEntry.COLUMN_PREGNENT_ALLOWED, forPragnant);
        cv.put(DataContract.DrugsEntry.COLUMN_DRUG_BARCODE, barcode);


        Uri uri = getContentResolver().insert(DataContract.DrugsEntry.CONTENT_URI, cv);

        finish();
    }

}
