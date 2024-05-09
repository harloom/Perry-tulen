package com.ferry.tulen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

import com.ferry.tulen.datasources.firebase.InitializeDbTest;
import com.ferry.tulen.presentation.home.HomeActivity;
import com.ferry.tulen.presentation.welcome.WelcomeActivity;
import com.google.firebase.FirebaseApp;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static final int LOADING_SCREEN_DELAY = 3000; // Delay in milliseconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();

        /// initialize Firebase
        FirebaseApp.initializeApp(this);

        // Start the loading animation
        ProgressBar progressBar = findViewById(R.id.idPBLoading);
        progressBar.setVisibility(View.VISIBLE);

        new InitializeDbTest().initialObject();

        // Delay the loading screen for a few seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start the main activity after the delay
                goWelcome();
                // Close the loading screen
                finish();
            }
        }, LOADING_SCREEN_DELAY);
    }


    private void goHome(){
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(intent);
    }


    private  void goWelcome(){
        Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
        startActivity(intent);
    }

}