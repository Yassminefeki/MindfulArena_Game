package com.example.xo.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.xo.R;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        // Add switches for sound, theme, player name, etc.
    }
}