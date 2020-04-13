package com.zq.error;

/**
 * Created by 86132 on 2020/01/19.
 * 包装器业务异常类实现
 */
public class BusinessException extends Exception implements CommonError {

    private CommonError commonError;


    //直接接收EmBusinessError的传参用于构造业务异常
    public BusinessException(CommonError commonError) {
        //一定要调用super(),因为exception自身会有初始化机制
        super();
        this.commonError = commonError;
    }

    //接受自定义errMsg的方式构造业务异常
    public BusinessException(CommonError commonError, String errMsg) {
        super();
        this.commonError = commonError;
        this.commonError.setErrMsg(errMsg);
    }

    @Override
    public int getErrCode() {
        return this.commonError.getErrCode();
    }

    @Override
    public String getErrMsg() {
        return this.commonError.getErrMsg();
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        return this.commonError.setErrMsg(errMsg);
    }


}
