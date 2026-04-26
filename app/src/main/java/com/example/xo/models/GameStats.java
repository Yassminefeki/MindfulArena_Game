package com.example.xo.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "game_stats")
public class GameStats {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String gameMode;
    public String winnerName;
    public int    movesPlayed;
    public long   durationMillis;
    public long   timestamp;
    public boolean playerXWon;
    public boolean playerOWon;
    public boolean isDraw;
    public int     hintsUsed;

    public GameStats() {
        this.timestamp = System.currentTimeMillis();
    }

    @Ignore
    public GameStats(String gameMode, String winnerName) {
        this.gameMode = gameMode;
        this.winnerName = winnerName;
        this.timestamp = System.currentTimeMillis();
        
        if ("X".equals(winnerName)) {
            this.playerXWon = true;
        } else if ("O".equals(winnerName)) {
            this.playerOWon = true;
        } else {
            this.isDraw = true;
        }
    }
}