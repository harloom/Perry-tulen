package com.ferry.tulen.presentation.home.rcv;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ferry.tulen.R;
import com.ferry.tulen.datasources.models.Category;

import java.util.List;

public class CategoryRecyclerViewAdapter extends RecyclerView.Adapter<CategoryRecyclerViewAdapter.ViewHolder> {
    private List<Category> mData;
    private OnItemClickListener mListener;

    public CategoryRecyclerViewAdapter(List<Category> data, OnItemClickListener listener) {
        mData = data;
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_c, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mTextView.setText(mData.get(position).getName());
        holder.itemView.setOnClickListener(v -> mListener.onItemClick(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.idnamajob);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
