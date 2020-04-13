package com.zq.dao;

import com.zq.bean.OrderDo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderDoMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(OrderDo record);

    int insertSelective(OrderDo record);

    OrderDo selectByPrimaryKey(Integer id);

    List<OrderDo> selectByUserIdAndStatus(@Param("userId") Integer userId, @Param("status") Integer status);

    List<OrderDo> selectOrderByStatus(Integer status);

    List<OrderDo> selectOrderByOrderNo(String orderNo);

    List<OrderDo> selectOrderByEmail(String email);

    List<OrderDo> selectOrderByStart(@Param("start") String start,@Param("status")Integer status);

    List<OrderDo> selectOrderByEnd(@Param("end") String end,@Param("status")Integer status);

    List<OrderDo> selectOrderByDate(@Param("start") String start,@Param("end") String end,@Param("status")Integer status);

    int updateByPrimaryKeySelective(OrderDo record);

    int updateByPrimaryKey(OrderDo record);

    int confirmOrder(String orderNo);

    int sendOrder(String orderNo);

    int deleteByOrderNo(String orderNo);
}