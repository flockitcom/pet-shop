package com.zq.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.PageInfo;
import com.zq.bean.UserDo;
import com.zq.error.BusinessException;
import com.zq.error.EmBusinessError;
import com.zq.response.CommonReturnType;
import com.zq.service.FileService;
import com.zq.service.impl.UserServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created by 86132 on 2020/01/27.
 */
@Api(value = "用户模块", description = "用户模块")
@RestController
@RequestMapping("user")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
public class UserController extends BaseController{

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private FileService fileUploadService;

    @ApiOperation(value = "注册")
    @PostMapping(value = "/register")
    public CommonReturnType register(@ApiParam(name = "name", value = "账号", required = true) @RequestParam String name,
                                  @ApiParam(name = "password", value = "密码", required = true) @RequestParam String password,
                                     @ApiParam(name = "email", value = "邮箱", required = true) @RequestParam String email,
                                     @ApiParam(name = "emailCode", value = "邮箱验证码", required = true) @RequestParam String emailCode) throws BusinessException {
        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(password)|| StringUtils.isEmpty(email)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        UserDo userDo = new UserDo();
        userDo.setName(name);
        userDo.setPassword(password);
        userDo.setEmail(email);
        userService.register(userDo,emailCode);
        return CommonReturnType.createSuccess(null);
    }

    @ApiOperation(value = "邮箱验证码")
    @GetMapping(value = "/emailCode")
    public CommonReturnType emailCode(@ApiParam(name = "email", value = "邮箱", required = true) @RequestParam String email) throws BusinessException {
        Integer code = userService.emailCode(email);
        return CommonReturnType.createSuccess(code);
    }

    @ApiOperation(value = "密码登录")
    @PostMapping(value = "/login")
    public CommonReturnType login(@ApiParam(name = "email", value = "邮箱", required = true) @RequestParam String email,
                                    @ApiParam(name = "password", value = "密码", required = true) @RequestParam String password) throws BusinessException, JsonProcessingException {

        if (StringUtils.isEmpty(email) || StringUtils.isEmpty(password)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        UserDo userDo = userService.login(email, password);
        return CommonReturnType.createSuccess(userDo);
    }

    @ApiOperation(value = "获取当前用户信息")
    @GetMapping(value = "getCurrentUser")
    public CommonReturnType getCurrentUser(@ApiParam(name = "id", value = "用户id", required = true) @RequestParam Integer id) throws IOException, BusinessException {
        UserDo userDo = userService.getCurrentUser(id);
        return CommonReturnType.createSuccess(userDo);
    }

    @ApiOperation(value = "用户退出登录")
    @GetMapping(value = "exit")
    public CommonReturnType exit(@ApiParam(name = "id", value = "用户id", required = true) @RequestParam Integer id) throws BusinessException {
        userService.exit(id);
        return CommonReturnType.createSuccess(null);
    }

    @ApiOperation(value = "根据Id用户信息")
    @GetMapping(value = "getUserById")
    public CommonReturnType getUserById(@ApiParam(name = "id", value = "用户id", required = true) @RequestParam Integer id) throws IOException, BusinessException {
        UserDo userDo = userService.getCurrentUser(id);
        return CommonReturnType.createSuccess(userDo);
    }

    @ApiOperation(value = "更新用户信息")
    @PostMapping(value = "updateUser")
    public CommonReturnType updateUser(@ApiParam UserDo userDo,
                                       @ApiParam(name = "emailCode", value = "邮箱验证码", required = true) @RequestParam String emailCode,
                                       @ApiParam(name = "oldImg", value = "旧头像", required = true) @RequestParam String oldImg) throws BusinessException {
//        String addressStr = new String(userDo.getAddress().getBytes("ISO_8859-1"), "UTF-8");
//        userDo.setAddress(addressStr);
        userService.updateUser(userDo,emailCode,oldImg);
        return CommonReturnType.createSuccess(null);
    }

    @ApiOperation(value = "修改图像")
    @PostMapping(value = "headImg")
    public CommonReturnType headImg(@RequestParam(value = "file") MultipartFile file) throws BusinessException {
        String headImg = fileUploadService.fileUpload(file).get(0);
        return CommonReturnType.createSuccess(headImg);
    }

    @ApiOperation(value = "根据不同条件用户信息")
    @GetMapping(value = "findUserByCondition")
    public CommonReturnType findUserByCondition(@ApiParam(name = "adminId", value = "管理员id",required = true) @RequestParam Integer adminId,
                                                @ApiParam(name = "userId", value = "用户id") @RequestParam(required = false) Integer userId,
                                                @ApiParam(name = "name", value = "用户账号") @RequestParam(required = false) String name,
                                                @ApiParam(name = "email", value = "用户邮箱") @RequestParam(required = false) String email) throws IOException, BusinessException {
        UserDo userDo = userService.findUserByCondition(adminId,userId,name,email);
        return CommonReturnType.createSuccess(userDo);
    }
}
