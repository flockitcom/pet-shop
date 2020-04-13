package com.zq.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sun.mail.imap.protocol.ID;
import com.zq.bean.UserDo;
import com.zq.dao.UserDoMapper;
import com.zq.error.BusinessException;
import com.zq.error.EmBusinessError;
import com.zq.service.AdminService;
import com.zq.service.FileService;
import com.zq.service.MailService;
import com.zq.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by 86132 on 2020/01/27.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDoMapper userDoMapper;

    @Autowired
    private AdminServiceImpl adminService;

    @Autowired
    private MailService mailService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private FileService fileService;

    @Override
    public UserDo login(String email, String password) throws BusinessException, JsonProcessingException {
        UserDo userDo = userDoMapper.selectByEmail(email);
        if (userDo == null) {
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }
        if (!StringUtils.equals(userDo.getPassword(), password)) {
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }
        ObjectMapper mapper = new ObjectMapper();
        String userDoStr = mapper.writeValueAsString(userDo);
        redisTemplate.opsForValue().set(String.valueOf(userDo.getId()), userDoStr,3,TimeUnit.DAYS);
        return userDo;
    }

    @Override
    @Transactional
    public void register(UserDo userDo,String emailCode) throws BusinessException {
        if (userDo == null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
//        判断验证码是否正确
        if (!StringUtils.equals(emailCode,redisTemplate.opsForValue().get(userDo.getEmail()))){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"验证码不正确");
        }
        try {
            userDoMapper.insertSelective(userDo);
        } catch (Exception e) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"该邮箱已注册");
        }

    }

    @Override
    public void exit(Integer id) throws BusinessException {
        try {
            redisTemplate.delete(String.valueOf(id));
        } catch (Exception e) {
            throw new BusinessException(EmBusinessError.USER_EXIT,"用户尚未登录,无法退出");
        }
    }

    @Override
    public Integer emailCode(String email) throws BusinessException {
        Random random = new Random();
        int code = random.nextInt(9000)+1000;
        try {
            mailService.sendMail(email,code);
//            mailService.sendMail("1253013462@qq.com",code);
        } catch (Exception e) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"请输入正确邮箱");
        }
        UserDo userDo = userDoMapper.selectByEmail(email);
        System.out.println(code);
        redisTemplate.opsForValue().set(email, String.valueOf(code),300, TimeUnit.SECONDS);
        return Integer.valueOf(code);
    }

    @Override
    public UserDo getCurrentUser(Integer id) throws BusinessException, IOException {
        if (id == null) {
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN);
        }
        String userLoginStr = isUserLogin(id);
        ObjectMapper mapper = new ObjectMapper();
        UserDo userDo = mapper.readValue(userLoginStr, UserDo.class);
        return userDo;
    }

    @Override
    public UserDo getUserById(Integer id) throws BusinessException {
        UserDo userDo = userDoMapper.selectByPrimaryKey(id);
        if (userDo == null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        return userDo;
    }

    @Override
    @Transactional
    public void updateUser(UserDo userDo,String emailCode,String oldImg) throws BusinessException {
        if (userDo == null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        if (!StringUtils.equals(emailCode,redisTemplate.opsForValue().get(userDo.getEmail()))){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"验证码不正确");
        }
        userDoMapper.updateByPrimaryKeySelective(userDo);
        if (!userDo.getHeadImg().isEmpty()){
            fileService.deleteFile(oldImg);

        }
    }

    //从redis缓存中查询当前用户的个人信息
    @Override
    public String isUserLogin(Integer userId) throws BusinessException {
        String loginUserStr = redisTemplate.opsForValue().get(String.valueOf(userId));
        if (StringUtils.isEmpty(loginUserStr)) {
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN);
        }
        return loginUserStr;
    }

    @Override
    public PageInfo<UserDo> getUserList(Integer pageNo, Integer pageSize) {
        PageHelper.startPage(pageNo,pageSize);
        List<UserDo> userDoList = userDoMapper.selectAll();
        PageInfo<UserDo> userDoPageInfo = new PageInfo<>(userDoList);
        return userDoPageInfo;
    }

    @Override
    public UserDo findUserByCondition(Integer adminId,Integer userId, String name, String email) throws BusinessException {
        if (adminId == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        //判断管理员是否登录
        adminService.isAdminLogin(adminId);
        UserDo userDo = null;
        //1.根据用户id查询
        if (null != userId) {
            userDo = userDoMapper.selectByPrimaryKey(userId);
            //2.根据邮箱查询
        }else if (!StringUtils.isEmpty(name)) {
            userDo = userDoMapper.selectByName(name);
        }else if (!StringUtils.isEmpty(email)){
            userDo = userDoMapper.selectByEmail(email);
        }
        if (null == userDo){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        return userDo;
    }
}
