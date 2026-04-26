package com.example.xo.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefsManager {
    private static final String PREFS_NAME = "xo_prefs";
    private static final String KEY_SOUND  = "sound_enabled";
    private static final String KEY_VIBRATE= "vibrate_enabled";
    private static final String KEY_THEME  = "theme_dark";
    private static final String KEY_P1_NAME= "player1_name";
    private static final String KEY_P2_NAME= "player2_name";

    private final SharedPreferences prefs;

    public PrefsManager(Context ctx) {
        prefs = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public boolean isSoundEnabled()   { return prefs.getBoolean(KEY_SOUND,   true); }
    public boolean isVibrateEnabled() { return prefs.getBoolean(KEY_VIBRATE, true); }
    public boolean isDarkTheme()      { return prefs.getBoolean(KEY_THEME,   true); }
    public String  getPlayer1Name()   { return prefs.getString(KEY_P1_NAME,  "Player X"); }
    public String  getPlayer2Name()   { return prefs.getString(KEY_P2_NAME,  "Player O"); }

    public void setSoundEnabled(boolean v)   { prefs.edit().putBoolean(KEY_SOUND,   v).apply(); }
    public void setVibrateEnabled(boolean v) { prefs.edit().putBoolean(KEY_VIBRATE, v).apply(); }
    public void setDarkTheme(boolean v)      { prefs.edit().putBoolean(KEY_THEME,   v).apply(); }
    public void setPlayer1Name(String v)     { prefs.edit().putString(KEY_P1_NAME,  v).apply(); }
    public void setPlayer2Name(String v)     { prefs.edit().putString(KEY_P2_NAME,  v).apply(); }
}
