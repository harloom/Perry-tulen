package com.ferry.tulen.presentation.business.worker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ferry.tulen.R;

import java.util.List;


import com.ferry.tulen.datasources.models.Order;

public class RcvOrderAdapter extends RecyclerView.Adapter<RcvOrderAdapter.ViewHolder> {
    private List<Order> mData;
    private OnItemClickListener mListener;

    public RcvOrderAdapter(List<Order> data,OnItemClickListener listener) {
        mData = data;
        mListener = listener;
    }

    @NonNull
    @Override
    public RcvOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new RcvOrderAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RcvOrderAdapter.ViewHolder holder, int position) {

        holder.mTextView.setText(mData.get(position).getCategoryJob());
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
