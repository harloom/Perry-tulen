package com.ferry.tulen.presentation.business.user;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
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
import com.ferry.tulen.datasources.firebase.OrderDataSource;
import com.ferry.tulen.datasources.firebase.RatingDataSource;
import com.ferry.tulen.datasources.listener.ResultListener;
import com.ferry.tulen.datasources.models.OrderWithUser;
import com.ferry.tulen.datasources.models.Rating;
import com.ferry.tulen.presentation.business.ConfirmOrderActivity;
import com.ferry.tulen.presentation.business.SuccessComplatedActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;

public class CreateRatingActivity extends AppCompatActivity {
    private TextView namaTukang;

    private TextView categoryTukang;
    private  TextView jenisPekerjaanTv;

    private  TextView tipePekerjaanTv;
    private  TextView jadwalPekerjaanTv;
    private EditText etUlasan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_rating);
        Objects.requireNonNull(getSupportActionBar()).hide();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), new OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                Insets navigationBars = insets.getInsets(WindowInsetsCompat.Type.navigationBars());
                v.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(), navigationBars.bottom);
                return insets;
            }
        });

        namaTukang = findViewById(R.id.namaUser);
        categoryTukang = findViewById(R.id.categoryTukang);
        jenisPekerjaanTv = findViewById(R.id.resjenispekerjaan);
        tipePekerjaanTv = findViewById(R.id.restipepengerjaan);
        jadwalPekerjaanTv = findViewById(R.id.jadwalpengerjaan);

        etUlasan = findViewById(R.id.inputUlasan);





        getDataOrder();
    }

    void getDataOrder(){
        Intent intent = getIntent();

        String idOrder = intent.getStringExtra("idOrder");

        OrderDataSource orderDataSource =  OrderDataSource.getInstance(FirebaseFirestore.getInstance());
        orderDataSource.getOrderDetailFinishWithUser(idOrder, new ResultListener<OrderWithUser>() {
            @Override
            public void onSuccess(OrderWithUser result) {
                System.out.println("debug : " + result.toString() );
                setData(result);
            };

            @Override
            public void onError(Throwable error) {

            }
        });


    }

    private void setData(OrderWithUser orderWithUser){
        namaTukang.setText(orderWithUser.getWorkMan().getWorkMan().getFullName());
        categoryTukang.setText("Tukang "+orderWithUser.getWorkMan().getWorkMan().getJob());
        jenisPekerjaanTv.setText(orderWithUser.getOrder().getCategoryJob());
        tipePekerjaanTv.setText(orderWithUser.getOrder().getTypeWork());
        jadwalPekerjaanTv.setText(String.format("Mulai %s - %s", new SimpleDateFormat("yyyy-MM-dd").format(orderWithUser.getOrder().getStartDate()), new SimpleDateFormat("yyyy-MM-dd").format(orderWithUser.getOrder().getEndDate())));



        RatingDataSource ratingDataSource = RatingDataSource.getInstance(FirebaseFirestore.getInstance());

        ratingDataSource.checkRatingHave(orderWithUser.getOrder().getId(), new ResultListener<List<Rating>>() {
            @Override
            public void onSuccess(List<Rating> result) {
                if(result.isEmpty()){
                    findViewById(R.id.ratingBar).setVisibility(View.VISIBLE);
                    findViewById(R.id.kirimRating).setVisibility(View.VISIBLE);
                    etUlasan.setVisibility(View.VISIBLE);
                }else{
                    findViewById(R.id.kirimRating).setVisibility(View.GONE);
//                    findViewById(R.id.ratingBar).setVisibility(View.GONE);
                    RatingBar ratingBar = findViewById(R.id.ratingBar);
                    ratingBar.setIsIndicator(true);
                    ratingBar.setRating((float) result.get(0).getRating());
                    etUlasan.setEnabled(false);
                    etUlasan.setText(result.get(0).getComment());

//                    etUlasan.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(Throwable error) {

            }
        });

        findViewById(R.id.kirimRating).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createRating(view,orderWithUser);
            }
        });

    }

    private  void createRating(View view, OrderWithUser orderWithUser){
        RatingBar ratingBar = findViewById(R.id.ratingBar);
        LottieAnimationView animationView =  findViewById(R.id.loadingAnimation);
        animationView.setVisibility(View.VISIBLE);


        RatingDataSource ratingDataSource = RatingDataSource.getInstance(FirebaseFirestore.getInstance());

        String ulasan = etUlasan.getText().toString();
        float rating = ratingBar.getRating();  // Get the rating of the RatingBar
        double ratingDouble = Float.valueOf(rating).doubleValue();

        ratingDataSource.setRatingWorkman(orderWithUser, ratingDouble, ulasan, new ResultListener<String>() {
            @Override
            public void onSuccess(String result) {
                animationView.setVisibility(View.GONE);
                goToSuccess();
            }

            @Override
            public void onError(Throwable error) {
                animationView.setVisibility(View.GONE);
                Snackbar.make(view, Objects.requireNonNull(error.getMessage()), Snackbar.LENGTH_LONG)
                        .setBackgroundTint(Color.RED)
                        .show();
            }
        });


    }

    private  void testRating(){
        RatingBar ratingBar = findViewById(R.id.ratingBar);
        String ulasan = etUlasan.getText().toString();
        float rating = ratingBar.getRating();  // Get the rating of the RatingBar
        double ratingDouble = Float.valueOf(rating).doubleValue();

        System.out.println("debug : "+ "Rating: " + ratingDouble + " Ulasan : " + ulasan);

        LottieAnimationView animationView =  findViewById(R.id.loadingAnimation);
        animationView.setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start the main activity after the delay
                animationView.setVisibility(View.GONE);
                goToSuccess();
            }
        }, 3000);
    }

    void goToSuccess(){
        Intent intent = new Intent(CreateRatingActivity.this, SuccessRatingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}

