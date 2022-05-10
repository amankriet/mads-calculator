package com.amankriet.madscalculator.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amankriet.madscalculator.databinding.LayoutHistoryListBinding;
import com.amankriet.madscalculator.models.HistoryData;

import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    List<HistoryData> historyDataArrayList;

    LayoutHistoryListBinding binding;

    public HistoryAdapter(List<HistoryData> historyDataArrayList) {
        this.historyDataArrayList = historyDataArrayList;
    }

    @NonNull
    @Override
    public HistoryAdapter.HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = LayoutHistoryListBinding.inflate(LayoutInflater.from(parent.getContext()), parent,
                false);
        return new HistoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.HistoryViewHolder holder, int position) {
        Log.i("HistoryAdapter", "onBindViewHolder: "+ historyDataArrayList.get(position).getOperation());
       holder.binding.textViewOperationsHistory.setText(historyDataArrayList.get(position).getOperation());
       holder.binding.textViewResultHistory.setText(historyDataArrayList.get(position).getResult());
    }

    @Override
    public int getItemCount() {
        return historyDataArrayList!=null?historyDataArrayList.size():0;
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {

        private final LayoutHistoryListBinding binding;

        public HistoryViewHolder(@NonNull LayoutHistoryListBinding layoutHistoryListBinding) {
            super(layoutHistoryListBinding.getRoot());
            binding = layoutHistoryListBinding;
        }
    }
}
