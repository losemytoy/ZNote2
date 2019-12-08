package com.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.util.Utils;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int id = intent.getIntExtra("noteId",-1);
        if (id == -1){
            return;
        }
        Toast.makeText(context, "Receiver Alarm,id is"+id, Toast.LENGTH_SHORT).show();
        Utils.sendNotification(context,id);
    }
}
