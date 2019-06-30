package com.deneme.tuna.yuztanima;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class SplashScreenActivity extends AppCompatActivity {

    private ImageView splashGif;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        splashGif = findViewById(R.id.splash);
        AnimationDrawable animationDrawable = (AnimationDrawable) splashGif.getDrawable();
        animationDrawable.start();


        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(mainIntent);
                finish();
            }
        };

        handler = new Handler();

        handler.postDelayed(runnable,3000);






    }


}
