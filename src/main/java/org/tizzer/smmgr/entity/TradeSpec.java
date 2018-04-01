package org.tizzer.smmgr.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class TradeSpec implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //条码
    @Column(nullable = false)
    private String upc;

    //名称
    @Column(nullable = false)
    private String name;

    //原价
    @Column(nullable = false, scale = 2)
    private double primeCost;

    //售价
    @Column(nullable = false)
    private double presentCost;

    //数量
    @Column(nullable = false)
    private Integer quantity;

    //流水号
    @Column(nullable = false)
    private String serialNo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public double getPrimeCost() {
        return primeCost;
    }

    public void setPrimeCost(double primeCost) {
        this.primeCost = primeCost;
    }

    public double getPresentCost() {
        return presentCost;
    }

    public void setPresentCost(double presentCost) {
        this.presentCost = presentCost;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }
}
