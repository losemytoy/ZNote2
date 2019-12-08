package com.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.entity.Alarm;
import com.entity.Note;

@Database(entities = {Note.class, Alarm.class},version = 1,exportSchema = false)
public abstract class AppDatabaseNew extends RoomDatabase {

    private static final String DB_NAME = "UserDatabase.db";

    private static volatile AppDatabaseNew instance;
    public static synchronized AppDatabaseNew getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }
    private static AppDatabaseNew create(final Context context) {
        return Room.databaseBuilder(context, AppDatabaseNew.class,DB_NAME).build();
    }
    public abstract NoteDao getNoteDao();
    public abstract AlarmDao getAlarmDao();


}
