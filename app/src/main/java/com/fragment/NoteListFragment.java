package com.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.db.AppDatabase;
import com.entity.Note;
import com.example.znote2.MyNoteActivity;
import com.example.znote2.R;
import com.viewmodel.NoteViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NoteListFragment extends ListFragment {

    private View mView;
    private OnNoteClickListener mListener;
    private List<Note> mNoteList;
    private ImageView mAddView;
    private SimpleAdapter mSimpleAdapter;
    private List<Map<String,String>> mMapList;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnNoteClickListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()+"must implement OnNoteClickListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_not_list,container,false);
        mAddView = mView.findViewById(R.id.img_add);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAddView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(-1,MyNoteFragment.STATE_ADD);
            }
        });
        mMapList = new ArrayList<>();
        ViewModelProviders.of(this).get(NoteViewModel.class).getmLiveData()
                .observe(this, new Observer<List<Note>>() {
                    @Override
                    public void onChanged(List<Note> notes) {
                        mMapList.clear();
                        mNoteList=notes;
                        for (Note note : notes){
                            Map<String,String>map1=new HashMap<>();
                            map1.put("title",note.getTitle());
                            map1.put("content",note.getContent());
                            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
                            map1.put("data",format.format(note.getCreatedTime()));
                            mMapList.add(map1);
                        }

                        mSimpleAdapter.notifyDataSetChanged();
                    }
                });
        mSimpleAdapter =new SimpleAdapter(getActivity(),mMapList,
                R.layout.list_note_item,new String[]{"title","content","data"},new int[]{R.id.tv_note_title,R.id.tv_note_content,R.id.tv_note_data});
        setListAdapter(mSimpleAdapter);
        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(getActivity()).setMessage("是否真的删除")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Note note = mNoteList.get(position);
                                ViewModelProviders.of(NoteListFragment.this).get(NoteViewModel.class).deleteNote(note);
                            }
                        })
                        .setNegativeButton(android.R.string.cancel,null).show();
                return true;
            }
        });
    }

/*    public List<Map<String,String>> getData(){
        mNoteList = AppDatabase.getInstance(getActivity()).getNoteDao().getAll();
        List<Map<String,String>> list=new ArrayList<>();

        for (Note note : mNoteList){
            Map<String,String>map1=new HashMap<>();
            map1.put("title",note.getTitle());
            map1.put("content",note.getContent());
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
            map1.put("data",format.format(note.getCreatedTime()));
            list.add(map1);
        }
        return list;

        Map<String,String>map2=new HashMap<>();
        map2.put("title","Note 2");
        map2.put("content","Note 2 Summary");
        map2.put("data","2015/3/3");
        list.add(map2);

        Map<String,String>map3=new HashMap<>();
        map3.put("title","Note 3");
        map3.put("content","Note 3 Summary");
        map3.put("data","2016/4/4");
        list.add(map3);

    }*/

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        mListener.onItemClick(mNoteList.get(position).getId(),MyNoteFragment.STATE_EDIT);
    }
}













