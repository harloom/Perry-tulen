package com.ferry.tulen.presentation.business;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ferry.tulen.R;
import com.ferry.tulen.datasources.SharedPreferences.SharedPreferenceHelper;
import com.ferry.tulen.presentation.home.HomeActivity;
import com.ferry.tulen.presentation.home.WorkManHomeActivity;

import java.util.Objects;

public class SuccessOrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_success);

        Objects.requireNonNull(getSupportActionBar()).hide();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), new OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                Insets navigationBars = insets.getInsets(WindowInsetsCompat.Type.navigationBars());
                v.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(), navigationBars.bottom);
                return insets;
            }
        });
        SharedPreferenceHelper sharedPreferenceHelper = new SharedPreferenceHelper(this);
        findViewById(R.id.btnOK).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String typeLoginString = sharedPreferenceHelper.getString(SharedPreferenceHelper.KEY_TYPE_LOGIN,"-1");
                int typeLogin = Integer.parseInt(typeLoginString);

                if(typeLogin == 1){
                    Intent intent = new Intent(SuccessOrderActivity.this, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }else if(typeLogin == 2){
                    Intent intent = new Intent(SuccessOrderActivity.this, WorkManHomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }

            }
        });
    }
}