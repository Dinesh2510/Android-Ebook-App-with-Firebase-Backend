package com.pixeldev.firepdf.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import com.cloud.firepdf.R;

public class SplashActivity extends AppCompatActivity {
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_splash);
        findViewById(R.id.splash).setSystemUiVisibility(514);

        new Thread() {
            public void run() {
                try {
                    sleep(4000);
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
