package com.example.xo.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.xo.R;
import com.example.xo.models.GameStats;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StatsAdapter extends RecyclerView.Adapter<StatsAdapter.ViewHolder> {

    private List<GameStats> statsList;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault());

    public StatsAdapter(List<GameStats> statsList) {
        this.statsList = statsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_game_stats, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GameStats stats = statsList.get(position);
        String winnerText = stats.isDraw ? "Draw" : "Winner: " + stats.winnerName;
        holder.tvWinner.setText(winnerText);
        holder.tvMode.setText(stats.gameMode);
        holder.tvDate.setText(dateFormat.format(new Date(stats.timestamp)));
    }

    @Override
    public int getItemCount() {
        return statsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvWinner, tvMode, tvDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvWinner = itemView.findViewById(R.id.tv_item_winner);
            tvMode = itemView.findViewById(R.id.tv_item_mode);
            tvDate = itemView.findViewById(R.id.tv_item_date);
        }
    }
}