package com.example.nmims_canteen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class Game extends AppCompatActivity {
    int temp1,temp;
    FirebaseUser user;
    String wallet_db;
    FirebaseAuth auth;
    DatabaseReference reff;
    FirebaseDatabase database;
    TextView scoreee;

    // Frame
    private FrameLayout gameFrame;
    private int frameHeight, frameWidth, initialFrameWidth;
    private LinearLayout startLayout;

    // Image
    private ImageView box, black, orange, pink;
    private Drawable imageBoxRight, imageBoxLeft;

    // Size
    private int boxSize;

    // Position
    private float boxX, boxY;
    private float blackX, blackY;
    private float orangeX, orangeY;
    private float pinkX, pinkY;

    // Score
    private TextView scoreLabel, highScoreLabel;
    private int score, highScore, timeCount;
    private SharedPreferences settings;

    // Class
    private Timer timer;
    private Handler handler = new Handler();
    private SoundPlayer soundPlayer;

    // Status
    private boolean start_flg = false;
    private boolean action_flg = false;
    private boolean pink_flg = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title   
        getSupportActionBar().hide();// hide the title bar  
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);//enable full screen
        setContentView(R.layout.activity_game);
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        reff = FirebaseDatabase.getInstance().getReference().child("Member").child(user.getUid());
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                wallet_db=dataSnapshot.child("wallet").getValue().toString();
                temp=Integer.parseInt(wallet_db);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        soundPlayer = new SoundPlayer(this);

        gameFrame = findViewById(R.id.gameFrame);
        startLayout = findViewById(R.id.startLayout);
        box = findViewById(R.id.box);
        black = findViewById(R.id.black);
        orange = findViewById(R.id.orange);
        pink = findViewById(R.id.pink);
        scoreLabel = findViewById(R.id.scoreLabel);
        highScoreLabel = findViewById(R.id.highScoreLabel);

        imageBoxLeft = getResources().getDrawable(R.drawable.box_left);
        imageBoxRight = getResources().getDrawable(R.drawable.box_right);

        scoreee=findViewById(R.id.scoreee);



        // High Score
        settings = getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);
        highScore = settings.getInt("HIGH_SCORE", 0);
        highScoreLabel.setText("High Score : " + highScore);
    }

    public void changePos() {

        // Add timeCount
        timeCount += 20;

        // Orange
        orangeY += 12;

        float orangeCenterX = orangeX + orange.getWidth() / 2;
        float orangeCenterY = orangeY + orange.getHeight() / 2;

        if (hitCheck(orangeCenterX, orangeCenterY)) {
            orangeY = frameHeight + 100;
            score += 10;
            soundPlayer.playHitOrangeSound();
        }

        if (orangeY > frameHeight) {
            orangeY = -100;
            orangeX = (float) Math.floor(Math.random() * (frameWidth - orange.getWidth()));
        }
        orange.setX(orangeX);
        orange.setY(orangeY);

        // Pink
        if (!pink_flg && timeCount % 10000 == 0) {
            pink_flg = true;
            pinkY = -20;
            pinkX = (float) Math.floor(Math.random() * (frameWidth - pink.getWidth()));
        }

        if (pink_flg) {
            pinkY += 20;

            float pinkCenterX = pinkX + pink.getWidth() / 2;
            float pinkCenterY = pinkY + pink.getWidth() / 2;

            if (hitCheck(pinkCenterX, pinkCenterY)) {
                pinkY = frameHeight + 30;
                score += 30;
                // Change FrameWidth
                if (initialFrameWidth > frameWidth * 110 / 100) {
                    frameWidth = frameWidth * 110 / 100;
                    changeFrameWidth(frameWidth);
                }
                soundPlayer.playHitPinkSound();
            }

            if (pinkY > frameHeight) pink_flg = false;
            pink.setX(pinkX);
            pink.setY(pinkY);
        }

        // Black
        blackY += 18;

        float blackCenterX = blackX + black.getWidth() / 2;
        float blackCenterY = blackY + black.getHeight() / 2;

        if (hitCheck(blackCenterX, blackCenterY)) {
            blackY = frameHeight + 100;

            // Change FrameWidth
            frameWidth = frameWidth * 80 / 100;
            changeFrameWidth(frameWidth);
            soundPlayer.playHitBlackSound();
            if (frameWidth <= boxSize) {
                gameOver();
            }

        }

        if (blackY > frameHeight) {
            blackY = -100;
            blackX = (float) Math.floor(Math.random() * (frameWidth - black.getWidth()));
        }

        black.setX(blackX);
        black.setY(blackY);

        // Move Box
        if (action_flg) {
            // Touching
            boxX += 14;
            box.setImageDrawable(imageBoxRight);
        } else {
            // Releasing
            boxX -= 14;
            box.setImageDrawable(imageBoxLeft);
        }

        // Check box position.
        if (boxX < 0) {
            boxX = 0;
            box.setImageDrawable(imageBoxRight);
        }
        if (frameWidth - boxSize < boxX) {
            boxX = frameWidth - boxSize;
            box.setImageDrawable(imageBoxLeft);
        }

        box.setX(boxX);

        scoreLabel.setText("Score : " + score);
        temp1=score/50;
        //reff.child("wallet").setValue(Integer.toString(temp+temp1));
    }

    public boolean hitCheck(float x, float y) {
        if (boxX <= x && x <= boxX + boxSize &&
                boxY <= y && y <= frameHeight) {
            return true;
        }
        return false;
    }

    public void changeFrameWidth(int frameWidth) {
        ViewGroup.LayoutParams params = gameFrame.getLayoutParams();
        params.width = frameWidth;
        gameFrame.setLayoutParams(params);
    }

    public void gameOver() {

        reff.child("wallet").setValue(Integer.toString(score/50+temp));
        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
        Date todayDate = new Date();
        String thisDate = currentDate.format(todayDate);
        reff.child("wallet_txn").push().setValue(thisDate+"@+"+Integer.toString(score/50));
        // Stop timer.
        timer.cancel();
        timer = null;
        start_flg = false;

        // Before showing startLayout, sleep 1 second.
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        changeFrameWidth(initialFrameWidth);

        startLayout.setVisibility(View.VISIBLE);
        box.setVisibility(View.INVISIBLE);
        black.setVisibility(View.INVISIBLE);
        orange.setVisibility(View.INVISIBLE);
        pink.setVisibility(View.INVISIBLE);

        // Update High Score
        if (score > highScore) {
            highScore = score;
            highScoreLabel.setText("High Score : " + highScore);

            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("HIGH_SCORE", highScore);
            editor.commit();

        }

        Toast.makeText(this, "Money Earned: ₹"+(score/50), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (start_flg) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                action_flg = true;

            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                action_flg = false;

            }
        }
        return true;
    }

    public void startGame(View view) {
        start_flg = true;
        startLayout.setVisibility(View.INVISIBLE);

        if (frameHeight == 0) {
            frameHeight = gameFrame.getHeight();
            frameWidth = gameFrame.getWidth();
            initialFrameWidth = frameWidth;

            boxSize = box.getHeight();
            boxX = box.getX();
            boxY = box.getY();
        }

        frameWidth = initialFrameWidth;

        box.setX(0.0f);
        black.setY(3000.0f);
        orange.setY(3000.0f);
        pink.setY(3000.0f);

        blackY = black.getY();
        orangeY = orange.getY();
        pinkY = pink.getY();

        box.setVisibility(View.VISIBLE);
        black.setVisibility(View.VISIBLE);
        orange.setVisibility(View.VISIBLE);
        pink.setVisibility(View.VISIBLE);

        timeCount = 0;
        score = 0;
        scoreLabel.setText("Score : 0");


        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (start_flg) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            changePos();
                        }
                    });
                }
            }
        }, 0, 20);
    }

    public void quitGame(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAndRemoveTask();
        } else {
            finish();
        }
    }

}
