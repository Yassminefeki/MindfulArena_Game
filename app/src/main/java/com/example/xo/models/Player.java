package com.example.xo.models;

public class Player {
    private String name;
    private int    symbol;   // GameState.PLAYER_X or PLAYER_O
    private boolean isAi;
    private int    wins;
    private int    losses;
    private int    draws;
    private int    totalGames;

    public Player(String name, int symbol, boolean isAi) {
        this.name   = name;
        this.symbol = symbol;
        this.isAi   = isAi;
    }

    public void recordWin()   { wins++;   totalGames++; }
    public void recordLoss()  { losses++; totalGames++; }
    public void recordDraw()  { draws++;  totalGames++; }

    public double getWinRate() {
        return totalGames == 0 ? 0.0 : (double) wins / totalGames * 100.0;
    }

    // ── Getters / Setters ────────────────────────────────────────────────────
    public String getName()       { return name; }
    public void   setName(String n){ this.name = n; }
    public int    getSymbol()     { return symbol; }
    public boolean isAi()         { return isAi; }
    public int    getWins()       { return wins; }
    public int    getLosses()     { return losses; }
    public int    getDraws()      { return draws; }
    public int    getTotalGames() { return totalGames; }
}