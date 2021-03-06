package com.zq.service.model;

import java.util.List;

/**
 * Created by 86132 on 2020/01/28.
 */
public class PetModel {

    private Integer id;

    private String title;

    private Integer typeId;

    private String typeStr;

    private Double price;

    private Double primaryPrice;

    private String description;

    private Integer sales;

    private Integer stock;

    private Integer browse;

    private String mainimgUrl;

    private List<String> imgs;

    private Integer merchantId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getTypeStr() {
        return typeStr;
    }

    public void setTypeStr(String typeStr) {
        this.typeStr = typeStr;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getPrimaryPrice() {
        return primaryPrice;
    }

    public void setPrimaryPrice(Double primaryPrice) {
        this.primaryPrice = primaryPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSales() {
        return sales;
    }

    public void setSales(Integer sales) {
        this.sales = sales;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getBrowse() {
        return browse;
    }

    public void setBrowse(Integer browse) {
        this.browse = browse;
    }

    public String getMainimgUrl() {
        return mainimgUrl;
    }

    public void setMainimgUrl(String mainimgUrl) {
        this.mainimgUrl = mainimgUrl;
    }

    public List<String> getImgs() {
        return imgs;
    }

    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }

    public Integer getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }
}
