package com.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.db.AppDatabaseNew;
import com.entity.Alarm;
import com.entity.Note;
import com.repository.NoteRepository;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {

    private LiveData<List<Note>> mLiveData;
    private NoteRepository mNoteRepository;

    public NoteViewModel(Application application){
        super(application);
        mNoteRepository = new NoteRepository(application);
    }

    public LiveData<List<Note>> getmLiveData(){
        if (mLiveData == null){
            mLiveData = mNoteRepository.getLiveData();
        }
        return mLiveData;
    }

    public void deleteNote(Note note){
        mNoteRepository.deleteNote(note);
    }

    public void addNote(Note note,NoteRepository.OnAddNoteCompletedListener listener){
        mNoteRepository.addNote(note,listener);
    }

    public void updateNote(Note note){
        mNoteRepository.updateNote(note);
    }

    public void addAlarm(Alarm alarm){
        mNoteRepository.addAlarm(alarm);
    }


    public void syncNote() {
        mNoteRepository.sync();
    }
}
