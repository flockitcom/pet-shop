package com.zq.service.model;

import com.zq.bean.PetDo;
import com.zq.bean.UserDo;
import org.joda.time.DateTime;

import java.util.Date;

/**
 * Created by 86132 on 2020/01/31.
 */
public class OrderModel {

    private Integer id;

    private String orderNo;

    private Date time;

    private String timeStr;

    private Integer userId;

    private Integer petId;

    private Double petPrice;

    private Integer amount;

    private Double orderPrice;

    private Integer promoId;

    private Integer status;

    private UserDo userDo;

    private PetDo petDo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
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

    public Double getPetPrice() {
        return petPrice;
    }

    public void setPetPrice(Double petPrice) {
        this.petPrice = petPrice;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Double getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(Double orderPrice) {
        this.orderPrice = orderPrice;
    }

    public Integer getPromoId() {
        return promoId;
    }

    public void setPromoId(Integer promoId) {
        this.promoId = promoId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public UserDo getUserDo() {
        return userDo;
    }

    public void setUserDo(UserDo userDo) {
        this.userDo = userDo;
    }

    public PetDo getPetDo() {
        return petDo;
    }

    public void setPetDo(PetDo petDo) {
        this.petDo = petDo;
    }

    public String getTimeStr() {
        return timeStr;
    }

    public void setTimeStr(String timeStr) {
        this.timeStr = timeStr;
    }
}
