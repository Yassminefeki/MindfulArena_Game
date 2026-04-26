package com.example.xo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.xo.R;
import com.example.xo.models.GameStats;
import com.example.xo.utils.AppDatabase;
import com.google.android.material.progressindicator.LinearProgressIndicator;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        int xWins = getIntent().getIntExtra("X_WINS", 0);
        int oWins = getIntent().getIntExtra("O_WINS", 0);
        int draws = getIntent().getIntExtra("DRAWS", 0);
        int total = getIntent().getIntExtra("TOTAL", 5);

        TextView tvWinner = findViewById(R.id.tv_tournament_winner);
        TextView tvFinalX = findViewById(R.id.tv_final_x);
        TextView tvFinalO = findViewById(R.id.tv_final_o);
        TextView tvFinalDraws = findViewById(R.id.tv_final_draws);
        LinearProgressIndicator progress = findViewById(R.id.progress_win_rate);

        tvFinalX.setText(String.valueOf(xWins));
        tvFinalO.setText(String.valueOf(oWins));
        tvFinalDraws.setText(String.valueOf(draws));

        String winner;
        String winnerSymbol = "Draw";
        if (xWins > oWins) {
            winner = "Player X is the Champion!";
            winnerSymbol = "X";
        } else if (oWins > xWins) {
            winner = "Player O is the Champion!";
            winnerSymbol = "O";
        } else {
            winner = "It's a Tournament Draw!";
        }
        tvWinner.setText(winner);

        int winRate = total > 0 ? (int) (((float) xWins / total) * 100) : 0;
        progress.setProgress(winRate);

        findViewById(R.id.btn_back_home).setOnClickListener(v -> {
            startActivity(new Intent(ResultActivity.this, HomeActivity.class));
            finish();
        });
        
        String finalWinnerSymbol = winnerSymbol;
        findViewById(R.id.btn_save_tournament).setOnClickListener(v -> {
            new Thread(() -> {
                GameStats stats = new GameStats("Tournament", finalWinnerSymbol);
                // Basic info for now
                stats.movesPlayed = total; 
                AppDatabase.getInstance(this).gameStatsDao().insert(stats);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Tournament saved to history!", Toast.LENGTH_SHORT).show();
                    findViewById(R.id.btn_save_tournament).setEnabled(false);
                });
            }).start();
        });
    }
}