package com.example.android.homepharmacy.Database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import static android.content.ContentValues.TAG;

public class HPContentProvider extends ContentProvider {

    public static final int USERS = 1;
    public static final int USER_WITH_ID = 2;

    public static final int DRUGS = 3;
    public static final int DRUG_WITH_ID= 4;

    public static final int DRUG_LIST_= 5;
    public static final int DRUG_LIST_ITEM_WITH_ID = 6;

    public static final int MEMBERS = 7;
    public static final int MEMBER_WITH_ID = 8;

    public static final int FIRST_AID = 9;
    public static final int FIRST_AID_WITH_ID = 10;

    private static final UriMatcher sUrimatcher = buildUriMatcher();
    private DB dbHelper;

    public static UriMatcher buildUriMatcher(){

        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        // <schema>:// <authority> / <path>
        // content://com.example.android.homepharmacyproject/USER_TABLE
        uriMatcher.addURI(DataContract.AUTHORITY, DataContract.PATH_USER, USERS);
        // content://com.example.android.homepharmacyproject.database/USER_TABLE/15
        uriMatcher.addURI(DataContract.AUTHORITY, DataContract.PATH_USER + "/#", USER_WITH_ID);

        uriMatcher.addURI(DataContract.AUTHORITY, DataContract.PATH_MEMBERS, MEMBERS);
        uriMatcher.addURI(DataContract.AUTHORITY, DataContract.PATH_MEMBERS + "/#", MEMBER_WITH_ID);

        uriMatcher.addURI(DataContract.AUTHORITY, DataContract.PATH_DRUGS, DRUGS);
        uriMatcher.addURI(DataContract.AUTHORITY, DataContract.PATH_DRUGS + "/#", DRUG_WITH_ID);

        uriMatcher.addURI(DataContract.AUTHORITY, DataContract.PATH_DRUG_LIST, DRUG_LIST_);
        uriMatcher.addURI(DataContract.AUTHORITY, DataContract.PATH_DRUG_LIST + "/#", DRUG_LIST_ITEM_WITH_ID);

        uriMatcher.addURI(DataContract.AUTHORITY, DataContract.PATH_FIRST_AID, FIRST_AID);
        uriMatcher.addURI(DataContract.AUTHORITY, DataContract.PATH_FIRST_AID + "/#", FIRST_AID_WITH_ID);

        return uriMatcher;
    }


