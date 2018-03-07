package org.tizzer.smmgr.model.response;

import java.util.ArrayList;
import java.util.List;

public class ResultListResponse<Entity> extends ResultResponse {
    private List<Entity> data = new ArrayList<>();
    private Integer pageCount;
    private Integer currentPage;
    private Long total;

    public List<Entity> getData() {
        return data;
    }

    public void setData(Entity data) {
        this.data.add(data);
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

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
