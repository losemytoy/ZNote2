package com.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.entity.Alarm;

import java.util.List;

@Dao
public interface AlarmDao {

    @Insert
    long insert(Alarm alarm);

    @Query("select * from alarm")
    List<Alarm> getAll();

    @Query("select * from alarm where noteId=:noteId")
    Alarm getByNoteId(int noteId);

    @Query("delete from alarm where noteId=:noteId")
    int delete(int noteId);
}
