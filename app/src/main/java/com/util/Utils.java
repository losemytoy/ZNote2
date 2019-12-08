package com.util;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.application.MyApplication;
import com.db.AppDatabase;
import com.example.znote2.MyNoteActivity;
import com.example.znote2.R;
import com.fragment.MyNoteFragment;
import com.receiver.AlarmReceiver;

public class Utils {
    public static void setNoteAlarm(Context context,int noteId,long time){
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("noteId",noteId);
        PendingIntent pi = PendingIntent.getBroadcast(context,noteId,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (manager != null){
            manager.set(AlarmManager.RTC_WAKEUP,time,pi);
        }
    }
    public static void sendNotification(Context context,int id){
        Intent intent = new Intent(context, MyNoteActivity.class);
        intent.putExtra("id",id);
        intent.putExtra("state", MyNoteFragment.STATE_EDIT);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(intent);
        PendingIntent pi = stackBuilder.getPendingIntent(id,PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, MyApplication.CHANNEL_ID);
        builder.setSmallIcon(R.drawable.add)
                .setContentTitle("Note Alarm")
                .setContentIntent(pi);
        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        if (manager != null){
            manager.notify(id,notification);
        }
        AppDatabase.getInstance(context).getAlarmDao().delete(id);
    }
}







