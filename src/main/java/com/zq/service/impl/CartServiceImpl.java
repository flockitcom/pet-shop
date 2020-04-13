package com.zq.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zq.bean.CartDo;
import com.zq.bean.PetDo;
import com.zq.dao.CartDoMapper;
import com.zq.dao.PetDoMapper;
import com.zq.error.BusinessException;
import com.zq.error.EmBusinessError;
import com.zq.service.CartService;
import com.zq.service.model.CartModel;
import com.zq.utlis.OrderUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 86132 on 2020/01/31.
 */
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartDoMapper cartDoMapper;

    @Autowired
    private PetDoMapper petDoMapper;

    @Autowired
    private UserServiceImpl userService;

    @Override
    @Transactional
    public void createCart(Integer userId, Integer petId, Integer amount) throws BusinessException {
        if (userId == null){
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN);
        }
        userService.isUserLogin(userId);
        CartDo cartDo = cartDoMapper.selectByUserIdAndPetId(userId, petId);
        if (cartDo==null){
            cartDo = new CartDo();
            cartDo.setUserId(userId);
            cartDo.setPetId(petId);
            cartDo.setAmount(amount);
            cartDoMapper.insertSelective(cartDo);
        }else {
            //购物车数量相加
            cartDo.setAmount(Integer.valueOf(cartDo.getAmount().intValue() + amount.intValue()));
            cartDoMapper.updateByPrimaryKeySelective(cartDo);
        }
    }

    @Override
    public PageInfo<CartModel> findCartListByUserId(Integer userId,Integer pageNo,Integer pageSize) throws BusinessException, IOException {
        if (userId == null){
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN);
        }
        userService.isUserLogin(userId);
        PageHelper.startPage(pageNo,pageSize);
        List<CartDo> cartDoList = cartDoMapper.selectListByUserId(userId);
        PageInfo cartPageInfo = new PageInfo(cartDoList);
        List<CartModel> cartModelList = new ArrayList<>();
        CartModel cartModel ;
        for (CartDo cartDo : cartDoList) {
            PetDo petDo = petDoMapper.selectByPrimaryKey(cartDo.getPetId());
            cartModel = convertCartModelFromCartDo(cartDo);
            cartModel.setPetDo(petDo);
            cartModel.setTotalPrice(OrderUtil.totalPrice(petDo.getPrice(),cartDo.getAmount()));
            cartModelList.add(cartModel);
        }
        cartPageInfo.setList(cartModelList);
        return cartPageInfo;
    }

    @Override
    public CartModel findCartByCartId(Integer cartId) {
        CartDo cartDo = cartDoMapper.selectByPrimaryKey(cartId);
        PetDo petDo = petDoMapper.selectByPrimaryKey(cartDo.getPetId());
        CartModel cartModel = convertCartModelFromCartDo(cartDo);
        cartModel.setPetDo(petDo);
        cartModel.setTotalPrice(OrderUtil.totalPrice(petDo.getPrice(),cartDo.getAmount()));
        return cartModel;
    }

    @Override
    @Transactional
    public void updateCartAmount(Integer userId, Integer cartId, Integer amount) throws BusinessException {
        if (userId == null){
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN);
        }
        CartDo cartDo = cartDoMapper.selectByPrimaryKey(cartId);
        if (cartDo==null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        cartDo.setAmount(amount);
        cartDoMapper.updateByPrimaryKeySelective(cartDo);
    }

    @Override
    @Transactional
    public void deleteCart(Integer userId, Integer cartId) throws BusinessException {
        if (userId == null){
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN);
        }
        try {
            cartDoMapper.deleteByPrimaryKey(cartId);
        } catch (Exception e) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
    }

    //Do转model
    private CartModel convertCartModelFromCartDo(CartDo cartDo){
        CartModel cartModel = new CartModel();
        if (cartDo == null){
            return null;
        }
        BeanUtils.copyProperties(cartDo,cartModel);
        return cartModel;
    }

}
