package com.ferry.tulen.presentation.auth;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.airbnb.lottie.LottieAnimationView;
import com.ferry.tulen.R;
import com.ferry.tulen.datasources.SharedPreferences.SharedPreferenceHelper;
import com.ferry.tulen.datasources.firebase.CategoryDataSource;
import com.ferry.tulen.datasources.firebase.UserDataSource;
import com.ferry.tulen.datasources.listener.ResultListener;
import com.ferry.tulen.datasources.models.Category;
import com.ferry.tulen.presentation.home.HomeActivity;
import com.ferry.tulen.presentation.home.WorkManHomeActivity;
import com.ferry.tulen.presentation.home.rcv.CategoryRecyclerViewAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SetWorkmanActivity extends AppCompatActivity {

    private TextInputEditText fullName;

    private TextInputEditText alamatLengkap;

    private TextInputEditText noHandphone ;

//    private TextInputEditText job;

    private Spinner spinnerJob;

    private Category categorySelected;


    int typeLogin;
    String idUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_set_workman);
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
        //        job = findViewById(R.id.textJob);
        spinnerJob = findViewById(R.id.etJob);


        CategoryDataSource dataSource = CategoryDataSource.getInstance(FirebaseFirestore.getInstance());
        dataSource.getAllCategory(new ResultListener<List<Category>>() {
            @Override
            public void onSuccess(List<Category> result) {
                ArrayList<Category> cate = new ArrayList<>(result);
                ArrayAdapter<Category> adapter = new ArrayAdapter<>(SetWorkmanActivity.this, android.R.layout.simple_spinner_item, cate);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerJob.setAdapter(adapter);
            }
            @Override
            public void onError(Throwable e) {
                System.err.println("debug: Error category: " + e.getMessage());
            }
        });

        spinnerJob.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Category cs = (Category) adapterView.getItemAtPosition(i);
                categorySelected = cs;
                Toast.makeText(SetWorkmanActivity.this, "You selected " + cs.getName() + " with population " + cs.getId(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        findViewById(R.id.btnSet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    setWorkMan(view);

            }
        });
        

    }


    private void setWorkMan(View view) {

        LottieAnimationView animationView =  findViewById(R.id.loadingAnimation);
        animationView.setVisibility(View.VISIBLE);

        UserDataSource dataSource = UserDataSource.getInstance(FirebaseFirestore.getInstance());
        String valueFullName = fullName.getText().toString();
        String valueAddress = alamatLengkap.getText().toString();
        String valueNoHandPhone = noHandphone.getText().toString();
//        String valueJob = job.getText().toString();
        dataSource.setWorkMan(idUser, valueFullName, valueAddress, valueNoHandPhone, categorySelected.getName(), new ResultListener<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                animationView.setVisibility(View.GONE);
                if(result){
                    Snackbar.make(view, "Berhasil Menyimpan", Snackbar.LENGTH_LONG)
                            .setBackgroundTint(Color.GREEN)
                            .show();

                    /// to Home Screen

                    Intent intent = new Intent(SetWorkmanActivity.this, WorkManHomeActivity.class);
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