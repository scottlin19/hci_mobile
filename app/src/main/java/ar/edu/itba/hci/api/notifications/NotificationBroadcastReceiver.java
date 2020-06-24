package ar.edu.itba.hci.api.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.content.res.ResourcesCompat;

import java.sql.SQLOutput;

import ar.edu.itba.hci.MainActivity;
import ar.edu.itba.hci.R;

public class NotificationBroadcastReceiver extends BroadcastReceiver {

    public static final String CHANNEL_ID = "General notifications";

    public static final String DEVICE_NOTIFICATION = "ar.edu.itba.hci.DEVICE_NOTIFICATION";




    public static final int AC_NOTIFICATION_ID = 1;
    public static final int ALARM_NOTIFICATION_ID = 2;
    public static final int BLINDS_NOTIFICATION_ID = 3;
    public static final int DOOR_NOTIFICATION_ID = 4;
    public static final int FAUCET_NOTIFICATION_ID = 5;
    public static final int FRIDGE_NOTIFICATION_ID = 6;
    public static final int LAMP_NOTIFICATION_ID = 7;
    public static final int OVEN_NOTIFICATION_ID = 8;
    public static final int SPEAKER_NOTIFICATION_ID = 9;
    public static final int VACUUM_NOTIFICATION_ID = 0;

    private static final int MY_NOTIFICATION_ID = 1;

    @Override
    public void onReceive(Context context, Intent intent) {

        System.out.println("//////////////////////////////////////////////////// BROADCAST RECEIVER /////////////////////////////////////////////////");
        NotificationManager manager =
                (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Register the channel with the system (required by Android 8.0 - Oreo)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = CHANNEL_ID;
            String description = "Device notification channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            manager.createNotificationChannel(channel);
        }

       Intent notificationIntent = new Intent(context, MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(notificationIntent);

        final PendingIntent contentIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // Set notification channel id (required by Android 8.0 - Oreo)
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context.getApplicationContext(), CHANNEL_ID)
               .setContentTitle("title")
               .setContentText("text")
               .setSmallIcon(R.mipmap.appicon)
               .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.appicon))
               .setContentIntent(contentIntent);


        manager.notify(SPEAKER_NOTIFICATION_ID, builder.build());
    }
}
