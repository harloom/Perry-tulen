package com.ferry.tulen.presentation.business;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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
import com.ferry.tulen.datasources.firebase.WorkManDataSource;
import com.ferry.tulen.datasources.listener.ResultListener;
import com.ferry.tulen.datasources.models.WorkMan;
import com.ferry.tulen.presentation.home.HomeActivity;
import com.ferry.tulen.presentation.home.rcv.WorkManRecyclerViewAdapter;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Objects;

public class ChooseWorkManActivity extends AppCompatActivity {
    private RecyclerView workManRcv;
    private WorkManRecyclerViewAdapter workManRecyclerViewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_choose_work_man);

        Objects.requireNonNull(getSupportActionBar()).hide();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), new OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                Insets navigationBars = insets.getInsets(WindowInsetsCompat.Type.navigationBars());
                v.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(), navigationBars.bottom);
                return insets;
            }
        });

        Intent intent = getIntent();

        String pekerjaan = intent.getStringExtra("job");
        SharedPreferenceHelper sharedPreferenceHelper = new SharedPreferenceHelper(this);
        String idUser = sharedPreferenceHelper.getString(SharedPreferenceHelper.KEY_ID_USER,"");

        WorkManDataSource workManDataSource = WorkManDataSource.getInstance(FirebaseFirestore.getInstance());
                System.out.println("debug: getWorkMan");
        workManRcv = findViewById(R.id.workman_rc);
        workManDataSource.getListByFiler(idUser,pekerjaan,new ResultListener<List<WorkMan>>() {
            @Override
            public void onSuccess(List<WorkMan> result) {

                System.out.println("debug: size : " + result.size());/**/
                workManRecyclerViewAdapter = new WorkManRecyclerViewAdapter(result, new WorkManRecyclerViewAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        setResultAndFinish(result.get(position));
                    }
                });
                workManRcv.setLayoutManager(new LinearLayoutManager(ChooseWorkManActivity.this,LinearLayoutManager.VERTICAL, false));
                workManRcv.setAdapter(workManRecyclerViewAdapter);
            }

            @Override
            public void onError(Throwable error) {

            }
        });

    }

    private void setResultAndFinish(WorkMan workMan) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("result_code", OrderWorkerCreateActivity.REQUEST_CODE_WORK_MAN);
        resultIntent.putExtra("workMan",workMan);
        System.out.println("priceMin : " + workMan.getPriceMin());
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}