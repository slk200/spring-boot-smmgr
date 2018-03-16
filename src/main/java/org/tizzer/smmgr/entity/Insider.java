package org.tizzer.smmgr.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Insider implements Serializable {
    //会员卡号
    @Id
    private String cardNo;

    //密码
    private String password;

    //会员姓名
    @Column(nullable = false)
    private String name;

    //联系电话
    @Column(nullable = false)
    private String phone;

    //生日
    private Date birth;

    //余额
    private Double balance;

    //联系地址
    private String address;

    //备注
    private String note;

    //会员类型id
    @ManyToOne
    @JoinColumn(name = "type_id", referencedColumnName = "id")
    private InsiderType insiderType;

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
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

    public InsiderType getInsiderType() {
        return insiderType;
    }

    public void setInsiderType(InsiderType insiderType) {
        this.insiderType = insiderType;
    }
}
