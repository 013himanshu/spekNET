package com.example.himanshu.speknet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseAuth.AuthStateListener mAuthListener;
    EditText postText;
    Button postButton;

    FirebaseDatabase db;
    DatabaseReference rootRef;
    DatabaseReference userRef;
    DatabaseReference getUserNameRef;
    String uid;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth=FirebaseAuth.getInstance();
        user= FirebaseAuth.getInstance().getCurrentUser();
        uid=user.getUid();
        db = FirebaseDatabase.getInstance();
        rootRef = db.getReference();
        userRef = rootRef.child("Posts");
        getUserNameRef = rootRef.child("Users").child(uid).child("userName");


        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null) {
                    startActivity(new Intent(HomeActivity.this, MainActivity.class));
                }
            }
        };

        BottomNavigationView navigation=findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        loadFragment(new HomeFragment());




    }

    private boolean loadFragment(Fragment fragment) {
        if(fragment!=null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment=null;
        switch (item.getItemId()) {
            case R.id.navigation_home:
                fragment = new HomeFragment();
                break;
            case R.id.navigation_post:
                fragment = new PostFragment();
                break;
            case R.id.navigation_account:
                fragment = new AccountFragment();
                break;
        }

        return loadFragment(fragment);
    }

    //Sharing Post...
    public void postClick(View view) {
        final ProgressDialog dialog = ProgressDialog.show(HomeActivity.this, "", "Please wait...", true);
        postButton=(Button)findViewById(R.id.postButton);
        postText=(EditText)findViewById(R.id.postTextBox);

        final String post=postText.getText().toString().trim();
        final String email=user.getEmail().toString().trim();


        if(post.isEmpty()) {
            dialog.dismiss();
            Toast.makeText(HomeActivity.this,"Post Empty.", Toast.LENGTH_SHORT).show();
        }
        else if(post.length()>200) {
            dialog.dismiss();
            Toast.makeText(HomeActivity.this,"Post should be under 200 characters.", Toast.LENGTH_SHORT).show();
        }
        else {
            getUserNameRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    name = dataSnapshot.getValue().toString();

                    HashMap<String, String> userMap = new HashMap<String, String>();
                    userMap.put("Email", email);
                    userMap.put("UID", uid);
                    userMap.put("userName", name);
                    userMap.put("PostText", post);
                    userRef.push().setValue(userMap);
                    dialog.dismiss();
                    postText.setText("");

                    Toast.makeText(HomeActivity.this,"Posted successfully.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }




    //logout code...
    public void logout(View view) {
        mAuth.getInstance().signOut();
        Toast.makeText(HomeActivity.this,"Logged out success.",Toast.LENGTH_SHORT).show();
        Intent myIntent = new Intent(HomeActivity.this, MainActivity.class);
        startActivity(myIntent);
    }



}
