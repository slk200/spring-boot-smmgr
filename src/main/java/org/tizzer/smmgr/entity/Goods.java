package org.tizzer.smmgr.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Goods {
    @Id
    //条形码
    private String upc;
    //名称
    private String name;
    //拼音码
    private String spell;
    //种类id
    private Integer typeId;
    //采购价
    private Double jPrice;
    //零售价
    private Double sPrice;
    //库存
    private Long inventory;
    //生产日期
    private Date scDate;
    //截至日期
    private Date jzDate;

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

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
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

    public Date getJzDate() {
        return jzDate;
    }

    public void setJzDate(Date jzDate) {
        this.jzDate = jzDate;
    }
}
