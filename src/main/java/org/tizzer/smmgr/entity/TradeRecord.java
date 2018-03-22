package org.tizzer.smmgr.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
public class TradeRecord implements Serializable {
    //流水号
    @Id
    @Column(name = "serial_no")
    private String serialNo;

    //员工号
    @Column(name = "staff_no", nullable = false)
    private String staffNo;

    //是否为会员订单
    @Column(name = "is_insider")
    private boolean isInsider;

    //折扣
    @Column(nullable = false, scale = 2)
    private Float discount;

    //出售时间
    @Column(name = "sold_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date soldTime;

    //支付方式
    @JoinColumn(name = "type_id", referencedColumnName = "id")
    @ManyToOne
    private PayType payType;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tradeRecord")
    private List<TradeSpec> tradeSpecs;

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

    public boolean isInsider() {
        return isInsider;
    }

    public void setInsider(boolean insider) {
        isInsider = insider;
    }

    public Float getDiscount() {
        return discount;
    }

    public void setDiscount(Float discount) {
        this.discount = discount;
    }

    public Date getSoldTime() {
        return soldTime;
    }

    public void setSoldTime(Date soldTime) {
        this.soldTime = soldTime;
    }

    public PayType getPayType() {
        return payType;
    }

    public void setPayType(PayType payType) {
        this.payType = payType;
    }
}
