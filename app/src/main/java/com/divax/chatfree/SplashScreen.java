package com.divax.chatfree;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {
    public static int SPLASH_SCREEN = 3000;
    Animation topanim,botanim;
    ImageView logo;
    TextView name,high;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        topanim = AnimationUtils.loadAnimation(SplashScreen.this,R.anim.top_anim);
        botanim = AnimationUtils.loadAnimation(SplashScreen.this,R.anim.bot_anim);
        logo = findViewById(R.id.Logo);
        name = findViewById(R.id.Nameapp);
        high = findViewById(R.id.hignapp);

        logo.setAnimation(topanim);
        name.setAnimation(botanim);
        high.setAnimation(botanim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this,Login.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_SCREEN);
    }
}