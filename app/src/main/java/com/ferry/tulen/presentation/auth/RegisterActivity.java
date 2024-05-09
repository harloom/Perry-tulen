package com.ferry.tulen.presentation.auth;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.ferry.tulen.R;
import com.ferry.tulen.UtilsKey;
import com.ferry.tulen.datasources.firebase.AuthDataSource;
import com.ferry.tulen.datasources.listener.ResultListener;
import com.ferry.tulen.presentation.home.HomeActivity;
import com.ferry.tulen.presentation.welcome.WelcomeActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    private TextInputEditText emailView;
    private TextInputEditText passwordView;

    private TextInputEditText passwordView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        Objects.requireNonNull(getSupportActionBar()).hide();

        emailView = findViewById(R.id.txt_email);

        passwordView = findViewById(R.id.txt_password);

        passwordView2 = findViewById(R.id.txt_password2);

        Intent intent = getIntent();
        int typeLoginId = intent.getIntExtra(UtilsKey.TYPE_AUTH, -1);

        findViewById(R.id.txt_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        findViewById(R.id.btn_daftar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register(view,typeLoginId);
            }
        });
    }

    private  void register (View view, int typeLogin){
        AuthDataSource dataSource = AuthDataSource.getInstance(FirebaseFirestore.getInstance());


        LottieAnimationView animationView =  findViewById(R.id.loadingAnimation);
        animationView.setVisibility(View.VISIBLE);

        if(!passwordView.getText().toString().equals(passwordView2.getText().toString())){

            Snackbar.make(view,"Konfirmasi password tidak sama!", Snackbar.LENGTH_LONG)
                    .setBackgroundTint(Color.RED)
                    .show();

            animationView.setVisibility(View.GONE);
            return;
        }

        if(typeLogin == 1){
            dataSource.registerUser(emailView.getText().toString(), passwordView.getText().toString(), new ResultListener<String>() {
                @Override
                public void onSuccess(String result) {
                    animationView.setVisibility(View.GONE);
                    successMainAuth(view,result);
                }

                @Override
                public void onError(Throwable error) {
                    animationView.setVisibility(View.GONE);
                    Snackbar.make(view,error.getMessage(), Snackbar.LENGTH_LONG)
                            .setBackgroundTint(Color.RED)
                            .show();

                }
            });
        }else if( typeLogin  == 2) {
            dataSource.registerWorkMan(emailView.getText().toString(), passwordView.getText().toString(), new ResultListener<String>() {
                @Override
                public void onSuccess(String result) {
                    animationView.setVisibility(View.GONE);
                    successMainAuth(view,result);
                }

                @Override
                public void onError(Throwable error) {
                    animationView.setVisibility(View.GONE);
                    Snackbar.make(view,error.getMessage(), Snackbar.LENGTH_LONG)
                            .setBackgroundTint(Color.RED)
                            .show();

                }
            });
        }





    }

    private void successMainAuth(View view, String message){
        Snackbar.make(view,message, Snackbar.LENGTH_LONG)
                .setBackgroundTint(Color.GREEN)
                .show();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                Intent intent = new Intent(RegisterActivity.this, WelcomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }, 3000);
    }
}