package com.example.himanshu.speknet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPswActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    EditText email;

    @Override
    protected void onStart() {
        super.onStart();

        mAuth=FirebaseAuth.getInstance();
        mAuthListener=new FirebaseAuth.AuthStateListener() {

            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!=null) {
                    startActivity(new Intent(ForgotPswActivity.this, HomeActivity.class));
                }
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_psw);
        getSupportActionBar().hide();

        email=(EditText)findViewById(R.id.email);
    }

    public void onSubmit(View view) {
        final ProgressDialog dialog = ProgressDialog.show(ForgotPswActivity.this, "", "Please wait...", true);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String emailAddress= email.getText().toString().trim();
        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //Log.d(TAG, "Email sent.");
                            dialog.dismiss();
                            Toast.makeText(ForgotPswActivity.this, "Password reset email link sent.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}
