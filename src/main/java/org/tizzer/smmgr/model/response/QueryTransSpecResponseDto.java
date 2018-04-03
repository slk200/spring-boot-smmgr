package org.tizzer.smmgr.model.response;

import java.util.List;

public class QueryTransSpecResponseDto<T> extends ResultResponse {
    private List<T> data;
    private Double cost;
    private String name;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
