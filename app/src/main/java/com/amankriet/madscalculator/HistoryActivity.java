package com.amankriet.madscalculator;

import static com.amankriet.madscalculator.MainActivity.HISTORY_ARRAYLIST;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amankriet.madscalculator.adapters.HistoryAdapter;
import com.amankriet.madscalculator.databinding.ActivityHistoryBinding;
import com.amankriet.madscalculator.models.HistoryData;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    private static final String LINEAR_LAYOUT_MANAGER = "LinearLayoutManager";
    protected String mCurrentLayoutManagerType;
    protected RecyclerView.LayoutManager mLayoutManager;

    private ActivityHistoryBinding binding;
    HistoryAdapter historyAdapter;
    ArrayList<HistoryData> historyDataArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);

        historyDataArrayList = (ArrayList) getIntent().getParcelableArrayListExtra(HISTORY_ARRAYLIST);

        setRecyclerViewLayoutManager();

        historyAdapter = new HistoryAdapter(historyDataArrayList);
        binding.layoutContentHistory.recyclerViewHistory.setAdapter(historyAdapter);
    }

    public void setRecyclerViewLayoutManager() {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (binding.layoutContentHistory.recyclerViewHistory.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) binding.layoutContentHistory.recyclerViewHistory.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        mLayoutManager = new LinearLayoutManager(this);
        mCurrentLayoutManagerType = LINEAR_LAYOUT_MANAGER;

        binding.layoutContentHistory.recyclerViewHistory.setLayoutManager(mLayoutManager);
        binding.layoutContentHistory.recyclerViewHistory.scrollToPosition(scrollPosition);
    }

//    private void initHistory() {
//        historyDataArrayList = new ArrayList<>();
//        historyDataArrayList.add(new HistoryData("5+5", "10"));
//        historyDataArrayList.add(new HistoryData("5*4", "20"));
//        historyDataArrayList.add(new HistoryData("50÷25", "2"));
//        historyDataArrayList.add(new HistoryData("5×6", "30"));
//        historyDataArrayList.add(new HistoryData("5+5-2", "8"));
//        historyDataArrayList.add(new HistoryData("5+6+2", "13"));
//        historyDataArrayList.add(new HistoryData("5+5×2", "15"));
//        historyDataArrayList.add(new HistoryData("5+5÷2", "5"));
//        historyDataArrayList.add(new HistoryData("5+5+0-0", "10"));
//        historyDataArrayList.add(new HistoryData("30+50÷2×4-3", "7"));
//    }
}