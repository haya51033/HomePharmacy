package com.example.android.homepharmacy.Activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.homepharmacy.Database.DB;
import com.example.android.homepharmacy.Database.DataContract;
import com.example.android.homepharmacy.R;

import java.sql.Timestamp;
import java.util.Calendar;

public class NewCourseActivity extends BaseActivity {

    Intent intent;
    int memberId;

    EditText  et4, et5, et6;
    TextView et1, et2;
    Button button;
    Spinner spinner;

    SQLiteDatabase mDb;
    DB dbHelper;


    int drugId;
    Intent intent1;

    private Calendar calendar;
    private int year, month, day;
    String spinnerValue;
    String FirstTime = "00:00:00";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupSharedPreferences();

        setContentView(R.layout.activity_new_course);


        intent = this.getIntent();
        drugId = intent.getIntExtra(Intent.EXTRA_TEXT,0);

        intent1 = this.getIntent();
        memberId = intent1.getIntExtra("memberId",0);


        dbHelper = new DB(this);
        mDb = dbHelper.getWritableDatabase();

        et1 = (TextView) findViewById(R.id.etStartDate);
        et2 = (TextView) findViewById(R.id.etEndDate);
        spinner = (Spinner) findViewById(R.id.etFirstTime);
        et4 = (EditText) findViewById(R.id.etDoseQ);
        et5 = (EditText) findViewById(R.id.etDoseR);
        et6 = (EditText) findViewById(R.id.etDescription);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);
        showDate2(year, month+1, day);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinnerValue =adapterView.getItemAtPosition(i).toString();
                setFirstTime(spinnerValue);
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }

        });


        button = (Button) findViewById(R.id.btnAdd);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(drugId != 0 && et1.getText().toString().trim().length() != 0
                        && et2.getText().toString().trim().length() != 0
                        && et4.getText().toString().trim().length() != 0
                        && et5.getText().toString().trim().length() != 0
                        && et6.getText().toString().trim().length() != 0  ) {

                    addCourse(drugId ,memberId, et1.getText().toString(),
                            et2.getText().toString(), FirstTime,  et4.getText().toString(),
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




    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
    }

    public void setDate2(View view) {
        showDialog(888);
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        if(id == 888){
            return new DatePickerDialog(this,
                    myDateListener2, year, month, day);
        }

        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    showDate(arg1, arg2+1, arg3);

                }
            };

    private DatePickerDialog.OnDateSetListener myDateListener2 = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    showDate2(arg1, arg2+1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        et1.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }

    private void showDate2(int year, int month, int day) {
        et2.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }

    private void addCourse(int _drugId, int _memberId, String _start,
                           String _end, String _firstTime,  String _doseQ,
                           String _doseR, String _description) {
        ContentValues cv = new ContentValues();
        cv.put(DataContract.DrugListEntry.COLUMN_DRUG_L_ID, _drugId);
        cv.put(DataContract.DrugListEntry.COLUMN_MEMBER_L_ID, _memberId);
        cv.put(DataContract.DrugListEntry.COLUMN_DRUG_START_DATE, _start);
        cv.put(DataContract.DrugListEntry.COLUMN_DRUG_FIRST_TIME, _firstTime);
        cv.put(DataContract.DrugListEntry.COLUMN_DRUG_END_DATE, _end);
        cv.put(DataContract.DrugListEntry.COLUMN_DRUG_DOSE_QUANTITY, _doseQ);
        cv.put(DataContract.DrugListEntry.COLUMN_DRUG_DOSE_REPEAT, _doseR);
        cv.put(DataContract.DrugListEntry.COLUMN_DRUG_DOSE_DESCRIPTION, _description);


        Uri uri = getContentResolver().insert(DataContract.DrugListEntry.CONTENT_URI, cv);

        finish();
    }


    public void setFirstTime(String selectedTime){
        switch (selectedTime){
            case "12 AM":
                FirstTime = "00:00:00";
                break;
            case "1 AM":
                FirstTime = "01:00:00";
                break;
            case "2 AM":
                FirstTime = "02:00:00";
                break;
            case "3 AM":
                FirstTime = "03:00:00";
                break;
            case "4 AM":
                FirstTime = "04:00:00";
                break;
            case "5 AM":
                FirstTime = "05:00:00";
                break;
            case "6 AM":
                FirstTime = "06:00:00";
                break;
            case "7 AM":
                FirstTime = "07:00:00";
                break;
            case "8 AM":
                FirstTime = "08:00:00";
                break;
            case "9 AM":
                FirstTime = "09:00:00";
                break;
            case "10 AM":
                FirstTime = "10:00:00";
                break;
            case "11 AM":
                FirstTime = "11:00:00";
                break;
            case "12 PM":
                FirstTime = "12:00:00";
                break;
            case "1 PM":
                FirstTime = "13:00:00";
                break;
            case "2 PM":
                FirstTime = "14:00:00";
                break;
            case "3 PM":
                FirstTime = "15:00:00";
                break;
            case "4 PM":
                FirstTime = "16:00:00";
                break;
            case "5 PM":
                FirstTime = "17:00:00";
                break;
            case "6 PM":
                FirstTime = "18:00:00";
                break;
            case "7 PM":
                FirstTime = "19:00:00";
                break;
            case "8 PM":
                FirstTime = "20:00:00";
                break;
            case "9 PM":
                FirstTime = "21:00:00";
                break;
            case "10 PM":
                FirstTime = "22:00:00";
                break;
            case "11 PM":
                FirstTime = "23:00:00";
                break;
                default:
                    FirstTime = "00:00:00";
                    break;
        }
    }
}
