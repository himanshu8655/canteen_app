package com.example.nmims_canteen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.icu.text.CaseMap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfIndirectReference;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


import in.shadowfax.proswipebutton.ProSwipeButton;
import java.util.Date;
public class Canteen extends AppCompatActivity {
    private static final int STORAGE_CODE = 1000 ;
    public static String mpath;
    public static String mname;
    ProSwipeButton btn_cart;
    private ElegantNumberButton coffee_e;
    private ElegantNumberButton tea_e;
    private ElegantNumberButton burger_e;
    private ElegantNumberButton donuts_e;
    private ElegantNumberButton samosa_e;
    private ElegantNumberButton vada_e;
    private ElegantNumberButton bhel_e;
    private ElegantNumberButton cheese_e;
    private ElegantNumberButton manchurian_e;
    private ElegantNumberButton frankie_e;
    private ElegantNumberButton dosa_e;
    private ElegantNumberButton pizza_e;
    private ElegantNumberButton dessert_e;



    private Button coffee_a;
    private Button tea_a;
    private Button burger_a;
    private Button donuts_a;
    private Button samosa_a;
    private Button vada_a;
    private Button bhel_a;
    private Button cheese_a;
    private Button manchurian_a;
    private Button frankie_a;
    private Button dosa_a;
    private Button pizza_a;
    private Button dessert_a;
    int db_wallet;
    private String email,name,phn,Email,Name,Phone;
    FirebaseUser user;
    FirebaseAuth auth;
    int d1,d2,d3,d4,d5,d6,d7,d8,d9,d10,d11,d12,d13;
    int t1=0,t2=0,t3=0,t4=0,t5=0,t6=0,t7=0,t8=0,t9=0,t10=0,t11=0,t12=0,t13=0,temp11=0,intTotal=0,sum=0;
    private TextView paisa;



