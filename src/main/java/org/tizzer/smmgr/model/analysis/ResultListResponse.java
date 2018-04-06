package org.tizzer.smmgr.model.analysis;

import org.tizzer.smmgr.model.response.ResultResponse;

import java.util.ArrayList;
import java.util.List;

public class ResultListResponse<T> extends ResultResponse {
    private List<T> data = new ArrayList<>();

    public List<T> getData() {
        return data;
    }

    public void setData(T data) {
        this.data.add(data);
    }
}
