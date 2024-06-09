package com.ferry.tulen.presentation.profile;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.ferry.tulen.R;
import com.ferry.tulen.datasources.SharedPreferences.SharedPreferenceHelper;
import com.ferry.tulen.datasources.firebase.UserDataSource;
import com.ferry.tulen.datasources.listener.ResultListener;
import com.ferry.tulen.datasources.models.WorkMainWithIdDocument;
import com.ferry.tulen.presentation.auth.SetUserActivity;
import com.ferry.tulen.presentation.home.HomeActivity;
import com.ferry.tulen.presentation.home.WorkManHomeActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class PriceWorkerUpdateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_price_worker_update);

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
        String typeLoginString = sharedPreferenceHelper.getString(SharedPreferenceHelper.KEY_TYPE_LOGIN,"");
        Integer typeLogin = Integer.parseInt(typeLoginString);

        String idUser = sharedPreferenceHelper.getString(SharedPreferenceHelper.KEY_ID_USER,"");


        UserDataSource dataSource = UserDataSource.getInstance(FirebaseFirestore.getInstance());
            dataSource.getWorkMan(idUser, new ResultListener<WorkMainWithIdDocument>() {
                @Override
                public void onSuccess(WorkMainWithIdDocument result) {

                    TextView namaTukang = findViewById(R.id.namatukang);
                    TextView txAddress = findViewById(R.id.txtAddress);
                    TextView txtMinPrice = findViewById(R.id.textMin);
                    TextView txtMaxPrice = findViewById(R.id.textMax);

                    txAddress.setText(result.getWorkMan().getAddress());

                    txtMinPrice.setText(result.getWorkMan().getPriceMin());

                    txtMaxPrice.setText(result.getWorkMan().getPriceMax());

                    namaTukang.setText(result.getWorkMan().getFullName());

                    RatingBar ratingBar =  findViewById(R.id.ratingBar);
                    ratingBar.setIsIndicator(true);
                    ratingBar.setRating(result.getWorkMan().getRating().floatValue());

                    findViewById(R.id.btnSet).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            setUser(idUser,view);

                        }
                    });
                }

                @Override
                public void onError(Throwable error) {

                }
            });


    }

    private void setUser(String idUser, View view){
        LottieAnimationView animationView =  findViewById(R.id.loadingAnimation);
        animationView.setVisibility(View.VISIBLE);

        UserDataSource dataSource = UserDataSource.getInstance(FirebaseFirestore.getInstance());
        TextView txAddress = findViewById(R.id.txtAddress);
        TextView txtMinPrice = findViewById(R.id.textMin);
        TextView txtMaxPrice = findViewById(R.id.textMax);

        dataSource.setWorkerAddressAndPrice(idUser, txAddress.getText().toString(), txtMinPrice.getText().toString(), txtMaxPrice.getText().toString(), new ResultListener<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                animationView.setVisibility(View.GONE);
                if(result){
                    Snackbar.make(view, "Berhasil Menyimpan", Snackbar.LENGTH_LONG)
                            .setBackgroundTint(Color.GREEN)
                            .show();

                    /// to Home Screen

                    Intent intent = new Intent(PriceWorkerUpdateActivity.this, WorkManHomeActivity.class);
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