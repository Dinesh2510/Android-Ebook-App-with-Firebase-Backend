package com.pixeldev.firepdf.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pixeldev.firepdf.adapter.TopicAdapter;
import com.pixeldev.firepdf.models.TopicModel;
import com.pixeldev.firepdf.utils.Methods;
import com.cloud.firepdf.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class TopicsFragment extends Fragment {
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    private RelativeLayout no_internet;
    
    public ProgressBar progressBar;

    public RecyclerView recyclerViewBook;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_topics, container, false);
        FirebaseDatabase instance = FirebaseDatabase.getInstance();
        firebaseDatabase = instance;
        databaseReference = instance.getReference("/topics");
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewBook);
        recyclerViewBook = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        recyclerViewBook.setClipToPadding(false);
        recyclerViewBook.setHasFixedSize(true);
        recyclerViewBook.setItemAnimator(new DefaultItemAnimator());
        no_internet = (RelativeLayout) view.findViewById(R.id.no_internet);
        fetchData();
        ((Button) view.findViewById(R.id.Retrybtn)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                fetchData();
            }
        });
        return view;
    }

    
    public void fetchData() {
        if (Methods.isNetworkAvailable(requireActivity())) {
            progressBar.setVisibility(View.VISIBLE);
            no_internet.setVisibility(View.GONE);
            getdata();
            return;
        }
        no_internet.setVisibility(View.VISIBLE);
    }

    private void getdata() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                Log.e("TAG_APP_A", "onDataChange: " + snapshot.getValue());
                DataSnapshot dataSnapshotRes = snapshot.child("restaurants");

                recyclerViewBook.setAdapter(new TopicAdapter((List) snapshot.getValue(new GenericTypeIndicator<List<TopicModel>>() {
                })));
                progressBar.setVisibility(View.GONE);
            }

            public void onCancelled(DatabaseError error) {
                Toast.makeText(requireActivity(), "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
