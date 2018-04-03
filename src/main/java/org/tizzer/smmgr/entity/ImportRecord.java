package org.tizzer.smmgr.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
public class ImportRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    //总额
    @Column(nullable = false)
    private Double cost;
    //备注
    private String note;
    //生成时间
    @Column(nullable = false)
    private Date createAt;

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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }
}
