package com.pixeldev.firepdf.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.cloud.firepdf.R;
import com.pixeldev.firepdf.fragments.CategoryFragment;
import com.pixeldev.firepdf.fragments.TopicsFragment;
import com.pixeldev.firepdf.utils.AdsManager;
import com.github.barteksc.pdfviewer.BuildConfig;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    public static FragmentManager fragmentManager;
    FrameLayout adContainerView;
    AdsManager adsManager;
    BottomNavigationView bottomNavigationView;
    MaterialToolbar toolbar;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        AdsManager instance = AdsManager.getInstance();
        adsManager = instance;
        instance.loadInterAd(this);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        MaterialToolbar materialToolbar = (MaterialToolbar) findViewById(R.id.toolbar);
        toolbar = materialToolbar;
        setSupportActionBar(materialToolbar);
        adContainerView = (FrameLayout) findViewById(R.id.adContainerView);
        if (savedInstanceState == null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add((int) R.id.fragment_container, (Fragment) new TopicsFragment());
            fragmentTransaction.commit();
        }
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.bottom_nav1) {
                    adsManager.showInterAdOnClick(MainActivity.this, new AdsManager.InterAdListener() {
                        public void onClick(String type) {
                            getSupportActionBar().setTitle((CharSequence) getString(R.string.bottom_nav_title_1));
                            displaySelectedFragment(new TopicsFragment());
                        }
                    }, "");
                    return true;
                } else if (id == R.id.bottom_nav2) {
                    adsManager.showInterAdOnClick(MainActivity.this, new AdsManager.InterAdListener() {
                        public void onClick(String type) {
                            getSupportActionBar().setTitle((CharSequence) getString(R.string.bottom_nav_title_2));
                            displaySelectedFragment(new CategoryFragment());
                        }
                    }, "");
                    return true;
                }
                return true;
            }
        });
    }

    
    public void displaySelectedFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_share) {
            adsManager.showInterAdOnClick(this, new AdsManager.InterAdListener() {
                public void onClick(String type) {
                    Intent shareIntent = new Intent("android.intent.action.SEND");
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra("android.intent.extra.SUBJECT", getString(R.string.app_name));
                    shareIntent.putExtra("android.intent.extra.TEXT", getString(R.string.share_app_text) + "\n\nhttps://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                }
            }, "");
        } else if (id == R.id.action_about) {
            adsManager.showInterAdOnClick(this, new AdsManager.InterAdListener() {
                public void onClick(String type) {
                    startActivity(new Intent(MainActivity.this, AboutActivity.class));
                }
            }, "");
        } else if (id == R.id.action_policy) {
            adsManager.showInterAdOnClick(this, new AdsManager.InterAdListener() {
                public void onClick(String type) {
                    startActivity(new Intent(MainActivity.this, PrivacyActivity.class));
                }
            }, "");
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    private void updateAds() {
        adsManager.showBannerAd(this, adContainerView);
    }


    public void onPause() {
        super.onPause();
        adsManager.destroyBannerAds();
    }


    public void onResume() {
        super.onResume();
        updateAds();
    }


    public void onDestroy() {
        adsManager.destroyInterAds();
        super.onDestroy();
    }
}
