package com.zq.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.PageInfo;
import com.zq.bean.UserDo;
import com.zq.error.BusinessException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created by 86132 on 2020/01/27.
 */
public interface UserService {

    UserDo login(String email, String password) throws BusinessException, JsonProcessingException;

    void register(UserDo userDo,String emailCode) throws BusinessException;

    void exit(Integer id) throws BusinessException;

    Integer emailCode(String email) throws BusinessException;

    UserDo getCurrentUser(Integer id) throws BusinessException, IOException;

    UserDo getUserById(Integer id) throws BusinessException;

    void updateUser(UserDo userDo,String emailCode,String oldImg) throws BusinessException;

    String isUserLogin(Integer userId) throws BusinessException;

    PageInfo<UserDo> getUserList(Integer pageNo,Integer pageSize);

    UserDo findUserByCondition(Integer adminId, Integer userId, String name, String email) throws BusinessException;
}
