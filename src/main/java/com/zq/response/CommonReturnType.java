package com.zq.response;

/**
 * Created by 86132 on 2020/01/19.
 */
public class CommonReturnType {
    //表明对应请求的返回结果"success"或"fail"
    private String status;
    private Object data;

    //定义通用的创建方法
    public static CommonReturnType createSuccess(Object result) {
        return create("success", result);
    }

    public static CommonReturnType createFail(Object result) {
        return create("fail", result);
    }

    public static CommonReturnType create(String status, Object result) {
        CommonReturnType type = new CommonReturnType();
        type.status = status;
        type.data = result;
        return type;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
