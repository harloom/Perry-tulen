package com.ferry.tulen.presentation.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ferry.tulen.MainActivity;
import com.ferry.tulen.R;
import com.ferry.tulen.datasources.SharedPreferences.SharedPreferenceHelper;
import com.ferry.tulen.datasources.firebase.AuthDataSource;
import com.ferry.tulen.datasources.firebase.CategoryDataSource;
import com.ferry.tulen.datasources.firebase.UserDataSource;
import com.ferry.tulen.datasources.firebase.WorkManDataSource;
import com.ferry.tulen.datasources.listener.ResultListener;
import com.ferry.tulen.datasources.models.Category;
import com.ferry.tulen.datasources.models.UserWithIdDocument;
import com.ferry.tulen.datasources.models.WorkMan;
import com.ferry.tulen.presentation.business.OrderWorkerCreateActivity;
import com.ferry.tulen.presentation.home.rcv.CategoryRecyclerViewAdapter;
import com.ferry.tulen.presentation.home.rcv.WorkManRecyclerViewAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.List;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView cRecyclerView;
    private CategoryRecyclerViewAdapter cAdapter;


    private RecyclerView workManTopRcView;
    private WorkManRecyclerViewAdapter workManRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Objects.requireNonNull(getSupportActionBar()).hide();

        CategoryDataSource dataSource = CategoryDataSource.getInstance(FirebaseFirestore.getInstance());

        AuthDataSource authDataSource = AuthDataSource.getInstance(FirebaseFirestore.getInstance());
        UserDataSource userDataSource = UserDataSource.getInstance(FirebaseFirestore.getInstance());
        WorkManDataSource workManDataSource = WorkManDataSource.getInstance(FirebaseFirestore.getInstance());

        /// get User
        SharedPreferenceHelper sharedPreferenceHelper = new SharedPreferenceHelper(this);
        String typeLoginString = sharedPreferenceHelper.getString(SharedPreferenceHelper.KEY_TYPE_LOGIN,"");
        int typeLogin = Integer.parseInt(typeLoginString);
        String idUser = sharedPreferenceHelper.getString(SharedPreferenceHelper.KEY_ID_USER,"");


        /// set token
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



        findViewById(R.id.btnLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authDataSource.logout(HomeActivity.this, new ResultListener<Boolean>() {
                    @Override
                    public void onSuccess(Boolean result) {
                        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
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



        userDataSource.getDataUser(idUser, new ResultListener<UserWithIdDocument>() {
            @Override
            public void onSuccess(UserWithIdDocument result) {
                TextView textView = findViewById(R.id.titleHome);
                System.out.println(result.getUser().toString());
                textView.setText("Selamat Datang \n " + result.getUser().getFullName());

            }

            @Override
            public void onError(Throwable error) {

            }
        });



        cRecyclerView = findViewById(R.id.kategori_menu_rc);


        dataSource.getAllCategory(new ResultListener<List<Category>>() {
            @Override
            public void onSuccess(List<Category> result) {

                cAdapter = new CategoryRecyclerViewAdapter(result, new CategoryRecyclerViewAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Intent intent = new Intent(HomeActivity.this, OrderWorkerCreateActivity.class);
                        intent.putExtra("pekerjaan",result.get(position).getName());
                        startActivity(intent);


                    }
                });
                cRecyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this,LinearLayoutManager.HORIZONTAL, false));
                cRecyclerView.setAdapter(cAdapter);
            }
            @Override
            public void onError(Throwable e) {
                System.err.println("debug: Error category: " + e.getMessage());
            }
        });


        workManTopRcView = findViewById(R.id.workman_top_rc);

        workManDataSource.getListTopWorkMan(new ResultListener<List<WorkMan>>() {
            @Override
            public void onSuccess(List<WorkMan> result) {
                workManRecyclerViewAdapter = new WorkManRecyclerViewAdapter(result, new WorkManRecyclerViewAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Toast.makeText(HomeActivity.this, "Item " + position + " clicked", Toast.LENGTH_SHORT).show();

                    }
                });
                workManTopRcView.setLayoutManager(new LinearLayoutManager(HomeActivity.this,LinearLayoutManager.VERTICAL, false));
                workManTopRcView.setAdapter(workManRecyclerViewAdapter);
            }

            @Override
            public void onError(Throwable error) {

            }
        });


    }


}