package com.example.nmims_canteen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {
    protected ImageButton btnSignUp;
    private EditText inputEmail, inputPassword;     //hit option + enter if you on mac , for windows hit ctrl + enter

    private FirebaseAuth auth;
    private EditText phn_no,name;
    DatabaseReference reff;
    Member member;
    FirebaseUser user;
    CheckBox showpassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title   
        getSupportActionBar().hide();// hide the title bar  
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);//enable full screen
        setContentView(R.layout.activity_sign_up);

        showpassword=findViewById(R.id.showpassword);

        showpassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    inputPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else
                {
                    inputPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        auth = FirebaseAuth.getInstance();
        phn_no = (EditText) findViewById(R.id.phone_no);
        name = (EditText) findViewById(R.id.name);

        btnSignUp = (ImageButton) findViewById(R.id.sign_up_button);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        user=auth.getCurrentUser();

        reff= FirebaseDatabase.getInstance().getReference().child("Member");

//        btnResetPassword.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(Register.this, ResetPasswordActivity.class));
//            }
//        });
        if(auth.getCurrentUser()!=null)
        {
            Intent intent=new Intent(SignUp.this,FirstScreen.class);
            startActivity(intent);

        }


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                final String phone_number = phn_no.getText().toString().trim();
                final String namee = name.getText().toString().trim();


                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(email.contains("@") && email.contains(".com"))
                {}
                else {
                    Toast.makeText(getApplicationContext(), "Email ID is not of proper format!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(phone_number)) {
                    Toast.makeText(getApplicationContext(), "Enter Phone Number!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(namee)) {
                    Toast.makeText(getApplicationContext(), "Enter name!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (phone_number.length() !=10) {
                    Toast.makeText(getApplicationContext(), "Phone number must be of 10 digits", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;

                }

                //create user
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(SignUp.this,"Account Created", Toast.LENGTH_SHORT).show();

                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignUp.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    String wallet="0";
                                    String wallet_txn=null;
                                    Member member=new Member(namee,phone_number,email,wallet,wallet_txn);
                                    String uid = task.getResult().getUser().getUid();
                                    reff.child(uid).setValue(member);
                                    Intent intent = new Intent(SignUp.this,FirstScreen.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}

