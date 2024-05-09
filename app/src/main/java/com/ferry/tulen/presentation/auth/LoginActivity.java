package com.ferry.tulen.presentation.auth;

import static android.content.Intent.FLAG_ACTIVITY_NO_HISTORY;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import com.ferry.tulen.datasources.models.User;
import com.ferry.tulen.datasources.models.WorkMan;
import com.ferry.tulen.presentation.home.HomeActivity;
import com.ferry.tulen.presentation.home.WorkManHomeActivity;
import com.ferry.tulen.presentation.welcome.WelcomeActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private View view;

    private TextInputEditText emailView;
    private TextInputEditText passwordView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        Objects.requireNonNull(getSupportActionBar()).hide();

        view = findViewById(R.id.view_login);


        emailView = findViewById(R.id.etEmail);
        passwordView = findViewById(R.id.etPassword);



        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        Intent intent = getIntent();
        int typeLoginId = intent.getIntExtra(UtilsKey.TYPE_AUTH, -1);
        findViewById(R.id.txt_daftar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToRegister(typeLoginId);
            }
        });

        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login(typeLoginId);
            }
        });
    }

    private void login(int id){
        AuthDataSource dataSource = AuthDataSource.getInstance(FirebaseFirestore.getInstance());


       LottieAnimationView animationView =  findViewById(R.id.loadingAnimation);
        animationView.setVisibility(View.VISIBLE);
        dataSource.login(id, emailView.getText().toString(), passwordView.getText().toString(), new ResultListener() {
            @Override
            public void onSuccess(Object result) {
                if(result instanceof WorkMan){
                    System.out.println("debug: Success Login WorkMan " + result.toString());
                    saveSessionWorkman((WorkMan) result);
                }else if(result instanceof User){
                    System.out.println("debug: Success Login User " + result.toString());
                    saveSessionUser((User) result);

                }else{
                    //// not allow
                }

                animationView.setVisibility(View.GONE);
            }

            @Override
            public void onError(Throwable e) {
//                System.err.println("debug: Error Login: " + e.getMessage());
                // Improved code
                animationView.setVisibility(View.GONE);
                Snackbar.make(view,e.getMessage(), Snackbar.LENGTH_LONG)
                        .setBackgroundTint(Color.RED)
                        .show();
            }
        });
    }

    private void saveSessionUser(User user){
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void saveSessionWorkman(WorkMan workMan){
        Intent intent = new Intent(LoginActivity.this, WorkManHomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


    private void goToRegister(int id){
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        intent.putExtra(UtilsKey.TYPE_AUTH, id); // replace 42 with your integer value
        startActivity(intent);
    }
}