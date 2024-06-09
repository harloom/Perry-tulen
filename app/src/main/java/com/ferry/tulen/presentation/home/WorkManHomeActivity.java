package com.ferry.tulen.presentation.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ferry.tulen.MainActivity;
import com.ferry.tulen.R;
import com.ferry.tulen.datasources.SharedPreferences.SharedPreferenceHelper;
import com.ferry.tulen.datasources.firebase.AuthDataSource;
import com.ferry.tulen.datasources.listener.ResultListener;
import com.ferry.tulen.presentation.business.OrderWorkerCreateActivity;
import com.ferry.tulen.presentation.business.worker.WorkerListOderActivity;
import com.ferry.tulen.presentation.profile.PriceWorkerUpdateActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Objects;

public class WorkManHomeActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_work_man_home);

        Objects.requireNonNull(getSupportActionBar()).hide();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), new OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                Insets navigationBars = insets.getInsets(WindowInsetsCompat.Type.navigationBars());
                v.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(), navigationBars.bottom);
                return insets;
            }
        });

        AuthDataSource authDataSource = AuthDataSource.getInstance(FirebaseFirestore.getInstance());
        SharedPreferenceHelper sharedPreferenceHelper = new SharedPreferenceHelper(this);
        String idUser = sharedPreferenceHelper.getString(SharedPreferenceHelper.KEY_ID_USER,"");
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
//                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        authDataSource.setToken(idUser, token, new ResultListener<String>() {
                            @Override
                            public void onSuccess(String result) {
                                System.out.println("Token Set");
                            }

                            @Override
                            public void onError(Throwable error) {
                                System.out.println("Error Token Set");

                            }
                        });

                    }
                });

        findViewById(R.id.btnLihatPesanan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goListOrder();
            }
        });

        findViewById(R.id.btnLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authDataSource.logout(WorkManHomeActivity.this, new ResultListener<Boolean>() {
                    @Override
                    public void onSuccess(Boolean result) {
                        Intent intent = new Intent(WorkManHomeActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(Throwable error) {
                        Snackbar.make(view,error.getMessage(), Snackbar.LENGTH_LONG)
                                .setBackgroundTint(Color.RED)
                                .show();
                    }
                });
            }
        });

        findViewById(R.id.btnLihatPrice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WorkManHomeActivity.this, PriceWorkerUpdateActivity.class);
                startActivity(intent);
            }
        });


    }

    void goListOrder(){
        Intent intent = new Intent(WorkManHomeActivity.this, WorkerListOderActivity.class);
        startActivity(intent);
    }

}