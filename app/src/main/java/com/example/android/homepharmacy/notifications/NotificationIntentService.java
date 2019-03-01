package com.example.android.homepharmacy.notifications;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.JobIntentService;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.example.android.homepharmacy.Activity.CourseActivity;
import com.example.android.homepharmacy.DataModel.DrugAlert;
import com.example.android.homepharmacy.Database.DataContract;
import com.example.android.homepharmacy.R;
import com.example.android.homepharmacy.broadcast_receivers.NotificationEventReceiver;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class NotificationIntentService extends JobIntentService {

    private int NOTIFICATION_ID = 1;
    private static final String ACTION_START = "ACTION_START";
    private static final String ACTION_DELETE = "ACTION_DELETE";

    private static String TAG = NotificationIntentService.class.getName();

    String drugName;
    String memName;
    int dose;

    Cursor cursor;
    ArrayList<DrugAlert> arrayList1 = new ArrayList<>();
    DrugAlert drugAlert;

    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

    int __id;
    String memName1;
    int _memId;
    String drugName1;
    int _drugId;
    String drugTime;
    int dose_q;
    int dose_r;
    String _end_date;
    String _start_date;
    int courseId;
    String toda;
    Date today;

    public static void startService(Context context) {
        enqueueWork(context, NotificationIntentService.class, 1001, new Intent());
    }

    @Override
    protected void onHandleWork(Intent intent) {

        Date date = new Date();
        toda = dateFormat.format(date);
        today = dateFormat.parse(toda, new ParsePosition(0));

        java.util.Date date1 = new java.util.Date();
        String current = sdf.format(date1);

        Date now1 = sdf.parse(current, new ParsePosition(0));
        String now = sdf.format(now1);
        arrayList1 = setTimeAlarm();
        if (arrayList1 != null) {
            if (arrayList1.size() > 0) {
                for (DrugAlert d : arrayList1) {
                    Log.d(getClass().getSimpleName(), "heeeeeeeeeeeeer");

                    String start = d.get_start_date();
                    Date d1 = dateFormat.parse(start, new ParsePosition(0));
                    String end = d.get_end_date();
                    Date d2 = dateFormat.parse(end, new ParsePosition(0));
                    String firstTime = d.getAlert_time();
                    Date t1 = sdf.parse(firstTime, new ParsePosition(0));
                    String t = sdf.format(t1);
                    int repeat = d.getDose_r();

                    Calendar cal = Calendar.getInstance(); // creates calendar
                    cal.setTime(t1); // sets calendar time/date
                    cal.add(Calendar.HOUR_OF_DAY, repeat); // adds one hour
                    cal.getTime(); // returns new date object, one hour in the future
                    Date repDose1 = cal.getTime();
                    String repDose = sdf.format(repDose1);

                    if ((d2.after(today) || d2.equals(today)) && (d1.equals(today) || today.after(d1))) {
                        boolean nn = t.equals(now);
                        boolean mm = repDose.equals(now);
                        if (t.equals(now) || repDose.equals(now)) {
                            memName = d.getMember_name();
                            drugName = d.getDrug_name();
                            courseId = d.get__id();
                            dose = d.getDose_q();
                            Log.d(getClass().getSimpleName(), "onHandleIntent, started handling a notification event");
                            try {
                                processStartNotification();
                                NOTIFICATION_ID = NOTIFICATION_ID +1;
                                String action = intent.getAction();
                                if (ACTION_START.equals(action)) {
                                    processStartNotification();
                                }
                            } finally {
                                WakefulBroadcastReceiver.completeWakefulIntent(intent);
                            }
                        }
                    }
                }
            }
        }


    }


    public static Intent createIntentStartNotificationService(Context context) {
        Intent intent = new Intent(context, NotificationIntentService.class);
        intent.setAction(ACTION_START);
        return intent;
    }

    public static Intent createIntentDeleteNotification(Context context) {
        Intent intent = new Intent(context, NotificationIntentService.class);
        intent.setAction(ACTION_DELETE);
        return intent;
    }


    private void processDeleteNotification(Intent intent) {
        // Log something?
    }


    private void processStartNotification() {
        // Do something. For example, fetch fresh data from backend to create a rich notification?

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("Drug Reminder for: " + memName)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.drug_icon)
                .setColor(getResources().getColor(R.color.colorAccent))
                .setContentText("Now its time for " + memName +
                        " to get " + drugName)
                .setSmallIcon(R.drawable.logo);

        Intent mainIntent = new Intent(this, CourseActivity.class);
        mainIntent.putExtra(Intent.EXTRA_TEXT, courseId);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                NOTIFICATION_ID,
                mainIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        builder.setDeleteIntent(NotificationEventReceiver.getDeleteIntent(this));

        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        manager.notify(NOTIFICATION_ID, builder.build());

    }


    public ArrayList<DrugAlert> setTimeAlarm() {
        cursor = getContentResolver().query(DataContract.DrugListEntry.CONTENT_URI,
                null,
                null,
                null,
                DataContract.DrugListEntry._ID);
        if (cursor != null) {
            if (cursor.moveToFirst()) {// data?
                while (!cursor.isAfterLast()) {
                    __id = cursor.getInt(cursor.getColumnIndex("_id"));
                    _memId = cursor.getInt(cursor.getColumnIndex("member_list_id"));
                    _drugId = cursor.getInt(cursor.getColumnIndex("drug_list_id"));
                    drugTime = cursor.getString(cursor.getColumnIndex("drug_first_time"));
                    dose_q = cursor.getInt(cursor.getColumnIndex("drug_dose_quantity"));
                    dose_r = cursor.getInt(cursor.getColumnIndex("drug_dose_repeat"));
                    _end_date = cursor.getString(cursor.getColumnIndex("end_date"));
                    _start_date = cursor.getString(cursor.getColumnIndex("start_date"));

                    drugName1 = getDrugName(String.valueOf(_drugId));
                    memName1 = getMemberName(String.valueOf(_memId));

                    drugAlert = new DrugAlert(__id, memName1, drugName1, drugTime,
                            dose_q, dose_r, _end_date, _start_date);
                    arrayList1.add(drugAlert);
                    cursor.moveToNext();
                }
            }
        }
        return arrayList1;
    }

    public String getDrugName(String query) {
        String[] selectionArgs = new String[]{query};

        Cursor c = getContentResolver().query(DataContract.DrugsEntry.CONTENT_URI,
                null,
                DataContract.DrugsEntry._ID + " LIKE ? ",
                selectionArgs,
                null);
        if (c != null) {
            if (c.moveToFirst()) {
                drugName = c.getString(c.getColumnIndex("commercial_name"));
                return drugName;
            }
        }
        return "";
    }


    public String getMemberName(String query) {
        String[] selectionArgs = new String[]{query};

        Cursor c = getContentResolver().query(DataContract.MemberEntry.CONTENT_URI,
                null,
                DataContract.MemberEntry._ID + " LIKE ? ",
                selectionArgs,
                null);
        if (c != null) {
            if (c.moveToFirst()) {
                memName = c.getString(c.getColumnIndex("member_name"));
                return memName;
            }
        }
        return "";
    }
}
