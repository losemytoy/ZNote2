package com.fragment;


import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.db.AppDatabase;
import com.db.AppDatabaseNew;
import com.entity.Alarm;
import com.entity.Note;
import com.example.znote2.R;
import com.repository.NoteRepository;
import com.util.Utils;
import com.viewmodel.NoteViewModel;

import java.util.Calendar;
import java.util.Date;

import xyz.time.DateAndTimePicker;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyNoteFragment extends Fragment {

    public static final int STATE_ADD=1;
    public static final int STATE_EDIT=2;
    private int mState;
    private ImageView mAlarmImageView;
    private int mId;
    private boolean isHavingAlarm;
    private Calendar mCalendar;

    public static Fragment getInstance(int id,int state){
        Fragment fragment=new MyNoteFragment();
        Bundle bundle=new Bundle();
        bundle.putInt("id",id);
        bundle.putInt("state",state);
        fragment.setArguments(bundle);
        return fragment;
    }

    public MyNoteFragment() {
        // Required empty public constructor
    }

    private View mView;
    private Note mNote;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mId=getArguments().getInt("id");
        mState = getArguments().getInt("state");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_my_note, container, false);
        mAlarmImageView = mView.findViewById(R.id.img_alarm);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final EditText titleEdt = mView.findViewById(R.id.edt_note_title);
        final EditText contentEdt = mView.findViewById(R.id.edt_note_content);
      //  titleEdt.setText(NoteListFragment.getData().get(id).get("title"));
      //  contentEdt.setText(NoteListFragment.getData().get(id).get("content"));

        if (mState == STATE_EDIT){
            mNote = AppDatabase.getInstance(getActivity()).getNoteDao().get(mId);
            titleEdt.setText(mNote.getTitle());
            contentEdt.setText(mNote.getContent());
            Alarm alarm=AppDatabase.getInstance(getActivity()).getAlarmDao().getByNoteId(mId);
            if (alarm != null && alarm.getNoteId() > 0){
                mAlarmImageView = mView.findViewById(R.id.img_alarm);
                isHavingAlarm = true;
            }
        }

        Button mButton01 = mView.findViewById(R.id.btn_ok);
        mButton01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleEdt.getText().toString();
                String content = contentEdt.getText().toString();
                Note note = new Note();
                note.setTitle(title);
                note.setContent(content);
                note.setCreatedTime(new Date());
                if (mState==STATE_ADD){
                    note.setCreatedTime(new Date());
                    ViewModelProviders.of(MyNoteFragment.this).get(NoteViewModel.class).addNote(note, new NoteRepository.OnAddNoteCompletedListener() {
                        @Override
                        public void onAddCompleted(long id) {
                            if(id>0 && isHavingAlarm){
                                Alarm alarm=new Alarm();
                                alarm.setNoteId((int)id);
                                Date date = new Date();
                                date.setTime(mCalendar.getTimeInMillis());
                                alarm.setAlarmTime(date);
                                ViewModelProviders.of(MyNoteFragment.this).get(NoteViewModel.class).addAlarm(alarm);
                                Utils.setNoteAlarm(getActivity(),(int)id,mCalendar.getTimeInMillis());
                            }
                        }
                    });
                }
                if (mState == STATE_EDIT){
                    note.setId(mId);
                    note.setCreatedTime(mNote.getCreatedTime());
                    note.setModifiedTime(new Date());
                    AppDatabase.getInstance(getActivity()).getNoteDao().update(note);
                    ViewModelProviders.of(MyNoteFragment.this).get(NoteViewModel.class).updateNote(note);
                    if(isHavingAlarm){
                        Alarm alarm=new Alarm();
                        alarm.setNoteId(mId);
                        Date date = new Date();
                        date.setTime(mCalendar.getTimeInMillis());
                        alarm.setAlarmTime(date);
                        ViewModelProviders.of(MyNoteFragment.this).get(NoteViewModel.class).addAlarm(alarm);
                        Utils.setNoteAlarm(getActivity(),mId,mCalendar.getTimeInMillis());
                    }


                }
                if (getActivity()!=null)
                    getActivity().finish();
            }
        });


        mAlarmImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isHavingAlarm){
                    mAlarmImageView.setImageResource(R.drawable.no_alarm);
                    AppDatabase.getInstance(getActivity()).getAlarmDao().delete(mId);
                    isHavingAlarm=false;
                }else {
                    DateAndTimePicker dateAndTimePicker = new DateAndTimePicker(getActivity(),null);
                    dateAndTimePicker.setOnMyTimeChangedListener(new DateAndTimePicker.OnMyTimeChangedListener() {
                        @Override
                        public void onSelected(Calendar calendar) {
                            mCalendar = calendar;
                            isHavingAlarm=true;
                            mAlarmImageView.setImageResource(R.drawable.hava_alarm);
                        }

                        @Override
                        public void onCanceled() {
                            isHavingAlarm=false;
                        }
                    });
                    dateAndTimePicker.show();
                }
            }
        });
    }
}
