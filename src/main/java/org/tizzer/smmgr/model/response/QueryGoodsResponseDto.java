package org.tizzer.smmgr.model.response;

public class QueryGoodsResponseDto {
    private String upc;
    private String name;
    private Double jPrice;
    private Double sPrice;
    private Integer inventory;

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getjPrice() {
        return jPrice;
    }

    public void setjPrice(Double jPrice) {
        this.jPrice = jPrice;
    }

    public Double getsPrice() {
        return sPrice;
    }

    public void setsPrice(Double sPrice) {
        this.sPrice = sPrice;
    }

    public Integer getInventory() {
        return inventory;
    }

    public void setInventory(Integer inventory) {
        this.inventory = inventory;
    }
}