    @Override
    public boolean onCreate() {
        Context context = getContext();
        dbHelper = new DB(context);

        return true;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        Log.i(TAG, "bulkInsert");
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int mach = sUrimatcher.match(uri);
        if (values.length == 0)
            return 0;
        int insertCount = 0;
        try {
            switch (mach) {

               case USERS:
                    try {
                        db.beginTransaction();
                        for (ContentValues value : values) {
                            long id =
                                    db.insertWithOnConflict(
                                            DataContract.UserEntry.TABLE_NAME,
                                            null,
                                            value,
                                            SQLiteDatabase.CONFLICT_REPLACE);
                            if (id > 0)
                                insertCount++;
                        }
                        db.setTransactionSuccessful();
                    } catch (Exception e) {
                        //  error handling
                    } finally {
                        db.endTransaction();
                    }
                    break;
                case MEMBERS:
                    try {
                        db.beginTransaction();
                        for (ContentValues value : values) {
                            long id = db.insertWithOnConflict(DataContract.MemberEntry.TABLE_NAME, null, value, SQLiteDatabase.CONFLICT_REPLACE);
                            if (id > 0)
                                insertCount++;
                        }
                        db.setTransactionSuccessful();
                    } catch (Exception e) {
                        //  error handling
                    } finally {
                        db.endTransaction();
                    }
                    break;

                case DRUGS:
                    try{
                        db.beginTransaction();
                        for (ContentValues value : values){
                            long id = db.insertWithOnConflict(DataContract.DrugsEntry.TABLE_NAME, null, value, SQLiteDatabase.CONFLICT_REPLACE);
                            if (id > 0)
                                insertCount++;
                        }
                        db.setTransactionSuccessful();
                    } catch (Exception e) {
                        //  error handling
                    } finally {
                        db.endTransaction();
                    }
                    break;

                case DRUG_LIST_:
                    try{
                        db.beginTransaction();
                        for (ContentValues value : values){
                            long id = db.insertWithOnConflict(DataContract.DrugListEntry.TABLE_NAME, null, value, SQLiteDatabase.CONFLICT_REPLACE);
                            if (id > 0)
                                insertCount++;
                        }
                        db.setTransactionSuccessful();
                    } catch (Exception e) {
                        //  error handling
                    } finally {
                        db.endTransaction();
                    }
                    break;
                case FIRST_AID:
                    try{
                        db.beginTransaction();
                        for (ContentValues value : values){
                            long id = db.insertWithOnConflict(DataContract.FirstAidEntry.TABLE_NAME, null, value, SQLiteDatabase.CONFLICT_REPLACE);
                            if (id > 0)
                                insertCount++;
                        }
                        db.setTransactionSuccessful();
                    } catch (Exception e) {
                        //  error handling
                    } finally {
                        db.endTransaction();
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Unknown URI " + uri);
            }
            getContext().getContentResolver().notifyChange(uri, null);
        } catch (Exception e) {
            Log.i(TAG, "Exception : " + e);
        }
        return insertCount;

    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues contentValues) {
        // Get access to the task database (to write new data to)
        final SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Write URI matching code to identify the match for the tasks directory
        int match = sUrimatcher.match(uri);

        Uri returnUri; // URI to be returned

        switch (match)
        {
            case USERS:
            {
                long id = db.insert(DataContract.UserEntry.TABLE_NAME, null, contentValues);
                if ( id > 0 ) {
                    returnUri = ContentUris.withAppendedId(DataContract.UserEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case MEMBERS:
            {
                long id = db.insert(DataContract.MemberEntry.TABLE_NAME,null,contentValues);
                if(id > 0){
                    returnUri = ContentUris.withAppendedId(DataContract.MemberEntry.CONTENT_URI, id);

                    String ss ="";
                }
                else {
                    throw new android.database.SQLException("Failed to insert row "+ uri);
                }
                break;
            }
            case DRUGS:
            {
                long id = db.insert(DataContract.DrugsEntry.TABLE_NAME,null,contentValues);
                if(id > 0){
                    returnUri = ContentUris.withAppendedId(DataContract.DrugsEntry.CONTENT_URI, id);
                    Log.i(TAG, "AAAAAAAAAAAAAAAAAAAAAAA");

                }
                else {
                    throw new android.database.SQLException("Failed to insert row "+ uri);
                }
                break;
            }
            case DRUG_LIST_:
            {
                long id = db.insert(DataContract.DrugListEntry.TABLE_NAME,null,contentValues);
                if(id > 0){
                    returnUri = ContentUris.withAppendedId(DataContract.DrugListEntry.CONTENT_URI, id);

                }
                else {
                    throw new android.database.SQLException("Failed to insert row "+ uri);
                }
                break;
            }
            case FIRST_AID:
            {
                long id = db.insert(DataContract.DrugListEntry.TABLE_NAME,null,contentValues);
                if(id > 0){
                    returnUri = ContentUris.withAppendedId(DataContract.DrugListEntry.CONTENT_URI, id);

                }
                else {
                    throw new android.database.SQLException("Failed to insert row "+ uri);
                }
                break;
            }
            case USER_WITH_ID:
            {
                long id = db.insert(DataContract.UserEntry.TABLE_NAME,null,contentValues);
                if(id > 0){
                    returnUri = ContentUris.withAppendedId(DataContract.UserEntry.CONTENT_URI, id);

                }
                else {
                    throw new android.database.SQLException("Failed to insert row "+ uri);
                }
                break;
            }
            case MEMBER_WITH_ID:
            {
                long id = db.insert(DataContract.MemberEntry.TABLE_NAME,null,contentValues);
                if(id > 0){
                    returnUri = ContentUris.withAppendedId(DataContract.MemberEntry.CONTENT_URI, id);

                }
                else {
                    throw new android.database.SQLException("Failed to insert row "+ uri);
                }
                break;
            }
            case DRUG_WITH_ID:
            {
                long id = db.insert(DataContract.DrugsEntry.TABLE_NAME,null,contentValues);
                if(id > 0){
                    returnUri = ContentUris.withAppendedId(DataContract.DrugsEntry.CONTENT_URI, id);

                }
                else {
                    throw new android.database.SQLException("Failed to insert row "+ uri);
                }
                break;
            }
            case DRUG_LIST_ITEM_WITH_ID:
            {
                long id = db.insert(DataContract.DrugListEntry.TABLE_NAME,null,contentValues);
                if(id > 0){
                    returnUri = ContentUris.withAppendedId(DataContract.DrugListEntry.CONTENT_URI, id);

                }
                else {
                    throw new android.database.SQLException("Failed to insert row "+ uri);
                }
                break;
            }
            case FIRST_AID_WITH_ID:
            {
                long id = db.insert(DataContract.FirstAidEntry.TABLE_NAME,null,contentValues);
                if(id > 0){
                    returnUri = ContentUris.withAppendedId(DataContract.FirstAidEntry.CONTENT_URI, id);

                }
                else {
                    throw new android.database.SQLException("Failed to insert row "+ uri);
                }
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown URI"+ uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return returnUri;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        // Get access to the database and write URI matching code to recognize a single item
        final SQLiteDatabase db = dbHelper.getWritableDatabase();

        int match = sUrimatcher.match(uri);
        // Keep track of the number of deleted tasks
        int tasksUpdated; // starts as 0

        // Write the code to update a single row of data
        // [Hint] Use selections to update an item by its row ID
        switch (match) {
            // Handle the single item case, recognized by the ID included in the URI path

           case USERS:{
                // Get the task ID from the URI path
                //  String id = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID
                tasksUpdated = db.update(DataContract.UserEntry.TABLE_NAME, contentValues, s,strings);

                String sd ="";
                break;
            }
            case MEMBERS:{
                // Get the task ID from the URI path
                //  String id = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID
                tasksUpdated = db.update(DataContract.MemberEntry.TABLE_NAME, contentValues, s,strings);

                String sd ="";
                break;
            }
            case DRUGS:{
                // Get the task ID from the URI path
                //  String id = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID
                tasksUpdated = db.update(DataContract.DrugsEntry.TABLE_NAME, contentValues, s,strings);

                String sd ="";
                break;
            }
            case DRUG_LIST_:{
                // Get the task ID from the URI path
                //  String id = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID
                tasksUpdated = db.update(DataContract.DrugListEntry.TABLE_NAME, contentValues, s,strings);

                String sd ="";
                break;
            }
            case FIRST_AID:{
                // Get the task ID from the URI path
                //  String id = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID
                tasksUpdated = db.update(DataContract.FirstAidEntry.TABLE_NAME, contentValues, s,strings);

                String sd ="";
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri UPDATE: " + uri);
        }

        // Notify the resolver of a change and return the number of items Updated
        if (tasksUpdated != 0) {
            // A task was Updated, set notification
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of tasks Updated
        return tasksUpdated;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {

        // Get access to the database and write URI matching code to recognize a single item
        final SQLiteDatabase db = dbHelper.getWritableDatabase();

        int match = sUrimatcher.match(uri);
        // Keep track of the number of deleted tasks
        int tasksDeleted; // starts as 0

        // Write the code to delete a single row of data
        // [Hint] Use selections to delete an item by its row ID
        switch (match) {
            // Handle the single item case, recognized by the ID included in the URI path
            case USER_WITH_ID:{
                // Get the task ID from the URI path
                String id = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID
                tasksDeleted = db.delete(DataContract.UserEntry.TABLE_NAME, "_id=?", new String[]{id});
                break;
            }
            case  USERS:
            {
                tasksDeleted = db.delete(DataContract.UserEntry.TABLE_NAME,null,null);
                break;
            }
            case MEMBER_WITH_ID:{
                // Get the task ID from the URI path
                String id = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID
                tasksDeleted = db.delete(DataContract.MemberEntry.TABLE_NAME, "_id=?", new String[]{id});
                break;
            }
            case  MEMBERS:{

                tasksDeleted = db.delete(DataContract.MemberEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case DRUG_WITH_ID:{
                // Get the task ID from the URI path
                String id = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID
                tasksDeleted = db.delete(DataContract.DrugsEntry.TABLE_NAME, "_id=?", new String[]{id});
                break;
            }
            case  DRUGS:
            {
                tasksDeleted = db.delete(DataContract.DrugsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case DRUG_LIST_ITEM_WITH_ID:{
                // Get the task ID from the URI path
                String id = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID
                tasksDeleted = db.delete(DataContract.DrugListEntry.TABLE_NAME, "_id=?", new String[]{id});
                break;
            }
            case  DRUG_LIST_:
            {
                tasksDeleted = db.delete(DataContract.DrugListEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case FIRST_AID_WITH_ID:{
                // Get the task ID from the URI path
                String id = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID
                tasksDeleted = db.delete(DataContract.FirstAidEntry.TABLE_NAME, "_id=?", new String[]{id});
                break;
            }
            case  FIRST_AID:
            {
                tasksDeleted = db.delete(DataContract.FirstAidEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri DELETE: " + uri);
        }

        // Notify the resolver of a change and return the number of items deleted
        if (tasksDeleted != 0) {
            // A task was deleted, set notification
            getContext().getContentResolver().notifyChange(uri, null);
        }


        // Return the number of tasks deleted
        return tasksDeleted;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings,
                        @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        final int mach = sUrimatcher.match(uri);
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor returnCursor = null;
        switch (mach) {
           case USERS: {
                returnCursor = db.query(DataContract.UserEntry.TABLE_NAME,
                        strings, s, strings1, null, null, s1);
                Log.i(TAG, returnCursor.toString()+"mmmmmmmmmmmmmmmmmmmmmmm");
                break;
            }
            case USER_WITH_ID: {
                String id = uri.getPathSegments().get(1);
                String mSelection = "_id=?";
                String mSelectionArgs [] =new String[]{id};
                returnCursor = db.query(
                        DataContract.UserEntry.TABLE_NAME,
                        strings,
                        mSelection,
                        mSelectionArgs,
                        null,
                        null,
                        s1);

                break;
            }
            case MEMBERS: {
                returnCursor = db.query(DataContract.MemberEntry.TABLE_NAME,
                        strings, s, strings1, null, null, s1);
                Log.i(TAG, returnCursor.toString() + "mmmmmmmmmmmmmmmmmmmmmmm1");
                break;
            }
            case MEMBER_WITH_ID: {
                String id = uri.getPathSegments().get(1);
                String mSelection = "_id=?";
                String mSelectionArgs [] =new String[]{id};
                returnCursor = db.query(
                        DataContract.MemberEntry.TABLE_NAME,
                        strings,
                        mSelection,
                        mSelectionArgs,
                        null,
                        null,
                        s1);

                break;
            }
            case DRUGS: {
                returnCursor = db.query(DataContract.DrugsEntry.TABLE_NAME,
                        strings, s, strings1, null, null, s1);
                Log.i(TAG, returnCursor.toString()+"mmmmmmmmmmmmmmmmmmmmmmm");
                break;
            }
            case DRUG_WITH_ID: {
                String id = uri.getPathSegments().get(1);
                String mSelection = "_id=?";
                String mSelectionArgs [] =new String[]{id};
                returnCursor = db.query(
                        DataContract.DrugsEntry.TABLE_NAME,
                        strings,
                        mSelection,
                        mSelectionArgs,
                        null,
                        null,
                        s1);

                break;
            }
            case DRUG_LIST_: {
                returnCursor = db.query(DataContract.DrugListEntry.TABLE_NAME,
                        strings, s, strings1, null, null, s1);
                break;
            }
            case DRUG_LIST_ITEM_WITH_ID: {
                String id = uri.getPathSegments().get(1);
                String mSelection = "_id=?";
                String mSelectionArgs [] =new String[]{id};
                returnCursor = db.query(
                        DataContract.DrugListEntry.TABLE_NAME,
                        strings,
                        mSelection,
                        mSelectionArgs,
                        null,
                        null,
                        s1);

                break;
            }
            case FIRST_AID: {
                returnCursor = db.query(DataContract.FirstAidEntry.TABLE_NAME,
                        strings, s, strings1, null, null, s1);
                break;
            }
            case FIRST_AID_WITH_ID: {
                String id = uri.getPathSegments().get(1);
                String mSelection = "_id=?";
                String mSelectionArgs [] =new String[]{id};
                returnCursor = db.query(
                        DataContract.FirstAidEntry.TABLE_NAME,
                        strings,
                        mSelection,
                        mSelectionArgs,
                        null,
                        null,
                        s1);

                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }
        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return returnCursor;    }

}



