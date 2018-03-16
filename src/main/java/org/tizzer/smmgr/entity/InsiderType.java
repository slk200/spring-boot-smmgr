package org.tizzer.smmgr.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class InsiderType implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    //类型名称
    @Column(nullable = false)
    private String name;

    //折扣
    @Column(nullable = false)
    private Float discount;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "insiderType")
    private List<Insider> insiders;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getDiscount() {
        return discount;
    }

    public void setDiscount(Float discount) {
        this.discount = discount;
    }
}
