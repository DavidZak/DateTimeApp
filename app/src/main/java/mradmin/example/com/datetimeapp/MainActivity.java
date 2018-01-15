package mradmin.example.com.datetimeapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.util.ArrayList;
import java.util.List;

import mradmin.example.com.datetimeapp.model.NoteContent;
import mradmin.example.com.datetimeapp.model.NoteEntity;
import mradmin.example.com.datetimeapp.view.adapter.MyItemTouchHelper;
import mradmin.example.com.datetimeapp.view.adapter.NoteAdapter;

public class MainActivity extends AppCompatActivity {

    public RecyclerView recyclerViewMain;
    public ItemTouchHelper itemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerViewMain = findViewById(R.id.recyclerViewMain);

        List<NoteEntity> noteEntities = new ArrayList<>();
        for (int i=0;i<7;i++){
            noteEntities.add(new NoteEntity(String.valueOf(i), i, new NoteContent("","", ""), true));

        }

        NoteAdapter noteAdapter = new NoteAdapter(noteEntities);
        recyclerViewMain.setAdapter(noteAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerViewMain.setLayoutManager(manager);

        ItemTouchHelper.Callback callback = new MyItemTouchHelper(noteAdapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerViewMain);
    }
}
