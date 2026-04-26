package com.example.xo.activities;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.xo.R;
import com.example.xo.models.GameStats;
import com.example.xo.utils.AppDatabase;
import com.example.xo.utils.StatsAdapter;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import java.util.List;

public class StatisticsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        initViews();
    }

    private void initViews() {
        TextView tvTotalGames = findViewById(R.id.tv_total_games);
        CircularProgressIndicator progressIndicator = findViewById(R.id.progress_overall_win_rate);
        TextView tvWinRatePercentage = findViewById(R.id.tv_win_rate_percentage);
        
        RecyclerView rvHistory = findViewById(R.id.rv_history);
        rvHistory.setLayoutManager(new LinearLayoutManager(this));

        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(this);
            int total = db.gameStatsDao().getTotalGames();
            int xWins = db.gameStatsDao().getXWins();
            List<GameStats> allStats = db.gameStatsDao().getAll();

            runOnUiThread(() -> {
                tvTotalGames.setText(String.valueOf(total));
                if (total > 0) {
                    int winRate = (int) (((float) xWins / total) * 100);
                    progressIndicator.setProgress(winRate);
                    tvWinRatePercentage.setText(winRate + "% Win Rate");
                } else {
                    progressIndicator.setProgress(0);
                    tvWinRatePercentage.setText("0% Win Rate");
                }
                
                StatsAdapter adapter = new StatsAdapter(allStats);
                rvHistory.setAdapter(adapter);
            });
        }).start();

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
    }
}