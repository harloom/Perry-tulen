package com.ferry.tulen.presentation.business.worker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ferry.tulen.R;
import com.ferry.tulen.datasources.SharedPreferences.SharedPreferenceHelper;
import com.ferry.tulen.datasources.firebase.OrderDataSource;
import com.ferry.tulen.datasources.listener.ResultListener;
import com.ferry.tulen.datasources.models.Order;
import com.ferry.tulen.presentation.business.ConfirmOrderActivity;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Objects;

public class WorkerListOderActivity extends AppCompatActivity {
    private RecyclerView rcvOrder;
    private RcvOrderAdapter rcvOrderAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_worker_list_oder);
        Objects.requireNonNull(getSupportActionBar()).hide();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), new OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                Insets navigationBars = insets.getInsets(WindowInsetsCompat.Type.navigationBars());
                v.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(), navigationBars.bottom);
                return insets;
            }
        });

        OrderDataSource orderDataSource =  OrderDataSource.getInstance(FirebaseFirestore.getInstance());


        SharedPreferenceHelper sharedPreferenceHelper = new SharedPreferenceHelper(this);
         String idUser = sharedPreferenceHelper.getString(SharedPreferenceHelper.KEY_ID_USER,"");

        rcvOrder = findViewById(R.id.rcv_order);
        orderDataSource.getListOrderWithIdWorker(idUser, new ResultListener<List<Order>>() {
            @Override
            public void onSuccess(List<Order> result) {
                rcvOrderAdapter = new RcvOrderAdapter(result, new RcvOrderAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Intent intent = new Intent(WorkerListOderActivity.this, ConfirmOrderActivity.class);
                        intent.putExtra("idOrder",result.get(position).getId());
                        startActivity(intent);
                    }
                });
                rcvOrder.setLayoutManager(new LinearLayoutManager(WorkerListOderActivity.this,LinearLayoutManager.VERTICAL, false));
                rcvOrder.setAdapter(rcvOrderAdapter);
            }

            @Override
            public void onError(Throwable error) {

            }
        });

    }


}