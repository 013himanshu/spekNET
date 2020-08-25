package com.example.himanshu.speknet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {

    EditText userName;
    EditText instName;
    EditText email;
    EditText psw;

    private FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser user;
    String uid;

    FirebaseDatabase db;
    DatabaseReference rootRef;
    DatabaseReference userRef;

    @Override
    public void onStart() {
        super.onStart();
        //mAuth.addAuthStateListener(mAuthListener);
        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth=FirebaseAuth.getInstance();
        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!=null) {
                    startActivity(new Intent(SignupActivity.this, HomeActivity.class));
                }
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().hide();

        mAuth=FirebaseAuth.getInstance();


        db = FirebaseDatabase.getInstance();
        rootRef = db.getReference();
        userRef = rootRef.child("Users");

        userName=(EditText)findViewById(R.id.fullName);
        instName=(EditText)findViewById(R.id.instituteName);
        email=(EditText)findViewById(R.id.email);
        psw=(EditText)findViewById(R.id.psw);

        //mAuth = FirebaseAuth.getInstance();

    }

    public void reg(View view) {
        final ProgressDialog dialog = ProgressDialog.show(SignupActivity.this, "", "Please wait...", true);
        final String myName=userName.getText().toString().trim();
        final String myInstName=instName.getText().toString().trim();
        final String myEmail=email.getText().toString().trim();
        final String myPsw=psw.getText().toString().trim();

        if(myName.isEmpty()) {
            dialog.dismiss();
            Toast.makeText(SignupActivity.this, "Name required.",
                    Toast.LENGTH_SHORT).show();
        }
        else if(myInstName.isEmpty()) {
            dialog.dismiss();
            Toast.makeText(SignupActivity.this, "Institute name required.",
                    Toast.LENGTH_SHORT).show();
        }
        else if(myEmail.isEmpty()) {
            dialog.dismiss();
            Toast.makeText(SignupActivity.this, "Email required.",
                    Toast.LENGTH_SHORT).show();
        }
        else if(myPsw.isEmpty()) {
            dialog.dismiss();
            Toast.makeText(SignupActivity.this, "Password required.",
                    Toast.LENGTH_SHORT).show();
        }
        else if(myPsw.length()<6) {
            dialog.dismiss();
            Toast.makeText(SignupActivity.this, "Password must be of atleast 6 characters.",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            mAuth.createUserWithEmailAndPassword(myEmail, myPsw)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.i("TAG", "createUserWithEmail:success");

                                //getting user id...
                                user=FirebaseAuth.getInstance().getCurrentUser();
                                uid=user.getUid();

                                //storing values in database
                                HashMap<String, String> userMap = new HashMap<String, String>();
                                userMap.put("userName", myName);
                                userMap.put("instituteName", myInstName);
                                userMap.put("email", myEmail);

                                userRef.child(uid).setValue(userMap);

                                dialog.dismiss();
                                Toast.makeText(SignupActivity.this, "Authentication success.",
                                        Toast.LENGTH_SHORT).show();
                                FirebaseUser user = mAuth.getCurrentUser();
                                Intent jumpIntent = new Intent(SignupActivity.this, HomeActivity.class);
                                startActivity(jumpIntent);
                                //updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.i("TAG", "createUserWithEmail:failure", task.getException());
                                dialog.dismiss();
                                Toast.makeText(SignupActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                            }

                            // ...
                        }
                    });
        }


    }
}
