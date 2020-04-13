package com.zq.controller;

import com.zq.error.BusinessException;
import com.zq.error.EmBusinessError;
import com.zq.response.CommonReturnType;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 86132 on 2020/01/19.
 */

public class BaseController {

    public static final String CONTENT_TYPE_FORMED="application/x-www-form-urlencoded";
    public static final String CONTENT_TYPE_JSON="application/json;charset=UTF-8";

    //定义exceptionhandler解决未被controller层吸收的exception
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public Object handlerException(HttpServletRequest request , Exception ex){
        Map<String,Object> responseData = new HashMap<>();
        if (ex instanceof BusinessException){
            BusinessException businessException = (BusinessException)ex;
            responseData.put("errCode",businessException.getErrCode());
            responseData.put("errMsg",businessException.getErrMsg());
        }else {
            responseData.put("errCode", EmBusinessError.UNKONW_ERROR.getErrCode());
            responseData.put("errMsg",EmBusinessError.UNKONW_ERROR.getErrMsg());
        }
        return CommonReturnType.createFail(responseData);
    }
}