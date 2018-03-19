package org.tizzer.smmgr.entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
    @Column(name = "j_price", nullable = false, scale = 2)
    private Double jPrice;

    //零售价
    @Column(name = "s_price", nullable = false, scale = 2)
    private Double sPrice;

    //库存
    @Column(nullable = false)
    private Long inventory;

    //生产日期
    @Column(name = "sc_date", nullable = false)
    @Temporal(value = TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date scDate;

    //截至日期
    @Column(name = "jz_date", nullable = false)
    @Temporal(value = TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date jzDate;

    //种类id
    @ManyToOne
    @JoinColumn(name = "type_id", referencedColumnName = "id")
    private GoodsType goodsType;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "goods")
    private List<TradeSpec> tradeSpecs;

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

    public Long getInventory() {
        return inventory;
    }

    public void setInventory(Long inventory) {
        this.inventory = inventory;
    }

    public Date getScDate() {
        return scDate;
    }

    public void setScDate(Date scDate) {
        this.scDate = scDate;
    }

    public List<TradeSpec> getTradeSpecs() {
        return tradeSpecs;
    }

    public void setTradeSpecs(List<TradeSpec> tradeSpecs) {
        this.tradeSpecs = tradeSpecs;
    }

    public Date getJzDate() {
        return jzDate;
    }

    public void setJzDate(Date jzDate) {
        this.jzDate = jzDate;
    }

    public GoodsType getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(GoodsType goodsType) {
        this.goodsType = goodsType;
    }
}
