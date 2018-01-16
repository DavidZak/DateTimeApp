package mradmin.example.com.datetimeapp.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import mradmin.example.com.datetimeapp.R;
import mradmin.example.com.datetimeapp.model.NoteContent;
import mradmin.example.com.datetimeapp.model.NoteEntity;
import mradmin.example.com.datetimeapp.util.LastSeen;

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

    @BindView(R.id.timeTextInput)
    TextInputLayout textInputLayout;

    @BindView(R.id.switchCompatSetTime)
    SwitchCompat switchCompat;

    @BindView(R.id.noteImageView)
    ImageView noteImageView;

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
        textInputLayout = findViewById(R.id.timeTextInput);
        switchCompat = findViewById(R.id.switchCompatSetTime);
        noteImageView = findViewById(R.id.noteImageView);

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

        if (switchCompat.isChecked()) {
            textInputLayout.setVisibility(View.VISIBLE);
        } else {
            textInputLayout.setVisibility(View.GONE);
        }

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (switchCompat.isChecked()){
                    textInputLayout.setVisibility(View.VISIBLE);
                } else {
                    textInputLayout.setVisibility(View.GONE);
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

            }
        });
    }

    private void saveNote (String title, String desc, @Nullable String imageUrl, @Nullable Date date, boolean isDated) {

        noteEntity = new NoteEntity(UUID.randomUUID().toString(), 0, new NoteContent(title, desc, imageUrl), false, date, isDated);

    }

    private void getExistingNoteDataMode(NoteEntity noteEntity) {

        textViewTitle.setText(noteEntity.getContent().getTitle());
        textViewDesc.setText(noteEntity.getContent().getDescription());


        if (noteEntity.isDated()) {

            switchCompat.setChecked(true);

            if (noteEntity.getDate()!=null){

                Date date = noteEntity.getDate();
                textViewTime.setText(new SimpleDateFormat("yyyy-mm-dd").format(date));

            }
        }
        else {
            switchCompat.setChecked(false);
        }

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {

    }
}
