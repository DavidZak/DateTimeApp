package mradmin.example.com.datetimeapp.view.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mradmin.example.com.datetimeapp.R;
import mradmin.example.com.datetimeapp.activity.NoteDetailActivity;
import mradmin.example.com.datetimeapp.model.NoteEntity;
import mradmin.example.com.datetimeapp.util.LastSeen;

/**
 * Created by yks-11 on 1/15/18.
 */

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> implements MyItemTouchHelper.ItemTouchHelperAdapter {
    private List<NoteEntity> items;
    public static final String NOTE_ITEM = "com.example.mradmin.datetimeapp.MainActivity";

    LastSeen lastSeen = new LastSeen();

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

        items.remove(items.get(position));
        notifyItemRemoved(position);

    }

    @Override
    public NoteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_row_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final NoteAdapter.ViewHolder holder, final int position) {
        NoteEntity noteEntity = items.get(position);

        holder.textViewTitle.setText(noteEntity.getTitle());
        holder.textViewDescription.setText(noteEntity.getDescription());

        if (noteEntity.isDated()) {
            if (noteEntity.getDate() != null && !noteEntity.getDate().isEmpty()){
                holder.textViewTime.setText(noteEntity.getDate());
            }
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

                    NoteEntity note = items.get(ViewHolder.this.getAdapterPosition());
                    Intent intent = new Intent(v.getContext(), NoteDetailActivity.class);
                    intent.putExtra(NOTE_ITEM, note);
                    v.getContext().startActivity(intent);
                }
            });

            imageViewImage=v.findViewById(R.id.imageViewNoteImage);
            textViewTitle = v.findViewById(R.id.textViewNoteTitle);
            textViewDescription = v.findViewById(R.id.textViewNoteDescription);
            textViewTime = v.findViewById(R.id.textViewNoteTime);
            imageViewPinned = v.findViewById(R.id.imageViewNotePinned);

        }
    }
}
