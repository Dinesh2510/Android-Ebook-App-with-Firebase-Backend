package com.pixeldev.firepdf.activities;

import android.os.Bundle;
import android.text.Html;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.cloud.firepdf.R;
import com.pixeldev.firepdf.utils.AdsManager;
import com.google.android.material.appbar.MaterialToolbar;
import java.io.IOException;
import java.io.InputStream;

public class PrivacyActivity extends AppCompatActivity {
    FrameLayout adContainerView;
    AdsManager adsManager;
    String str;
    TextView textview_privacy_policy;

    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_privacy);
       adsManager = AdsManager.getInstance();
       adContainerView = (FrameLayout) findViewById(R.id.adContainerView);
       textview_privacy_policy = (TextView) findViewById(R.id.textview_privacy_policy);
       // getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        MaterialToolbar toolbar = (MaterialToolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle((CharSequence) getString(R.string.privacy_policy));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        try {
            InputStream is = getAssets().open("privacy_policy.txt");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            String str2 = new String(buffer);
           str = str2;
           textview_privacy_policy.setText(Html.fromHtml(str2));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    private void updateAds() {
       adsManager.showBannerAd(this,adContainerView);
    }

    
    public void onPause() {
        super.onPause();
       adsManager.destroyBannerAds();
    }

    
    public void onResume() {
        super.onResume();
        updateAds();
    }
}
