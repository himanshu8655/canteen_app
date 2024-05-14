package com.example.nmims_canteen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Tapatap extends AppCompatActivity {
    public EditText mEmail;
    public EditText mSubject;
    public  EditText mMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tapatap);
        mEmail=findViewById(R.id.mailID);
        mSubject=findViewById(R.id.subjectID);
        mMessage=findViewById(R.id.messageID);



        FloatingActionButton fab=findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sendMail();

            }
        });

    }

//    private void sendMail() {
//
//        String mail= mEmail.getText().toString().trim();
//        String subject= mSubject.getText().toString().trim();
//        String message= mMessage.getText().toString();
//
//        //Send Mail
//        JavaMailAPI javaMailAPI = new JavaMailAPI(this,mail,subject,message);
//        javaMailAPI.execute();
//    }
}
