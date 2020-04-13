package com.zq.controller;

import com.github.pagehelper.PageInfo;
import com.zq.error.BusinessException;
import com.zq.response.CommonReturnType;
import com.zq.service.impl.CartServiceImpl;
import com.zq.service.model.CartModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * Created by 86132 on 2020/01/31.
 */
@Api(value = "购物车模块", description = "购物车模块")
@RestController
@RequestMapping("cart")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
public class CartController extends BaseController {

    @Autowired
    private CartServiceImpl cartService;

    @ApiOperation(value = "添加购物车")
    @PostMapping(value = "/createCart")
    public CommonReturnType createCart(@ApiParam(name = "petId", value = "宠物id", required = true) @RequestParam Integer petId,
                                       @ApiParam(name = "amount", value = "数量", required = true) @RequestParam Integer amount,
                                       @ApiParam(name = "userId", value = "用户id") @RequestParam(required = false) Integer userId) throws BusinessException {
        cartService.createCart(userId,petId,amount);
        return CommonReturnType.createSuccess(null);
    }

    @ApiOperation(value = "根据用户id查询购物车")
    @PostMapping(value = "/findCartByUserId")
    public CommonReturnType findCartListByUserId(@ApiParam(name = "pageNo", value = "页码") @RequestParam(required = false,defaultValue = "1") Integer pageNo,
                                                 @ApiParam(name = "pageSize", value = "每页条数") @RequestParam(required = false,defaultValue = "10") Integer pageSize,
                                                 @ApiParam(name = "userId", value = "用户id", required = true) @RequestParam Integer userId) throws BusinessException, IOException {
        PageInfo<CartModel> cartModelPageInfo = cartService.findCartListByUserId(userId, pageNo, pageSize);
        return CommonReturnType.createSuccess(cartModelPageInfo);
    }

    @ApiOperation(value = "根据cartId查询购物车")
    @PostMapping(value = "/findCartByCartId")
    public CommonReturnType findCartByCartId(@ApiParam(name = "cartId", value = "购物车id", required = true) @RequestParam Integer cartId) throws BusinessException {
        CartModel cartModel = cartService.findCartByCartId(cartId);
        return CommonReturnType.createSuccess(cartModel);
    }

    @ApiOperation(value = "修改购物车数量")
    @PostMapping(value = "/updateCartAmount")
    public CommonReturnType updateCartAmount(@ApiParam(name = "userId", value = "用户id",required = true) @RequestParam Integer userId,
                                             @ApiParam(name = "cartId", value = "购物车id", required = true) @RequestParam Integer cartId,
                                             @ApiParam(name = "amount", value = "数量", required = true) @RequestParam Integer amount) throws BusinessException {
        cartService.updateCartAmount(userId,cartId,amount);
        return CommonReturnType.createSuccess(null);
    }

    @ApiOperation(value = "删除购物车")
    @PostMapping(value = "/deleteCart")
    public CommonReturnType deleteCart(@ApiParam(name = "userId", value = "用户id",required = true) @RequestParam Integer userId,
                                       @ApiParam(name = "cartId", value = "购物车id", required = true) @RequestParam Integer cartId) throws BusinessException {
        cartService.deleteCart(userId,cartId);
        return CommonReturnType.createSuccess(null);
    }
}
