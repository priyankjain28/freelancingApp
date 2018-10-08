package com.peeru.task.freelancingapp.ui.push;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.peeru.task.freelancingapp.R;

import java.util.Map;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
            Log.d("Insert Notifications: ", "Inserting ..");

            Map<String, String> data = remoteMessage.getData();
            String title = data.get("title");
            String msgStatus = data.get("msg_status");
            String message = data.get("message");
            String click_action = data.get("action");

            Log.d(TAG, "onMessageReceived: click_action : " + click_action);
            Log.d(TAG, "onMessageReceived: title : " + title);
            Log.d(TAG, "onMessageReceived: body : " + title);
            Log.d(TAG, "onMessageReceived: tag : " + msgStatus);

            sendNotification(message, msgStatus, title, click_action);
        }


    }


    private void sendNotification(String message, String msgStatus, String messageTitle, String click_action) {
        Intent intent = new Intent(click_action);
        // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("title", messageTitle);
        intent.putExtra("msgStatus", msgStatus);
        intent.putExtra("message", message);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);


        notificationBuilder.setContentTitle(messageTitle)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher))
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());

    }


}