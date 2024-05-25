package com.ferry.tulen.presentation.business;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.ferry.tulen.R;
import com.ferry.tulen.UtilsKey;
import com.ferry.tulen.datasources.firebase.OrderDataSource;
import com.ferry.tulen.datasources.listener.ResultListener;
import com.ferry.tulen.datasources.models.OrderWithUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Objects;

public class ConfirmOrderActivity extends AppCompatActivity {

    private TextView namaUserTv;
    private  TextView jenisPekerjaanTv;

    private  TextView tipePekerjaanTv;
    private  TextView jadwalPekerjaanTv;

    private  TextView descPesanTv;

    private  TextView resalamat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);

        Objects.requireNonNull(getSupportActionBar()).hide();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), new OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                Insets navigationBars = insets.getInsets(WindowInsetsCompat.Type.navigationBars());
                v.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(), navigationBars.bottom);
                return insets;
            }
        });
        /// varibale

        namaUserTv = findViewById(R.id.namaUser);
        jenisPekerjaanTv = findViewById(R.id.resjenispekerjaan);
        tipePekerjaanTv = findViewById(R.id.restipepengerjaan);
        jadwalPekerjaanTv = findViewById(R.id.jadwalpengerjaan);
        descPesanTv = findViewById(R.id.descPesan);
        resalamat = findViewById(R.id.resalamat);


        getDataOrder();



    }

    void getDataOrder(){
        Intent intent = getIntent();

        String idOrder = intent.getStringExtra("idOrder");

        OrderDataSource orderDataSource =  OrderDataSource.getInstance(FirebaseFirestore.getInstance());
        orderDataSource.getOrderDetailWithUser(idOrder, new ResultListener<OrderWithUser>() {
            @Override
            public void onSuccess(OrderWithUser result) {
                System.out.println("debug : " + result.toString() );
                setData(result);
            };

            @Override
            public void onError(Throwable error) {

            }
        });

        findViewById(R.id.selesaipesanan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                successJob(idOrder);
            }
        });


        /// rejectOrder
        findViewById(R.id.tolakOrder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    rejectOrder(idOrder);
            }
        });

        findViewById(R.id.confirmOrder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmOrder(idOrder);
            }
        });
    }


    private void setData(OrderWithUser orderWithUser){
        namaUserTv.setText(orderWithUser.getUser().getUser().getFullName());
        jenisPekerjaanTv.setText(orderWithUser.getOrder().getCategoryJob());
        tipePekerjaanTv.setText(orderWithUser.getOrder().getTypeWork());
        jadwalPekerjaanTv.setText(String.format("Mulai %s - %s", new SimpleDateFormat("yyyy-MM-dd").format(orderWithUser.getOrder().getStartDate()), new SimpleDateFormat("yyyy-MM-dd").format(orderWithUser.getOrder().getEndDate())));
        descPesanTv.setText(orderWithUser.getOrder().getDescWork());
        resalamat.setText(orderWithUser.getUser().getUser().getAddress());


        findViewById(R.id.btnToLokasi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                   toMap( orderWithUser.getOrder().getLat(),orderWithUser.getOrder().getLng());
            }
        });

        if(orderWithUser.getOrder().getStatusTransaction() == 0){
            findViewById(R.id.linearConfimrAndReject).setVisibility(View.VISIBLE);
        }else{
            findViewById(R.id.linearConfimrAndReject).setVisibility(View.GONE);
        }

        if(orderWithUser.getOrder().getStatusTransaction() == 1){
            findViewById(R.id.selesaipesanan).setVisibility(View.VISIBLE);
        }else{
            findViewById(R.id.selesaipesanan).setVisibility(View.GONE);
        }


    }

    private void toMap(double lat, double lng) {
        Uri gmmIntentUri = Uri.parse("geo:" + lat + "," + lng);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    public  void testCompleted(){
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

    public  void successJob(String idOrder){
        LottieAnimationView animationView =  findViewById(R.id.loadingAnimation);
        animationView.setVisibility(View.VISIBLE);

        OrderDataSource orderDataSource =  OrderDataSource.getInstance(FirebaseFirestore.getInstance());

        orderDataSource.finishOrder(idOrder, new ResultListener<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                animationView.setVisibility(View.GONE);
                goToSuccess();
            }

            @Override
            public void onError(Throwable error) {
                animationView.setVisibility(View.GONE);
            }
        });

    }

    public  void rejectOrder(String idOrder){
        LottieAnimationView animationView =  findViewById(R.id.loadingAnimation);
        animationView.setVisibility(View.VISIBLE);

        OrderDataSource orderDataSource =  OrderDataSource.getInstance(FirebaseFirestore.getInstance());

        orderDataSource.rejectOrder(idOrder, new ResultListener<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                animationView.setVisibility(View.GONE);
                getDataOrder();
            }

            @Override
            public void onError(Throwable error) {
                animationView.setVisibility(View.GONE);
            }
        });

    }

    public  void confirmOrder(String idOrder) {
        LottieAnimationView animationView = findViewById(R.id.loadingAnimation);

        animationView.setVisibility(View.VISIBLE);

        OrderDataSource orderDataSource = OrderDataSource.getInstance(FirebaseFirestore.getInstance());

        orderDataSource.confirmOrder(idOrder, new ResultListener<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                animationView.setVisibility(View.GONE);
                getDataOrder();
            }

            @Override
            public void onError(Throwable error) {
                animationView.setVisibility(View.GONE);
            }
        });
    }


    void goToSuccess(){
        Intent intent = new Intent(ConfirmOrderActivity.this, SuccessComplatedActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}