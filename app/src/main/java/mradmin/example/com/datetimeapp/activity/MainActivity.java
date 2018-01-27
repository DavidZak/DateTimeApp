package mradmin.example.com.datetimeapp.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import mradmin.example.com.datetimeapp.NoteNotificationService;
import mradmin.example.com.datetimeapp.R;
import mradmin.example.com.datetimeapp.model.NoteEntity;
import mradmin.example.com.datetimeapp.model.db.AppDatabase;
import mradmin.example.com.datetimeapp.model.db.NoteEntityDao;
import mradmin.example.com.datetimeapp.util.NoteAlarmManager;
import mradmin.example.com.datetimeapp.util.NoteEntityComparator;
import mradmin.example.com.datetimeapp.view.adapter.CustomRecyclerScrollViewListener;
import mradmin.example.com.datetimeapp.view.adapter.MyItemTouchHelper;
import mradmin.example.com.datetimeapp.view.adapter.RecyclerSectionItemDecoration;
import mradmin.example.com.datetimeapp.view.adapter.RecyclerViewEmptySupport;

public class MainActivity extends AppCompatActivity {

    NoteAlarmManager noteAlarmManager;

    public static final String NOTE_ITEM = "com.example.mradmin.datetimeapp.MainActivity";

    private static final int REQUEST_ID_NOTE_ITEM = 100;

    public RecyclerViewEmptySupport recyclerViewMain;
    public ItemTouchHelper itemTouchHelper;
    public CoordinatorLayout myCoordinatorLayout;

    private FloatingActionButton floatingActionButton;

    private AppDatabase appDatabase;
    private NoteEntityDao noteEntityDao;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d MMM yyyy");

    SimpleDateFormat dateFormatFull = new SimpleDateFormat("d MMM yyyy HH:mm");

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        noteAlarmManager = new NoteAlarmManager(this);

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
                    Toast.makeText(MainActivity.this, "Notes does not exist! Add some here", Toast.LENGTH_LONG).show();
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

