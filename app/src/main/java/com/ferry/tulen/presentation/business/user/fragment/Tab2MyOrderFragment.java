package com.ferry.tulen.presentation.business.user.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ferry.tulen.R;
import com.ferry.tulen.datasources.SharedPreferences.SharedPreferenceHelper;
import com.ferry.tulen.datasources.firebase.OrderDataSource;
import com.ferry.tulen.datasources.listener.ResultListener;
import com.ferry.tulen.datasources.models.Order;
import com.ferry.tulen.presentation.business.user.CreateRatingActivity;
import com.ferry.tulen.presentation.business.user.OrderOnProgressActivity;
import com.ferry.tulen.presentation.business.user.fragment.rcv.RcvComplatedAdapter;
import com.ferry.tulen.presentation.business.user.fragment.rcv.RcvOrderOnProgressAdapter;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Tab2MyOrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Tab2MyOrderFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView rcvOrder;
    private RcvComplatedAdapter rcvOrderAdapter;
    private String mParam1;
    private String mParam2;

    public Tab2MyOrderFragment() {
        // Required empty public constructor
    }

    public static Tab2MyOrderFragment newInstance(String param1, String param2) {
        Tab2MyOrderFragment fragment = new Tab2MyOrderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /// setup data
        OrderDataSource orderDataSource =  OrderDataSource.getInstance(FirebaseFirestore.getInstance());


        SharedPreferenceHelper sharedPreferenceHelper = new SharedPreferenceHelper(view.getContext());
        String idUser = sharedPreferenceHelper.getString(SharedPreferenceHelper.KEY_ID_USER,"");

        rcvOrder = view.findViewById(R.id.rcv_order);
        orderDataSource.getListOrderFinishWithIdUser(idUser, new ResultListener<List<Order>>() {
            @Override
            public void onSuccess(List<Order> result) {
                rcvOrderAdapter = new RcvComplatedAdapter(result, new RcvComplatedAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Intent intent = new Intent(view.getContext(), CreateRatingActivity.class);
                        intent.putExtra("idOrder",result.get(position).getId());
                        startActivity(intent);
                    }
                });
                rcvOrder.setLayoutManager(new LinearLayoutManager(view.getContext(),LinearLayoutManager.VERTICAL, false));
                rcvOrder.setAdapter(rcvOrderAdapter);
            }

            @Override
            public void onError(Throwable error) {

            }
        });

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab2_my_order, container, false);
    }
}