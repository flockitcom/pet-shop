package com.zq.controller;

import com.github.pagehelper.PageInfo;
import com.zq.error.BusinessException;
import com.zq.response.CommonReturnType;
import com.zq.service.impl.OrderServiceImpl;
import com.zq.service.model.OrderModel;
import com.zq.service.model.StatisticsModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by 86132 on 2020/01/31.
 */
@Api(value = "订单模块", description = "订单模块")
@RestController
@RequestMapping("order")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
public class OrderController extends BaseController{

    @Autowired
    private OrderServiceImpl orderService;

    @ApiOperation(value = "添加订单")
    @PostMapping(value = "/createOrder")
    public CommonReturnType createOrder(@ApiParam(name = "petId", value = "宠物id", required = true) @RequestParam Integer petId,
                                       @ApiParam(name = "amount", value = "数量", required = true) @RequestParam Integer amount,
                                       @ApiParam(name = "userId", value = "用户id") @RequestParam(required = false) Integer userId) throws BusinessException {
        OrderModel order = orderService.createOrder(userId, petId, amount);
        return CommonReturnType.createSuccess(order);
    }

    @ApiOperation(value = "查询当前订单")
    @PostMapping(value = "/findCurrentOrderByUserId")
    public CommonReturnType findCurrentOrderByUserId(@ApiParam(name = "pageNo", value = "页码") @RequestParam(required = false,defaultValue = "1") Integer pageNo,
                                        @ApiParam(name = "pageSize", value = "每页条数") @RequestParam(required = false,defaultValue = "100") Integer pageSize,
                                        @ApiParam(name = "userId", value = "用户id") @RequestParam(required = false) Integer userId,
                                        @ApiParam(name = "status", value = "订单状态",required = true) @RequestParam Integer status) throws BusinessException {
        PageInfo<OrderModel> orderModelPageInfo = orderService.findCurrentOrderByUserId(userId, status,pageNo,pageSize);
        return CommonReturnType.createSuccess(orderModelPageInfo);
    }

    @ApiOperation(value = "确认收货")
    @PostMapping(value = "/confirmOrder")
    public CommonReturnType confirmOrder(@ApiParam(name = "userId", value = "用户id",required = true) @RequestParam Integer userId,
                                        @ApiParam(name = "orderNo", value = "订单号",required = true) @RequestParam String orderNo) throws BusinessException {
        orderService.confirmOrder(userId, orderNo);
        return CommonReturnType.createSuccess(null);
    }

    @ApiOperation(value = "删除订单")
    @PostMapping(value = "/deleteOrder")
    public CommonReturnType deleteOrder(@ApiParam(name = "userId", value = "用户id") @RequestParam(required = false) Integer userId,
                                         @ApiParam(name = "orderNo", value = "订单号",required = true) @RequestParam String orderNo) throws BusinessException {
        orderService.deleteOrder(userId, orderNo);
        return CommonReturnType.createSuccess(null);
    }

    @ApiOperation(value = "发货")
    @PostMapping(value = "/sendOrder")
    public CommonReturnType sendOrder(@ApiParam(name = "orderNo",required = true) @RequestParam String orderNo) throws BusinessException {
        orderService.sendOrder(orderNo);
        return CommonReturnType.createSuccess(null);
    }

    @ApiOperation(value = "根据不同条件查询订单")
    @PostMapping(value = "/findOrderByCondition")
    public CommonReturnType findOrderByCondition(@ApiParam(name = "pageNo", value = "页码") @RequestParam(required = false,defaultValue = "1") Integer pageNo,
                                                     @ApiParam(name = "pageSize", value = "每页条数") @RequestParam(required = false,defaultValue = "7") Integer pageSize,
                                                     @ApiParam(name = "adminId", value = "管理员id",required = true) @RequestParam Integer adminId,
                                                     @ApiParam(name = "orderNo", value = "订单编号") @RequestParam(required = false) String orderNo,
                                                     @ApiParam(name = "email", value = "用户邮箱") @RequestParam(required = false) String email,
                                                     @ApiParam(name = "start", value = "开始时间") @RequestParam(required = false) String start,
                                                     @ApiParam(name = "end", value = "结束时间") @RequestParam(required = false) String end,
                                                     @ApiParam(name = "status", value = "订单状态") @RequestParam(required = false)Integer status) throws BusinessException {
        PageInfo<OrderModel> orderModelPageInfo = orderService.findOrderByCondition(adminId, status,orderNo,email,start,end,pageNo,pageSize);
        return CommonReturnType.createSuccess(orderModelPageInfo);
    }

    @ApiOperation(value = "订单统计")
    @PostMapping(value = "/OrderStatistics")
    public CommonReturnType OrderStatistics(@ApiParam(name ="date", value = "统计时间") @RequestParam(required = false) String date )throws BusinessException {
        StatisticsModel statisticsModel = orderService.OrderStatistics(date);
        return CommonReturnType.createSuccess(statisticsModel);
    }
}
