package org.tizzer.smmgr.model.request;

public class QueryGoodsRequestDto extends PageableRequestDto {
    private String keyword;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
