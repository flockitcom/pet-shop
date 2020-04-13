package com.zq.error;

/**
 * Created by 86132 on 2020/01/19.
 */
public interface CommonError {
    int getErrCode();
    String getErrMsg();
    CommonError setErrMsg(String errMsg);
}
