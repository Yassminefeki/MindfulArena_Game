package com.example.xo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.xo.R;
import com.example.xo.models.GameMode;
import com.example.xo.models.OnlineRoom;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class HomeActivity extends AppCompatActivity {

    private MaterialCardView cardX, cardO;
    private MaterialButtonToggleGroup toggleRounds, toggleDifficulty, toggleMode;
    private MaterialButton btnPlay, btnHowToPlay;
    private LinearLayout layoutAiDifficulty;
    private TextView tvTournamentLabel;
    
    private String selectedSymbol = "X";
    private int selectedRounds = 5;
    private GameMode currentMode = GameMode.VS_AI_HARD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initViews();
        setupListeners();
        updateSelectionUI();
    }

    private void initViews() {
        cardX = findViewById(R.id.card_x);
        cardO = findViewById(R.id.card_o);
        toggleRounds = findViewById(R.id.toggle_rounds);
        toggleDifficulty = findViewById(R.id.toggle_difficulty);
        toggleMode = findViewById(R.id.toggle_mode);
        btnPlay = findViewById(R.id.btn_play);
        btnHowToPlay = findViewById(R.id.btn_how_to_play);
        layoutAiDifficulty = findViewById(R.id.layout_ai_difficulty);
        tvTournamentLabel = findViewById(R.id.tv_tournament_depth_label);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_arena) return true;
            if (id == R.id.nav_history) {
                startActivity(new Intent(this, StatisticsActivity.class));
                return true;
            }
            return false;
        });
    }

    private void setupListeners() {
        cardX.setOnClickListener(v -> selectSymbol("X"));
        cardO.setOnClickListener(v -> selectSymbol("O"));

        toggleMode.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                if (checkedId == R.id.btn_mode_pvp) {
                    currentMode = GameMode.TWO_PLAYER;
                    layoutAiDifficulty.setVisibility(View.GONE);
                    toggleRounds.setVisibility(View.VISIBLE);
                    tvTournamentLabel.setVisibility(View.VISIBLE);
                } else if (checkedId == R.id.btn_mode_ai) {
                    currentMode = GameMode.VS_AI_HARD; // Default to hard
                    layoutAiDifficulty.setVisibility(View.VISIBLE);
                    toggleRounds.setVisibility(View.VISIBLE);
                    tvTournamentLabel.setVisibility(View.VISIBLE);
                } else if (checkedId == R.id.btn_mode_online) {
                    currentMode = GameMode.ONLINE;
                    layoutAiDifficulty.setVisibility(View.GONE);
                    toggleRounds.setVisibility(View.GONE);
                    tvTournamentLabel.setVisibility(View.GONE);
                }
            }
        });

        toggleDifficulty.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                if (checkedId == R.id.btn_diff_easy) currentMode = GameMode.VS_AI_EASY;
                else if (checkedId == R.id.btn_diff_medium) currentMode = GameMode.VS_AI_MEDIUM;
                else if (checkedId == R.id.btn_diff_hard) currentMode = GameMode.VS_AI_HARD;
            }
        });

        toggleRounds.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                if (checkedId == R.id.btn_5_rounds) selectedRounds = 5;
                else if (checkedId == R.id.btn_10_rounds) selectedRounds = 10;
                else if (checkedId == R.id.btn_15_rounds) selectedRounds = 15;
            }
        });

        btnPlay.setOnClickListener(v -> {
            if (currentMode == GameMode.ONLINE) {
                showOnlineDialog();
            } else {
                startLocalGame();
            }
        });

        btnHowToPlay.setOnClickListener(v -> showHowToPlay());
    }

    private void startLocalGame() {
        Intent intent = new Intent(HomeActivity.this, GameActivity.class);
        intent.putExtra("SYMBOL", selectedSymbol);
        intent.putExtra("ROUNDS", selectedRounds);
        intent.putExtra(GameActivity.EXTRA_GAME_MODE, currentMode.name());
        startActivity(intent);
    }

    private void showOnlineDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_online_room, null);
        EditText etRoomId = view.findViewById(R.id.et_room_id);

        new MaterialAlertDialogBuilder(this)
                .setTitle("Online Arena")
                .setView(view)
                .setPositiveButton("Enter", (dialog, which) -> {
                    String roomId = etRoomId.getText().toString().trim();
                    if (roomId.isEmpty()) {
                        createOnlineRoom();
                    } else {
                        joinOnlineRoom(roomId);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void createOnlineRoom() {
        String roomId = String.valueOf(1000 + new Random().nextInt(9000));
        String userId = FirebaseAuth.getInstance().getUid();
        
        OnlineRoom room = new OnlineRoom(roomId, userId, "Player 1");
        
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("rooms").child(roomId);
        ref.setValue(room).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                navigateToOnlineGame(roomId, true);
            } else {
                Toast.makeText(this, "Failed to create room", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void joinOnlineRoom(String roomId) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("rooms").child(roomId);
        ref.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                OnlineRoom room = task.getResult().getValue(OnlineRoom.class);
                if (room.status.equals("waiting")) {
                    room.player2Id = FirebaseAuth.getInstance().getUid();
                    room.player2Name = "Player 2";
                    room.status = "playing";
                    ref.setValue(room);
                    navigateToOnlineGame(roomId, false);
                } else {
                    Toast.makeText(this, "Room is full or game started", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Room not found", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToOnlineGame(String roomId, boolean isHost) {
        Intent intent = new Intent(this, OnlineGameActivity.class);
        intent.putExtra("ROOM_ID", roomId);
        intent.putExtra("IS_HOST", isHost);
        startActivity(intent);
    }

    private void selectSymbol(String symbol) {
        selectedSymbol = symbol;
        updateSelectionUI();
    }

    private void updateSelectionUI() {
        if (selectedSymbol.equals("X")) {
            cardX.setStrokeWidth(6);
            cardX.setStrokeColor(getResources().getColor(R.color.primary));
            cardO.setStrokeWidth(0);
        } else {
            cardO.setStrokeWidth(6);
            cardO.setStrokeColor(getResources().getColor(R.color.secondary));
            cardX.setStrokeWidth(0);
        }
    }

    private void showHowToPlay() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("The Way of the Grid")
                .setMessage("Mindful Arena is a game of strategy and focus.\n\n" +
                           "• Objective: Align 3 of your symbols (X or O) horizontally, vertically, or diagonally.\n\n" +
                           "• Online: Create a room and share the 4-digit code with a friend to play over the internet.")
                .setPositiveButton("Enter Arena", null)
                .show();
    }
}