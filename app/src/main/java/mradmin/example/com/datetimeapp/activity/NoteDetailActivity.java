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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.ParseException;
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


    private Date noteDate;

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

                if (switchCompat.isChecked()){
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

            }
        });

        imageViewSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = textViewTitle.getText().toString();
                String description = textViewDesc.getText().toString();

                Date date=null;
                boolean isDated = false;

                SimpleDateFormat dateFormatter = new SimpleDateFormat("d MMM yyyy HH:mm");

                if (switchCompat.isChecked() && textViewTime.getText()!=null && !textViewTime.getText().equals("")){
                    if (textViewTimeTime.getText()!=null && !textViewTimeTime.getText().equals("")){
                        try {

                            date = dateFormatter.parse(textViewTime.getText().toString() + " " + textViewTimeTime.getText().toString());
                            isDated = true;

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }

                saveNote(title, description, null, date, isDated);
            }
        });
    }

    private void saveNote (String title, String desc, @Nullable String imageUrl, @Nullable Date date, boolean isDated) {

        if (noteEntity == null) {
            noteEntity = new NoteEntity(UUID.randomUUID().toString(), 0, new NoteContent(title, desc, imageUrl), false, date, isDated);
        } else {
            noteEntity.setDate(date);
            noteEntity.setDated(isDated);

            NoteContent noteContent = noteEntity.getContent();
            noteContent.setTitle(title);
            noteContent.setDescription(desc);
            noteContent.setImageUrl(imageUrl);

            noteEntity.setContent(noteContent);
        }

        System.out.println("+++++++++++++++++++++++ " + noteEntity);

    }

    private void getExistingNoteDataMode(NoteEntity noteEntity) {

        textViewTitle.setText(noteEntity.getContent().getTitle());
        textViewDesc.setText(noteEntity.getContent().getDescription());

        if (noteEntity.isDated()) {

            switchCompat.setChecked(true);

            if (noteEntity.getDate()!=null){

                Date date = noteEntity.getDate();
                if (date != null) {
                    noteDate = date;
                }
                setDateEditText();
                setTimeEditText();

            }
        }
        else {
            switchCompat.setChecked(false);
        }

    }

    public void setDate(int year, int month, int day){
        Calendar calendar = Calendar.getInstance();
        int hour, minute;

        Calendar reminderCalendar = Calendar.getInstance();
        reminderCalendar.set(year, month, day);

        if(reminderCalendar.before(calendar)){
            Toast.makeText(this, "My time-machine is a bit rusty", Toast.LENGTH_SHORT).show();
            return;
        }

        if(noteDate!=null){
            calendar.setTime(noteDate);
        }

        hour = calendar.get(Calendar.HOUR_OF_DAY);

        minute = calendar.get(Calendar.MINUTE);

        calendar.set(year, month, day, hour, minute);
        noteDate = calendar.getTime();
        setDateEditText();
    }

    public void setTime(int hour, int minute){
        Calendar calendar = Calendar.getInstance();
        if(noteDate!=null){
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
        if (noteDate!=null)
            textViewTime.setText(new SimpleDateFormat("d MMM yyyy").format(noteDate));
    }

    public void setTimeEditText() {
        if (noteDate!=null)
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
}
