package com.zq.error;

/**
 * Created by 86132 on 2020/01/19.
 * 业务异常枚举类
 */
public enum EmBusinessError implements CommonError {

    //通用错误类型10000
    PARAMETER_VALIDATION_ERROR(10001,"参数不合法"),
    UNKONW_ERROR(10002,"未知错误"),

    //20000开头为用户信息相关错误定义
    USER_NOT_EXIST(20001,"用户不存在"),
    USER_LOGIN_FAIL(20002,"用户手机号或密码不正确"),
    USER_NOT_LOGIN(20003,"用户还未登录"),
    USER_EXIT(20004,"用户退出失败"),

    //3000开头为用户信息相关错误定义
    ADMIN_NOT_EXIST(30001,"管理员不存在"),
    ADMIN_LOGIN_FAIL(30002,"管理员账号或密码错误"),
    ADMIN_NOT_LOGIN(30003,"管理员还未登录"),
    ADMIN_NOT_PERMISSIONS(30004,"管理员权限不足"),
    ADMIN_EXIT(30005,"管理员退出失败"),

    //40000开头为交易信息错误定义
    STOCK_NOT_ENOUGH(40001,"库存不足"),
    ORDERNO_ERROR(40002,"订单号错误")
    ;
    private int errCode;
    private String errMsg;

    EmBusinessError(int errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    @Override
    public int getErrCode() {
        return this.errCode;
    }

    @Override
    public String getErrMsg() {
        return this.errMsg;
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.errMsg = errMsg;
        return this;
    }
}
