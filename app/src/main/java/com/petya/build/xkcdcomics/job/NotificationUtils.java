package com.petya.build.xkcdcomics.job;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.petya.build.xkcdcomics.MainActivity;
import com.petya.build.xkcdcomics.R;


/**
 * Created by Petya Marinova on 7/31/2018.
 */
public class NotificationUtils{
        private static final int COMICS_REMINDER_NOTIFICATION_ID = 1138;
        private static final int COMICS_REMINDER_PENDING_INTENT_ID = 3417;
        private static final String COMICS_REMINDER_NOTIFICATION_CHANNEL_ID = "reminder_notification_channel";
        public static void remindUserNewComixArrived(Context context, String newComicsTitle, String newComicsBody) {
            NotificationManager notificationManager = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel mChannel = new NotificationChannel(
                        COMICS_REMINDER_NOTIFICATION_CHANNEL_ID,
                        context.getString(R.string.main_notification_channel_name),
                        NotificationManager.IMPORTANCE_HIGH);
                notificationManager.createNotificationChannel(mChannel);
            }
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, COMICS_REMINDER_NOTIFICATION_CHANNEL_ID)
                    .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                    .setSmallIcon(R.drawable.ic_back)
                    .setLargeIcon(largeIcon(context))
                    .setContentTitle(newComicsTitle)
                    .setContentText(newComicsBody)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(
                            newComicsBody))
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .setContentIntent(contentIntent(context))
                    .setAutoCancel(true);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                    && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
            }

            notificationManager.notify(COMICS_REMINDER_NOTIFICATION_ID, notificationBuilder.build());
        }

        private static PendingIntent contentIntent(Context context) {
            Intent startActivityIntent = new Intent(context, MainActivity.class);
            return PendingIntent.getActivity(
                    context,
                    COMICS_REMINDER_PENDING_INTENT_ID,
                    startActivityIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
        }


        private static Bitmap largeIcon(Context context) {
            Resources res = context.getResources();
            Bitmap largeIcon = BitmapFactory.decodeResource(res, R.drawable.fav_no);
            return largeIcon;
        }
}