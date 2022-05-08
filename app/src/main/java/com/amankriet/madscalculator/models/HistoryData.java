package com.amankriet.madscalculator.models;

import java.io.Serializable;

public class HistoryData implements Serializable {

    String operation;
    String result;

    public HistoryData(String operation, String result) {
        this.operation = operation;
        this.result = result;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
