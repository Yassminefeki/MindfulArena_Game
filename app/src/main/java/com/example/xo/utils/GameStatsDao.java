package com.example.xo.utils;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.xo.models.GameStats;
import java.util.List;

@Dao
public interface GameStatsDao {

    @Insert
    void insert(GameStats stats);

    @Query("SELECT * FROM game_stats ORDER BY timestamp DESC")
    List<GameStats> getAll();

    @Query("SELECT COUNT(*) FROM game_stats")
    int getTotalGames();

    @Query("SELECT COUNT(*) FROM game_stats WHERE playerXWon = 1")
    int getXWins();

    @Query("SELECT COUNT(*) FROM game_stats WHERE playerOWon = 1")
    int getOWins();

    @Query("SELECT COUNT(*) FROM game_stats WHERE isDraw = 1")
    int getDraws();

    @Query("SELECT AVG(movesPlayed) FROM game_stats")
    double getAvgMoves();

    @Query("SELECT AVG(durationMillis) FROM game_stats")
    double getAvgDuration();

    @Query("SELECT COUNT(*) FROM game_stats WHERE gameMode = :mode")
    int getGamesForMode(String mode);

    @Query("SELECT SUM(hintsUsed) FROM game_stats")
    int getTotalHintsUsed();

    @Query("DELETE FROM game_stats")
    void deleteAll();
}