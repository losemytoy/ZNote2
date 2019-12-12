package com.example.znote2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.fragment.MyNoteFragment;

import com.fragment.OnNoteClickListener;
import com.viewmodel.NoteViewModel;

public class MainActivity extends AppCompatActivity implements OnNoteClickListener {

    private boolean mDualPane;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View noteFrame = findViewById(R.id.frame_note);
        mDualPane = noteFrame != null && noteFrame.getVisibility() == View.VISIBLE;


//        FragmentManager manager = getSupportFragmentManager();
 //       Fragment fragment = manager.findFragmentById(R.id.frame);
 //       if (fragment==null){
 //           fragment=new NoteListFragment();
 //           manager.beginTransaction().add(R.id.frame,fragment).commit();
//        }

    }


    private void showNote(int id){
        MyNoteFragment myNoteFragment = (MyNoteFragment) getSupportFragmentManager().findFragmentById(R.id.frame_note);
        myNoteFragment = (MyNoteFragment) MyNoteFragment.getInstance(id,MyNoteFragment.STATE_EDIT);
        FragmentManager manager=getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.frame_note,myNoteFragment).commit();
    }

    private void showNoteInActivity(int id,int state){
        Intent intent = new Intent(MainActivity.this, MyNoteActivity.class);
        intent.putExtra("id",id);
        intent.putExtra("state",state);
        startActivity(intent);
    }

    @Override
    public void onItemClick(int id, int state) {
        if (mDualPane){
            showNote(id);
        }else {
            showNoteInActivity(id,state);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_action_sync){
            ViewModelProviders.of(MainActivity.this).get(NoteViewModel.class)
                    .syncNote();
        }
        return super.onOptionsItemSelected(item);
    }
}













