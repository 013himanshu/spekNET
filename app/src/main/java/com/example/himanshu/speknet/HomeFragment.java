package com.example.himanshu.speknet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment {
    //newsFeed variables...
    RecyclerView recyclerView;
    NewsFeedAdapter adapter;
    List<NewsFeed> newsItems;

    FirebaseAuth mAuth;
    FirebaseUser user;

    ProgressBar loader;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, null);
        loader=(ProgressBar)rootView.findViewById(R.id.loader);
        loader.setVisibility(View.VISIBLE);
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        String uid = user.getUid();
        //For News Feed...
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        newsItems = new ArrayList<>();
        DatabaseReference dbnews = FirebaseDatabase.getInstance().getReference("Posts");
        dbnews.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    for(DataSnapshot newsSnapshot:dataSnapshot.getChildren()) {
                        NewsFeed p = newsSnapshot.getValue(NewsFeed.class);
                        newsItems.add(p);
                        Collections.reverse(newsItems);
                    }
                    adapter=new NewsFeedAdapter(HomeFragment.this, newsItems);
                    recyclerView.setAdapter(adapter);
                    loader.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return rootView;
    }
}
