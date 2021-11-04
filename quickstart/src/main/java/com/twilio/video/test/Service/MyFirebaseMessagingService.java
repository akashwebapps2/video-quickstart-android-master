package com.twilio.video.test.Service;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.twilio.video.Video;
import com.twilio.video.test.R;
import com.twilio.video.test.activity.IncominCallActivity;
import com.twilio.video.test.activity.VideoActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private String title = "", body = "", type = "";


    Uri ringtone = Uri.parse(
            "android.resource://" +
                    "com.twilio.video.test" + "/" +
                    R.raw.call_ringtone);


    AudioAttributes audioAttributes = new AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .setUsage(AudioAttributes.USAGE_ALARM)
            .build();


    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        storeRegIdInPref(s);
    }

    private void storeRegIdInPref(String token) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("regId", token);
        editor.apply();
    }

    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage == null) {
            return;
        }


        try {

            JSONObject jsonObj = new JSONObject(remoteMessage.getData().toString());

            Log.d("notification_jsonObj:", String.valueOf(jsonObj));


            JSONObject dataObj = jsonObj.getJSONObject("data");

           // JSONObject obj = new JSONObject(jsonObj.getJSONObject("data").toString());

            title = dataObj.getString("title");
            body = dataObj.getString("body");
            // type = jsonObj.getString("Notification_type");
            String room_id = dataObj.getString("room_id");
            String device_id = dataObj.getString("device_id");


           // handleSimpleNoti(title, body, room_id);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                Intent serviceIntent = new Intent(getApplicationContext(), HeadsUpNotificationService.class);
                Bundle mBundle = new Bundle();
                mBundle.putString("inititator", device_id);
                mBundle.putString("call_type","Video");
                mBundle.putString("type", "notification");
                mBundle.putString("room_id", room_id);
                serviceIntent.putExtras(mBundle);
                ContextCompat.startForegroundService(getApplicationContext(), serviceIntent);
            }

            else
            {
                Intent serviceIntent = new Intent(getApplicationContext(), HeadsUpNotificationService.class);
                Bundle mBundle = new Bundle();
                mBundle.putString("inititator", device_id);
                mBundle.putString("call_type","Video");
                mBundle.putString("type", "notification");
                mBundle.putString("room_id", room_id);
                serviceIntent.putExtras(mBundle);
                startService(serviceIntent);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("noti_err:", String.valueOf(e));
        }
    }

    public void handleSimpleNoti(String title, String message, String roomId) {
        int icon = R.drawable.ic_bluetooth_white_24dp;
        int min = 65;
        int max = 80;
        long[] v = {500, 1000, 500, 1000, 500, 1000};

        Random r = new Random();
        int i1 = r.nextInt(max - min + 1) + min;
        int requestID = i1;
        String CHANNEL_ID = "1002";// The id of the channel.
        CharSequence name = "Aariv School";// The user-visible name of the channel.
        int importance = NotificationManager.IMPORTANCE_HIGH;


        Intent notificationIntent = null;
        notificationIntent = new Intent(this, IncominCallActivity.class);
        notificationIntent.putExtra("type", "notification");
        notificationIntent.putExtra("room_id", roomId);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);


        final NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        PendingIntent contentIntent = PendingIntent.getActivity(this, requestID, notificationIntent, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.enableVibration(true);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
            builder.setContentTitle(title);
            builder.setContentText(message);
            builder.setSmallIcon(icon);
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(message).setBigContentTitle(title));
            builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
            builder.setVibrate(v);
            mChannel.setSound(ringtone, audioAttributes);
            builder.setContentIntent(contentIntent);
            builder.setChannelId(CHANNEL_ID);
            builder.setAutoCancel(true);
            final Notification notification = builder.build();
            mNotificationManager.createNotificationChannel(mChannel);
            mNotificationManager.notify(requestID, notification);
        } else {
            final Notification notification;
            notification = new Notification.Builder(getApplicationContext())
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(icon)
                    .setStyle(new Notification.BigTextStyle().bigText(message).setBigContentTitle(title))
                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                    .setVibrate(v)
                    .setSound(ringtone)
                    .setContentIntent(contentIntent)
                    .setAutoCancel(true)
                    .build();
            mNotificationManager.notify(requestID, notification);

        }
    }

}
