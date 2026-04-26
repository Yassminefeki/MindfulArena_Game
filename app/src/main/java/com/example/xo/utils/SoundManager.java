package com.example.xo.utils;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;
import com.example.xo.R;

public class SoundManager {

    private static SoundManager instance;
    private SoundPool soundPool;
    private int soundTapX, soundTapO, soundWin, soundDraw, soundHint;
    private boolean enabled = true;

    private SoundManager(Context ctx) {
        AudioAttributes attrs = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder()
                .setMaxStreams(5)
                .setAudioAttributes(attrs)
                .build();
        
        // Load raw sound resources from res/raw/
        soundTapX = soundPool.load(ctx, R.raw.x_play, 1);
        soundTapO = soundPool.load(ctx, R.raw.y_play, 1);
        soundWin  = soundPool.load(ctx, R.raw.congrats, 1);
        soundDraw = soundPool.load(ctx, R.raw.draw, 1);
        soundHint = soundPool.load(ctx, R.raw.hint, 1);
    }

    public static SoundManager getInstance(Context ctx) {
        if (instance == null) instance = new SoundManager(ctx.getApplicationContext());
        return instance;
    }

    public void playTap(boolean isX) {
        if (enabled) {
            int soundId = isX ? soundTapX : soundTapO;
            soundPool.play(soundId, 1, 1, 0, 0, 1);
        }
    }

    public void playWin()  { if (enabled) soundPool.play(soundWin,  1,1,0,0,1); }
    public void playDraw() { if (enabled) soundPool.play(soundDraw, 1,1,0,0,1); }
    public void playHint() { if (enabled) soundPool.play(soundHint, 1,1,0,0,1); }

    public void setEnabled(boolean e) { this.enabled = e; }
    public boolean isEnabled()        { return enabled; }
    public void release()             { soundPool.release(); instance = null; }
}