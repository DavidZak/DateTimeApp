package mradmin.example.com.datetimeapp.view.adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import mradmin.example.com.datetimeapp.R;

public class NoteRowOptionsLayout extends LinearLayout {

        public NoteRowOptionsLayout(Context context) {
            super(context);
        }

        public NoteRowOptionsLayout(Context context, AttributeSet attrs) {
            super(context, attrs);

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.note_row_options_layout, this, true);
        }

    }
