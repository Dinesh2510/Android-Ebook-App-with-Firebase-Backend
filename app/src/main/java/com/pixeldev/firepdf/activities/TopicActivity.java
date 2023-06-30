package com.pixeldev.firepdf.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cloud.firepdf.R;
import com.pixeldev.firepdf.adapter.TopicAdapter;
import com.pixeldev.firepdf.models.TopicModel;
import com.pixeldev.firepdf.models.CategoryModel;
import com.pixeldev.firepdf.utils.AdsManager;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;

public class TopicActivity extends AppCompatActivity {
    FrameLayout adContainerView;
    AdsManager adsManager;
    public ArrayList<TopicModel> chapterList;
    private RecyclerView recyclerViewChapter;
    MaterialToolbar toolbar;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_topic);
        adsManager = AdsManager.getInstance();
        MaterialToolbar materialToolbar = (MaterialToolbar) findViewById(R.id.toolbar);
        toolbar = materialToolbar;
        setSupportActionBar(materialToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        adContainerView = (FrameLayout) findViewById(R.id.adContainerView);
        findViewById(R.id.no_datalyt).setVisibility(View.GONE);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewTopic);
        Bundle bundle = getIntent().getExtras();
        CategoryModel modelItem3 = (CategoryModel) bundle.getSerializable("model");
        if (modelItem3 != null) {
            if (modelItem3.topiclist == null || modelItem3.topiclist.isEmpty()) {
                findViewById(R.id.no_datalyt).setVisibility(View.VISIBLE);
            } else {
                modelItem3.topiclist.size();
                chapterList = modelItem3.topiclist;
                toolbar.setTitle((CharSequence) modelItem3.getTitle());
                recyclerViewChapter = recyclerView;
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerViewChapter.setItemAnimator(new DefaultItemAnimator());
                recyclerViewChapter.setAdapter(new TopicAdapter(chapterList));
                findViewById(R.id.no_datalyt).setVisibility(View.GONE);
            }
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
}
