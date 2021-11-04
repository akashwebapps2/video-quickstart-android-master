package com.twilio.video.test.dialog;

import android.content.Context;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import android.widget.EditText;

import com.twilio.video.test.R;

public class Dialog {

    public static AlertDialog createConnectDialog(
            EditText roomName,
            EditText participantEditText,
            DialogInterface.OnClickListener callParticipantsClickListener,
            DialogInterface.OnClickListener cancelClickListener,
            Context context) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        alertDialogBuilder.setIcon(R.drawable.ic_video_call_white_24dp);
        alertDialogBuilder.setTitle("Connect to a room");
        alertDialogBuilder.setPositiveButton("Connect", callParticipantsClickListener);
        alertDialogBuilder.setNegativeButton("Cancel", cancelClickListener);
        alertDialogBuilder.setCancelable(false);

        setRoomNameFieldInDialog(roomName, participantEditText, alertDialogBuilder);
        //setRoomNameFieldInDialog(roomName, alertDialogBuilder);

        return alertDialogBuilder.create();
    }

    private static void setRoomNameFieldInDialog(
            EditText roomNameEditText, EditText participantNameEditText, AlertDialog.Builder alertDialogBuilder) {
        roomNameEditText.setHint("room name");
        participantNameEditText.setHint("participant name");
        alertDialogBuilder.setView(roomNameEditText);
        alertDialogBuilder.setView(participantNameEditText);
    }
}
