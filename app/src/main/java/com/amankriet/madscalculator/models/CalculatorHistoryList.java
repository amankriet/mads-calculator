package com.amankriet.madscalculator.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CalculatorHistoryList implements Serializable {

    List<HistoryData> historyDataList = new ArrayList<>();

    public List<HistoryData> getHistoryDataList() {
        return historyDataList;
    }

    public void setHistoryDataList(List<HistoryData> historyDataList) {
        this.historyDataList = new ArrayList<>(historyDataList);
    }
}
