package mradmin.example.com.datetimeapp.activity;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

import mradmin.example.com.datetimeapp.R;
import mradmin.example.com.datetimeapp.model.NoteEntity;
import mradmin.example.com.datetimeapp.model.db.AppDatabase;
import mradmin.example.com.datetimeapp.model.db.NoteEntityDao;
import mradmin.example.com.datetimeapp.view.adapter.CustomRecyclerScrollViewListener;
import mradmin.example.com.datetimeapp.view.adapter.MyItemTouchHelper;
import mradmin.example.com.datetimeapp.view.adapter.RecyclerViewEmptySupport;

public class MainActivity extends AppCompatActivity {

    public static final String NOTE_ITEM = "com.example.mradmin.datetimeapp.MainActivity";

    public RecyclerViewEmptySupport recyclerViewMain;
    public ItemTouchHelper itemTouchHelper;
    public CoordinatorLayout myCoordinatorLayout;

    private FloatingActionButton floatingActionButton;

    private CustomRecyclerScrollViewListener customRecyclerScrollViewListener;

    List<NoteEntity> noteEntities;

    AppDatabase appDatabase;
    NoteEntityDao noteEntityDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerViewMain = findViewById(R.id.recyclerViewMain);
        recyclerViewMain.setEmptyView(findViewById(R.id.notesEmptyView));
        myCoordinatorLayout = findViewById(R.id.myCoordinatorLayout);

        appDatabase = Room.databaseBuilder(this,
                AppDatabase.class, "room-notes-database").build();

        noteEntityDao = appDatabase.getNoteEntityDao();

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
        if (noteEntities != null && noteEntities.size() > 0) {
            NoteAdapter noteAdapter = new NoteAdapter(noteEntities);
            recyclerViewMain.setAdapter(noteAdapter);
            LinearLayoutManager manager = new LinearLayoutManager(this);
            recyclerViewMain.setLayoutManager(manager);

            ItemTouchHelper.Callback callback = new MyItemTouchHelper(noteAdapter);
            itemTouchHelper = new ItemTouchHelper(callback);
            itemTouchHelper.attachToRecyclerView(recyclerViewMain);
        }
    }

    public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> implements MyItemTouchHelper.ItemTouchHelperAdapter {

        private List<NoteEntity> items;

        public static final String NOTE_ITEM = "com.example.mradmin.datetimeapp.MainActivity";

        @Override
        public void onItemMoved(int fromPosition, int toPosition) {
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(items, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(items, i, i - 1);
                }
            }
            notifyItemMoved(fromPosition, toPosition);
        }

        @Override
        public void onItemRemoved(final int position) {

            final int removeIndex = position;
            final NoteEntity removeEntity = items.get(position);

            items.remove(items.get(position));
            notifyItemRemoved(position);

            new AsyncTask<NoteEntity, Void, Void>() {
                @Override
                protected Void doInBackground(NoteEntity... params) {
                    noteEntityDao.delete(removeEntity);
                    return null;
                }
            }.execute();

            String toShow = "Note";
            Snackbar.make(myCoordinatorLayout, "Deleted " + toShow, Snackbar.LENGTH_LONG)
                    .setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            items.add(removeIndex, removeEntity);

                            notifyItemInserted(removeIndex);

                            new AsyncTask<NoteEntity, Void, Void>() {

                                @Override
                                protected Void doInBackground(NoteEntity... voids) {
                                    noteEntityDao.insertAll(removeEntity);
                                    return null;
                                }

                                @Override
                                protected void onPostExecute(Void aVoid) {
                                    Toast.makeText(MainActivity.this, "Undo pressed", Toast.LENGTH_SHORT).show();
                                }
                            }.execute();
                        }
                    }).show();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_row_layout, parent, false);
            return new mradmin.example.com.datetimeapp.activity.MainActivity.NoteAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            NoteEntity noteEntity = items.get(position);

            holder.textViewTitle.setText(noteEntity.getTitle());
            holder.textViewDescription.setText(noteEntity.getDescription());

            if (noteEntity.isDated()) {
                if (noteEntity.getDate() != null && !noteEntity.getDate().isEmpty()) {
                    holder.textViewTime.setText(noteEntity.getDate());
                    holder.textViewTime.setVisibility(View.VISIBLE);
                }
            } else {
                holder.textViewTime.setVisibility(View.GONE);
            }

            if (noteEntity.isPinned()) {
                holder.imageViewPinned.setVisibility(View.VISIBLE);
            } else {
                holder.imageViewPinned.setVisibility(View.GONE);
            }

        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public NoteAdapter(List<NoteEntity> items) {

            this.items = items;
        }


        @SuppressWarnings("deprecation")
        public class ViewHolder extends RecyclerView.ViewHolder {

            View mView;

            public ImageView imageViewImage;
            public TextView textViewTitle;
            public TextView textViewDescription;
            public TextView textViewTime;
            public ImageView imageViewPinned;

            public ViewHolder(View v) {
                super(v);

                mView = v;

                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        NoteEntity note = items.get(mradmin.example.com.datetimeapp.activity.MainActivity.NoteAdapter.ViewHolder.this.getAdapterPosition());
                        Intent intent = new Intent(v.getContext(), NoteDetailActivity.class);
                        intent.putExtra(NOTE_ITEM, note);
                        v.getContext().startActivity(intent);
                    }
                });

                imageViewImage = v.findViewById(R.id.imageViewNoteImage);
                textViewTitle = v.findViewById(R.id.textViewNoteTitle);
                textViewDescription = v.findViewById(R.id.textViewNoteDescription);
                textViewTime = v.findViewById(R.id.textViewNoteTime);
                imageViewPinned = v.findViewById(R.id.imageViewNotePinned);

            }
        }
    }
}
