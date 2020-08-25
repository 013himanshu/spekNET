package com.example.himanshu.speknet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountFragment extends Fragment {
    FirebaseAuth mAuth;
    FirebaseUser user;
    String uid;
    String myEmail;
    String userName;
    String instiName;


    FirebaseDatabase db;
    DatabaseReference rootRef;
    DatabaseReference userRef;
    DatabaseReference getUserNameRef;
    DatabaseReference getInstiNameRef;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_account, null);

        final TextView accName = (TextView)rootView.findViewById(R.id.accName);
        TextView accEmail = (TextView)rootView.findViewById(R.id.accEmail);
        final TextView accInstiName = (TextView)rootView.findViewById(R.id.accInstiName);

        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        uid=user.getUid();
        myEmail=user.getEmail();

        db = FirebaseDatabase.getInstance();
        rootRef = db.getReference();
        userRef = rootRef.child("Posts");
        getUserNameRef = rootRef.child("Users").child(uid).child("userName");
        getInstiNameRef = rootRef.child("Users").child(uid).child("instituteName");

        accEmail.setText(myEmail);

        getUserNameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    userName = dataSnapshot.getValue().toString();
                    accName.setText(userName);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        getInstiNameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    instiName = dataSnapshot.getValue().toString();
                    accInstiName.setText(instiName);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return rootView;
    }

}