    private void setAlarms(List<NoteEntity> entities){
        if(entities != null) {
            for(NoteEntity item : entities){
                if(item.isDated() && item.getDate() != null){
                    Date date = null;
                    try {
                        date = dateFormatFull.parse(item.getDate());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if(date != null && date.before(new Date())){
                        item.setDate(null);
                        item.setDated(false);
                        continue;
                    }
                    Intent i = new Intent(this, NoteNotificationService.class);
                    i.putExtra(NoteNotificationService.NOTEID, item.getId());
                    i.putExtra(NoteNotificationService.NOTETEXT, item.getTitle());
                    noteAlarmManager.createAlarm(i, item.getId().hashCode(), date.getTime());
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        this.moveTaskToBack(true);
    }

    public void initRecyclerView(final List<NoteEntity> noteEntities) {
        if (noteEntities != null && noteEntities.size() > 0) {

            Collections.sort(noteEntities, new NoteEntityComparator());

            NoteAdapter noteAdapter = new NoteAdapter(noteEntities);
            recyclerViewMain.setAdapter(noteAdapter);
            LinearLayoutManager manager = new LinearLayoutManager(this);
            recyclerViewMain.setLayoutManager(manager);

            initRecyclerItemSectionDecoration(recyclerViewMain, noteEntities);

            ItemTouchHelper.Callback callback = new MyItemTouchHelper(noteAdapter);
            itemTouchHelper = new ItemTouchHelper(callback);
            itemTouchHelper.attachToRecyclerView(recyclerViewMain);

            setAlarms(noteEntities);
        }
    }

    public void initRecyclerItemSectionDecoration(RecyclerView recyclerView, List<NoteEntity> noteEntities){
        RecyclerSectionItemDecoration sectionItemDecoration =
                new RecyclerSectionItemDecoration(getResources().getDimensionPixelSize(R.dimen.header),
                        true,
                        getSectionCallback(noteEntities));
        recyclerView.addItemDecoration(sectionItemDecoration);
    }

    NoteEntity containsIndex(List<NoteEntity> list, int index)  {
        try {
            return list.get(index);
        } catch (IndexOutOfBoundsException e){
            return null;
        }
    }

    private RecyclerSectionItemDecoration.SectionCallback getSectionCallback(final List<NoteEntity> notes) {
        return new RecyclerSectionItemDecoration.SectionCallback() {
            @Override
            public boolean isSection(int position) {

                if (containsIndex(notes, position) == null)
                    return position == 0;
                else
                    return position == 0
                        ||  !(simpleDateFormat.format(new Date(notes.get(position).getCreationDate())).equals(simpleDateFormat.format(new Date(notes.get(position - 1).getCreationDate()))));
            }

            @Override
            public CharSequence getSectionHeader(int position) {
                if (containsIndex(notes, position) == null)
                    return "";
                else
                    return simpleDateFormat.format(new Date(notes.get(position).getCreationDate()));
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_CANCELED && requestCode == REQUEST_ID_NOTE_ITEM){
            NoteEntity item =(NoteEntity) data.getSerializableExtra(NOTE_ITEM);
            if(item.getTitle().length() <= 0 || item.getDescription().length() <= 0){
                return;
            }

            if(item.isDated() && item.getDate() != null) {
                Intent i = new Intent(this, NoteNotificationService.class);
                i.putExtra(NoteNotificationService.NOTETEXT, item.getTitle());
                i.putExtra(NoteNotificationService.NOTEID, item.getId());

                Date date = null;
                try {
                    date = dateFormatFull.parse(item.getDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (date != null)
                noteAlarmManager.createAlarm(i, item.getId().hashCode(), date.getTime());
            }
        }
    }

    public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> implements MyItemTouchHelper.ItemTouchHelperAdapter {

        private List<NoteEntity> items;

        public static final String NOTE_ITEM = "com.example.mradmin.datetimeapp.MainActivity";

        //////////////////////////
        private boolean multiSelect = false;
        private ArrayList<NoteEntity> selectedItems = new ArrayList<>();

        private ActionMode actionMode;

        private ActionMode.Callback actionModeCallbacks = new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                multiSelect = true;
                getMenuInflater().inflate(R.menu.menu_note_detail, menu);
                actionMode = mode;
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                for (final NoteEntity intItem : selectedItems) {

                    new AsyncTask<NoteEntity, Void, Void>() {
                        @Override
                        protected Void doInBackground(NoteEntity... params) {
                            noteEntityDao.delete(intItem);
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            items.remove(intItem);
                            startSupportActionMode(actionModeCallbacks).finish();
                            actionMode.finish();
                        }
                    }.execute();

                }
                mode.finish();
                actionMode.finish();
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                multiSelect = false;
                selectedItems.clear();
                notifyDataSetChanged();
            }
        };
        ///////////////////////////////

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
            Intent i = new Intent(MainActivity.this,NoteNotificationService.class);
            noteAlarmManager.deleteAlarm(i, removeEntity.getId().hashCode());
            notifyItemRemoved(position);

            new AsyncTask<NoteEntity, Void, Void>() {
                @Override
                protected Void doInBackground(NoteEntity... params) {
                    noteEntityDao.delete(removeEntity);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    startSupportActionMode(actionModeCallbacks).finish();
                    actionMode.finish();
                }
            }.execute();

            String toShow = "Note";
            Snackbar.make(myCoordinatorLayout, "Deleted " + toShow, Snackbar.LENGTH_LONG)
                    .setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            items.add(removeIndex, removeEntity);

                            if(removeEntity.getDate() != null && removeEntity.isDated()){
                                Intent i = new Intent(MainActivity.this, NoteNotificationService.class);
                                i.putExtra(NoteNotificationService.NOTETEXT, removeEntity.getTitle());
                                i.putExtra(NoteNotificationService.NOTEID, removeEntity.getId());

                                Date date = null;
                                try {
                                    date = dateFormatFull.parse(removeEntity.getDate());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                if (date != null)
                                noteAlarmManager.createAlarm(i, removeEntity.getId().hashCode(), date.getTime());
                            }

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

            if (noteEntity.getImageUrl() != null && !noteEntity.getImageUrl().isEmpty()) {

                Picasso.with(MainActivity.this)
                        .load(new File(noteEntity.getImageUrl()))
                        .placeholder(R.mipmap.ic_launcher)
                        .resize(128, 128)
                        .into(holder.imageViewImage);
            }

            holder.update(noteEntity);

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

                imageViewImage = v.findViewById(R.id.imageViewNoteImage);
                textViewTitle = v.findViewById(R.id.textViewNoteTitle);
                textViewDescription = v.findViewById(R.id.textViewNoteDescription);
                textViewTime = v.findViewById(R.id.textViewNoteTime);
                imageViewPinned = v.findViewById(R.id.imageViewNotePinned);

            }

            void selectItem(NoteEntity item) {
                if (multiSelect) {
                    if (selectedItems.contains(item)) {
                        selectedItems.remove(item);
                        mView.setBackground(getResources().getDrawable(R.drawable.note_row_layout_ripple));
                    } else {
                        selectedItems.add(item);
                        mView.setBackgroundColor(getResources().getColor(R.color.rippleEffect));
                    }

                    actionMode.setTitle(selectedItems.size() + " selected");
                }
            }

            void update(final NoteEntity value) {
                //textView.setText(value + "");
                if (selectedItems.contains(value)) {
                    mView.setBackgroundColor(getResources().getColor(R.color.rippleEffect));
                } else {
                    mView.setBackground(getResources().getDrawable(R.drawable.note_row_layout_ripple));
                }
                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        if (!multiSelect) {
                            startSupportActionMode(actionModeCallbacks);
                            selectItem(value);
                        }
                        return true;
                    }
                });
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (multiSelect) {

                            if (selectedItems.size() == 1 && selectedItems.contains(value)){
                                startSupportActionMode(actionModeCallbacks).finish();
                                actionMode.finish();
                            }

                            selectItem(value);

                        } else {
                            NoteEntity note = items.get(mradmin.example.com.datetimeapp.activity.MainActivity.NoteAdapter.ViewHolder.this.getAdapterPosition());
                            Intent intent = new Intent(MainActivity.this, NoteDetailActivity.class);
                            intent.putExtra(NOTE_ITEM, note);
                            startActivity(intent);
                        }
                    }
                });
            }

        }
    }
}
