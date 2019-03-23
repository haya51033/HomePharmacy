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

import com.example.android.homepharmacy.Activity.CourseActivity;
import com.example.android.homepharmacy.Activity.DrugActivity;
import com.example.android.homepharmacy.DataModel.DrugAlert2;
import com.example.android.homepharmacy.Database.DataContract;
import com.example.android.homepharmacy.R;
import com.example.android.homepharmacy.broadcast_receivers.NotificationEventReceiver;
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
    String CHANNEL_ID = "home_pharmacy_02";// The id of the channel.
    CharSequence name = "home_pharmacy_02";
    int importance = NotificationManager.IMPORTANCE_HIGH;
    private static String TAG = NotificationIntentService2.class.getName();

    ArrayList<DrugAlert2> arrayList2 = new ArrayList<>();
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    ArrayList<DrugAlert2> drugsExList = new ArrayList<>();

    String drugEpiryName = "Drug";
    String drugExpiryDate;
    int drugEpityId;
    String toda;
    Date today;

    String exNotDrugName;
    String exNotDrugDate;
    int exNotDrugId;
    Context context = this;

    int userId, _drugId;
    Cursor cur1;
    Cursor cur2;
    String drugName;

    String selectionArgs11[];
    String selection_;
    String selectionArgs_[];

    public static void startService2(Context context) {
        enqueueWork(context, NotificationIntentService2.class, 1002, new Intent());
    }

    @Override
    protected void onHandleWork(Intent intent) {
        Date date = new Date();
        toda = dateFormat.format(date);
        today = dateFormat.parse(toda, new ParsePosition(0));

        arrayList2 = setTimeAlarm2();
        if(arrayList2 != null){
            if(arrayList2.size() > 0){
                for(DrugAlert2 d2 : arrayList2){
                    exNotDrugName = d2.getDrug_name();
                    exNotDrugDate = d2.getDrug_ex_date();
                    exNotDrugId = d2.getIdEx();
                    Date expiryDate = dateFormat.parse(exNotDrugDate, new ParsePosition(0));

                    Calendar cal = Calendar.getInstance(); // creates calendar
                    cal.setTime(today); // sets calendar time/date
                    cal.add(Calendar.DATE, + 30);
                    Date dateBefore30Days = cal.getTime();

                    Calendar ca2 = Calendar.getInstance(); // creates calendar
                    ca2.setTime(today); // sets calendar time/date
                    ca2.add(Calendar.DATE, +2 );
                    Date dateBefore2Days = ca2.getTime();

                    if(expiryDate.equals(dateBefore2Days) || expiryDate.equals(dateBefore30Days)){
                        String [] selectionArgs_ = {String.valueOf(exNotDrugName)};
                        String selection_ = DataContract.DrugsEntry._ID + " =?";
                        Cursor cd = getContentResolver().query(DataContract.DrugsEntry.CONTENT_URI,
                                null,
                                selection_,
                                selectionArgs_,
                                null);

                        if(cd!= null){
                            if(cd.moveToFirst()){
                                drugName = cd.getString(cd.getColumnIndex(DataContract.DrugsEntry.COLUMN_DRUG_COMMERCIAL_NAME));
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            final NotificationCompat.Builder builder = new NotificationCompat.Builder(context , CHANNEL_ID);
            builder.setContentTitle("Drug Expiry soon!! ")
                    .setAutoCancel(true)
                    .setColor(getResources().getColor(R.color.colorAccent))
                    .setContentText(drugName + " drug will Expire in  " + exNotDrugDate )
                    .setSmallIcon(R.drawable.logo)
                    .setChannelId(CHANNEL_ID);


            Intent mainIntent = new Intent(this, CourseActivity.class);
            mainIntent.putExtra(Intent.EXTRA_TEXT, exNotDrugId);
            mainIntent.putExtra("userId", userId);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,
                    NOTIFICATION_ID,
                    mainIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);
            builder.setDeleteIntent(NotificationEventReceiver.getDeleteIntent(this));


            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            /* Create or update. */
            mNotificationManager.createNotificationChannel(mChannel);
            mNotificationManager.notify(NOTIFICATION_ID, builder.build());



        }
        else {
            final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            builder.setContentTitle("Drug Expiry soon!! ")
                    .setAutoCancel(true)
                    .setColor(getResources().getColor(R.color.colorAccent))
                    .setContentText(drugName + " drug will Expire in  " + exNotDrugDate )
                    .setSmallIcon(R.drawable.logo);

            Intent mainIntent = new Intent(this, CourseActivity.class);
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


    }
    public ArrayList<DrugAlert2> setTimeAlarm2(){
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

       Cursor c = getContentResolver().query(DataContract.DrugListEntry.CONTENT_URI,
                null,
                selection_,
                selectionArgs_,
                DataContract.DrugListEntry._ID);

        if(c != null){
            if(c.moveToFirst()) {
                while (!c.isAfterLast()) {
                    drugEpityId = c.getInt(c.getColumnIndex("_id"));
                    drugExpiryDate = c.getString(c.getColumnIndex("expiry_date"));
                    int x = c.getInt(c.getColumnIndex(DataContract.DrugListEntry.COLUMN_DRUG_L_ID));
                    DrugAlert2 drugAlert2 = new DrugAlert2(drugEpityId, String.valueOf(x), drugExpiryDate);
                    drugsExList.add(drugAlert2);
                    c.moveToNext();
                }
            }
            c.close();
        }
        return drugsExList;
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
            if(cur2.moveToFirst()){
                while (!cur2.isAfterLast()) {
                    memId = cur2.getInt(cur2.getColumnIndex(DataContract.MemberEntry._ID));
                    if (memId != 0){
                        membersId.add(String.valueOf(memId));
                    }
                    cur2.moveToNext();
                }
            }
        }
        return membersId;
    }

    public String makePlaceholders(int len) {
        StringBuilder sb = new StringBuilder(len * 2 - 1);
        sb.append("?");
        for (int i = 1; i < len; i++)
            sb.append(",?");
        return sb.toString();
    }


}
