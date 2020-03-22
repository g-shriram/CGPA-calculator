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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class register extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText email,pass,name;
    private String email1,pass1,name1;
    private Button go;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        email = (EditText)findViewById(R.id.email);
        name = (EditText)findViewById(R.id.name);
        pass = (EditText)findViewById(R.id.pass);
        go = (Button)findViewById(R.id.go);



      go.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              if(name.getText().toString().trim().isEmpty())
              {
                  name.setError("Please Enter your");
                  name.requestFocus();
                  return;
              }

              else  if(email.getText().toString().trim().isEmpty() || !email.getText().toString().trim().matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+"))
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
                  name1=name.getText().toString().trim();
                       register_email(email1,pass1,name1);
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



    private void register_email(final String email2, final String password2, final String name2) {
     final   ProgressDialog progressDialog = ProgressDialog.show(register.this,"Please wait...","Registering your credentials...",true);

        mAuth.createUserWithEmailAndPassword(email2, password2)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("SUCCESS", "createUserWithEmail:success");
                            Toast.makeText(register.this, "Authentication Success.",
                                    Toast.LENGTH_SHORT).show();

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference("Users/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/Name");

                            myRef.setValue(name2);
                            myRef = database.getReference("Users/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/Email");

                            myRef.setValue(email2);
                            myRef = database.getReference("Users/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/Password");

                            myRef.setValue(password2);


                            Intent i=new Intent(register.this,MainActivity.class);
                            startActivity(i);
                            finish();


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("ERROR", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(register.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }
}


