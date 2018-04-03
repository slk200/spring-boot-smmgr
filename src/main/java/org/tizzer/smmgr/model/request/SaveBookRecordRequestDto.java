package org.tizzer.smmgr.model.request;

public class SaveBookRecordRequestDto {
    private Double cost;
    private String note;
    private String[] upc;
    private String[] name;
    private Double[] primeCost;
    private Integer[] quantity;

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String[] getUpc() {
        return upc;
    }

    public void setUpc(String[] upc) {
        this.upc = upc;
    }

    public String[] getName() {
        return name;
    }

    public void setName(String[] name) {
        this.name = name;
    }

    public Double[] getPrimeCost() {
        return primeCost;
    }

    public void setPrimeCost(Double[] primeCost) {
        this.primeCost = primeCost;
    }

    public Integer[] getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer[] quantity) {
        this.quantity = quantity;
    }
}
