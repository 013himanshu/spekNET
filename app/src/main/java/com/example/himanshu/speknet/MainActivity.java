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

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    EditText email;
    EditText psw;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        mAuth=FirebaseAuth.getInstance();
        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!=null) {
                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                }
            }
        };

        email=(EditText)findViewById(R.id.email);
        psw=(EditText)findViewById(R.id.psw);
    }

    public void jumpSignup(View view) {
        Intent signupIntent = new Intent(this, SignupActivity.class);
        startActivity(signupIntent);
    }

    public void jumpForgotPsw(View view) {
        Intent signupIntent = new Intent(this, ForgotPswActivity.class);
        startActivity(signupIntent);
    }

    public void onLogin(View view) {
        final ProgressDialog dialog = ProgressDialog.show(MainActivity.this, "", "Please wait...", true);
        final String myEmail=email.getText().toString().trim();
        final String myPsw=psw.getText().toString().trim();

        if(myEmail.isEmpty()) {
            dialog.dismiss();
            Toast.makeText(MainActivity.this, "Email id required.",
                    Toast.LENGTH_SHORT).show();
        }
        else if(myPsw.isEmpty()) {
            dialog.dismiss();
            Toast.makeText(MainActivity.this, "Password required.",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            mAuth.signInWithEmailAndPassword(myEmail, myPsw)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.i("TAG", "signInWithEmail:success");
                                dialog.dismiss();
                                Toast.makeText(MainActivity.this, "Authentication success.",
                                        Toast.LENGTH_SHORT).show();
                                FirebaseUser user = mAuth.getCurrentUser();
                                Intent jumpIntent = new Intent(MainActivity.this, HomeActivity.class);
                                startActivity(jumpIntent);
                                //updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                dialog.dismiss();
                                Log.i("TAG", "signInWithEmail:failure", task.getException());
                                Toast.makeText(MainActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                            }

                            // ...
                        }
                    });
        }


    }
}