    DatabaseReference reff;
    private TextView total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title   
        getSupportActionBar().hide();// hide the title bar  
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);//enable full screen
        setContentView(R.layout.activity_canteen);
        paisa=(TextView)findViewById(R.id.paisa);
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        reff = FirebaseDatabase.getInstance().getReference().child("Member").child(user.getUid());
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                db_wallet=Integer.parseInt(dataSnapshot.child("wallet").getValue().toString());
                 Email= email=dataSnapshot.child("Email").getValue().toString();
                 Name= name=dataSnapshot.child("Name").getValue().toString();
                 Phone= phn=dataSnapshot.child("ph").getValue().toString();
                 paisa.setText("                                                  Balance: ₹ "+dataSnapshot.child("wallet").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
        btn_cart=findViewById(R.id.btn_cart);
        btn_cart.setOnSwipeListener(new ProSwipeButton.OnSwipeListener() {
            @Override
            public void onSwipeConfirm() {
                if(db_wallet>=Integer.parseInt(total.getText().toString()) && Integer.parseInt(total.getText().toString())!=0 )
                {
                    reff.child("wallet").setValue(Integer.toString(db_wallet-Integer.parseInt(total.getText().toString())));
                new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {


                            btn_cart.showResultIcon(true);

                            Toast.makeText(getApplicationContext(),"Order Placed Successfully  ",Toast.LENGTH_SHORT).show();
                            SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
                            Date todayDate = new Date();
                            String thisDate = currentDate.format(todayDate);
                            reff.child("wallet_txn").push().setValue(thisDate+"@-"+Integer.parseInt(total.getText().toString()));

                            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M)
                            {
                                if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
                                {
                                    //permission was not granted, request it
                                    String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                                    requestPermissions(permissions, STORAGE_CODE);



                                }
                                else
                                {
                                    //permission already granted, call save pdf method
                                    savePdf();
                                    sendMail();
                                    Intent intent=new Intent(Canteen.this,Confirm.class);
                                    startActivity(intent);

                                }
                            }
                            else
                            {
                                //system OS < Marshmallow, call save pdf method
                                savePdf();
                                sendMail();
                                Intent intent=new Intent(Canteen.this,Confirm.class);
                                startActivity(intent);
                            }
                        }

                    }, 2000);


                }
                else if(Integer.parseInt(total.getText().toString())==0)
                {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            btn_cart.showResultIcon(false);
                            Toast.makeText(getApplicationContext(),"No Item Selected",Toast.LENGTH_SHORT).show();
                        }
                    }, 2000);
                }
                else
                {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            btn_cart.showResultIcon(false);
                            Toast.makeText(getApplicationContext(),"Insufficient Balance",Toast.LENGTH_SHORT).show();
                        }
                    }, 2000);
                }

            }
        });

        total=findViewById(R.id.total);

        coffee_e=(ElegantNumberButton) findViewById(R.id.coffee_e);
        coffee_a=findViewById(R.id.coffee_a);

        tea_e=(ElegantNumberButton) findViewById(R.id.tea_e);
        tea_a=findViewById(R.id.tea_a);

        burger_e=(ElegantNumberButton) findViewById(R.id.burger_e);
        burger_a=findViewById(R.id.burger_a);

        donuts_e=(ElegantNumberButton) findViewById(R.id.donuts_e);
        donuts_a=findViewById(R.id.donuts_a);

        samosa_e=(ElegantNumberButton) findViewById(R.id.samosa_e);
        samosa_a=findViewById(R.id.samosa_a);

        vada_e=(ElegantNumberButton) findViewById(R.id.vada_e);
        vada_a=findViewById(R.id.vada_a);

        bhel_e=(ElegantNumberButton) findViewById(R.id.bhel_e);
        bhel_a=findViewById(R.id.bhel_a);

        cheese_e=(ElegantNumberButton) findViewById(R.id.cheese_e);
        cheese_a=findViewById(R.id.cheese_a);

        manchurian_e=(ElegantNumberButton) findViewById(R.id.manchurian_e);
        manchurian_a=findViewById(R.id.manchurian_a);

        frankie_e=(ElegantNumberButton) findViewById(R.id.frankie_e);
        frankie_a=findViewById(R.id.frankie_a);

        dosa_e=(ElegantNumberButton) findViewById(R.id.dosa_e);
        dosa_a=findViewById(R.id.dosa_a);

        pizza_e=(ElegantNumberButton) findViewById(R.id.pizza_e);
        pizza_a=findViewById(R.id.pizza_a);

        dessert_e=(ElegantNumberButton) findViewById(R.id.dessert_e);
        dessert_a=findViewById(R.id.dessert_a);

        coffee_e.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                if(newValue==0)
                {coffee_a.setVisibility(View.VISIBLE);
                    coffee_e.setVisibility(View.GONE);
                    coffee_e.setNumber("0");
                    temp11=temp11-100;
                    total.setText(Integer.toString(temp11));
                    d1=0;

                }

                else if(newValue>6)
                {
                    coffee_e.setNumber("6");
                    Toast.makeText(getApplicationContext(),"You cannot add more than 6",Toast.LENGTH_SHORT).show();
                    total.setText(Integer.toString(temp11));


                }
                else
                {
                    if(oldValue>newValue){
                    coffee_a.setVisibility(View.GONE);
                    coffee_e.setVisibility(View.VISIBLE);
                    temp11=temp11-100;
                    total.setText(Integer.toString(temp11));
                        d1=newValue;

                    }
                    else if(newValue>oldValue)
                    {

                    temp11=temp11+100;
                        total.setText(Integer.toString(temp11));
                        d1=newValue;

                    }

                    }
                }
        });
        coffee_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                coffee_a.setVisibility(View.GONE);
                coffee_e.setVisibility(View.VISIBLE);
                coffee_e.setNumber("1");
                temp11=temp11+100;
                total.setText(Integer.toString(temp11));
                d1=1;
            }
        });


        tea_e.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                if(newValue==0)
                {tea_a.setVisibility(View.VISIBLE);
                    tea_e.setVisibility(View.GONE);
                    tea_e.setNumber("0");
                    temp11=temp11-50;
                    total.setText(Integer.toString(temp11));
                    d2=0;

                }

                else if(newValue>6)
                {
                    tea_e.setNumber("6");
                    Toast.makeText(getApplicationContext(),"You cannot add more than 6",Toast.LENGTH_SHORT).show();
                    total.setText(Integer.toString(temp11));


                }
                else
                {
                    if(oldValue>newValue){
                        tea_a.setVisibility(View.GONE);
                        tea_e.setVisibility(View.VISIBLE);
                        temp11=temp11-50;
                        total.setText(Integer.toString(temp11));
                        d2=newValue;

                    }
                    else if(newValue>oldValue)
                    {

                        temp11=temp11+50;
                        total.setText(Integer.toString(temp11));
                        d2=newValue;
                    }

                }
            }
        });
        tea_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tea_a.setVisibility(View.GONE);
                tea_e.setVisibility(View.VISIBLE);
                tea_e.setNumber("1");
                temp11=temp11+50;
                total.setText(Integer.toString(temp11));
                d2=1;
            }
        });

        burger_e.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                if(newValue==0)
                {burger_a.setVisibility(View.VISIBLE);
                    burger_e.setVisibility(View.GONE);
                    burger_e.setNumber("0");
                    temp11=temp11-180;
                    total.setText(Integer.toString(temp11));
                    d3=0;

                }

                else if(newValue>6)
                {
                    burger_e.setNumber("6");
                    Toast.makeText(getApplicationContext(),"You cannot add more than 6",Toast.LENGTH_SHORT).show();
                    total.setText(Integer.toString(temp11));


                }
                else
                {
                    if(oldValue>newValue){
                        burger_a.setVisibility(View.GONE);
                        burger_e.setVisibility(View.VISIBLE);
                        temp11=temp11-180;
                        total.setText(Integer.toString(temp11));
                        d3=newValue;

                    }
                    else if(newValue>oldValue)
                    {

                        temp11=temp11+180;
                        total.setText(Integer.toString(temp11));
                        d3=newValue;
                    }

                }
            }
        });
        burger_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                burger_a.setVisibility(View.GONE);
                burger_e.setVisibility(View.VISIBLE);
                burger_e.setNumber("1");
                temp11=temp11+180;
                total.setText(Integer.toString(temp11));
                d3=1;
            }
        });


        donuts_e.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                if(newValue==0)
                {donuts_a.setVisibility(View.VISIBLE);
                    donuts_e.setVisibility(View.GONE);
                    donuts_e.setNumber("0");
                    temp11=temp11-80;
                    total.setText(Integer.toString(temp11));
                    d4=0;

                }

                else if(newValue>6)
                {
                    donuts_e.setNumber("6");
                    Toast.makeText(getApplicationContext(),"You cannot add more than 6",Toast.LENGTH_SHORT).show();
                    total.setText(Integer.toString(temp11));


                }
                else
                {
                    if(oldValue>newValue){
                        donuts_a.setVisibility(View.GONE);
                        donuts_e.setVisibility(View.VISIBLE);
                        temp11=temp11-80;
                        total.setText(Integer.toString(temp11));
                        d4=newValue;

                    }
                    else if(newValue>oldValue)
                    {

                        temp11=temp11+80;
                        total.setText(Integer.toString(temp11));
                        d4=newValue;
                    }

                }
            }
        });
        donuts_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                donuts_a.setVisibility(View.GONE);
                donuts_e.setVisibility(View.VISIBLE);
                donuts_e.setNumber("1");
                temp11=temp11+80;
                total.setText(Integer.toString(temp11));
                d4=1;
            }
        });


        samosa_e.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                if(newValue==0)
                {samosa_a.setVisibility(View.VISIBLE);
                    samosa_e.setVisibility(View.GONE);
                    samosa_e.setNumber("0");
                    temp11=temp11-25;
                    total.setText(Integer.toString(temp11));
                    d5=0;
                }

                else if(newValue>6)
                {
                    samosa_e.setNumber("6");
                    Toast.makeText(getApplicationContext(),"You cannot add more than 6",Toast.LENGTH_SHORT).show();
                    total.setText(Integer.toString(temp11));


                }
                else
                {
                    if(oldValue>newValue){
                        samosa_a.setVisibility(View.GONE);
                        samosa_e.setVisibility(View.VISIBLE);
                        temp11=temp11-25;
                        total.setText(Integer.toString(temp11));
                        d5=newValue;

                    }
                    else if(newValue>oldValue)
                    {

                        temp11=temp11+25;
                        total.setText(Integer.toString(temp11));
                        d5=newValue;
                    }

                }
            }
        });
        samosa_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                samosa_a.setVisibility(View.GONE);
                samosa_e.setVisibility(View.VISIBLE);
                samosa_e.setNumber("1");
                temp11=temp11+25;
                total.setText(Integer.toString(temp11));
                d5=1;
            }
        });

        vada_e.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                if(newValue==0)
                {vada_a.setVisibility(View.VISIBLE);
                    vada_e.setVisibility(View.GONE);
                    vada_e.setNumber("0");
                    temp11=temp11-20;
                    total.setText(Integer.toString(temp11));
                    d6=0;

                }

                else if(newValue>6)
                {
                    vada_e.setNumber("6");
                    Toast.makeText(getApplicationContext(),"You cannot add more than 6",Toast.LENGTH_SHORT).show();
                    total.setText(Integer.toString(temp11));


                }
                else
                {
                    if(oldValue>newValue){
                        vada_a.setVisibility(View.GONE);
                        vada_e.setVisibility(View.VISIBLE);
                        temp11=temp11-20;
                        total.setText(Integer.toString(temp11));
                        d6=newValue;

                    }
                    else if(newValue>oldValue)
                    {

                        temp11=temp11+20;
                        total.setText(Integer.toString(temp11));
                        d6=newValue;
                    }

                }
            }
        });
        vada_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vada_a.setVisibility(View.GONE);
                vada_e.setVisibility(View.VISIBLE);
                vada_e.setNumber("1");
                temp11=temp11+20;
                total.setText(Integer.toString(temp11));
                d6=1;
            }
        });

        bhel_e.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                if(newValue==0)
                {bhel_a.setVisibility(View.VISIBLE);
                    bhel_e.setVisibility(View.GONE);
                    bhel_e.setNumber("0");
                    temp11=temp11-30;
                    total.setText(Integer.toString(temp11));
                    d7=0;

                }

                else if(newValue>6)
                {
                    bhel_e.setNumber("6");
                    Toast.makeText(getApplicationContext(),"You cannot add more than 6",Toast.LENGTH_SHORT).show();
                    total.setText(Integer.toString(temp11));


                }
                else
                {
                    if(oldValue>newValue){
                        bhel_a.setVisibility(View.GONE);
                        bhel_e.setVisibility(View.VISIBLE);
                        temp11=temp11-30;
                        total.setText(Integer.toString(temp11));
                        d7=newValue;

                    }
                    else if(newValue>oldValue)
                    {

                        temp11=temp11+30;
                        total.setText(Integer.toString(temp11));
                        d7=newValue;
                    }

                }
            }
        });
        bhel_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bhel_a.setVisibility(View.GONE);
                bhel_e.setVisibility(View.VISIBLE);
                bhel_e.setNumber("1");
                temp11=temp11+30;
                total.setText(Integer.toString(temp11));
                d7=1;
            }
        });

        cheese_e.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                if(newValue==0)
                {cheese_a.setVisibility(View.VISIBLE);
                    cheese_e.setVisibility(View.GONE);
                    cheese_e.setNumber("0");
                    temp11=temp11-60;
                    total.setText(Integer.toString(temp11));
                    d8=0;

                }

                else if(newValue>6)
                {
                    cheese_e.setNumber("6");
                    Toast.makeText(getApplicationContext(),"You cannot add more than 6",Toast.LENGTH_SHORT).show();
                    total.setText(Integer.toString(temp11));


                }
                else
                {
                    if(oldValue>newValue){
                        cheese_a.setVisibility(View.GONE);
                        cheese_e.setVisibility(View.VISIBLE);
                        temp11=temp11-60;
                        total.setText(Integer.toString(temp11));
                        d8=newValue;

                    }
                    else if(newValue>oldValue)
                    {

                        temp11=temp11+60;
                        total.setText(Integer.toString(temp11));
                        d8=newValue;
                    }

                }
            }
        });
        cheese_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cheese_a.setVisibility(View.GONE);
                cheese_e.setVisibility(View.VISIBLE);
                cheese_e.setNumber("1");
                temp11=temp11+60;
                total.setText(Integer.toString(temp11));
                d8=1;
            }
        });

        manchurian_e.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                if(newValue==0)
                {manchurian_a.setVisibility(View.VISIBLE);
                    manchurian_e.setVisibility(View.GONE);
                    manchurian_e.setNumber("0");
                    temp11=temp11-80;
                    total.setText(Integer.toString(temp11));
                    d9=0;

                }

                else if(newValue>6)
                {
                    manchurian_e.setNumber("6");
                    Toast.makeText(getApplicationContext(),"You cannot add more than 6",Toast.LENGTH_SHORT).show();
                    total.setText(Integer.toString(temp11));


                }
                else
                {
                    if(oldValue>newValue){
                        manchurian_a.setVisibility(View.GONE);
                        manchurian_e.setVisibility(View.VISIBLE);
                        temp11=temp11-80;
                        total.setText(Integer.toString(temp11));
                        d9=newValue;

                    }
                    else if(newValue>oldValue)
                    {

                        temp11=temp11+80;
                        total.setText(Integer.toString(temp11));
                        d9=newValue;
                    }

                }
            }
        });
        manchurian_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manchurian_a.setVisibility(View.GONE);
                manchurian_e.setVisibility(View.VISIBLE);
                manchurian_e.setNumber("1");
                temp11=temp11+80;
                total.setText(Integer.toString(temp11));
                d9=1;
            }
        });

        frankie_e.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                if(newValue==0)
                {frankie_a.setVisibility(View.VISIBLE);
                    frankie_e.setVisibility(View.GONE);
                    frankie_e.setNumber("0");
                    temp11=temp11-40;
                    total.setText(Integer.toString(temp11));
                    d10=0;

                }

                else if(newValue>6)
                {
                    frankie_e.setNumber("6");
                    Toast.makeText(getApplicationContext(),"You cannot add more than 6",Toast.LENGTH_SHORT).show();
                    total.setText(Integer.toString(temp11));


                }
                else
                {
                    if(oldValue>newValue){
                        frankie_a.setVisibility(View.GONE);
                        frankie_e.setVisibility(View.VISIBLE);
                        temp11=temp11-40;
                        total.setText(Integer.toString(temp11));
                        d10=newValue;

                    }
                    else if(newValue>oldValue)
                    {

                        temp11=temp11+40;
                        total.setText(Integer.toString(temp11));
                        d10=newValue;
                    }

                }
            }
        });
        frankie_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frankie_a.setVisibility(View.GONE);
                frankie_e.setVisibility(View.VISIBLE);
                frankie_e.setNumber("1");
                temp11=temp11+40;
                total.setText(Integer.toString(temp11));
                d10=1;
            }
        });

        dosa_e.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                if(newValue==0)
                {dosa_a.setVisibility(View.VISIBLE);
                    dosa_e.setVisibility(View.GONE);
                    dosa_e.setNumber("0");
                    temp11=temp11-50;
                    total.setText(Integer.toString(temp11));
                    d11=0;

                }

                else if(newValue>6)
                {
                    dosa_e.setNumber("6");
                    Toast.makeText(getApplicationContext(),"You cannot add more than 6",Toast.LENGTH_SHORT).show();
                    total.setText(Integer.toString(temp11));


                }
                else
                {
                    if(oldValue>newValue){
                        dosa_a.setVisibility(View.GONE);
                        dosa_e.setVisibility(View.VISIBLE);
                        temp11=temp11-50;
                        total.setText(Integer.toString(temp11));
                        d11=newValue;

                    }
                    else if(newValue>oldValue)
                    {

                        temp11=temp11+50;
                        total.setText(Integer.toString(temp11));
                        d11=newValue;

                    }

                }
            }
        });
        dosa_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dosa_a.setVisibility(View.GONE);
                dosa_e.setVisibility(View.VISIBLE);
                dosa_e.setNumber("1");
                temp11=temp11+50;
                total.setText(Integer.toString(temp11));
                d11=1;

            }
        });

        pizza_e.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                if(newValue==0)
                {pizza_a.setVisibility(View.VISIBLE);
                    pizza_e.setVisibility(View.GONE);
                    pizza_e.setNumber("0");
                    temp11=temp11-90;
                    total.setText(Integer.toString(temp11));
                    d12=0;

                }

                else if(newValue>6)
                {
                    pizza_e.setNumber("6");
                    Toast.makeText(getApplicationContext(),"You cannot add more than 6",Toast.LENGTH_SHORT).show();
                    total.setText(Integer.toString(temp11));


                }
                else
                {
                    if(oldValue>newValue){
                        pizza_a.setVisibility(View.GONE);
                        pizza_e.setVisibility(View.VISIBLE);
                        temp11=temp11-90;
                        total.setText(Integer.toString(temp11));
                        d12=newValue;

                    }
                    else if(newValue>oldValue)
                    {

                        temp11=temp11+90;
                        total.setText(Integer.toString(temp11));
                        d12=newValue;
                    }

                }
            }
        });
        pizza_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pizza_a.setVisibility(View.GONE);
                pizza_e.setVisibility(View.VISIBLE);
                pizza_e.setNumber("1");
                temp11=temp11+90;
                total.setText(Integer.toString(temp11));
                d12=1;
            }
        });

        dessert_e.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                if(newValue==0)
                {dessert_a.setVisibility(View.VISIBLE);
                    dessert_e.setVisibility(View.GONE);
                    dessert_e.setNumber("0");
                    temp11=temp11-40;
                    total.setText(Integer.toString(temp11));
                    d13=0;

                }

                else if(newValue>6)
                {
                    dessert_e.setNumber("6");
                    Toast.makeText(getApplicationContext(),"You cannot add more than 6",Toast.LENGTH_SHORT).show();
                    total.setText(Integer.toString(temp11));


                }
                else
                {
                    if(oldValue>newValue){
                        dessert_a.setVisibility(View.GONE);
                        dessert_e.setVisibility(View.VISIBLE);
                        temp11=temp11-40;
                        total.setText(Integer.toString(temp11));
                        d13=newValue;

                    }
                    else if(newValue>oldValue)
                    {

                        temp11=temp11+40;
                        total.setText(Integer.toString(temp11));
                        d13=newValue;
                    }

                }
            }
        });
        dessert_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dessert_a.setVisibility(View.GONE);
                dessert_e.setVisibility(View.VISIBLE);
                dessert_e.setNumber("1");
                temp11=temp11+40;
                total.setText(Integer.toString(temp11));
                d13=1;
            }
        });

        total.setText("0");

    }


    private void savePdf()
    {
        //create object of Document class
        Document mDoc= new Document();
        //pdf filename
        String mFileName = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());
        // pdf file path
        String mFilePath = Environment.getExternalStorageDirectory() + "/" +mFileName + ".pdf";

        mpath=mFilePath;
        mname=mFileName;
        try
        {
            //create instance of PdfWriter class
            PdfWriter.getInstance(mDoc, new FileOutputStream(mFilePath));
            //open the document for writing
            mDoc.open();

            Image image=Image.getInstance("/storage/emulated/0/icon_circle.png");
            image.setAlignment(Element.ALIGN_CENTER);
            image.setScaleToFitHeight(true);
            mDoc.add(image);
            //get text String email,name,phn;
           String Email;
           String Name;
           String Phone;

           //add author of the document (optional)
            mDoc.addAuthor("The Tapree Cafe");



            //add paragraph to the document


            Paragraph p=new Paragraph();
            p.add("The Tapree Cafe");
            p.setAlignment(Element.ALIGN_CENTER);
            mDoc.add(p);


            mDoc.add(new Paragraph(
                    "\n36/42, 18th Square Street,                                                                    "+name+
                    "\nAndheri(West),                                                                                      "+email+
                    "\nMumbai-400053                                                                                    "+phn+
                    "\nPhone Number:022-23738274\nEmail Id: thetapreecafe@gmail.com"));


            mDoc.add(new Paragraph("Date & Time: "+new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(System.currentTimeMillis())));


            Paragraph p22= new Paragraph();
            p22.add("\n______________________________________________________________________________");
            mDoc.add(p22);

            Paragraph p1=new Paragraph();
            p1.add("GST Invoice");
            p1.setAlignment(Element.ALIGN_CENTER);
            mDoc.add(p1);

            Paragraph p2= new Paragraph();
            p2.add("______________________________________________________________________________");
            mDoc.add(p2);

            mDoc.add(new Paragraph("            Item                                                  Quantity                            Rate                      Amount                         \n______________________________________________________________________________"));

            Paragraph p3= new Paragraph();
            if(d1>0)
            {
                mDoc.add(new Paragraph("            Piccolo Latte                                         "+d1+"                                     100                          "+d1*100+"                         "));
            }
            mDoc.add(p3);

            Paragraph p4= new Paragraph();
            if(d2>0)
            {
                mDoc.add(new Paragraph("            Tapree Special Tea                              "+d2+"                                        50                          "+d2*50+"                         "));
            }
            mDoc.add(p4);

            Paragraph p5= new Paragraph();
            if(d3>0)
            {
                mDoc.add(new Paragraph("            Crispy Veg Burger                                "+d3+"                                      180                          "+d3*180+"                         "));
            }
            mDoc.add(p5);

            Paragraph p6= new Paragraph();
            if(d4>0)
            {
                mDoc.add(new Paragraph("            Classic Donuts                                     "+d4+"                                        80                           "+d4*80+"                         "));
            }
            mDoc.add(p6);

            Paragraph p7= new Paragraph();
            if(d5>0)
            {
                mDoc.add(new Paragraph("            Samosa                                                "+d5+"                                        25                           "+d5*25+"                         "));
            }
            mDoc.add(p7);

            Paragraph p8= new Paragraph();
            if(d6>0)
            {
                mDoc.add(new Paragraph("            Vada Pav                                              "+d6+"                                        20                           "+d6*20+"                         "));
            }
            mDoc.add(p8);

            Paragraph p9= new Paragraph();
            if(d7>0)
            {
                mDoc.add(new Paragraph("            Bhel Puri                                               "+d7+"                                        30                           "+d7*30+"                         "));
            }
            mDoc.add(p9);

            Paragraph p10= new Paragraph();
            if(d8>0)
            {
                mDoc.add(new Paragraph("            Cheese Balls                                        "+d8+"                                        60                           "+d8*60+"                         "));
            }
            mDoc.add(p10);

            Paragraph p11= new Paragraph();
            if(d9>0)
            {
                mDoc.add(new Paragraph("            Manchurian                                          "+d9+"                                         80                           "+d9*80+"                         "));
            }
            mDoc.add(p11);

            Paragraph p12= new Paragraph();
            if(d10>0)
            {
                mDoc.add(new Paragraph("            Frankie                                                 "+d10+"                                        40                            "+d10*40+"                         "));
            }
            mDoc.add(p12);

            Paragraph p13= new Paragraph();
            if(d11>0)
            {
                mDoc.add(new Paragraph("            Dosa                                                     "+d11+"                                        50                            "+d11*50+"                         "));
            }
            mDoc.add(p13);

            Paragraph p14= new Paragraph();
            if(d12>0)
            {
                mDoc.add(new Paragraph("            Pizza                                                     "+d12+"                                        90                           "+d12*90+"                         "));
            }
            mDoc.add(p14);

            Paragraph p15= new Paragraph();
            if(d13>0)
            {
                mDoc.add(new Paragraph("            Sizzling Brownie                                   "+d13+"                                        40                            "+d13*40+"                         "));
            }
            mDoc.add(p15);

            Paragraph p16= new Paragraph();
            p16.add("______________________________________________________________________________");
            mDoc.add(p16);

            Paragraph p17= new Paragraph();
            p17.add("            Total:                                                                                                                        "+" Rs."+temp11);
            mDoc.add(p17);



            mDoc.close();
            Toast.makeText(this,"Order Placed Successfully: \n"+mFileName +".pdf\nis saved to\n"+mFilePath,Toast.LENGTH_SHORT).show();







        }
        catch (Exception e)
        {
            //if any thing goes wrong causing exception, get and show exception message
            Toast.makeText(this, e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    // handle permission result

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode)
        {
            case STORAGE_CODE:
            {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    //permission was granted from popup, call savepdf method
                    savePdf();
                    sendMail();
                    Intent intent=new Intent(Canteen.this,Confirm.class);
                    startActivity(intent);

                }
                else
                {
                    //permission was denied from popup, show error message
                    Toast.makeText(this, "Permission Denied..",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void sendMail() {

        String mail=email;
        String subject= "Order ID: "+mname;
        String message="hi";

        //Send Mail
        JavaMailAPI javaMailAPI = new JavaMailAPI(this,mail,subject,message,mpath);
        javaMailAPI.execute();
    }
}