package com.zq.service.model;

/**
 * 数据统计
 * Created by 86132 on 2020/04/08.
 */
public class StatisticsModel {

    Integer NoSendOrder;

    Integer NoReceiptOrder;

    Integer successOrder;

    Double NoSendPrice;

    Double NoReceiptPricer;

    Double successPricer;

    public Integer getNoSendOrder() {
        return NoSendOrder;
    }

    public void setNoSendOrder(Integer noSendOrder) {
        NoSendOrder = noSendOrder;
    }

    public Integer getNoReceiptOrder() {
        return NoReceiptOrder;
    }

    public void setNoReceiptOrder(Integer noReceiptOrder) {
        NoReceiptOrder = noReceiptOrder;
    }

    public Integer getSuccessOrder() {
        return successOrder;
    }

    public void setSuccessOrder(Integer successOrder) {
        this.successOrder = successOrder;
    }

    public Double getNoSendPrice() {
        return NoSendPrice;
    }

    public void setNoSendPrice(Double noSendPrice) {
        NoSendPrice = noSendPrice;
    }

    public Double getNoReceiptPricer() {
        return NoReceiptPricer;
    }

    public void setNoReceiptPricer(Double noReceiptPricer) {
        NoReceiptPricer = noReceiptPricer;
    }

    public Double getSuccessPricer() {
        return successPricer;
    }

    public void setSuccessPricer(Double successPricer) {
        this.successPricer = successPricer;
    }
}
