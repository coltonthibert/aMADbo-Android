package com.example.amadbo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * Activity to display the splash screen
 * This activity is the first activity to be displayed when the app is launched
 * It displays the app logo for a few seconds before starting the main activity
 */
public class SplashScreenActivity extends AppCompatActivity {

    // Counter for splash screen timeout in milliseconds
    int SPLASH_SCREEN_TIME_OUT = 2500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Get Components
        ImageView background = findViewById(R.id.splash_background_scroll);
        ImageView logos = findViewById(R.id.splash_amadboLogo);

        final ImageView blueAmiibo = findViewById(R.id.splash_amiibo_blue);
        final ImageView redAmiibo = findViewById(R.id.splash_amiibo_red);
        final ImageView greenAmiibo = findViewById(R.id.splash_amiibo_green);

        // Hide Amiibo Icons initially
        blueAmiibo.setVisibility(View.INVISIBLE);
        redAmiibo.setVisibility(View.INVISIBLE);
        greenAmiibo.setVisibility(View.INVISIBLE);

        // Set up Animations for Splash Screen
        Animation scrollAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splash_scroll);
        Animation logoIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splash_logo);

        final Animation amiiboPopIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splash_amiibo_popin);
        final Animation amiiboPopIn2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splash_amiibo_popin2);
        final Animation amiiboPopIn3 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splash_amiibo_popin3);

        // Start the scroll animation for background and logo animation
        background.startAnimation(scrollAnim);
        logos.startAnimation(logoIn);


        // Have a delay between each Amiibo Showing up
        new Handler().postDelayed(new Runnable() { // SHOW BLUE AMIIBO
            @Override
            public void run() {
                blueAmiibo.setVisibility(View.VISIBLE);
                blueAmiibo.startAnimation(amiiboPopIn);
            }
        }, 500);

        new Handler().postDelayed(new Runnable() { // SHOW RED AMIIBO
            @Override
            public void run() {
                redAmiibo.setVisibility(View.VISIBLE);
                redAmiibo.startAnimation(amiiboPopIn2);
            }
        }, 700);

        new Handler().postDelayed(new Runnable() { // SHOW GREEN AMIIBO
            @Override
            public void run() {
                greenAmiibo.setVisibility(View.VISIBLE);
                greenAmiibo.startAnimation(amiiboPopIn3);
            }
        }, 900);

        // Handler to start the main activity after the splash screen timeout
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_SCREEN_TIME_OUT);
    }

}