package org.tizzer.smmgr.model.response;

import java.util.ArrayList;
import java.util.List;

public class ResultListResponse<T> extends ResultResponse {
    private List<T> data = new ArrayList<>();
    private Integer pageCount;
    private Integer currentPage;

    public List<T> getData() {
        return data;
    }

    public void setData(T t) {
        this.data.add(t);
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

}
