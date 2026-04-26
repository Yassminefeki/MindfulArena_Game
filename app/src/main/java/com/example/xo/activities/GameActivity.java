package com.example.xo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import com.example.xo.AI.TicTacToeAI;
import com.example.xo.models.GameMode;
import com.example.xo.models.GameState;
import com.example.xo.models.GameStats;
import com.example.xo.R;
import com.example.xo.utils.AppDatabase;
import com.example.xo.utils.SoundManager;

public class GameActivity extends AppCompatActivity {

    public static final String EXTRA_GAME_MODE = "extra_game_mode";

    private GameState gameState;
    private TicTacToeAI ai;
    private TicTacToeAI.Difficulty difficulty = TicTacToeAI.Difficulty.HARD;
    private GameMode mode;

    private Button[] cells = new Button[9];
    private TextView tvGameRound, tvScoreX, tvScoreO, tvScoreDraws, tvTurnIndicator;
    
    private String userSymbol;
    private int totalRounds;
    private int currentRound = 1;
    private int xWins = 0, oWins = 0, draws = 0;
    private SoundManager soundManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        soundManager = SoundManager.getInstance(this);

        userSymbol = getIntent().getStringExtra("SYMBOL");
        if (userSymbol == null) userSymbol = "X";
        totalRounds = getIntent().getIntExtra("ROUNDS", 5);

        String modeStr = getIntent().getStringExtra(EXTRA_GAME_MODE);
        mode = (modeStr != null) ? GameMode.valueOf(modeStr) : GameMode.VS_AI_HARD;

        if (mode == GameMode.VS_AI_EASY) difficulty = TicTacToeAI.Difficulty.EASY;
        else if (mode == GameMode.VS_AI_MEDIUM) difficulty = TicTacToeAI.Difficulty.MEDIUM;
        else difficulty = TicTacToeAI.Difficulty.HARD;

        gameState = new GameState();
        ai = new TicTacToeAI();

        initViews();
        updateUI();

        if (mode != GameMode.TWO_PLAYER && gameState.getCurrentPlayer() == GameState.PLAYER_X && userSymbol.equals("O")) {
            new Handler().postDelayed(this::aiMove, 600);
        }
    }

    private void initViews() {
        tvGameRound = findViewById(R.id.tv_game_round);
        tvScoreX = findViewById(R.id.tv_score_x);
        tvScoreO = findViewById(R.id.tv_score_o);
        tvScoreDraws = findViewById(R.id.tv_score_draws);
        tvTurnIndicator = findViewById(R.id.tv_turn_indicator);

        GridLayout grid = findViewById(R.id.game_grid);
        for (int i = 0; i < 9; i++) {
            int id = getResources().getIdentifier("cell_" + i, "id", getPackageName());
            cells[i] = findViewById(id);
            final int pos = i;
            cells[i].setOnClickListener(v -> makeMove(pos));
        }

        findViewById(R.id.btn_reveal_move).setOnClickListener(v -> showHint());
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
    }

    private void makeMove(int pos) {
        if (gameState.isGameOver()) return;

        int current = gameState.getCurrentPlayer();
        boolean isX = (current == GameState.PLAYER_X);

        // Check if it's the user's turn in AI mode
        if (mode != GameMode.TWO_PLAYER) {
            String currentSymbol = isX ? "X" : "O";
            if (!currentSymbol.equals(userSymbol)) return;
        }

        if (gameState.makeMove(pos)) {
            soundManager.playTap(isX);
            updateUI();
            if (checkGameOver()) return;

            if (mode != GameMode.TWO_PLAYER) {
                new Handler().postDelayed(this::aiMove, 500);
            }
        }
    }

    private void aiMove() {
        if (gameState.isGameOver()) return;
        int current = gameState.getCurrentPlayer();
        int move = ai.getBestMove(gameState, difficulty);
        if (move != -1 && gameState.makeMove(move)) {
            soundManager.playTap(current == GameState.PLAYER_X);
            updateUI();
            checkGameOver();
        }
    }

    private void updateUI() {
        int[] board = gameState.getBoard();
        for (int i = 0; i < 9; i++) {
            String text = "";
            if (board[i] == GameState.PLAYER_X) {
                text = "X";
                cells[i].setTextColor(getResources().getColor(R.color.primary));
            } else if (board[i] == GameState.PLAYER_O) {
                text = "O";
                cells[i].setTextColor(getResources().getColor(R.color.secondary));
            }
            cells[i].setText(text);
        }

        tvGameRound.setText("Game " + currentRound + "/" + totalRounds);
        tvScoreX.setText(String.valueOf(xWins));
        tvScoreO.setText(String.valueOf(oWins));
        tvScoreDraws.setText(String.valueOf(draws));

        int current = gameState.getCurrentPlayer();
        tvTurnIndicator.setText("Player " + (current == GameState.PLAYER_X ? "X" : "O") + "'s Turn");
    }

    private boolean checkGameOver() {
        if (!gameState.isGameOver()) return false;

        int winner = gameState.getWinner();
        if (winner == GameState.PLAYER_X) {
            xWins++;
            soundManager.playWin();
        } else if (winner == GameState.PLAYER_O) {
            oWins++;
            soundManager.playWin();
        } else {
            draws++;
            soundManager.playDraw();
        }

        updateUI();

        new Handler().postDelayed(() -> {
            if (currentRound < totalRounds) {
                currentRound++;
                gameState.reset();
                updateUI();
                if (mode != GameMode.TWO_PLAYER && gameState.getCurrentPlayer() == GameState.PLAYER_X && userSymbol.equals("O")) {
                    aiMove();
                }
            } else {
                finishTournament();
            }
        }, 1500);

        return true;
    }

    private void finishTournament() {
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("X_WINS", xWins);
        intent.putExtra("O_WINS", oWins);
        intent.putExtra("DRAWS", draws);
        intent.putExtra("TOTAL", totalRounds);
        startActivity(intent);
        finish();
    }

    private void showHint() {
        soundManager.playHint();
        int bestMove = ai.getBestMove(gameState, difficulty);
        if (bestMove != -1) {
            cells[bestMove].setBackgroundColor(getResources().getColor(R.color.background));
            new Handler().postDelayed(() -> cells[bestMove].setBackgroundResource(R.drawable.bg_cell), 1000);
        }
    }
}