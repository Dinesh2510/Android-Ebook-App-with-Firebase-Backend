package com.pixeldev.firepdf.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.cloud.firepdf.R;
import com.google.android.material.appbar.MaterialToolbar;

public class AboutActivity extends AppCompatActivity {
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_about);
        MaterialToolbar toolbar = (MaterialToolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle((CharSequence) getString(R.string.about_us));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void onBackPressed() {
        super.onBackPressed();
    }
}
