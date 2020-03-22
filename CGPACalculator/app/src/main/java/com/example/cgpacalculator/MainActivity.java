package com.example.cgpacalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText email,pass;
    private Button go,reg;
    private FirebaseAuth mAuth;
    private String email1,pass1,name1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = (EditText)findViewById(R.id.email);
        pass = (EditText)findViewById(R.id.pass);
        go = (Button)findViewById(R.id.go);
        reg=(Button)findViewById(R.id.reg);

        mAuth = FirebaseAuth.getInstance();

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, register.class);
                startActivity(intent);
                finish();
            }
        });

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(email.getText().toString().trim().isEmpty() || !email.getText().toString().trim().matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+"))
                {
                    email.setError("Please Enter Valid email");
                    email.requestFocus();
                    return;
                }
                else if(pass.getText().toString().trim().isEmpty())
                {
                    pass.setError("Please Enter your password");
                    pass.requestFocus();
                    return;
                }

                else {

                    email1=email.getText().toString().trim();
                    pass1=pass.getText().toString().trim();
                                        login(email1,pass1);
                }
            }
        });




    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Do you want to Exit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user pressed "yes", then he is allowed to exit from application
                finish();
            }
        });
        builder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                dialog.cancel();
            }
        });
        AlertDialog alert=builder.create();
        alert.show();
    }

    private void login(String email2, String password2) {
        final ProgressDialog progressDialog = ProgressDialog.show(MainActivity.this, "Please wait...", "Logging in ...", true);

        mAuth.signInWithEmailAndPassword(email2, password2)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Success" ,"signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent in = new Intent(MainActivity.this, GradeDetails.class);
                            startActivity(in);
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("ERROR", "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });

    }

    }
