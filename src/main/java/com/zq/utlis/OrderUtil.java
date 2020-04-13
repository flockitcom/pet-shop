package com.zq.utlis;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 86132 on 2020/02/02.
 * 交易相关
 */
public class OrderUtil {

    //    根据单价和数量获取总价
    public static double totalPrice(double price,Integer amount){
        BigDecimal priceBig = new BigDecimal(price);
        BigDecimal divide = priceBig.multiply(new BigDecimal(amount));
        String totalpriceStr = String.format("%.2f", divide.doubleValue());
        return Double.valueOf(totalpriceStr);
    }

    public static String formatDate(Date date){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }

}
