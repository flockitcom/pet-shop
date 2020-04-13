package com.zq.service;

import com.github.pagehelper.PageInfo;
import com.zq.error.BusinessException;
import com.zq.service.model.OrderModel;
import com.zq.service.model.StatisticsModel;
import org.springframework.core.annotation.Order;

import java.util.List;

/**
 * Created by 86132 on 2020/01/31.
 */
public interface OrderService {

    OrderModel createOrder(Integer userId, Integer petId, Integer amount) throws BusinessException;

    //根据用户还有状态查询当前订单
    PageInfo<OrderModel> findCurrentOrderByUserId(Integer userId, Integer status, Integer pageNo, Integer pageSize) throws BusinessException;

    void confirmOrder(Integer userId, String orderNo) throws BusinessException;

    void deleteOrder(Integer userId, String orderNo) throws BusinessException;

    PageInfo<OrderModel> findOrderByCondition(Integer adminId, Integer status, String orderNo, String email, String start, String end, Integer pageNo, Integer pageSize) throws BusinessException;

    void sendOrder(String orderNo) throws BusinessException;

    StatisticsModel OrderStatistics(String date);
}
