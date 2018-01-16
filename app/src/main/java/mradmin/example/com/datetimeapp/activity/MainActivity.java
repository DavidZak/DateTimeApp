package mradmin.example.com.datetimeapp.activity;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mradmin.example.com.datetimeapp.R;
import mradmin.example.com.datetimeapp.model.NoteContent;
import mradmin.example.com.datetimeapp.model.NoteEntity;
import mradmin.example.com.datetimeapp.view.adapter.CustomRecyclerScrollViewListener;
import mradmin.example.com.datetimeapp.view.adapter.MyItemTouchHelper;
import mradmin.example.com.datetimeapp.view.adapter.NoteAdapter;
import mradmin.example.com.datetimeapp.view.adapter.RecyclerViewEmptySupport;

public class MainActivity extends AppCompatActivity {

    public static final String NOTE_ITEM = "com.example.mradmin.datetimeapp.MainActivity";

    public RecyclerViewEmptySupport recyclerViewMain;
    public ItemTouchHelper itemTouchHelper;

    private FloatingActionButton floatingActionButton;

    CustomRecyclerScrollViewListener customRecyclerScrollViewListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerViewMain = findViewById(R.id.recyclerViewMain);
        recyclerViewMain.setEmptyView(findViewById(R.id.notesEmptyView));

        List<NoteEntity> noteEntities = new ArrayList<>();
        for (int i=0;i<5;i++){
            noteEntities.add(new NoteEntity(String.valueOf(i), i, new NoteContent("not pinned " + i,"not pinned description very long and not good for now" + i, ""), true, null, false));

        }
        for (int i=0;i<5;i++){
            noteEntities.add(new NoteEntity(String.valueOf(i), i, new NoteContent("note " + i,"description " + i, ""), false, new Date(2017,9,13), true));

        }

        customRecyclerScrollViewListener = new CustomRecyclerScrollViewListener() {
            @Override
            public void show() {

                floatingActionButton.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
//                mAddToDoItemFAB.animate().translationY(0).setInterpolator(new AccelerateInterpolator(2.0f)).start();
            }

            @Override
            public void hide() {

                CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams)floatingActionButton.getLayoutParams();
                int fabMargin = lp.bottomMargin;
                floatingActionButton.animate().translationY(floatingActionButton.getHeight()+fabMargin).setInterpolator(new AccelerateInterpolator(2.0f)).start();
            }
        };
        recyclerViewMain.addOnScrollListener(customRecyclerScrollViewListener);

        NoteAdapter noteAdapter = new NoteAdapter(noteEntities);
        recyclerViewMain.setAdapter(noteAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerViewMain.setLayoutManager(manager);

        ItemTouchHelper.Callback callback = new MyItemTouchHelper(noteAdapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerViewMain);

        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, NoteDetailActivity.class));

            }
        });
    }
}
