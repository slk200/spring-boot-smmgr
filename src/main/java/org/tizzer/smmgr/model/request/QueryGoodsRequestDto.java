package org.tizzer.smmgr.model.request;

public class QueryGoodsRequestDto extends PageableRequestDto {
    private Integer typeId;
    private String keyword;

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
