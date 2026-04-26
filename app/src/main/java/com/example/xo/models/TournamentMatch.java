package com.example.xo.models;


public class TournamentMatch {
    private String player1Name;
    private String player2Name;
    private String winnerName;   // null = not played yet
    private int    player1Score;
    private int    player2Score;
    private int    round;
    private int    matchIndex;
    private boolean played;

    public TournamentMatch(String p1, String p2, int round, int matchIndex) {
        this.player1Name = p1;
        this.player2Name = p2;
        this.round       = round;
        this.matchIndex  = matchIndex;
    }

    public void setResult(String winner, int s1, int s2) {
        this.winnerName   = winner;
        this.player1Score = s1;
        this.player2Score = s2;
        this.played       = true;
    }

    // ── Getters ──────────────────────────────────────────────────────────────
    public String  getPlayer1Name()  { return player1Name; }
    public String  getPlayer2Name()  { return player2Name; }
    public String  getWinnerName()   { return winnerName; }
    public int     getPlayer1Score() { return player1Score; }
    public int     getPlayer2Score() { return player2Score; }
    public int     getRound()        { return round; }
    public int     getMatchIndex()   { return matchIndex; }
    public boolean isPlayed()        { return played; }

    public String getScoreDisplay() {
        return played ? player1Score + " – " + player2Score : "vs";
    }
}
