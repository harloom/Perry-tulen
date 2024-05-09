package com.ferry.tulen.presentation.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ferry.tulen.MainActivity;
import com.ferry.tulen.R;
import com.ferry.tulen.UtilsKey;
import com.ferry.tulen.presentation.auth.LoginActivity;
import com.ferry.tulen.presentation.home.HomeActivity;

import java.util.Objects;




public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Objects.requireNonNull(getSupportActionBar()).hide();


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        /// function
        findViewById(R.id.btn_user).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goAuth(1);
            }
        });

        findViewById(R.id.btn_workman).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goAuth(2);
            }
        });
    }
    /// id 1 = User
    /// id 2 = Tukang
    private  void goAuth(int id){
        Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
        intent.putExtra(UtilsKey.TYPE_AUTH, id); // replace 42 with your integer value
        startActivity(intent);
    }
}