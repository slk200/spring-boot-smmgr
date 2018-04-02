package org.tizzer.smmgr.model.request;

public class SaveTradeRecordRequestDto {
    private String staffNo;
    private Integer discount;
    private String payType;
    private String cardNo;
    private String phone;
    private Double cost;
    private Boolean type;
    private String serialNo;
    private String[] upc;
    private String[] name;
    private Double[] primeCost;
    private Double[] presentCost;
    private Integer[] quantity;

    public String getStaffNo() {
        return staffNo;
    }

    public void setStaffNo(String staffNo) {
        this.staffNo = staffNo;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Boolean getType() {
        return type;
    }

    public void setType(Boolean type) {
        this.type = type;
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

    public Double[] getPresentCost() {
        return presentCost;
    }

    public void setPresentCost(Double[] presentCost) {
        this.presentCost = presentCost;
    }

    public Integer[] getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer[] quantity) {
        this.quantity = quantity;
    }
}
