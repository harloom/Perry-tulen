package com.ferry.tulen.presentation.business.user;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.ferry.tulen.R;
import com.ferry.tulen.datasources.firebase.OrderDataSource;
import com.ferry.tulen.datasources.firebase.StorageDownload;
import com.ferry.tulen.datasources.listener.ResultListener;
import com.ferry.tulen.datasources.models.OrderWithUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Objects;


    public class OrderOnProgressActivity extends AppCompatActivity {
/// untuk user

        private TextView namaUserTv;

        private TextView categoryTukang;
        private  TextView jenisPekerjaanTv;

        private  TextView tipePekerjaanTv;
        private  TextView jadwalPekerjaanTv;

        private  TextView descPesanTv;

        private  TextView resalamat;

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_detail_user);
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
            categoryTukang = findViewById(R.id.categoryTukang);
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


        }

        private void setData(OrderWithUser orderWithUser){
            namaUserTv.setText(orderWithUser.getWorkMan().getWorkMan().getFullName());
            categoryTukang.setText("Tukang "+orderWithUser.getWorkMan().getWorkMan().getJob());
            jenisPekerjaanTv.setText(orderWithUser.getOrder().getCategoryJob());
            tipePekerjaanTv.setText(orderWithUser.getOrder().getTypeWork());
            jadwalPekerjaanTv.setText(String.format("Mulai %s - %s", new SimpleDateFormat("yyyy-MM-dd").format(orderWithUser.getOrder().getStartDate()), new SimpleDateFormat("yyyy-MM-dd").format(orderWithUser.getOrder().getEndDate())));
            descPesanTv.setText(orderWithUser.getOrder().getDescWork());
            resalamat.setText(orderWithUser.getWorkMan().getWorkMan().getAddress());

            ImageView fotopesanan = findViewById(R.id.fotopesanan);


            StorageDownload.getInstance(FirebaseStorage.getInstance()).getUrlDownload(orderWithUser.getOrder().getFileStorage(), new ResultListener<Uri>() {
                @Override
                public void onSuccess(Uri result) {
                    Glide.with(OrderOnProgressActivity.this)
                            .load(result)
                            .into(fotopesanan);
                }

                @Override
                public void onError(Throwable error) {

                }
            });



            findViewById(R.id.btnToLokasi).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toMap( orderWithUser.getOrder().getLat(),orderWithUser.getOrder().getLng());
                }
            });
        }

        private void toMap(double lat, double lng) {
            Uri gmmIntentUri = Uri.parse("geo:" + lat + "," + lng);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        }

    }