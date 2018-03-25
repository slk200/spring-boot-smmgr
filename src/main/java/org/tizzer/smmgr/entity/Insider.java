package org.tizzer.smmgr.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
public class Insider implements Serializable {
    //会员卡号
    @Id
    @Column(name = "card_no")
    private String cardNo;

    //会员姓名
    @Column(nullable = false)
    private String name;

    //联系电话
    @Column(nullable = false)
    private String phone;

    //生日
    @Temporal(TemporalType.DATE)
    private Date birth;

    //联系地址
    private String address;

    //备注
    private String note;

    //录入时间
    @Column(name = "create_at", nullable = false)
    private Date createAt;

    //会员类型id
    @ManyToOne
    @JoinColumn(name = "type_id", referencedColumnName = "id")
    private InsiderType insiderType;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "insider")
    List<TradeRecord> tradeRecords;

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public InsiderType getInsiderType() {
        return insiderType;
    }

    public void setInsiderType(InsiderType insiderType) {
        this.insiderType = insiderType;
    }
}
