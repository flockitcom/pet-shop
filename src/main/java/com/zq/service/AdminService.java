package com.zq.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.PageInfo;
import com.zq.bean.AdminDo;
import com.zq.bean.UserDo;
import com.zq.error.BusinessException;

import java.io.IOException;

/**
 * Created by 86132 on 2020/01/19.
 */
public interface AdminService {

    AdminDo login(String name, String password) throws BusinessException, JsonProcessingException;

    AdminDo getCurrentAdmin(Integer id) throws BusinessException, IOException;

    void exit(Integer id) throws BusinessException;

    void updatePassword(Integer id, String password, String newPassword) throws BusinessException;

    void updateAdmin(Integer currentId, Integer id, String realname, String name,String password, Integer permissions) throws BusinessException;

    void createAdmin(Integer currentId, String realname, String name, String password, Integer permissions) throws BusinessException;

    void deleteAdmin(Integer currnetId,Integer id) throws BusinessException;

    AdminDo getAdmin(Integer id) throws BusinessException;

    PageInfo<AdminDo> getAdminList(Integer id,Integer pageNo,Integer pageSize) throws BusinessException;

    PageInfo<UserDo> getUserList(Integer id, Integer pageNo, Integer pageSize) throws BusinessException, IOException;

    void deleteUser(Integer id, Integer uid) throws BusinessException;
}
