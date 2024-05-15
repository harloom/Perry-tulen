package com.ferry.tulen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

import com.ferry.tulen.datasources.SharedPreferences.SharedPreferenceHelper;
import com.ferry.tulen.datasources.firebase.InitializeDbTest;
import com.ferry.tulen.presentation.auth.SetUserActivity;
import com.ferry.tulen.presentation.home.HomeActivity;
import com.ferry.tulen.presentation.home.WorkManHomeActivity;
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
        SharedPreferenceHelper sharedPreferenceHelper = new SharedPreferenceHelper(this);
        String typeLoginString = sharedPreferenceHelper.getString(SharedPreferenceHelper.KEY_TYPE_LOGIN,"-1");
        int typeLogin = Integer.parseInt(typeLoginString);
        String idUser = sharedPreferenceHelper.getString(SharedPreferenceHelper.KEY_ID_USER,"");

        if(!idUser.equals("")){
            if(typeLogin == 1 ){
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                return;
            }else if( typeLogin ==2 ){
                Intent intent = new Intent(MainActivity.this, WorkManHomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                return;
            }
        }


        Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
        startActivity(intent);
    }

}