package com.example.nmims_canteen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

public class Profile extends AppCompatActivity {

    FirebaseUser user;
    FirebaseAuth auth;
    DatabaseReference reff;
    private EditText name,phone_number,email,password;
    private Button db_btn;
    String name1,phone_number1,email1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title   
        getSupportActionBar().hide();// hide the title bar  
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//enable full screen
        setContentView(R.layout.activity_profile);
        db_btn=findViewById(R.id.db_button);
        phone_number=findViewById(R.id.phone_no);
        email=findViewById(R.id.email);
        email.setKeyListener(null);
        password=findViewById(R.id.password);
        password.setKeyListener(null);
        name=findViewById(R.id.name);
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        reff = FirebaseDatabase.getInstance().getReference().child("Member").child(user.getUid());
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name1=dataSnapshot.child("Name").getValue().toString();
                phone_number1=dataSnapshot.child("ph").getValue().toString();
                email1=dataSnapshot.child("Email").getValue().toString();
                name.setText(dataSnapshot.child("Name").getValue().toString());
                phone_number.setText(dataSnapshot.child("ph").getValue().toString());
                email.setText(dataSnapshot.child("Email").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        db_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((name.getText().toString()).equals(name1)&& (phone_number.getText().toString()).equals(phone_number1) && (email.getText().toString()).equals(email1))
                {
                    Toast.makeText(Profile.this,"No Changes Detected",Toast.LENGTH_SHORT).show();
                    return;}
                reff.child("Name").setValue(name.getText().toString());
                reff.child("ph").setValue(phone_number.getText().toString());
                reff.child("Email").setValue(email.getText().toString());
                Toast.makeText(Profile.this,"Profile Updated",Toast.LENGTH_SHORT).show();
            }
        });

    }
}
