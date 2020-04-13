package com.zq.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zq.bean.AdminDo;
import com.zq.bean.UserDo;
import com.zq.dao.AdminDoMapper;
import com.zq.dao.UserDoMapper;
import com.zq.error.BusinessException;
import com.zq.error.EmBusinessError;
import com.zq.service.AdminService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by 86132 on 2020/01/19.
 */
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminDoMapper adminDoMapper;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserDoMapper userDoMapper;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public AdminDo login(String name, String password) throws BusinessException, JsonProcessingException {
        AdminDo adminDo = adminDoMapper.selectByName(name);
        if (adminDo == null) {
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }
        if (!StringUtils.equals(adminDo.getPassword(), password)) {
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }
        ObjectMapper mapper = new ObjectMapper();
        String adminDoStr = mapper.writeValueAsString(adminDo);
        redisTemplate.opsForValue().set("a"+adminDo.getId(),adminDoStr,3, TimeUnit.DAYS);
        return adminDo;
    }

    @Override
    public AdminDo getCurrentAdmin(Integer id) throws BusinessException, IOException {
        if (id == null) {
            throw new BusinessException(EmBusinessError.ADMIN_NOT_LOGIN);
        }
        String adminLoginStr = isAdminLogin(id);
        ObjectMapper mapper = new ObjectMapper();
        AdminDo adminDo = mapper.readValue(adminLoginStr, AdminDo.class);
        return adminDo;
    }

    @Override
    public void exit(Integer id) throws BusinessException {
        try {
            redisTemplate.delete("a"+id);
        } catch (Exception e) {
            throw new BusinessException(EmBusinessError.ADMIN_EXIT,"管理员尚未登录");
        }
    }

    @Override
    @Transactional
    public void updatePassword(Integer id, String password, String newPassword) throws BusinessException {
        AdminDo adminDo = adminDoMapper.selectByIdAndPassword(id, password);
        if (adminDo == null) {
            throw new BusinessException(EmBusinessError.ADMIN_LOGIN_FAIL,"密码错误");
        }
        adminDo.setPassword(newPassword);
        int i = adminDoMapper.updateByPrimaryKey(adminDo);
        if (i == 0) {
            throw new BusinessException(EmBusinessError.UNKONW_ERROR, "修改密码错误");
        }
    }

    /**
     * 超级管理员修改管理员信息
     */
    @Override
    @Transactional
    public void updateAdmin(Integer currentId, Integer id, String realname, String name,String password, Integer permissions) throws BusinessException {
        isSuperAdmin(currentId);
        AdminDo adminDo = adminDoMapper.selectByPrimaryKey(id);
        if (adminDo == null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        adminDo.setRealname(realname);
        adminDo.setName(name);
        adminDo.setPassword(password);
        adminDo.setPermissions(permissions);
        int row = adminDoMapper.updateByPrimaryKeySelective(adminDo);
        if (row == 0){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"修改信息失败");
        }
    }

    /**
     * 超级管理员创建管理员
     * @param currentId 当前管理员
     * @param name     账号
     * @param realname 真名
     * @return:
     */
    @Override
    @Transactional
    public void createAdmin(Integer currentId, String realname, String name, String password, Integer permissions) throws BusinessException {
        isSuperAdmin(currentId);
        AdminDo adminDo = new AdminDo();
        adminDo.setName(name);
        adminDo.setRealname(realname);
        adminDo.setPassword(password);
        adminDo.setPermissions(permissions);
        try {
            adminDoMapper.insertSelective(adminDo);
        } catch (Exception e) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "该管理员已存在");
        }
    }

    @Override
    @Transactional
    public void deleteAdmin(Integer currnetId,Integer id) throws BusinessException {
        isSuperAdmin(currnetId);
        int row = adminDoMapper.deleteByPrimaryKey(id);
        if (row == 0){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"修改信息失败");
        }
    }

    @Override
    public PageInfo<AdminDo> getAdminList(Integer id, Integer pageNo, Integer pageSize) throws BusinessException {
        if (id == null){
            throw new BusinessException(EmBusinessError.ADMIN_NOT_LOGIN);
        }
        isSuperAdmin(id);
        PageHelper.startPage(pageNo,pageSize);
        List<AdminDo> adminDoList = adminDoMapper.selectAll();
        PageInfo<AdminDo> adminDoPageInfo = new PageInfo<>(adminDoList);
        return adminDoPageInfo;
    }

    @Override
    public PageInfo<UserDo> getUserList(Integer id, Integer pageNo, Integer pageSize) throws BusinessException{
        if (id == null){
            throw new BusinessException(EmBusinessError.ADMIN_NOT_LOGIN);
        }
        isSuperAdmin(id);
        PageInfo<UserDo> userList = userService.getUserList(pageNo, pageSize);
        return userList;
    }

    @Override
    public void deleteUser(Integer id,Integer uid) throws BusinessException {
        isAdminLogin(id);
        int row = userDoMapper.deleteByPrimaryKey(uid);
        if (0 == row){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
    }

    @Override
    public AdminDo getAdmin(Integer id) throws BusinessException {
        if (id == null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        AdminDo adminDo = adminDoMapper.selectByPrimaryKey(id);
        return adminDo;
    }

    //判断当前管理员等级是否为超级管理员
    private void isSuperAdmin(Integer id) throws BusinessException{
        String adminLogin = isAdminLogin(id);
        ObjectMapper mapper = new ObjectMapper();
        AdminDo adminDo = null;
        try {
            adminDo = mapper.readValue(adminLogin, AdminDo.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (1 != adminDo.getPermissions().intValue()) {
            throw new BusinessException(EmBusinessError.ADMIN_NOT_PERMISSIONS);
        }
    }

    //从缓存中读取管理员信息
    public String isAdminLogin(Integer adminId) throws BusinessException {
        String adminLoginStr = redisTemplate.opsForValue().get("a" + adminId);
        if (StringUtils.isEmpty(adminLoginStr)) {
            throw new BusinessException(EmBusinessError.ADMIN_NOT_LOGIN);
        }
        return adminLoginStr;
    }

}
