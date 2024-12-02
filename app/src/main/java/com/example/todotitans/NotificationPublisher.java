package com.example.todotitans;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.todotitans.R;

public class NotificationPublisher extends BroadcastReceiver {

    public static String NOTIFICATION_ID = "notification-id";
    public static String NOTIFICATION = "notification";

    /**
     * Handles the receipt of a broadcast for task reminders.
     * <p>
     * This method is triggered when a scheduled alarm for a task reminder goes off. It retrieves the
     * task reminder details from the {@link Intent} and displays a notification to the user.
     * </p>
     *
     * @param context The {@link Context} in which the receiver is running.
     * @param intent  The {@link Intent} containing the notification message and ID.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        // Retrieve the notification message and ID from the Intent
        String notificationMessage = intent.getStringExtra(NOTIFICATION);
        int id = intent.getIntExtra(NOTIFICATION_ID, 0);

        Log.d("NotificationDebug", "Notification triggered! ID: " + id + ", Message: " + notificationMessage);

        // Get the system's NotificationManager
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Build the notification with the provided message and properties
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "YOUR_CHANNEL_ID")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Task Reminder")
                .setContentText(notificationMessage)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_REMINDER);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "YOUR_CHANNEL_ID", "Task Notifications", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        // Display the notification
        notificationManager.notify(id, builder.build());
    }
}