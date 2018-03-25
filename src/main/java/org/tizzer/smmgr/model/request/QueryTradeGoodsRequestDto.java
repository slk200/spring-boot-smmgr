package org.tizzer.smmgr.model.request;

public class QueryTradeGoodsRequestDto {
    private String keyword;

    public String getKeyword() {
        return "%" + keyword + "%";
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
