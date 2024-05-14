package com.example.nmims_canteen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Slide;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;




public class FirstScreen extends AppCompatActivity {

    private ImageButton Game;
    private
    ViewFlipper v_flipper;

Spinner sp1;
String[] number={"","My Profile","LogOut"};
    ArrayAdapter<String> spin;


    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private ImageButton Wallet;
    private ImageButton Canteen;
    FirebaseUser user;
    FirebaseAuth auth;
    TextView name_Db;
    DatabaseReference reff;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title   
        getSupportActionBar().hide();// hide the title bar  
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//enable full screen
        setContentView(R.layout.activity_firstscreen);
        name_Db=findViewById(R.id.name_db);
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        reff = FirebaseDatabase.getInstance().getReference().child("Member").child(user.getUid());
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name_Db.setText(" "+dataSnapshot.child("Name").getValue().toString()+"");
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

        int images[] = {R.drawable.i1,R.drawable.i2,R.drawable.i3,R.drawable.i4,R.drawable.i5};
        v_flipper=findViewById(R.id.v_flipper);

        for(int i=0; i<images.length;i++)
            {
            flipperImages(images[i]);
            }

        for(int image: images)
            {
            flipperImages(image);
            }


        sp1=findViewById(R.id.sp1);

        setupFirebaseListener();

        Game = findViewById(R.id.Game);
        Game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Game();

            }
        });
        Wallet = findViewById(R.id.Wallet);
        Wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Wallet();
            }
        });
        Canteen = findViewById(R.id.Canteen);
        Canteen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Canteen();
            }
        });

        spin=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,number);


        sp1.setAdapter(spin);
        ArrayAdapter<CharSequence> spin= ArrayAdapter.createFromResource(this, R.array.numbers,android.R.layout.simple_spinner_item);
        spin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp1.setAdapter(spin);

        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        break;
                    case 1:
                        Intent intent=new Intent(FirstScreen.this,Profile.class);
                        startActivity(intent);
                        break;
                    case 2:
                        FirebaseAuth.getInstance().signOut();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });





    }

    public void flipperImages(int image)
    {
        ImageView imageView=new ImageView(this);
        imageView.setBackgroundResource(image);

        v_flipper.addView(imageView);
        v_flipper.setFlipInterval(4000);
        v_flipper.setAutoStart(true);

        v_flipper.setOutAnimation(this,android.R.anim.slide_out_right);


    }


    public void Wallet(){
        Intent intent=new Intent(this,Wallet.class);
        startActivity(intent);
    }
    public void Game(){
        Intent intent=new Intent(this,Game.class);
        startActivity(intent);
    }
    public void Canteen(){
        Intent intent=new Intent(this,Canteen.class);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        Intent intent=new Intent(FirstScreen.this,FirstScreen.class);
        startActivity(intent);
        finish();
    }
    private void setupFirebaseListener(){
        mAuthStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=firebaseAuth.getCurrentUser();
                if(user!=null)
                {

                }
                else {
                    Intent intent=new Intent(FirstScreen.this,Login.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        };
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onStop()  {
        super.onStop();
        if(mAuthStateListener!=null)
        {
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthStateListener);
        }
    }
}
