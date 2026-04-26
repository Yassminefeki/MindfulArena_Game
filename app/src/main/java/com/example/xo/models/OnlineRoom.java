package com.example.xo.models;

import java.util.ArrayList;
import java.util.List;

public class OnlineRoom {
    public String roomId;
    public String player1Id;
    public String player2Id;
    public String player1Name;
    public String player2Name;
    public String currentTurn; // ID of the player whose turn it is
    public List<Integer> board; // 0 for empty, 1 for X, 2 for O
    public String winner; // ID of the winner or "draw"
    public String status; // "waiting", "playing", "finished"

    public OnlineRoom() {
        // Default constructor for Firebase
    }

    public OnlineRoom(String roomId, String player1Id, String player1Name) {
        this.roomId = roomId;
        this.player1Id = player1Id;
        this.player1Name = player1Name;
        this.player2Id = "";
        this.player2Name = "";
        this.currentTurn = player1Id;
        this.status = "waiting";
        this.winner = "";
        
        // Initialize empty board
        this.board = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            this.board.add(0);
        }
    }
}