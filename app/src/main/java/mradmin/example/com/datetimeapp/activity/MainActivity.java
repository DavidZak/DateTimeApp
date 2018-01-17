package mradmin.example.com.datetimeapp.activity;

import android.app.Activity;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import mradmin.example.com.datetimeapp.App;
import mradmin.example.com.datetimeapp.R;
import mradmin.example.com.datetimeapp.model.NoteEntity;
import mradmin.example.com.datetimeapp.model.db.AppDatabase;
import mradmin.example.com.datetimeapp.model.db.NoteEntityDao;
import mradmin.example.com.datetimeapp.view.adapter.CustomRecyclerScrollViewListener;
import mradmin.example.com.datetimeapp.view.adapter.MyItemTouchHelper;
import mradmin.example.com.datetimeapp.view.adapter.NoteAdapter;
import mradmin.example.com.datetimeapp.view.adapter.RecyclerViewEmptySupport;

public class MainActivity extends AppCompatActivity {

    public static final String NOTE_ITEM = "com.example.mradmin.datetimeapp.MainActivity";

    public RecyclerViewEmptySupport recyclerViewMain;
    public ItemTouchHelper itemTouchHelper;

    private FloatingActionButton floatingActionButton;

    private CustomRecyclerScrollViewListener customRecyclerScrollViewListener;

    List<NoteEntity> noteEntities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerViewMain = findViewById(R.id.recyclerViewMain);
        recyclerViewMain.setEmptyView(findViewById(R.id.notesEmptyView));

        AppDatabase appDatabase = Room.databaseBuilder(this,
                AppDatabase.class, "room-notes-database").build();

        final NoteEntityDao noteEntityDao = appDatabase.getNoteEntityDao();

        new AsyncTask<Void, Void, List<NoteEntity>>() {
            @Override
            protected List<NoteEntity> doInBackground(Void... params) {
                return noteEntityDao.getAllNotes();
            }

            @Override
            protected void onPostExecute(List<NoteEntity> notes) {

                initRecyclerView(notes);
                if (notes.size() > 0) {
                    //2: If it already exists then prompt user
                    Toast.makeText(MainActivity.this, "Notes already exists!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, "Notes does not exist! Hurray :)", Toast.LENGTH_LONG).show();
                }
            }
        }.execute();

        recyclerViewMain.addOnScrollListener(
                new CustomRecyclerScrollViewListener() {

                    @Override
                    public void hide() {
                        floatingActionButton.hide();
                    }

                    @Override
                    public void show() {
                        floatingActionButton.show();
                    }
                });
        //-------------


        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, NoteDetailActivity.class));

            }
        });
    }

    public void initRecyclerView(List<NoteEntity> noteEntities) {
        if (noteEntities!=null && noteEntities.size()>0) {
            NoteAdapter noteAdapter = new NoteAdapter(noteEntities);
            recyclerViewMain.setAdapter(noteAdapter);
            LinearLayoutManager manager = new LinearLayoutManager(this);
            recyclerViewMain.setLayoutManager(manager);

            ItemTouchHelper.Callback callback = new MyItemTouchHelper(noteAdapter);
            itemTouchHelper = new ItemTouchHelper(callback);
            itemTouchHelper.attachToRecyclerView(recyclerViewMain);
        }
    }

}
