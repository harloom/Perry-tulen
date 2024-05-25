package com.ferry.tulen.presentation.business.user.fragment.rcv;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ferry.tulen.R;
import com.ferry.tulen.datasources.models.Order;

import java.util.List;

public class RcvOrderOnProgressAdapter extends RecyclerView.Adapter<RcvOrderOnProgressAdapter.ViewHolder> {
    private List<Order> mData;
    private OnItemClickListener mListener;

    public RcvOrderOnProgressAdapter(List<Order> data, OnItemClickListener listener) {
        mData = data;
        mListener = listener;
    }

    @NonNull
    @Override
    public RcvOrderOnProgressAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_progress, parent, false);
        return new RcvOrderOnProgressAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RcvOrderOnProgressAdapter.ViewHolder holder, int position) {

        holder.mTextView.setText("Pekerjaan : " + mData.get(position).getCategoryJob());
        holder.mTextView2.setText(mData.get(position).getDescWork());
        Long transactionStatusId = mData.get(position).getStatusTransaction();

        holder.itemView.setOnClickListener(v -> mListener.onItemClick(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public TextView mTextView2;

        public TextView mStatusTransaction;
        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.categoryOrder);

            mTextView2 = itemView.findViewById(R.id.descOrder);

            mStatusTransaction = itemView.findViewById(R.id.statusTransaction);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
