package com.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.application.MyApplication;
import com.db.AlarmDao;
import com.db.AppDatabaseNew;
import com.db.NoteDao;
import com.entity.Alarm;
import com.entity.Note;
import com.example.znote2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class NoteRepository {

    private NoteDao mNoteDao;
    private AlarmDao mAlarmDao;
    private LiveData<List<Note>> mLiveData;
    private Application mApplication;
    public NoteRepository(Application application){
        mApplication = application;
        mNoteDao = AppDatabaseNew.getInstance(application).getNoteDao();
    }
    public void newAlarmDao(){
        if(mAlarmDao == null){
            mAlarmDao= AppDatabaseNew.getInstance(mApplication).getAlarmDao();
        }
    }

    public LiveData<List<Note>> getLiveData() {
        if (mLiveData == null){
            mLiveData = mNoteDao.getAllNotes();
        }
        return mLiveData;
    }

    public void deleteNote(Note note){
        new DeleteNoteTask(mNoteDao,mApplication).execute(note);
    }

    public void addNote(Note note,OnAddNoteCompletedListener listener){
        new AddNoteTask(mNoteDao,mApplication,listener).execute(note);
    }

    public void updateNote(Note note){
        new UpdateNoteTask(mNoteDao,mApplication).execute(note);
    }

    public void addAlarm(Alarm alarm){
        newAlarmDao();
        new AddNoteAlarmTask(mAlarmDao,mApplication).execute(alarm);
    }

    public void sync() {
        new SyncNote(mNoteDao,mApplication).execute();
    }

    private static class AddNoteTask extends AsyncTask<Note,Void,Long>{
        private NoteDao mNoteDao;
        private Application mApplication;
        private OnAddNoteCompletedListener mListener;
        AddNoteTask(NoteDao dao,Application application,OnAddNoteCompletedListener listener){
            mNoteDao=dao;
            mApplication=application;
            mListener = listener;
        }

        @Override
        protected Long doInBackground(Note... notes) {
            return mNoteDao.insert(notes[0]);
        }

        @Override
        protected void onPostExecute(Long id) {
            super.onPostExecute(id);
            if (id>0){
                Toast.makeText(mApplication,mApplication.getString(R.string.add_success),Toast.LENGTH_SHORT).show();
            }
            mListener.onAddCompleted(id);
        }
    }

    private static class UpdateNoteTask extends AsyncTask<Note,Void,Boolean>{
        private NoteDao mNoteDao;
        private Application mApplication;
        UpdateNoteTask(NoteDao dao,Application application){
            mNoteDao=dao;
            mApplication=application;
        }

        @Override
        protected Boolean doInBackground(Note... notes) {
            return mNoteDao.update(notes[0])>0;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean){
                Toast.makeText(mApplication,mApplication.getString(R.string.update_success),Toast.LENGTH_SHORT).show();
            }
        }
    }

    private static class DeleteNoteTask extends AsyncTask<Note,Void,Boolean>{

        private NoteDao mNoteDao;
        private Application mApplication;
        DeleteNoteTask(NoteDao dao,Application application){
            mNoteDao=dao;
            mApplication=application;
        }

        @Override
        protected Boolean doInBackground(Note... notes) {
            return mNoteDao.delelt(notes[0])>0;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean){
                Toast.makeText(mApplication,mApplication.getString(R.string.delete_success),Toast.LENGTH_SHORT).show();
            }
        }
    }
    private static class AddNoteAlarmTask extends AsyncTask<Alarm,Void,Boolean>{
        private AlarmDao mAlarmDao;
        private Application mApplication;
        AddNoteAlarmTask(AlarmDao dao, Application application){
            mAlarmDao=dao;
            mApplication=application;
        }

        @Override
        protected Boolean doInBackground(Alarm... alarms) {
            return mAlarmDao.insert(alarms[0])>0;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean){
                Toast.makeText(mApplication,mApplication.getString(R.string.add_success),Toast.LENGTH_SHORT).show();
            }
        }
    }

    public interface OnAddNoteCompletedListener{
        void onAddCompleted(long id);
    }

    private static class SyncNote extends AsyncTask<Void,Void,List<Note>>{

        private NoteDao mNoteDao;
        private Application mApplication;
        SyncNote(NoteDao noteDao,Application application){
            this.mNoteDao = noteDao;
            mApplication=application;
        }
        @Override
        protected List<Note> doInBackground(Void... voids) {
            return mNoteDao.getAll();
        }

        @Override
        protected void onPostExecute(List<Note> noteList) {
            super.onPostExecute(noteList);
            Log.d("lym", "onPostExecute: "+noteList.toString());
            sendMessage(noteList);
        }

        private void sendMessage(List<Note> noteList){
            JSONObject messageObj = new JSONObject();
            JSONObject userObj = new JSONObject();
            JSONArray noteArray = new JSONArray();

            try{
                messageObj.put("reqCode",3);
                messageObj.put("count",noteList.size());
                userObj.put("id",17210544021L);
                userObj.put("password","17210544021");
                messageObj.put("user",userObj);

                for (Note note:noteList){
                    JSONObject noteObj = new JSONObject();
                    noteObj.put("id",note.getId());
                    noteObj.put("name",note.getTitle());
                    noteObj.put("content",note.getContent());
                    noteObj.put("created_time",note.getCreatedTime().getTime());
                    noteArray.put(noteObj);
                }
                messageObj.put("notes",noteArray);
                Log.d("lym", "sendMessage: "+messageObj.toString());

            }catch (JSONException e){
                e.printStackTrace();
            }
            String url="http://10.145.45.76:8080/note?method=1";
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, messageObj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if (response.getInt("ret") == 0) {
                            Toast.makeText(mApplication, "Successful", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mApplication, "Failure", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(mApplication,"Failure",Toast.LENGTH_SHORT).show();
                }
            });
            MyApplication.getQueue().add(request);
        }
    }
}
