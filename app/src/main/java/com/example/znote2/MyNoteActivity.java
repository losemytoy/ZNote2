package com.example.znote2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.fragment.MyNoteFragment;

public class MyNoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_note);
        FragmentManager manager=getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.frame);
        if (fragment==null){
            int id=getIntent().getIntExtra("id",-1);
            int state=getIntent().getIntExtra("state",-1);
            FragmentTransaction transaction=manager.beginTransaction();
            fragment=MyNoteFragment.getInstance(id,state);
            transaction.add(R.id.frame,fragment,"");
            transaction.commit();
        }

    }
}
