package com.example.android.homepharmacy.notifications;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.JobIntentService;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.example.android.homepharmacy.Activity.DrugActivity;
import com.example.android.homepharmacy.DataModel.DrugAlert2;
import com.example.android.homepharmacy.Database.DataContract;
import com.example.android.homepharmacy.R;
import com.example.android.homepharmacy.broadcast_receivers.NotificationEventReceiver2;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class NotificationIntentService2 extends JobIntentService {

    private  int NOTIFICATION_ID = 2;
    private static final String ACTION_START = "ACTION_START2";
    private static final String ACTION_DELETE = "ACTION_DELETE2";

    private static String TAG = NotificationIntentService2.class.getName();

    ArrayList<DrugAlert2> arrayList2 = new ArrayList<>();
   // DrugAlert2 drugAlert2;

    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    ArrayList<DrugAlert2> drugsExList = new ArrayList<>();

    String drugEpiryName;
    String drugExpiryDate;
    int drugEpityId;
    String toda;
    Date today;

    String exNotDrugName;
    String exNotDrugDate;
    int exNotDrugId;

    public static void startService2(Context context) {
        enqueueWork(context, NotificationIntentService2.class, 1002, new Intent());
    }

    @Override
    protected void onHandleWork(Intent intent) {
        Date date = new Date();
        toda = dateFormat.format(date);
        today = dateFormat.parse(toda, new ParsePosition(0));
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        java.util.Date date1 = new java.util.Date();
        String current = sdf.format(date1);


            arrayList2 = setTimeAlarm2();
            if(arrayList2 != null){
                if(arrayList2.size() > 0){
                    for(DrugAlert2 d2 : arrayList2){
                        exNotDrugName = d2.getDrug_name();
                        exNotDrugDate = d2.getDrug_ex_date();
                        exNotDrugId = d2.getIdEx();
                        try {
                            processStartNotification2();
                            NOTIFICATION_ID = NOTIFICATION_ID +1;
                            String action = intent.getAction();
                            if (ACTION_START.equals(action)) {
                                processStartNotification2();
                            }
                        } finally {
                            WakefulBroadcastReceiver.completeWakefulIntent(intent);
                        }

                }
            }
        }
    }



    public static Intent createIntentStartNotificationService2(Context context) {
        Intent intent = new Intent(context, NotificationIntentService2.class);
        intent.setAction(ACTION_START);
        return intent;
    }

    public static Intent createIntentDeleteNotification2(Context context) {
        Intent intent = new Intent(context, NotificationIntentService2.class);
        intent.setAction(ACTION_DELETE);
        return intent;
    }



    private void processDeleteNotification2(Intent intent) {
        // Log something?
    }


    private void processStartNotification2() {
        // Do something. For example, fetch fresh data from backend to create a rich notification?

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("Drug Expiry soon!! ")
                .setAutoCancel(true)
                .setColor(getResources().getColor(R.color.colorAccent))
                .setContentText("The " + exNotDrugName + " Will Expiry in: " + exNotDrugDate )
                .setSmallIcon(R.drawable.logo);

        Intent mainIntent = new Intent(this, DrugActivity.class);
        mainIntent.putExtra(Intent.EXTRA_TEXT, exNotDrugId);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                NOTIFICATION_ID,
                mainIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        builder.setDeleteIntent(NotificationEventReceiver2.getDeleteIntent(this));

        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
     //   manager.notify(NOTIFICATION_ID, builder.build());
        manager.notify(NOTIFICATION_ID, builder.build());

    }
    public ArrayList<DrugAlert2> setTimeAlarm2(){

        Calendar cal = Calendar.getInstance(); // creates calendar
        cal.setTime(today); // sets calendar time/date
        cal.add(Calendar.DATE, + 30);
        Date dateBefore30Days = cal.getTime();
        String beforeMonthe = dateFormat.format(dateBefore30Days);
        /////
        Calendar ca2 = Calendar.getInstance(); // creates calendar
        ca2.setTime(today); // sets calendar time/date
        ca2.add(Calendar.DATE, +2 );
        Date dateBefore2Days = ca2.getTime();
        String before2Days = dateFormat.format(dateBefore2Days);

        Cursor c122 = getContentResolver().query(DataContract.DrugsEntry.CONTENT_URI, DRUG_COLUMNS,
                "expiry_date='"+beforeMonthe+"'",
                null,null,null);
        Cursor c = getContentResolver().query(DataContract.DrugsEntry.CONTENT_URI, DRUG_COLUMNS,
                "expiry_date IN(?,?)",
                new String[]{before2Days , beforeMonthe}, null);

        if(c != null){
            int d = c.getCount();
            if(c.moveToFirst()) {
                while (!c.isAfterLast()) {
                    drugEpiryName = c.getString(c.getColumnIndex("commercial_name"));
                    drugEpityId = c.getInt(c.getColumnIndex("_id"));
                    drugExpiryDate = c.getString(c.getColumnIndex("expiry_date"));
                    DrugAlert2 drugAlert2 = new DrugAlert2(drugEpityId, drugEpiryName, drugExpiryDate);
                    drugsExList.add(drugAlert2);
                    c.moveToNext();
                }
            }
        }
        return drugsExList;
    }

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
}
