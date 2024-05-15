package com.ferry.tulen.presentation.home.rcv;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ferry.tulen.R;

import com.ferry.tulen.datasources.models.WorkMan;

import java.util.List;

public class WorkManRecyclerViewAdapter extends RecyclerView.Adapter<WorkManRecyclerViewAdapter.ViewHolder> {
    private List<WorkMan> mData;
    private OnItemClickListener mListener;

    public WorkManRecyclerViewAdapter(List<WorkMan> data, OnItemClickListener listener) {
        mData = data;
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_workman, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.namaTukang.setText(mData.get(position).getFullName());
        holder.categoryTukang.setText(String.format("Tukang %s", mData.get(position).getJob()));

        holder.itemView.setOnClickListener(v -> mListener.onItemClick(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView namaTukang;
        public TextView categoryTukang;
        public ViewHolder(View itemView) {
            super(itemView);
            namaTukang = itemView.findViewById(R.id.namatukang);

            categoryTukang = itemView.findViewById(R.id.categoryTukang);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}