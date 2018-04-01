package org.tizzer.smmgr.model.response;

import java.util.Date;

public class QueryTradeRecordResponseDto {
    private String serialNo;
    private String markNo;
    private boolean type;
    private Date soldTime;

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getMarkNo() {
        return markNo;
    }

    public void setMarkNo(String markNo) {
        this.markNo = markNo;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public Date getSoldTime() {
        return soldTime;
    }

    public void setSoldTime(Date soldTime) {
        this.soldTime = soldTime;
    }
}
