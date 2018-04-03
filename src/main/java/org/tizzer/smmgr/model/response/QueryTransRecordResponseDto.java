package org.tizzer.smmgr.model.response;

import java.util.Date;

public class QueryTransRecordResponseDto {
    private Long id;
    private Date createAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }
}
