package org.tizzer.smmgr.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
public class LossRecord {
    //单号
    @Id
    private String id;
    //员工号
    @Column(nullable = false)
    private String staffNo;
    //牌号
    @Column(nullable = false)
    private String markNo;
    //总额
    @Column(nullable = false)
    private Double cost;
    //备注
    private String note;
    //生成时间
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
