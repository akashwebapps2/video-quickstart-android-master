package com.twilio.video.test.receiver;

import static tvi.webrtc.ContextUtils.getApplicationContext;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;

import com.twilio.video.test.Service.HeadsUpNotificationService;
import com.twilio.video.test.activity.IncominCallActivity;
import com.twilio.video.test.activity.VideoActivity;
import com.twilio.video.test.util.AppController;

public class CallNotificationActionReceiver extends BroadcastReceiver {


    Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.mContext=context;
        if (intent != null && intent.getExtras() != null) {

            String action ="";
            action=intent.getStringExtra("ACTION_TYPE");

            if (action != null&& !action.equalsIgnoreCase("")) {
                performClickAction(context, action,intent);
            }

            // Close the notification after the click action is performed.
            Intent iclose = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            context.sendBroadcast(iclose);
            context.stopService(new Intent(context, IncominCallActivity.class));

        }


    }
    private void performClickAction(Context context, String action, Intent intentdata) {
        if(action.equalsIgnoreCase("RECEIVE_CALL")) {

            if (checkAppPermissions()) {
                Intent intentCallReceive = new Intent(mContext, VideoActivity.class);
                intentCallReceive.putExtra("Call", "incoming");
                intentCallReceive.putExtra("type",intentdata.getStringExtra("type"));
                intentCallReceive.putExtra("room_id",intentdata.getStringExtra("room_id"));
                intentCallReceive.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mContext.startActivity(intentCallReceive);
            }
            else{
                Intent intent = new Intent(AppController.getInstance(), VideoActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("CallFrom","call from push");
                intent.putExtra("type",intentdata.getStringExtra("type"));
                intent.putExtra("room_id",intentdata.getStringExtra("room_id"));
                mContext.startActivity(intent);

            }

            mContext.stopService(new Intent(AppController.getInstance(), HeadsUpNotificationService.class));
            Intent istop = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            mContext.sendBroadcast(istop);

        }
        else if(action.equalsIgnoreCase("DIALOG_CALL")){

            // show ringing activity when phone is locked
            Intent intent = new Intent(AppController.getInstance(), IncominCallActivity.class);
            intent.putExtra("type",intentdata.getStringExtra("type"));
            intent.putExtra("room_id",intentdata.getStringExtra("room_id"));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mContext.startActivity(intent);

            mContext.stopService(new Intent(AppController.getInstance(), HeadsUpNotificationService.class));
            Intent istop = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            mContext.sendBroadcast(istop);



        } else if(action.equalsIgnoreCase("CANCEL_CALL")){

            mContext.stopService(new Intent(AppController.getInstance(), HeadsUpNotificationService.class));
            Intent istop = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            mContext.sendBroadcast(istop);
        }

        else {
            context.stopService(new Intent(context, IncominCallActivity.class));
            Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            context.sendBroadcast(it);
        }
    }

    private Boolean checkAppPermissions() {
        return hasReadPermissions() && hasWritePermissions() && hasCameraPermissions() && hasAudioPermissions();
    }

    private boolean hasAudioPermissions() {
        return (ContextCompat.checkSelfPermission(AppController.getInstance(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED);
    }

    private boolean hasReadPermissions() {
        return (ContextCompat.checkSelfPermission(AppController.getInstance(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    private boolean hasWritePermissions() {
        return (ContextCompat.checkSelfPermission(AppController.getInstance(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }
    private boolean hasCameraPermissions() {
        return (ContextCompat.checkSelfPermission(AppController.getInstance(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED);
    }
}
