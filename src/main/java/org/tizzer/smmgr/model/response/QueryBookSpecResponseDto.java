package org.tizzer.smmgr.model.response;

import java.util.List;

public class QueryBookSpecResponseDto<T> extends ResultResponse {
    private List<T> data;
    private Double cost;
    private String note;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
