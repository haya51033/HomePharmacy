package com.example.android.homepharmacy.Database;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import com.example.android.homepharmacy.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class DB extends SQLiteOpenHelper {
    // The database name
    private static final String DATABASE_NAME = "HOMEPHARMACY.db";

    // If you change the database schema, you must increment the database version
    private static final int DATABASE_VERSION = 2;
    private Context _context;


    // Constructor
    public DB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        _context =context;
        context = this._context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

       final String SQL_CREATE_USER_TABLE = "CREATE TABLE " + DataContract.UserEntry.TABLE_NAME + " (" +
                DataContract.UserEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DataContract.UserEntry.COLUMN_FULL_NAME+ " TEXT NOT NULL, " +
                DataContract.UserEntry.COLUMN_USER_NAME + " TEXT NOT NULL, " +
                DataContract.UserEntry.COLUMN_EMAIL + " TEXT NOT NULL, " +
                DataContract.UserEntry.COLUMN_PASSWORD + " TEXT NOT NULL, " +
                DataContract.UserEntry.COLUMN_REMINDER_QUESTION + " TEXT NOT NULL " +

                " );";


        final String SQL_CREATE_MEMBERS_TABLE = "CREATE TABLE " + DataContract.MemberEntry.TABLE_NAME + " (" +
                DataContract.MemberEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DataContract.MemberEntry.COLUMN_MEMBER_NAME +" TEXT NOT NULL, " +
                DataContract.MemberEntry.COLUMN_AGE +" INTEGER NOT NULL, " +
                DataContract.MemberEntry.COLUMN_GENDER + " TEXT NOT NULL, " +
                DataContract.MemberEntry.COLUMN_EMAIL + " TEXT NOT NULL, " +
                DataContract.MemberEntry.COLUMN_PREGNANT + " BOOLEAN NOT NULL " +

                " );";


        final String SQL_CREATE_DRUGS_TABLE = "CREATE TABLE " + DataContract.DrugsEntry.TABLE_NAME + " (" +
                DataContract.DrugsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DataContract.DrugsEntry.COLUMN_DRUG_COMMERCIAL_NAME+ " TEXT NOT NULL, " +
                DataContract.DrugsEntry.COLUMN_DRUG_COMMERCIAL_NAME_ARABIC+ " TEXT NOT NULL, " +
                DataContract.DrugsEntry.COLUMN_DRUG_SCIENTIFIC_NAME+ " TEXT NOT NULL, " +
                DataContract.DrugsEntry.COLUMN_DRUG_SCIENTIFIC_NAME_ARABIC+ " TEXT NOT NULL, " +
                DataContract.DrugsEntry.COLUMN_DRUG_INDICATION+ " TEXT NOT NULL, " +
                DataContract.DrugsEntry.COLUMN_DRUG_INDICATION_ARABIC+ " TEXT NOT NULL, " +
                DataContract.DrugsEntry.COLUMN_EXPIRY_DATE+ " TEXT NOT NULL, " +
                DataContract.DrugsEntry.COLUMN_DRUG_CONCENTRATION+ " TEXT NOT NULL, " +
                DataContract.DrugsEntry.COLUMN_DRUG_TYPE+ " TEXT NOT NULL, " +
                DataContract.DrugsEntry.COLUMN_DRUG_TYPE_ARABIC+ " TEXT NOT NULL, " +
                DataContract.DrugsEntry.COLUMN_DRUG_WARNINGS+ " TEXT NOT NULL, " +
                DataContract.DrugsEntry.COLUMN_DRUG_WARNINGS_ARABIC+ " TEXT NOT NULL, " +
                DataContract.DrugsEntry.COLUMN_SIDE_EFFECTS+ " TEXT NOT NULL, " +
                DataContract.DrugsEntry.COLUMN_SIDE_EFFECTS_ARABIC+ " TEXT NOT NULL, " +
                DataContract.DrugsEntry.COLUMN_PREGNENT_ALLOWED+ " BOOLEAN NOT NULL, " +
                DataContract.DrugsEntry.COLUMN_DRUG_DESCRIPTION+ " TEXT NOT NULL, " +
                DataContract.DrugsEntry.COLUMN_DRUG_DESCRIPTION_ARABIC+ " TEXT NOT NULL, " +
                DataContract.DrugsEntry.COLUMN_DRUG_BARCODE+ " TEXT NOT NULL" +

                " );";


        final String SQL_CREATE_DRUG_LIST_TABLE = "CREATE TABLE " + DataContract.DrugListEntry.TABLE_NAME + " (" +
                DataContract.DrugListEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DataContract.DrugListEntry.COLUMN_DRUG_L_ID+ " INTEGER NOT NULL, " +
                DataContract.DrugListEntry.COLUMN_MEMBER_L_ID+ " INTEGER NOT NULL, " +
                DataContract.DrugListEntry.COLUMN_DRUG_START_DATE+ " TEXT NOT NULL, " +
                DataContract.DrugListEntry.COLUMN_DRUG_END_DATE+ " TEXT NOT NULL, " +
                DataContract.DrugListEntry.COLUMN_DRUG_DOSE_QUANTITY+ " TEXT NOT NULL, " +
                DataContract.DrugListEntry.COLUMN_DRUG_DOSE_REPEAT+ " TEXT NOT NULL, " +
                DataContract.DrugListEntry.COLUMN_DRUG_DOSE_DESCRIPTION+ " TEXT NOT NULL, " +
                DataContract.DrugListEntry.COLUMN_DRUG_FIRST_TIME + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +


                " );";


        final String SQL_CREATE_FIRST_AID_TABLE = "CREATE TABLE " + DataContract.FirstAidEntry.TABLE_NAME + " (" +
                DataContract.FirstAidEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DataContract.FirstAidEntry.COLUMN_FIRST_AID_TITLE+ " TEXT NOT NULL, " +
                DataContract.FirstAidEntry.COLUMN_FIRST_AID_DESCRIPTION+ " TEXT NOT NULL, " +
                DataContract.FirstAidEntry.COLUMN_FIRST_AID_LINK+ " TEXT NOT NULL, " +
                DataContract.FirstAidEntry.COLUMN_FIRST_AID_IMAGE+ " TEXT NOT NULL, " +
                DataContract.FirstAidEntry.COLUMN_FIRST_AID_DRUG+ " INTEGER NOT NULL " +

                " );";


        db.execSQL(SQL_CREATE_USER_TABLE);
        db.execSQL(SQL_CREATE_DRUGS_TABLE);
        db.execSQL(SQL_CREATE_MEMBERS_TABLE);
        db.execSQL(SQL_CREATE_DRUG_LIST_TABLE);
        db.execSQL(SQL_CREATE_FIRST_AID_TABLE);
        Log.i(TAG, "SQLite Created successfully !!! ");

        ////////////// DRUG TABLE DATA FROM ML FILE ///////
        ContentValues _Values = new ContentValues();
        //Get xml resource file
        Resources res = _context.getResources();

        //Open xml file
        XmlResourceParser _xml = res.getXml(R.xml.drug_data);
        try
        {
            //Check for end of document
            int eventType = _xml.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                //Search for record tags
                if ((eventType == XmlPullParser.START_TAG) &&(_xml.getName().equals("record"))){
                    //Record tag found, now get values and insert record
                    String _DRUG_COMMERCIAL_NAME =
                            _xml.getAttributeValue(null, DataContract.DrugsEntry.COLUMN_DRUG_COMMERCIAL_NAME);
                    String _DRUG_COMMERCIAL_NAME_ARABIC =
                            _xml.getAttributeValue(null, DataContract.DrugsEntry.COLUMN_DRUG_COMMERCIAL_NAME_ARABIC);
                    String _DRUG_SCIENTIFIC_NAME =
                            _xml.getAttributeValue(null, DataContract.DrugsEntry.COLUMN_DRUG_SCIENTIFIC_NAME);
                    String _DRUG_SCIENTIFIC_NAME_ARABIC =
                            _xml.getAttributeValue(null, DataContract.DrugsEntry.COLUMN_DRUG_SCIENTIFIC_NAME_ARABIC);
                    String _DRUG_INDICATION =
                            _xml.getAttributeValue(null, DataContract.DrugsEntry.COLUMN_DRUG_INDICATION);
                    String _DRUG_INDICATION_ARABIC =
                            _xml.getAttributeValue(null, DataContract.DrugsEntry.COLUMN_DRUG_INDICATION_ARABIC);
                    String _EXPIRY_DATE =
                            _xml.getAttributeValue(null, DataContract.DrugsEntry.COLUMN_EXPIRY_DATE);
                    String _DRUG_CONCENTRATION =
                            _xml.getAttributeValue(null, DataContract.DrugsEntry.COLUMN_DRUG_CONCENTRATION);
                    String _DRUG_TYPE =
                            _xml.getAttributeValue(null, DataContract.DrugsEntry.COLUMN_DRUG_TYPE);
                    String _DRUG_TYPE_ARABIC =
                            _xml.getAttributeValue(null, DataContract.DrugsEntry.COLUMN_DRUG_TYPE_ARABIC);
                    String _DRUG_WARNINGS =
                            _xml.getAttributeValue(null, DataContract.DrugsEntry.COLUMN_DRUG_WARNINGS);
                    String _DRUG_WARNINGS_ARABIC =
                            _xml.getAttributeValue(null, DataContract.DrugsEntry.COLUMN_DRUG_WARNINGS_ARABIC);
                    String _SIDE_EFFECTS =
                            _xml.getAttributeValue(null, DataContract.DrugsEntry.COLUMN_SIDE_EFFECTS);
                    String _SIDE_EFFECTS_ARABIC =
                            _xml.getAttributeValue(null, DataContract.DrugsEntry.COLUMN_SIDE_EFFECTS_ARABIC);
                    String _PREGNENT_ALLOWED =
                            _xml.getAttributeValue(null, DataContract.DrugsEntry.COLUMN_PREGNENT_ALLOWED);
                    String _DRUG_DESCRIPTION =
                            _xml.getAttributeValue(null, DataContract.DrugsEntry.COLUMN_DRUG_DESCRIPTION);
                    String _DRUG_DESCRIPTION_ARABIC =
                            _xml.getAttributeValue(null, DataContract.DrugsEntry.COLUMN_DRUG_DESCRIPTION_ARABIC);
                    String _DRUG_BARCODE =
                            _xml.getAttributeValue(null, DataContract.DrugsEntry.COLUMN_DRUG_BARCODE);

                    _Values.put(DataContract.DrugsEntry.COLUMN_DRUG_COMMERCIAL_NAME, _DRUG_COMMERCIAL_NAME);
                    _Values.put(DataContract.DrugsEntry.COLUMN_DRUG_COMMERCIAL_NAME_ARABIC,_DRUG_COMMERCIAL_NAME_ARABIC);
                    _Values.put(DataContract.DrugsEntry.COLUMN_DRUG_SCIENTIFIC_NAME, _DRUG_SCIENTIFIC_NAME);
                    _Values.put(DataContract.DrugsEntry.COLUMN_DRUG_SCIENTIFIC_NAME_ARABIC,_DRUG_SCIENTIFIC_NAME_ARABIC);
                    _Values.put(DataContract.DrugsEntry.COLUMN_DRUG_INDICATION, _DRUG_INDICATION);
                    _Values.put(DataContract.DrugsEntry.COLUMN_DRUG_INDICATION_ARABIC, _DRUG_INDICATION_ARABIC);
                    _Values.put(DataContract.DrugsEntry.COLUMN_EXPIRY_DATE,_EXPIRY_DATE);
                    _Values.put(DataContract.DrugsEntry.COLUMN_DRUG_CONCENTRATION, _DRUG_CONCENTRATION);
                    _Values.put(DataContract.DrugsEntry.COLUMN_DRUG_TYPE, _DRUG_TYPE);
                    _Values.put(DataContract.DrugsEntry.COLUMN_DRUG_TYPE_ARABIC, _DRUG_TYPE_ARABIC);
                    _Values.put(DataContract.DrugsEntry.COLUMN_DRUG_WARNINGS, _DRUG_WARNINGS);
                    _Values.put(DataContract.DrugsEntry.COLUMN_DRUG_WARNINGS_ARABIC, _DRUG_WARNINGS_ARABIC);
                    _Values.put(DataContract.DrugsEntry.COLUMN_SIDE_EFFECTS, _SIDE_EFFECTS);
                    _Values.put(DataContract.DrugsEntry.COLUMN_SIDE_EFFECTS_ARABIC, _SIDE_EFFECTS_ARABIC);
                    _Values.put(DataContract.DrugsEntry.COLUMN_PREGNENT_ALLOWED, _PREGNENT_ALLOWED);
                    _Values.put(DataContract.DrugsEntry.COLUMN_DRUG_DESCRIPTION, _DRUG_DESCRIPTION);
                    _Values.put(DataContract.DrugsEntry.COLUMN_DRUG_DESCRIPTION_ARABIC, _DRUG_DESCRIPTION_ARABIC);
                    _Values.put(DataContract.DrugsEntry.COLUMN_DRUG_BARCODE, _DRUG_BARCODE);

                    db.insert(DataContract.DrugsEntry.TABLE_NAME, null, _Values);

                   // Uri uri = _context.getContentResolver().insert(DataContract.DrugsEntry.CONTENT_URI, _Values);


                }
                eventType = _xml.next();
            }


        }
        //Catch errors
        catch (XmlPullParserException e)
        {
            Log.e(TAG, e.getMessage(), e);
        }
        catch (IOException e)
        {
            Log.e(TAG, e.getMessage(), e);

        }
        finally
        {
            //Close the xml file
            _xml.close();
        }





    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + DataContract.UserEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DataContract.MemberEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DataContract.DrugsEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DataContract.DrugListEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DataContract.FirstAidEntry.TABLE_NAME);
        onCreate(db);
    }
}

