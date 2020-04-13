package com.zq.service.model;

import com.zq.bean.PetDo;

/**
 * Created by 86132 on 2020/01/31.
 */
public class CartModel {

    private Integer id;

    private Integer userId;

    private Integer petId;

    private Integer amount;

    private Double totalPrice;

    private Integer promoId;

    private PetDo petDo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getPetId() {
        return petId;
    }

    public void setPetId(Integer petId) {
        this.petId = petId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getPromoId() {
        return promoId;
    }

    public void setPromoId(Integer promoId) {
        this.promoId = promoId;
    }

    public PetDo getPetDo() {
        return petDo;
    }

    public void setPetDo(PetDo petDo) {
        this.petDo = petDo;
    }
}
