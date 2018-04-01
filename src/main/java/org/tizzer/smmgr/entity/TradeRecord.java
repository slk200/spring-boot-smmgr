package org.tizzer.smmgr.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class TradeRecord implements Serializable {
    //流水号
    @Id
    @Column(name = "serial_no")
    private String serialNo;

    //员工号
    @Column(nullable = false)
    private String staffNo;

    //牌号
    @Column(nullable = false)
    private String markNo;

    //支付方式
    @Column(nullable = false)
    private String payType;

    //会员号
    private String cardNo;

    //会员电话
    private String phone;

    //折扣
    @Column(nullable = false)
    private Integer discount;

    //总额
    @Column(nullable = false)
    private Double cost;

    //出售时间
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date soldTime;

    //类型
    @Column(nullable = false)
    private boolean type;

    //原单据
    private String originalSerial;

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getStaffNo() {
        return staffNo;
    }

    public void setStaffNo(String staffNo) {
        this.staffNo = staffNo;
    }

    public String getMarkNo() {
        return markNo;
    }

    public void setMarkNo(String markNo) {
        this.markNo = markNo;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
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

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Date getSoldTime() {
        return soldTime;
    }

    public void setSoldTime(Date soldTime) {
        this.soldTime = soldTime;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public String getOriginalSerial() {
        return originalSerial;
    }

    public void setOriginalSerial(String originalSerial) {
        this.originalSerial = originalSerial;
    }
}
