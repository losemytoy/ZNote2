package com.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.entity.Note;

import java.util.List;

@Dao
public interface NoteDao {
    @Insert
    long insert(Note note);

    @Query("select * from note")
    List<Note> getAll();

    @Query("select * from note where id=:id")
    Note get(int id);

    @Delete
    int delelt(Note note);

    @Update
    int update(Note note);

    @Query("select * from note")
    LiveData<List<Note>> getAllNotes();
}
