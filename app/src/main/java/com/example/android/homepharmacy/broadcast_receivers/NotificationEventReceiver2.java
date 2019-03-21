package com.example.android.homepharmacy.broadcast_receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;


import com.example.android.homepharmacy.notifications.NotificationIntentService2;

import java.util.Calendar;
import java.util.Date;


/**
 * WakefulBroadcastReceiver used to receive intents fired from the AlarmManager for showing notifications
 * and from the notification itself if it is deleted.
 */
public class NotificationEventReceiver2 extends WakefulBroadcastReceiver {

    private static final String ACTION_START_NOTIFICATION_SERVICE = "ACTION_START_NOTIFICATION_SERVICE2";
    private static final String ACTION_DELETE_NOTIFICATION = "ACTION_DELETE_NOTIFICATION2";

    public static void setupAlarm2(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent alarmIntent = getStartPendingIntent(context);

        Calendar firingCal = Calendar.getInstance();
        Calendar currentCal = Calendar.getInstance();

        firingCal.set(Calendar.HOUR_OF_DAY, 0); //24-hour format
        firingCal.set(Calendar.MINUTE, 00);
        firingCal.set(Calendar.SECOND, 00);

        long intendedTime = firingCal.getTimeInMillis();
        long currentTime = currentCal.getTimeInMillis();
        if( intendedTime >= currentTime)
        {
           // alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
            //                              getTriggerAt(new Date()),
            //                               60000 ,
            //                                alarmIntent);
          //  //this will set the alarm for current day if time is below 12 am
           alarmManager.setRepeating(AlarmManager.RTC, intendedTime , AlarmManager.INTERVAL_DAY, alarmIntent);
        }
        else {
            //this will set the alarm for the next day
            firingCal.add(Calendar.DAY_OF_MONTH, 1);
            intendedTime = firingCal.getTimeInMillis();
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, intendedTime ,
                    AlarmManager.INTERVAL_DAY, alarmIntent);
        }
    }




    public static void cancelAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent alarmIntent = getStartPendingIntent(context);
        alarmManager.cancel(alarmIntent);
    }

    private static long getTriggerAt(Date now) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        return calendar.getTimeInMillis();
    }

    private static PendingIntent getStartPendingIntent(Context context) {
        Intent intent = new Intent(context, NotificationEventReceiver2.class);
        intent.setAction(ACTION_START_NOTIFICATION_SERVICE);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static PendingIntent getDeleteIntent(Context context) {
        Intent intent = new Intent(context, NotificationEventReceiver2.class);
        intent.setAction(ACTION_DELETE_NOTIFICATION);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Intent serviceIntent = null;
        if (ACTION_START_NOTIFICATION_SERVICE.equals(action)) {
            Log.i(getClass().getSimpleName(), "onReceive from alarm, starting notification service");
            serviceIntent = NotificationIntentService2.createIntentStartNotificationService2(context);
        } else if (ACTION_DELETE_NOTIFICATION.equals(action)) {
            Log.i(getClass().getSimpleName(), "onReceive delete notification action, starting notification service to handle delete");
            serviceIntent = NotificationIntentService2.createIntentDeleteNotification2(context);
        }

        if (serviceIntent != null) {
            NotificationIntentService2.startService2(context);
        }
    }
}
