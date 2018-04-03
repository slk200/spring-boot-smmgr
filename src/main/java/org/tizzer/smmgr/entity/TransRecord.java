package org.tizzer.smmgr.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
public class TransRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    //总额
    @Column(nullable = false)
    private Double cost;
    //生成时间
    @Column(nullable = false)
    private Date createAt;
    //分店
    @JoinColumn(name = "store_id", referencedColumnName = "id")
    @ManyToOne
    private Store store;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }
}
