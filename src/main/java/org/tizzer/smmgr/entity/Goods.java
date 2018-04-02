package org.tizzer.smmgr.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Goods implements Serializable {
    //条形码
    @Id
    private String upc;
    //名称
    @Column(nullable = false)
    private String name;
    //拼音码
    @Column(nullable = false)
    private String spell;
    //采购价
    @Column(nullable = false, scale = 2)
    private Double jPrice;
    //零售价
    @Column(nullable = false, scale = 2)
    private Double sPrice;
    //库存
    @Column(nullable = false)
    private Integer inventory;
    //生产日期
    @Column(nullable = false)
    @Temporal(value = TemporalType.DATE)
    private Date scDate;
    //截至日期
    @Column(nullable = false)
    private Integer bzDate;
    //种类id
    @ManyToOne
    @JoinColumn(name = "type_id", referencedColumnName = "id")
    private GoodsType goodsType;

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

    public String getSpell() {
        return spell;
    }

    public void setSpell(String spell) {
        this.spell = spell;
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

    public Date getScDate() {
        return scDate;
    }

    public void setScDate(Date scDate) {
        this.scDate = scDate;
    }

    public Integer getBzDate() {
        return bzDate;
    }

    public void setBzDate(Integer bzDate) {
        this.bzDate = bzDate;
    }

    public GoodsType getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(GoodsType goodsType) {
        this.goodsType = goodsType;
    }
}
