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

public class RcvComplatedAdapter extends RecyclerView.Adapter<RcvComplatedAdapter.ViewHolder> {
    private List<Order> mData;
    private OnItemClickListener mListener;

    public RcvComplatedAdapter(List<Order> data, OnItemClickListener listener) {
        mData = data;
        mListener = listener;
    }

    @NonNull
    @Override
    public RcvComplatedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_complated, parent, false);
        return new RcvComplatedAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RcvComplatedAdapter.ViewHolder holder, int position) {

        holder.mTextView.setText("Pekerjaan : " + mData.get(position).getCategoryJob());
        holder.mTextView2.setText(mData.get(position).getDescWork());
        holder.itemView.setOnClickListener(v -> mListener.onItemClick(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public TextView mTextView2;
        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.categoryOrder);

            mTextView2 = itemView.findViewById(R.id.descOrder);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
