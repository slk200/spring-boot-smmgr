package org.tizzer.smmgr.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class TradeSpec implements Serializable {

    @Id
    private Long id;

    //数量
    @Column(nullable = false)
    private Integer quantity;

    //流水号
    @JoinColumn(name = "serial_no", referencedColumnName = "serial_no")
    @ManyToOne
    private TradeRecord tradeRecord;

    //条形码
    @JoinColumn(name = "upc", referencedColumnName = "upc")
    @ManyToOne
    private Goods goods;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public TradeRecord getTradeRecord() {
        return tradeRecord;
    }

    public void setTradeRecord(TradeRecord tradeRecord) {
        this.tradeRecord = tradeRecord;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }
}
