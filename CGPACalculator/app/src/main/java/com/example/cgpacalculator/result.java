package com.example.cgpacalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Type;

import io.netopen.hotbitmapgg.library.view.RingProgressBar;

public class result extends AppCompatActivity {

    RingProgressBar ring;
    int progress=0;

    TextView name,gpa,total,ctotal,remark;
    Button cal;
    ImageButton ex;


    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if(msg.what == 0)
            {
                if(progress<100)
                {
                    progress++;
                    ring.setProgress(progress);


                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()+"/Name");


        gpa=(TextView)findViewById(R.id.gpa);
        total=(TextView)findViewById(R.id.total);
        ctotal=(TextView)findViewById(R.id.ctotal);
        name=(TextView)findViewById(R.id.Name);
        remark=(TextView)findViewById(R.id.remark);
        cal=(Button)findViewById(R.id.Cal);
        ex=(ImageButton)findViewById(R.id.exit);

        remark.setVisibility(View.INVISIBLE);
        name.setText("Result for : ");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                name.setText("Result for : "+value);
                name.setTextColor(Color.parseColor("#FFEB719A"));

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("ERROR", "Failed to read value.", error.toException());
            }
        });

        ring=(RingProgressBar)findViewById(R.id.progress);




        final String gpa1=getIntent().getStringExtra("gp");
        Integer no = getIntent().getIntExtra("n",0);

        gpa.setText("CGPA  : ****");
        gpa.setTextColor(Color.parseColor("#FFB1ACAC"));

        ring.setOnProgressListener(new RingProgressBar.OnProgressListener() {
            @Override
            public void progressToComplete() {
                double r = Double.parseDouble(gpa1);
                r=Math.round(r*100.0)/100.0;
                String res = Double.toString(r);
                gpa.setText("CGPA  :  " + res);
                remark.setVisibility(View.VISIBLE);
                if(Float.parseFloat(gpa1)<6)
                {
                    remark.setText("Disappointing !!!");
                    gpa.setTextColor(Color.parseColor("#FF0000"));
                }
                else if(Float.parseFloat(gpa1)<7)
                {
                    remark.setText("Marginal !!!");
                    gpa.setTextColor(Color.parseColor("#FF4200"));
                }
                else if(Float.parseFloat(gpa1)<8)
                {
                    remark.setText("Admirable !!!");
                    gpa.setTextColor(Color.parseColor("#FF7F00"));
                }
                else if(Float.parseFloat(gpa1)<9)
                {
                    remark.setText("Fastidious !!!");
                    gpa.setTextColor(Color.parseColor("#FFBD00"));
                }
                else if(Float.parseFloat(gpa1)<10)
                {
                    remark.setText("Excellent !!!");
                    gpa.setTextColor(Color.parseColor("#A1F357"));
                }
                else
                {
                    remark.setText("Outstanding !!!");
                    gpa.setTextColor(Color.parseColor("#2ACB35"));
                }

            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {

                for(int i=0;i<100;i++)
                {
                    try {
                        Thread.sleep(100);
                        handler.sendEmptyMessage(0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

            }
        }).start();

        total.setText("Total Subject  :  "+Integer.toString(no));
        ctotal.setText("Total Credits  :  "+ getIntent().getStringExtra("tcredit"));


 cal.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View view) {
         Intent intent = new Intent(result.this,GradeDetails.class);
         startActivity(intent);
         finish();
     }
 });













        LinearLayout ll = (LinearLayout) findViewById(R.id.lay);

        final TextView et[] = new TextView[no];
        final TextView bt[] = new TextView[no];
        final TextView ct[] = new TextView[no];
        for(int k=0;k<no;k++)
        {
            et[k] = new TextView(result.this);
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            et[k].setLayoutParams(p);
            p.setMargins(150, 150, 70, 0);
            ll.addView(et[k]);
            et[k].setText("Subject  :  "+getIntent().getStringExtra("sub"+Integer.toString(k)));
            et[k].setTextColor(Color.parseColor("#ECD542"));

            bt[k] = new TextView(result.this);
            LinearLayout.LayoutParams pa = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            bt[k].setLayoutParams(pa);
            pa.setMargins(150, 20, 70, 0);
            ll.addView(bt[k]);
            bt[k].setText("Credits  :  "+getIntent().getStringExtra("credit"+Integer.toString(k)));
            bt[k].setTextColor(Color.parseColor("#DCAC59"));

            ct[k] = new TextView(result.this);
            LinearLayout.LayoutParams pb = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            ct[k].setLayoutParams(pb);
            pb.setMargins(150, 20, 70, 0);
            ll.addView(ct[k]);
            ct[k].setText("Grade Point  :  "+getIntent().getStringExtra("grade"+Integer.toString(k)));
            ct[k].setTextColor(Color.parseColor("#B99352"));

        }

        ex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(result.this);
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
