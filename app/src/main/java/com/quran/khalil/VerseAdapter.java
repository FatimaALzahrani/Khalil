package com.quran.khalil;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class VerseAdapter extends RecyclerView.Adapter<VerseAdapter.VerseViewHolder> {

    private List<String> verseList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public VerseAdapter(List<String> verseList, OnItemClickListener listener) {
        this.verseList = verseList;
        this.listener = listener;

    }

    @NonNull
    @Override
    public VerseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_surah, parent, false);
        return new VerseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VerseViewHolder holder, int position) {
        String verse = verseList.get(position);
        holder.bind(verse, listener);
    }

    @Override
    public int getItemCount() {
        return verseList.size();
    }

    public static class VerseViewHolder extends RecyclerView.ViewHolder {
        private final TextView verseTextView;

        public VerseViewHolder(@NonNull View itemView) {
            super(itemView);
            verseTextView = itemView.findViewById(R.id.surahNumberTextView);
        }

        public void bind(String verse,final OnItemClickListener listener) {
            verseTextView.setText(verse);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(getAdapterPosition());
                }
            });
        }
    }
}
