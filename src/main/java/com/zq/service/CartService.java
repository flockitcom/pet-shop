package com.zq.service;

import com.github.pagehelper.PageInfo;
import com.zq.error.BusinessException;
import com.zq.service.model.CartModel;

import java.io.IOException;
import java.util.List;

/**
 * Created by 86132 on 2020/01/31.
 */
public interface CartService {

    void createCart(Integer userId,Integer petId,Integer amount) throws BusinessException;

    PageInfo<CartModel> findCartListByUserId(Integer userId,Integer pageNo,Integer pageSize) throws BusinessException, IOException;

    CartModel findCartByCartId(Integer cartId);

    void updateCartAmount(Integer userId, Integer cartId, Integer amount) throws BusinessException;

    void deleteCart(Integer userId, Integer cartId) throws BusinessException;

}
