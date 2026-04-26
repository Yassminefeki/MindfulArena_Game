package com.example.xo;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.xo.activities.SplashActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Direct to splash screen as per original behavior
        startActivity(new Intent(this, SplashActivity.class));
        finish();
    }
}