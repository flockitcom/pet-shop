package com.zq.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zq.bean.OrderDo;
import com.zq.bean.PetDo;
import com.zq.bean.SequenceDo;
import com.zq.bean.UserDo;
import com.zq.dao.OrderDoMapper;
import com.zq.dao.PetDoMapper;
import com.zq.dao.SequenceDoMapper;
import com.zq.dao.UserDoMapper;
import com.zq.error.BusinessException;
import com.zq.error.EmBusinessError;
import com.zq.service.OrderService;
import com.zq.service.model.OrderModel;
import com.zq.service.model.StatisticsModel;
import com.zq.utlis.OrderUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 86132 on 2020/02/01.
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDoMapper orderDoMapper;

    @Autowired
    private PetDoMapper petDoMapper;

    @Autowired
    private UserDoMapper userDoMapper;

    @Autowired
    private PetServiceImpl petService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private AdminServiceImpl adminService;

    @Autowired
    private SequenceDoMapper sequenceDoMapper;


    @Override
    @Transactional
    public OrderModel createOrder(Integer userId, Integer petId, Integer amount) throws BusinessException {
        //1.参数校验
        if (userId == null) {
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN);
        }
        userService.isUserLogin(userId);
        if (petId == null || amount == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        PetDo petDo = petDoMapper.selectByPrimaryKey(petId);
        if (petDo == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "商品信息不存在");
        }
        if (amount <= 0 || amount > 99) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "商品数量信息不合法");
        }
        //2.落单减库存
        boolean result = petService.decreaseStock(petId, amount);
        if (!result) {
            throw new BusinessException(EmBusinessError.STOCK_NOT_ENOUGH);
        }

        //3.订单入库
        OrderModel orderModel = new OrderModel();
        orderModel.setUserId(userId);
        orderModel.setPetId(petId);
        orderModel.setAmount(amount);
        orderModel.setPetPrice(petDo.getPrice());
        orderModel.setOrderPrice(OrderUtil.totalPrice(petDo.getPrice(), amount));
        orderModel.setStatus(2);
        orderModel.setPetDo(petDo);
        //生成订单号
        orderModel.setOrderNo(generateOrderNo());
        OrderDo orderDo = convertOrderDoFormOrderModel(orderModel);
        orderDoMapper.insertSelective(orderDo);

        //4.加上商品销量
        petService.increaseSales(petId, amount);

        return orderModel;


    }

    @Override
    public PageInfo<OrderModel> findCurrentOrderByUserId(Integer userId, Integer status, Integer pageNo, Integer pageSize) throws BusinessException {
        if (userId == null) {
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN);
        }
        userService.isUserLogin(userId);
        PageHelper.startPage(pageNo, pageSize);
        List<OrderDo> orderDoList = orderDoMapper.selectByUserIdAndStatus(userId, status);
        return setPageInfoOrderModelFormOrderDo(orderDoList, false);
    }

    @Override
    @Transactional
    public void confirmOrder(Integer userId, String orderNo) throws BusinessException {
        if (userId == null) {
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN);
        }
        userService.isUserLogin(userId);
        int row = orderDoMapper.confirmOrder(orderNo);
        if (row == 0) {
            throw new BusinessException(EmBusinessError.ORDERNO_ERROR);
        }
    }

    @Override
    @Transactional
    public void deleteOrder(Integer userId, String orderNo) throws BusinessException {
        int row = orderDoMapper.deleteByOrderNo(orderNo);
        if (row == 0) {
            throw new BusinessException(EmBusinessError.ORDERNO_ERROR);
        }
    }

    @Override
    public PageInfo<OrderModel> findOrderByCondition(Integer adminId, Integer status, String orderNo, String email, String start, String end, Integer pageNo, Integer pageSize) throws BusinessException {
        if (adminId == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        //判断管理员是否登录
        adminService.isAdminLogin(adminId);
        UserDo userDo = null;
        if (!StringUtils.isEmpty(email)){
            userDo = userDoMapper.selectByEmail(email);
            if (null == userDo) {
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
            }
        }
        PageHelper.startPage(pageNo, pageSize);
        List<OrderDo> orderDoList;
        //1.根据订单号查询
        if (!StringUtils.isEmpty(orderNo)) {
            orderDoList = orderDoMapper.selectOrderByOrderNo(orderNo);
            //2.根据邮箱查询
        } else if (!StringUtils.isEmpty(email)) {
            orderDoList = orderDoMapper.selectByUserIdAndStatus(userDo.getId(), status);
            //3.根据日期查询
        } else if (!StringUtils.isEmpty(start) || !StringUtils.isEmpty(end)) {
            if (!StringUtils.isEmpty(start) && StringUtils.isEmpty(end)) {
                orderDoList = orderDoMapper.selectOrderByStart(start, status);
            } else if (StringUtils.isEmpty(start) && !StringUtils.isEmpty(end)) {
                orderDoList = orderDoMapper.selectOrderByEnd(end, status);
            } else {
                orderDoList = orderDoMapper.selectOrderByDate(start, end, status);
            }
        } else {
            orderDoList = orderDoMapper.selectOrderByStatus(status);
        }
        return setPageInfoOrderModelFormOrderDo(orderDoList, true);
    }

    @Override
    @Transactional
    public void sendOrder(String orderNo) throws BusinessException {
        int row = orderDoMapper.sendOrder(orderNo);
        if (row == 0) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
    }

    @Override
    public StatisticsModel OrderStatistics(String date) {
        List<OrderDo> noSendList = orderDoMapper.selectOrderByStart(date, 2);
        List<OrderDo> noReceiptList = orderDoMapper.selectOrderByStart(date, 3);
        List<OrderDo> successList = orderDoMapper.selectOrderByStart(date, 5);
        StatisticsModel statisticsModel = new StatisticsModel();
        statisticsModel.setNoSendOrder(noSendList.size());
        statisticsModel.setNoReceiptOrder(noReceiptList.size());
        statisticsModel.setSuccessOrder(successList.size());
        statisticsModel.setNoSendPrice(OrderPrice(noSendList));
        statisticsModel.setNoReceiptPricer(OrderPrice(noReceiptList));
        statisticsModel.setSuccessPricer(OrderPrice(successList));
        return statisticsModel;
    }

    /**
     * @return: 生成订单号
     */
    private String generateOrderNo() {
        //订单号有16位
        StringBuilder stringBuilder = new StringBuilder();
        //前8位为时间信息,年月日
        LocalDateTime now = LocalDateTime.now();
        String nowDate = now.format(DateTimeFormatter.ISO_DATE).replace("-", "");
        stringBuilder.append(nowDate);
        //中间6位为自增序列
        int sequence = 0;
        SequenceDo sequenceDo = sequenceDoMapper.getSequenceByName("order_info");
        sequence = sequenceDo.getCurrentValue();
        sequenceDo.setCurrentValue(sequenceDo.getCurrentValue() + sequenceDo.getStep());
        sequenceDoMapper.updateByPrimaryKeySelective(sequenceDo);
        String sequenceStr = String.valueOf(sequence);
        for (int i = 0; i < 6 - sequenceStr.length(); i++) {
            stringBuilder.append(0);
        }
        stringBuilder.append(sequenceStr);
        //最后2位为分库分表位
        stringBuilder.append("00");
        return stringBuilder.toString();
    }

    private OrderDo convertOrderDoFormOrderModel(OrderModel orderModel) {
        if (orderModel == null) {
            return null;
        }
        OrderDo orderDo = new OrderDo();
        BeanUtils.copyProperties(orderModel, orderDo);
        return orderDo;
    }

    private OrderModel convertOrderModelFormOrderDo(OrderDo orderDo) {
        if (orderDo == null) {
            return null;
        }
        OrderModel orderModel = new OrderModel();
        BeanUtils.copyProperties(orderDo, orderModel);
        return orderModel;
    }

    /**
     * 更改pageInfo的list (orderDO --> orderMOdel)
     *
     * @param isUser 是否查询userDo
     * @return:
     */
    private PageInfo<OrderModel> setPageInfoOrderModelFormOrderDo(List<OrderDo> orderDoList, Boolean isUser) {
        PageInfo orderPageInfo = new PageInfo(orderDoList);
        List<OrderModel> orderModelList = new ArrayList<>();
        for (OrderDo orderDo : orderDoList) {
            OrderModel orderModel = convertOrderModelFormOrderDo(orderDo);
            PetDo petDo = petDoMapper.selectByPrimaryKey(orderDo.getPetId());
            orderModel.setPetDo(petDo);
            if (isUser) {
                UserDo userDo = userDoMapper.selectByPrimaryKey(orderDo.getUserId());
                orderModel.setUserDo(userDo);
            }
            orderModel.setTimeStr(OrderUtil.formatDate(orderDo.getTime()));
            orderModelList.add(orderModel);
        }
        orderPageInfo.setList(orderModelList);
        return orderPageInfo;
    }

    private double OrderPrice(List<OrderDo> orderDoList) {
        BigDecimal priceBig = BigDecimal.ZERO;
        for (OrderDo orderDo : orderDoList) {
            priceBig = priceBig.add(new BigDecimal(orderDo.getOrderPrice()));
        }
        String totalpriceStr = String.format("%.2f", priceBig.doubleValue());
        return Double.valueOf(totalpriceStr);
    }

}
