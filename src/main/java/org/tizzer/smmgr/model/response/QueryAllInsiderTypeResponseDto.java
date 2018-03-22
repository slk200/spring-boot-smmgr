package org.tizzer.smmgr.model.response;

import java.util.List;

public class QueryAllInsiderTypeResponseDto<T> extends ResultResponse {
    private List<T> data;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
