package com.ferry.tulen.presentation.welcome;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
    private static final int REQUEST_POST_NOTIFICATIONS = 101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Objects.requireNonNull(getSupportActionBar()).hide();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_POST_NOTIFICATIONS);
            }
        }

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_POST_NOTIFICATIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "POST_NOTIFICATIONS permission granted");
            } else {
                Log.d(TAG, "POST_NOTIFICATIONS permission denied");
                Toast.makeText(this, "POST_NOTIFICATIONS permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}