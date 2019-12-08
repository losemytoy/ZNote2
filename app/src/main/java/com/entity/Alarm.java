package com.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.db.DateConverter;

import java.util.Date;

@Entity
@TypeConverters(DateConverter.class)
public class Alarm {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int noteId;
    private Date alarmTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public Date getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(Date alarmTime) {
        this.alarmTime = alarmTime;
    }
}
