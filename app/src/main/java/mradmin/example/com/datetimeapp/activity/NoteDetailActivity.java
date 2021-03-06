package mradmin.example.com.datetimeapp.activity;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.service.voice.VoiceInteractionService;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.zelory.compressor.Compressor;
import mradmin.example.com.datetimeapp.NoteNotificationService;
import mradmin.example.com.datetimeapp.R;
import mradmin.example.com.datetimeapp.model.NoteEntity;
import mradmin.example.com.datetimeapp.model.db.AppDatabase;
import mradmin.example.com.datetimeapp.model.db.NoteEntityDao;
import mradmin.example.com.datetimeapp.util.NoteAlarmManager;

public class NoteDetailActivity extends AppCompatActivity implements  DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    public NoteEntity noteEntity;
    public boolean createNewNote = false;

    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.titleEditText)
    EditText textViewTitle;

    @BindView(R.id.descriptionEditText)
    EditText textViewDesc;

    @BindView(R.id.timeEditText)
    EditText textViewTime;

    @BindView(R.id.timeEditTextTime)
    EditText textViewTimeTime;

    @BindView(R.id.linearLayoutDateTime)
    LinearLayout linearLayoutDateTime;

    @BindView(R.id.switchCompatSetTime)
    SwitchCompat switchCompat;

    @BindView(R.id.noteImageView)
    ImageView noteImageView;

    @BindView(R.id.imageViewSave)
    ImageView imageViewSave;

    @BindView(R.id.imageViewPin)
    ImageView imageViewPin;

    SimpleDateFormat dateFormat = new SimpleDateFormat("d MMM yyyy HH:mm");

    private Date noteDate;
    private String imagePath;

    private boolean isPinned;

    private static final int GALLERY_PICK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        getWindow().setStatusBarColor(Color.TRANSPARENT);

        noteEntity = (NoteEntity) getIntent().getSerializableExtra(MainActivity.NOTE_ITEM);

        collapsingToolbarLayout = findViewById(R.id.toolbar_layout);
        textViewTitle = findViewById(R.id.titleEditText);
        textViewDesc = findViewById(R.id.descriptionEditText);
        textViewTime = findViewById(R.id.timeEditText);
        textViewTimeTime = findViewById(R.id.timeEditTextTime);
        linearLayoutDateTime = findViewById(R.id.linearLayoutDateTime);
        switchCompat = findViewById(R.id.switchCompatSetTime);
        noteImageView = findViewById(R.id.noteImageView);
        imageViewSave = findViewById(R.id.imageViewSave);
        imageViewPin = findViewById(R.id.imageViewPin);

        textViewTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        NoteDetailActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });

        textViewTimeTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar now = Calendar.getInstance();
                TimePickerDialog tpd = TimePickerDialog.newInstance(NoteDetailActivity.this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        true);
                tpd.show(getFragmentManager(), "Timepickerdialog");
            }
        });

        if (switchCompat.isChecked()) {
            linearLayoutDateTime.setVisibility(View.VISIBLE);
        } else {
            linearLayoutDateTime.setVisibility(View.GONE);
        }

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (switchCompat.isChecked()) {
                    linearLayoutDateTime.setVisibility(View.VISIBLE);
                } else {
                    linearLayoutDateTime.setVisibility(View.GONE);
                }

            }
        });

        if (noteEntity != null) {

            createNewNote = false;

            collapsingToolbarLayout.setTitle("Edit note");

            getExistingNoteDataMode(noteEntity);

        } else {

            createNewNote = true;

            collapsingToolbarLayout.setTitle("Add new note");
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), GALLERY_PICK);

            }
        });

        imageViewSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = textViewTitle.getText().toString();
                String description = textViewDesc.getText().toString();

                String date = null;
                boolean isDated = false;

                if (switchCompat.isChecked() && textViewTime.getText() != null && !textViewTime.getText().equals("")) {
                    if (textViewTimeTime.getText() != null && !textViewTimeTime.getText().equals("")) {
                        date = textViewTime.getText().toString() + " " + textViewTimeTime.getText().toString();
                        isDated = true;
                    }
                }

                if (allFieldsOk())
                    saveNote(title, description, imagePath, date, isDated, isPinned);
                else
                    Toast.makeText(NoteDetailActivity.this, "You need to fill one or more fields", Toast.LENGTH_SHORT).show();
            }
        });

        imageViewPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPinned){
                    unpin();
                } else {
                    pin();
                }
            }
        });
    }

    public void pin() {
        imageViewPin.setColorFilter(getResources().getColor(R.color.colorAccent));
        isPinned = true;
        Toast.makeText(this, "pinned", Toast.LENGTH_SHORT).show();
    }

    public void unpin() {
        imageViewPin.setColorFilter(getResources().getColor(R.color.white));
        isPinned = false;
        Toast.makeText(this, "un pinned", Toast.LENGTH_SHORT).show();
    }

    public boolean allFieldsOk() {
        if (textViewTitle.getText().toString().equals("") || textViewDesc.getText().toString().equals("") || (textViewTime.getText().toString().equals("") &&
                switchCompat.isChecked()) || (textViewTimeTime.getText().toString().equals("") && switchCompat.isChecked())) {
            return false;
        }
        return true;
    }

    private void saveNote(String title, String desc, @Nullable String imageUrl, @Nullable String date, boolean isDated, boolean isPinned) {

        AppDatabase appDatabase = Room.databaseBuilder(this,
                AppDatabase.class, "room-notes-database").build();

        final NoteEntityDao noteEntityDao = appDatabase.getNoteEntityDao();

        long creationDate = System.currentTimeMillis();

        if (noteEntity == null) {

            noteEntity = new NoteEntity(UUID.randomUUID().toString(), 0, title, desc, imageUrl, isPinned, date, isDated, creationDate);

            new AsyncTask<NoteEntity, Void, Void>() {

                @Override
                protected Void doInBackground(NoteEntity... voids) {
                    noteEntityDao.insertNote(noteEntity);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    Toast.makeText(NoteDetailActivity.this, "Note added", Toast.LENGTH_SHORT).show();
                }
            }.execute();

        } else {
            noteEntity.setDate(date);
            noteEntity.setDated(isDated);
            noteEntity.setTitle(title);
            noteEntity.setDescription(desc);
            noteEntity.setImageUrl(imageUrl);
            noteEntity.setPinned(isPinned);
            noteEntity.setCreationDate(creationDate);

            new AsyncTask<NoteEntity, Void, Void>() {
                @Override
                protected Void doInBackground(NoteEntity... noteEntities) {
                    noteEntityDao.updateNote(noteEntity);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    Toast.makeText(NoteDetailActivity.this, "Note updated", Toast.LENGTH_SHORT).show();
                }
            }.execute();
        }

        System.out.println("+++++++++++++++++++++++ " + noteEntity);

        NavUtils.navigateUpFromSameTask(this);
    }

    private void getExistingNoteDataMode(NoteEntity noteEntity) {

        textViewTitle.setText(noteEntity.getTitle());
        textViewDesc.setText(noteEntity.getDescription());

        isPinned = noteEntity.isPinned();
        if (isPinned)
            pin();
        else
            unpin();


        if (noteEntity.isDated()) {

            switchCompat.setChecked(true);

            if (noteEntity.getDate() != null) {

                String date = noteEntity.getDate();
                if (date != null) {
                    try {
                        noteDate = dateFormat.parse(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                setDateEditText();
                setTimeEditText();
            }

        } else {
            switchCompat.setChecked(false);
        }

        if (noteEntity.getImageUrl() != null && !noteEntity.getImageUrl().isEmpty()) {

            imagePath = noteEntity.getImageUrl();

            showImage(new File(imagePath));
        }

    }

    public void setDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        int hour, minute;

        Calendar reminderCalendar = Calendar.getInstance();
        reminderCalendar.set(year, month, day);

        if (reminderCalendar.before(calendar)) {
            Toast.makeText(this, "My time-machine is a bit rusty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (noteDate != null) {
            calendar.setTime(noteDate);
        }

        hour = calendar.get(Calendar.HOUR_OF_DAY);

        minute = calendar.get(Calendar.MINUTE);

        calendar.set(year, month, day, hour, minute);
        noteDate = calendar.getTime();
        setDateEditText();
    }

    public void setTime(int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        if (noteDate != null) {
            calendar.setTime(noteDate);
        }
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        calendar.set(year, month, day, hour, minute, 0);
        noteDate = calendar.getTime();
        setTimeEditText();
    }

    public void setDateEditText() {
        if (noteDate != null)
            textViewTime.setText(new SimpleDateFormat("d MMM yyyy").format(noteDate));
    }

    public void setTimeEditText() {
        if (noteDate != null)
            textViewTimeTime.setText(new SimpleDateFormat("HH:mm").format(noteDate));
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        setDate(year, monthOfYear, dayOfMonth);
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        setTime(hourOfDay, minute);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setAspectRatio(1, 1)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                Uri resultUri = result.getUri();

                uploadAvatarImage(resultUri);

            }
        }
    }

    private void showImage(File file) {

        String path = file.getAbsolutePath();

        if (!path.isEmpty() && path != null) {

            imagePath = path;

            Picasso.with(NoteDetailActivity.this)
                    .load(file)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(noteImageView);
        }
    }

    private void uploadAvatarImage(Uri url){

        File filePath = new File(url.getPath());
        File file = null;

        try {
            file = new Compressor(this)
                    .setMaxWidth(400)
                    .setMaxHeight(400)
                    .setQuality(75)
                    .compressToFile(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        showImage(file);

    }
}
