package org.tizzer.smmgr.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    //名称
    @Column(nullable = false)
    private String name;
    //地址
    @Column(nullable = false)
    private String address;
    //创建时间
    @Column(name = "found_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date foundDate;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "store")
    private List<Employee> employees;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFoundDate() {
        return foundDate;
    }

    public void setFoundDate(Date foundDate) {
        this.foundDate = foundDate;
    }

    public String getName() {
        return name;

    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
