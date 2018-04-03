package org.tizzer.smmgr.model.response;

import java.util.List;

public class QueryLossSpecResponseDto<T> extends ResultResponse {
    private List<T> data;
    private double cost;
    private String note;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
