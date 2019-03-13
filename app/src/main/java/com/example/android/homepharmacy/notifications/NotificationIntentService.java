package com.example.android.homepharmacy.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.support.v4.app.JobIntentService;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.text.format.DateUtils;
import android.util.Log;

import com.example.android.homepharmacy.Activity.CourseActivity;
import com.example.android.homepharmacy.DataModel.DrugAlert;
import com.example.android.homepharmacy.Database.DataContract;
import com.example.android.homepharmacy.R;
import com.example.android.homepharmacy.broadcast_receivers.NotificationEventReceiver;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static java.util.Calendar.HOUR;
import static java.util.Calendar.HOUR_OF_DAY;


public class NotificationIntentService extends JobIntentService {

    private int NOTIFICATION_ID = 1;
    private static final String ACTION_START = "ACTION_START";
    private static final String ACTION_DELETE = "ACTION_DELETE";
    String CHANNEL_ID = "my_channel_01";// The id of the channel.
    CharSequence name = "my_channel_01";
    int importance = NotificationManager.IMPORTANCE_HIGH;


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
    Context context = this;
    int userId;
    Cursor cur1;
    Cursor cur2;
    String selectionArgs11[];
    String selection_;
    String selectionArgs_[];

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

                    if ((d2.after(today) || d2.equals(today)) && (d1.equals(today) || today.after(d1))) {
                          //  for (int i = repeat; i<=24; i = i +repeat){
                        int f = repeat;
                        while (f <= 24){
                            Calendar cal = Calendar.getInstance(); // creates calendar
                            cal.setTime(t1); // sets calendar time/date
                            cal.add(Calendar.HOUR_OF_DAY, f);// adds one hour if repeat == 1
                            cal.add(Calendar.MINUTE, 0);
                            cal.add(Calendar.SECOND, 0);

                                cal.getTime(); // returns new date object, one hour in the future
                                Date repDose1 = cal.getTime();
                                String repDose = sdf.format(repDose1);
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
                                f = f + repeat;

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            final NotificationCompat.Builder builder = new NotificationCompat.Builder(context , CHANNEL_ID);
            builder.setContentTitle("Drug Reminder for: " + memName)
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.drug_icon)
                    .setColor(getResources().getColor(R.color.colorAccent))
                    .setContentText("Now its time for " + memName +
                            " to get " + drugName)
                    .setSmallIcon(R.drawable.logo)
                    .setChannelId(CHANNEL_ID);

            Intent mainIntent = new Intent(this, CourseActivity.class);
            mainIntent.putExtra(Intent.EXTRA_TEXT, courseId);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,
                    NOTIFICATION_ID,
                    mainIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);
            builder.setDeleteIntent(NotificationEventReceiver.getDeleteIntent(this));


            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            /* Create or update. */
            NotificationChannel channel = new NotificationChannel("my_channel_01",
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            // mNotificationManager.createNotificationChannel(channel);
            mNotificationManager.createNotificationChannel(mChannel);
            mNotificationManager.notify(NOTIFICATION_ID, builder.build());



        }
        else {
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


    }


    public int checkLoggedUser() {
        String [] selectionArgs_ = {"1"};
        String selection_ = DataContract.UserEntry.COLUMN_IS_LOGGED + " =?";
        cur1 = getContentResolver().query(DataContract.UserEntry.CONTENT_URI,
                null,
                selection_,
                selectionArgs_,
                null);
        if (cur1.getCount() > 0) {
            if (cur1.moveToNext()) {
                userId = cur1.getInt(cur1.getColumnIndex(DataContract.UserEntry._ID));
                return userId;
            }
        }
        return 0;
    }

    public ArrayList<String> UserMember(){
       // userId = 1;
        int memId;
        ArrayList<String> membersId = new ArrayList();
        final String selection = DataContract.MemberEntry.COLUMN_USER_ID + " =?";
        final String[] selectionArgs = {String.valueOf(userId)};

        cur2 = getContentResolver().query(DataContract.MemberEntry.CONTENT_URI,
                null,
                selection,
                selectionArgs,
                null);
        if(cur2 != null){
            int g =  cur2.getCount();
            if(cur2.moveToFirst()){
                while (!cur2.isAfterLast()) {
                    int f;
                    memId = cur2.getInt(cur2.getColumnIndex(DataContract.MemberEntry._ID));
                    if (memId != 0){
                        membersId.add(String.valueOf(memId));
                    }
                    cur2.moveToNext();
                }
            }
        }

        // if(membersId!= null && membersId.size()>0)
        return membersId;
    }

    public String makePlaceholders(int len) {
        StringBuilder sb = new StringBuilder(len * 2 - 1);
        sb.append("?");
        for (int i = 1; i < len; i++)
            sb.append(",?");
        return sb.toString();
    }

    public ArrayList<DrugAlert> setTimeAlarm() {
        userId = checkLoggedUser();
        ArrayList<String> arrayList = UserMember();

        if(arrayList != null && !arrayList.isEmpty()){
            selectionArgs11 = arrayList.toArray(new String[arrayList.size()]);
            selection_ = DataContract.DrugListEntry.COLUMN_MEMBER_L_ID + " IN (" + makePlaceholders(selectionArgs11.length) + ")";
            selectionArgs_ = new String[selectionArgs11.length];
            for (int i = 0; i < selectionArgs11.length; i++) {
                selectionArgs_[i] = selectionArgs11[i];
            }
        }

        cursor = getContentResolver().query(DataContract.DrugListEntry.CONTENT_URI,
                null,
                selection_,
                selectionArgs_,
                DataContract.DrugListEntry._ID);
        if (cursor != null) {
            int v = cursor.getCount();
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
