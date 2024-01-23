package com.quran.khalil;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {

    private List<UserMistake> userMistakeList;

    public LeaderboardAdapter(List<UserMistake> userMistakeList) {
        this.userMistakeList = userMistakeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_leaderboard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserMistake userMistake = userMistakeList.get(position);

        holder.textViewUsername.setText(userMistake.getUsername());
        holder.textViewMistakes.setText(String.valueOf(userMistake.getMistakes()));
        holder.textViewWords.setText(String.valueOf(userMistake.getWords()));
    }

    @Override
    public int getItemCount() {
        return userMistakeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewUsername;
        TextView textViewMistakes;
        TextView textViewWords;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewUsername = itemView.findViewById(R.id.textViewUsername);
            textViewMistakes = itemView.findViewById(R.id.textViewMistakes);
            textViewWords = itemView.findViewById(R.id.textViewWords);
        }
    }

    // إضافة الطريقة التي تقوم بتحديث البيانات في الـ Adapter
    public void setData(List<UserMistake> newData) {
        userMistakeList.clear();
        userMistakeList.addAll(newData);
        notifyDataSetChanged();
    }
}
