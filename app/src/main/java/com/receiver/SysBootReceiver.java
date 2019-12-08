package com.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.db.AppDatabase;
import com.entity.Alarm;
import com.util.Utils;

import java.util.List;

public class SysBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction()!=null && !intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
            return;
        }
        List<Alarm> alarms = AppDatabase.getInstance(context).getAlarmDao().getAll();
        if (alarms.size() == 0){
            return;
        }
        long now = System.currentTimeMillis();
        for (Alarm alarm : alarms){
            long time = alarm.getAlarmTime().getTime();
            if (time >= now){
                Utils.setNoteAlarm(context,alarm.getNoteId(),time);
            }
        }
    }
}
