package com.example.cgpacalculator;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GradeDetails extends AppCompatActivity {

    private EditText sem;
    private Button go;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade_details);

       sem = (EditText)findViewById(R.id.sem);
       go = (Button)findViewById(R.id.go);

        String[] subject = getResources().getStringArray(R.array.subjects);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,subject);


        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sem.getText().toString().trim().isEmpty()) {
                    sem.setError("Please Enter your current Semester");
                    sem.requestFocus();
                    return;
                }
                sem.setEnabled(false);
                go.setEnabled(false);
                final int a = Integer.parseInt(sem.getText().toString());
                int id = 100, ab = 0,id1=1000;
                LinearLayout ll = (LinearLayout) findViewById(R.id.layout);
                // add edittext

                final AutoCompleteTextView et[] = new AutoCompleteTextView[a];
                final AutoCompleteTextView at[] = new AutoCompleteTextView[a];
                final AutoCompleteTextView bt[] = new AutoCompleteTextView[a];



                while (ab < a) {

                    bt[ab] = new AutoCompleteTextView(GradeDetails.this);
                    LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    bt[ab].setLayoutParams(p);
                    p.setMargins(100, 200, 70, 0);
                    ++ab;
                    bt[ab-1].setHint("Enter Name of Subject : " + ab);
                    --ab;
                    bt[ab].setId(id + 1);
                    ll.addView(bt[ab]);
                    bt[ab].setAdapter(adapter);
                    bt[ab].setThreshold(1);

                    et[ab] = new AutoCompleteTextView(GradeDetails.this);
                    LinearLayout.LayoutParams pa = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    et[ab].setLayoutParams(pa);
                    pa.setMargins(100, 20, 70, 0);
                    et[ab].setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL); //for decimal numbers
                    ++ab;
                    et[ab-1].setHint("Enter Credit for Subject : " + ab);
                    --ab;
                    et[ab].setId(id + 1);
                    ll.addView(et[ab]);


                    at[ab] = new AutoCompleteTextView(GradeDetails.this);
                    LinearLayout.LayoutParams pt = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    at[ab].setLayoutParams(pt);
                    at[ab].setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL); //for decimal numbers
                    pt.setMargins(100, 20, 10, 0);
                    ++ab;
                    at[ab-1].setHint("Enter grade point of Subject : " + ab);
                    --ab;
                    at[ab].setId(id1 + 1);
                    ll.addView(at[ab]);


                    id++;id1++;
                    ab++;
                }
                int bid=123;

                Button btn = new Button(GradeDetails.this);
                LinearLayout.LayoutParams pt = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                btn.setLayoutParams(pt);
                pt.setMargins(100, 20, 10, 0);
                btn.setText("Calculate");
                btn.setId(bid + 1);
                ll.addView(btn);

                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int credit=0,sum=0,id=101;
                        float gpa;

                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        String timeStamp= DateFormat.getDateTimeInstance().format(new Date());

                        for(int i=0;i<a;i++) {
                            if (bt[i].getText().toString().trim().isEmpty()) {
                                bt[i].setError("Please fill all fields");
                                bt[i].requestFocus();
                                return;
                            }
                            else if (et[i].getText().toString().trim().isEmpty()) {
                                et[i].setError("Please fill all fields");
                                et[i].requestFocus();
                                return;
                            }
                             if (at[i].getText().toString().trim().isEmpty()) {
                                at[i].setError("Please fill all fields");
                                at[i].requestFocus();
                                return;
                            }
                             if(Integer.parseInt(et[i].getText().toString())>10) {
                                et[i].setError("Please enter valid credit");
                                et[i].requestFocus();
                                return;
                            }
                             if(Integer.parseInt(at[i].getText().toString())>10) {
                                at[i].setError("Please enter valid grade point");
                                at[i].requestFocus();
                                return;
                            }


                        }


                        Intent intent = new Intent(GradeDetails.this, result.class);


                        String sub[] = new String[a];
                        String credits[] = new String[a];
                        String grades[] = new String[a];


                        for(int i=0;i<a;i++)
                        {
                            sub[i]=bt[i].getText().toString();
                            credits[i]=et[i].getText().toString();
                            grades[i]=at[i].getText().toString();
                            credit+=Integer.parseInt(et[i].getText().toString());
                          sum+=Integer.parseInt(et[i].getText().toString())*Integer.parseInt(at[i].getText().toString());;
                          DatabaseReference myRef = database.getReference("Users/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/"+ timeStamp +"/Subjects/"+bt[i].getText().toString()+"/Credit/");
                          myRef.setValue(et[i].getText().toString());
                          DatabaseReference mRef = database.getReference("Users/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/"+ timeStamp +"/Subjects/"+bt[i].getText().toString()+"/Grade/");
                          mRef.setValue(at[i].getText().toString());

                      }
                        intent.putExtra("tcredit",Integer.toString(credit) );

                      gpa=(float)sum/credit;

                        DatabaseReference mRef = database.getReference("Users/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/"+ timeStamp +"/CGPA/");
                        mRef.setValue(Float.toString(gpa));

                        intent.putExtra("n",a);

                        for(int j=0;j<a;j++)
                        {
                            intent.putExtra("sub"+Integer.toString(j),sub[j] );
                            intent.putExtra("credit"+Integer.toString(j),credits[j] );
                            intent.putExtra("grade"+Integer.toString(j),grades[j] );

                        }

intent.putExtra("gp", Float.toString(gpa));

startActivity(intent);
finish();


                    }
                });
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
                FirebaseAuth.getInstance().signOut();
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

}
