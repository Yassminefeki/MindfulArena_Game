package com.example.xo.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.xo.R;
import com.example.xo.models.OnlineRoom;
import com.example.xo.utils.SoundManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OnlineGameActivity extends AppCompatActivity {

    private String roomId;
    private boolean isHost;
    private String myId;
    private DatabaseReference roomRef;
    private ValueEventListener roomListener;

    private Button[] cells = new Button[9];
    private TextView tvRoomId, tvStatus, tvRole, tvTurn;
    private OnlineRoom currentRoom;
    private SoundManager soundManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_game);

        roomId = getIntent().getStringExtra("ROOM_ID");
        isHost = getIntent().getBooleanExtra("IS_HOST", false);
        myId = FirebaseAuth.getInstance().getUid();
        roomRef = FirebaseDatabase.getInstance().getReference("rooms").child(roomId);
        soundManager = SoundManager.getInstance(this);

        initViews();
        listenToRoom();
    }

    private void initViews() {
        tvRoomId = findViewById(R.id.tv_room_id);
        tvStatus = findViewById(R.id.tv_game_status);
        tvRole = findViewById(R.id.tv_player_role);
        tvTurn = findViewById(R.id.tv_turn_indicator);

        tvRoomId.setText("Room: " + roomId);
        tvRole.setText(isHost ? "You are Player X (Host)" : "You are Player O (Guest)");

        for (int i = 0; i < 9; i++) {
            int id = getResources().getIdentifier("cell_" + i, "id", getPackageName());
            cells[i] = findViewById(id);
            final int pos = i;
            cells[i].setOnClickListener(v -> makeMove(pos));
        }

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
    }

    private void listenToRoom() {
        roomListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                OnlineRoom newRoom = snapshot.getValue(OnlineRoom.class);
                if (newRoom != null) {
                    // Check if a move was made by the opponent to play the sound
                    if (currentRoom != null && !newRoom.currentTurn.equals(currentRoom.currentTurn)) {
                        boolean wasXTurn = currentRoom.currentTurn.equals(currentRoom.player1Id);
                        soundManager.playTap(wasXTurn);
                    }
                    
                    // Check if game just ended
                    if (currentRoom != null && !currentRoom.status.equals("finished") && newRoom.status.equals("finished")) {
                        if (newRoom.winner.equals("draw")) {
                            soundManager.playDraw();
                        } else {
                            soundManager.playWin();
                        }
                    }

                    currentRoom = newRoom;
                    updateUI();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(OnlineGameActivity.this, "Connection lost", Toast.LENGTH_SHORT).show();
            }
        };
        roomRef.addValueEventListener(roomListener);
    }

    private void updateUI() {
        for (int i = 0; i < 9; i++) {
            int val = currentRoom.board.get(i);
            if (val == 1) {
                cells[i].setText("X");
                cells[i].setTextColor(getResources().getColor(R.color.primary));
            } else if (val == 2) {
                cells[i].setText("O");
                cells[i].setTextColor(getResources().getColor(R.color.secondary));
            } else {
                cells[i].setText("");
            }
        }

        if (currentRoom.status.equals("waiting")) {
            tvStatus.setText("Waiting for opponent...");
            tvTurn.setText("Share Room ID: " + roomId);
        } else if (currentRoom.status.equals("playing")) {
            tvStatus.setText("Game in Progress");
            boolean myTurn = currentRoom.currentTurn.equals(myId);
            tvTurn.setText(myTurn ? "Your Turn!" : "Opponent's Turn...");
        } else if (currentRoom.status.equals("finished")) {
            tvStatus.setText("Game Over");
            if (currentRoom.winner.equals("draw")) {
                tvTurn.setText("It's a Draw!");
            } else {
                boolean iWon = currentRoom.winner.equals(myId);
                tvTurn.setText(iWon ? "Victory!" : "Defeat...");
            }
        }
    }

    private void makeMove(int pos) {
        if (currentRoom == null || !currentRoom.status.equals("playing")) return;
        if (!currentRoom.currentTurn.equals(myId)) {
            Toast.makeText(this, "Not your turn", Toast.LENGTH_SHORT).show();
            return;
        }
        if (currentRoom.board.get(pos) != 0) return;

        int symbolVal = isHost ? 1 : 2;
        currentRoom.board.set(pos, symbolVal);
        currentRoom.currentTurn = isHost ? currentRoom.player2Id : currentRoom.player1Id;
        
        checkWinner();
        roomRef.setValue(currentRoom);
        // Tap sound is handled in the listener for both players
    }

    private void checkWinner() {
        int[][] winPos = {{0,1,2}, {3,4,5}, {6,7,8}, {0,3,6}, {1,4,7}, {2,5,8}, {0,4,8}, {2,4,6}};
        for (int[] p : winPos) {
            int v0 = currentRoom.board.get(p[0]);
            int v1 = currentRoom.board.get(p[1]);
            int v2 = currentRoom.board.get(p[2]);
            if (v0 != 0 && v0 == v1 && v0 == v2) {
                currentRoom.status = "finished";
                currentRoom.winner = (v0 == 1) ? currentRoom.player1Id : currentRoom.player2Id;
                return;
            }
        }
        if (!currentRoom.board.contains(0)) {
            currentRoom.status = "finished";
            currentRoom.winner = "draw";
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (roomRef != null && roomListener != null) {
            roomRef.removeEventListener(roomListener);
        }
    }
}