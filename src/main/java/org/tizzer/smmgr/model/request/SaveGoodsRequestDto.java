package org.tizzer.smmgr.model.request;

public class SaveGoodsRequestDto {
    private String upc;
    private String name;
    private String spell;
    private Double jPrice;
    private Double sPrice;
    private Integer invention;
    private String scDate;
    private Integer bzDate;
    private Integer id;
    private String type;

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

    public Integer getInvention() {
        return invention;
    }

    public void setInvention(Integer invention) {
        this.invention = invention;
    }

    public String getScDate() {
        return scDate;
    }

    public void setScDate(String scDate) {
        this.scDate = scDate;
    }

    public Integer getBzDate() {
        return bzDate;
    }

    public void setBzDate(Integer bzDate) {
        this.bzDate = bzDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
