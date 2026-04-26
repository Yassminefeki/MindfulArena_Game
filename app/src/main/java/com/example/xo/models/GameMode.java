package com.example.xo.models;

public enum GameMode {
    TWO_PLAYER("2 Player"),
    VS_AI_EASY("vs AI - Easy"),
    VS_AI_MEDIUM("vs AI - Medium"),
    VS_AI_HARD("vs AI - Hard"),
    TOURNAMENT("Tournament"),
    ONLINE("Online");

    private final String displayName;

    GameMode(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isAiMode() {
        return this == VS_AI_EASY || this == VS_AI_MEDIUM || this == VS_AI_HARD;
    }

    public int getAiDifficulty() {
        switch (this) {
            case VS_AI_EASY:   return 1;
            case VS_AI_MEDIUM: return 2;
            case VS_AI_HARD:   return 3;
            default:           return 0;
        }
    }
}