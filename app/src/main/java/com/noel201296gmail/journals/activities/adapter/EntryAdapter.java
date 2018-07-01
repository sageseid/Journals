package com.noel201296gmail.journals.activities.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.noel201296gmail.journals.R;
import com.noel201296gmail.journals.activities.NewEntryActivity;
import com.noel201296gmail.journals.activities.model.EntriesModel;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class EntryAdapter extends RecyclerView.Adapter<EntryAdapter.EntryViewHolder>{

    private List<EntriesModel> entries;
    private Context context;
    private String noteId;
    private RecyclerViewItemClick mListener;


    public class EntryViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView txtTitle, txtTime;
        CardView noteCard;
        RelativeLayout mRelativeLayout;


        public EntryViewHolder(View itemView) {
            super(itemView);

            txtTitle =itemView.findViewById(R.id.note_title);
            txtTime = itemView.findViewById(R.id.note_time);
            noteCard = itemView.findViewById(R.id.note_card);
            mRelativeLayout= itemView.findViewById(R.id.relative_layout);

           /** mRelativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EntriesModel aEntrymodel = entries.get(getAdapterPosition());
                    Intent intent = new Intent(context, NewEntryActivity.class);
                    intent.putExtra("noteId",noteId);
                    intent.putExtra("title", aEntrymodel.getNoteTitle());
                    intent.putExtra("content", aEntrymodel.getNoteContent());
                    v.getContext().startActivity(intent);
                }
            });

            noteCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EntriesModel aEntrymodel = entries.get(getAdapterPosition());
                    Intent intent = new Intent(context, NewEntryActivity.class);
                    intent.putExtra("noteId",noteId);
                    intent.putExtra("title", aEntrymodel.getNoteTitle());
                    intent.putExtra("content", aEntrymodel.getNoteContent());
                    v.getContext().startActivity(intent);
                }
            });

            txtTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EntriesModel aEntrymodel = entries.get(getAdapterPosition());
                    Intent intent = new Intent(context, NewEntryActivity.class);
                    intent.putExtra("noteId",noteId);
                    intent.putExtra("title", aEntrymodel.getNoteTitle());
                    intent.putExtra("content", aEntrymodel.getNoteContent());
                    v.getContext().startActivity(intent);
                }
            });
            txtTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EntriesModel aEntrymodel = entries.get(getAdapterPosition());
                    Intent intent = new Intent(context, NewEntryActivity.class);
                    intent.putExtra("noteId",noteId);
                    intent.putExtra("title", aEntrymodel.getNoteTitle());
                    intent.putExtra("content", aEntrymodel.getNoteContent());
                    v.getContext().startActivity(intent);
                }
            });/**/

        }


        @Override
        public void onClick(View v) {
            EntriesModel aEntrymodel = entries.get(getAdapterPosition());
            Intent intent = new Intent(context, NewEntryActivity.class);
            intent.putExtra("noteId",noteId);
            intent.putExtra("title", aEntrymodel.getNoteTitle());
            intent.putExtra("content", aEntrymodel.getNoteContent());
            v.getContext().startActivity(intent);

        }
    }

    public interface RecyclerViewItemClick {

        public void OnItemClickListener(EntryAdapter.EntryViewHolder holder, int position);
    }

    public EntryAdapter(List<EntriesModel> entries, Context context,String noteId,RecyclerViewItemClick listener){
        this.entries = entries;
        this.context = context;
        this.noteId = noteId;
        this.mListener = listener;
    }


    @NonNull
    @Override
    public EntryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_entry, parent, false);
        return new EntryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final EntryViewHolder holder, final int position) {
        holder.txtTitle.setText(entries.get(position).getNoteTitle());

        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        holder.txtTime.setText(currentDateTimeString);


        holder.mRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.OnItemClickListener(holder, position);

            }
        });
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

}
