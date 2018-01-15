package mradmin.example.com.datetimeapp.view.adapter;

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
import mradmin.example.com.datetimeapp.model.NoteEntity;

/**
 * Created by yks-11 on 1/15/18.
 */

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> implements MyItemTouchHelper.ItemTouchHelperAdapter {
    private List<NoteEntity> items;

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
        NoteEntity item = items.get(position);
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

        ImageView imageViewImage;
        TextView textViewTitle;
        TextView textViewDescription;

        public ViewHolder(View v) {
            super(v);

            mView = v;

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }
    }
}
