package com.amankriet.madscalculator;

import static com.amankriet.madscalculator.MainActivity.CALCULATOR_HISTORY_LIST;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amankriet.madscalculator.adapters.HistoryAdapter;
import com.amankriet.madscalculator.databinding.ActivityHistoryBinding;
import com.amankriet.madscalculator.models.CalculatorHistoryList;
import com.amankriet.madscalculator.models.HistoryData;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private static final String LINEAR_LAYOUT_MANAGER = "LinearLayoutManager";
    protected String mCurrentLayoutManagerType;
    protected RecyclerView.LayoutManager mLayoutManager;

    private ActivityHistoryBinding binding;
    HistoryAdapter historyAdapter;
    CalculatorHistoryList calculatorHistoryList;
    List<HistoryData> historyDataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);

        calculatorHistoryList = (CalculatorHistoryList) getIntent()
                .getSerializableExtra(CALCULATOR_HISTORY_LIST);
        historyDataList = calculatorHistoryList.getHistoryDataList();

        setRecyclerViewLayoutManager();

        historyAdapter = new HistoryAdapter(historyDataList);
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
}