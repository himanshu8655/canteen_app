package com.example.nmims_canteen;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.graphics.Typeface.BOLD;

public class Wallet extends AppCompatActivity {
private TextView Coins;
private Button Redeem;
String wallet_db;
    FirebaseAuth auth;
    DatabaseReference reff,mDatabase;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title   
        getSupportActionBar().hide();// hide the title bar  
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);//enable full screen
        setContentView(R.layout.activity_wallet);
        auth = FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        Coins=(TextView)findViewById(R.id.Coins);
        final LinearLayout linearLayout=(LinearLayout)findViewById(R.id.ll);
        Redeem=findViewById(R.id.Redeem);

        Redeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Redeem();
            }
        });
        reff = FirebaseDatabase.getInstance().getReference().child("Member").child(user.getUid());

        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 wallet_db=dataSnapshot.child("wallet").getValue().toString();
                 Coins.setText(dataSnapshot.child("wallet").getValue().toString());



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Coins.setText(wallet_db);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Member").child(user.getUid()).child("wallet_txn");
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String data=dataSnapshot.getValue(String.class);
                String key=dataSnapshot.getKey();
                if(data.contains("+")) {
                    final LinearLayout linearLayout=(LinearLayout)findViewById(R.id.ll);
                    TextView textView = new TextView(Wallet.this);
                    textView.setTextColor(Color.rgb(63, 81, 181));
                    textView.setTypeface(null, BOLD);
                    textView.setLayoutParams(new TableLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0f));
                    textView.setText("MONEY ADDED");
                    textView.setTextSize(20);
                    textView.setPadding(30,30,0,0);
                    linearLayout.addView(textView);

                    TextView textView1 = new TextView(Wallet.this);
                    textView1.setLayoutParams(new TableLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0f));
                    textView1.setText("Txn ID: "+key);
                    textView1.setTextSize(14);
                    textView1.setTypeface(null, Typeface.ITALIC);
                    textView1.setPadding(30,10,0,0);
                    textView1.setTextColor(Color.rgb(0, 100, 0));
                    linearLayout.addView(textView1);

                    TextView textView2 = new TextView(Wallet.this);
                    textView2.setLayoutParams(new TableLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0f));
                    textView2.setText("Date: "+data.substring(0,data.indexOf("@")));
                    textView2.setTextSize(14);
                    textView2.setTypeface(null, BOLD);
                    textView2.setPadding(30,10,0,0);
                    textView2.setTextColor(Color.rgb(0, 100, 0));
                    linearLayout.addView(textView2);

                    TextView textView3 = new TextView(Wallet.this);
                    textView3.setLayoutParams(new TableLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0f));
                    textView3.setText("Balance: "+data.substring(data.indexOf("@")+1,data.length()));
                    textView3.setTextSize(14);
                    textView3.setTypeface(null, BOLD);
                    textView3.setPadding(30,10,0,0);
                    textView3.setTextColor(Color.rgb(0, 100, 0));
                    linearLayout.addView(textView3);

                    TextView textView4= new TextView(Wallet.this);
                    textView4.setLayoutParams(new TableLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0f));
                    textView4.setText("___________________________________________________________");
                    textView4.setTextSize(14);
                    textView4.setTypeface(null, BOLD);
                    textView4.setPadding(0,10,0,0);
                    textView4.setTextColor(Color.rgb(0, 0, 0));
                    linearLayout.addView(textView4);
                }

                else if(data.contains("-")) {

                   final LinearLayout linearLayout=(LinearLayout)findViewById(R.id.ll);
        TextView textView = new TextView(Wallet.this);
        textView.setTextColor(Color.rgb(63, 81, 181));
        textView.setTypeface(null, BOLD);
        textView.setLayoutParams(new TableLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0f));
        textView.setText("PAYMENT SUCCESSFUL");
        textView.setTextSize(20);
        textView.setPadding(30,30,0,0);
        linearLayout.addView(textView);

        TextView textView1 = new TextView(Wallet.this);
        textView1.setLayoutParams(new TableLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0f));
        textView1.setText("Txn ID: "+key);
        textView1.setTextSize(14);
        textView1.setTypeface(null, Typeface.ITALIC);
        textView1.setPadding(30,10,0,0);
        textView1.setTextColor(Color.rgb(217, 7, 7));
        linearLayout.addView(textView1);

        TextView textView2 = new TextView(Wallet.this);
        textView2.setLayoutParams(new TableLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0f));
        textView2.setText("Date: "+data.substring(0,data.indexOf("@")));
        textView2.setTextSize(14);
        textView2.setTypeface(null, BOLD);
        textView2.setPadding(30,10,0,0);
        textView2.setTextColor(Color.rgb(217, 7, 7));
        linearLayout.addView(textView2);

        TextView textView3 = new TextView(Wallet.this);
        textView3.setLayoutParams(new TableLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0f));
        textView3.setText("Balance: "+data.substring(data.indexOf("@")+1,data.length()));
        textView3.setTextSize(14);
        textView3.setTypeface(null, BOLD);
        textView3.setPadding(30,10,0,0);
        textView3.setTextColor(Color.rgb(217, 7, 7));
        linearLayout.addView(textView3);

        TextView textView4= new TextView(Wallet.this);
        textView4.setLayoutParams(new TableLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0f));
        textView4.setText("___________________________________________________________");
        textView4.setTextSize(14);
        textView4.setTypeface(null, BOLD);
        textView4.setPadding(0,10,0,0);
        textView4.setTextColor(Color.rgb(0, 0, 0));
        linearLayout.addView(textView4);

                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {


            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    public void Redeem() {
        Intent intent=new Intent(this,Canteen.class);
        startActivity(intent);
    }

}
