package com.ferry.tulen.presentation.auth;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.ferry.tulen.R;
import com.ferry.tulen.datasources.SharedPreferences.SharedPreferenceHelper;
import com.ferry.tulen.datasources.firebase.AuthDataSource;
import com.ferry.tulen.datasources.firebase.UserDataSource;
import com.ferry.tulen.datasources.listener.ResultListener;
import com.ferry.tulen.presentation.home.HomeActivity;
import com.ferry.tulen.presentation.home.WorkManHomeActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class SetUserActivity extends AppCompatActivity {

    private TextInputEditText fullName;

    private TextInputEditText alamatLengkap;

    private TextInputEditText noHandphone ;

    private TextInputEditText job;


    int typeLogin;
    String idUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_set_user);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        SharedPreferenceHelper sharedPreferenceHelper = new SharedPreferenceHelper(this);
        String typeLoginString = sharedPreferenceHelper.getString(SharedPreferenceHelper.KEY_TYPE_LOGIN,"");
         typeLogin = Integer.parseInt(typeLoginString);

         idUser = sharedPreferenceHelper.getString(SharedPreferenceHelper.KEY_ID_USER,"");

        System.out.println("debug: typeLogin : " + typeLogin + " idUser : " + idUser);

        fullName = findViewById(R.id.textFullName);

        alamatLengkap = findViewById(R.id.txtAddress);

        noHandphone = findViewById(R.id.textPhoneNumber);

        job = findViewById(R.id.textJob);

        findViewById(R.id.btnSet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    setUser(view);

            }
        });
        

    }

    private void setUser(View view) {

        LottieAnimationView animationView =  findViewById(R.id.loadingAnimation);
        animationView.setVisibility(View.VISIBLE);

        UserDataSource dataSource = UserDataSource.getInstance(FirebaseFirestore.getInstance());
        String valueFullName = fullName.getText().toString();
        String valueAddress = alamatLengkap.getText().toString();
        String valueNoHandPhone = noHandphone.getText().toString();
        String valueJob = job.getText().toString();
        dataSource.setUser(idUser, valueFullName, valueAddress, valueNoHandPhone, valueJob, new ResultListener<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                animationView.setVisibility(View.GONE);
                if(result){
                    Snackbar.make(view, "Berhasil Menyimpan", Snackbar.LENGTH_LONG)
                            .setBackgroundTint(Color.GREEN)
                            .show();

                    /// to Home Screen

                    Intent intent = new Intent(SetUserActivity.this, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }



            }

            @Override
            public void onError(Throwable error) {
                Snackbar.make(view, Objects.requireNonNull(error.getMessage()), Snackbar.LENGTH_LONG)
                        .setBackgroundTint(Color.RED)
                        .show();
                animationView.setVisibility(View.GONE);
            }
        });

    }

}