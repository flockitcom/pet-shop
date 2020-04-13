package com.zq.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.PageInfo;
import com.zq.bean.AdminDo;
import com.zq.bean.UserDo;
import com.zq.error.BusinessException;
import com.zq.error.EmBusinessError;
import com.zq.response.CommonReturnType;
import com.zq.service.impl.AdminServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * Created by 86132 on 2020/01/19.
 */
@Api(value = "管理员模块", description = "管理员模块")
@RestController
@RequestMapping("admin")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
public class AdminController extends BaseController {

    @Autowired
    private AdminServiceImpl adminService;

    @ApiOperation(value = "管理员登录")
    @PostMapping(value = "/login")
    public CommonReturnType login(@ApiParam(name = "name", value = "账号", required = true) @RequestParam String name,
                                  @ApiParam(name = "password", value = "密码", required = true) @RequestParam String password) throws BusinessException, JsonProcessingException {
        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(password)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        AdminDo adminDo = adminService.login(name, password);
        return CommonReturnType.createSuccess(adminDo);
    }

    @ApiOperation(value = "获取当前管理员信息")
    @PostMapping(value = "getCurrentAdmin")
    public CommonReturnType getCurrentAdmin(@ApiParam(name = "id", value = "管理员id", required = true) @RequestParam Integer id) throws IOException, BusinessException {
        AdminDo adminDo = adminService.getCurrentAdmin(id);
        return CommonReturnType.createSuccess(adminDo);
    }

    @ApiOperation(value = "管理员退出登录")
    @GetMapping(value = "exit")
    public CommonReturnType exit(@ApiParam(name = "id", value = "管理员id", required = true) @RequestParam Integer id) throws BusinessException {
        adminService.exit(id);
        return CommonReturnType.createSuccess(null);
    }

    @ApiOperation(value = "修改密码")
    @PostMapping(value = "updatePassword")
    public CommonReturnType updatePassword(@ApiParam(name = "id", value = "id", required = true) @RequestParam Integer id,
                                           @ApiParam(name = "password", value = "密码", required = true) @RequestParam String password,
                                           @ApiParam(name = "newPassword", value = "新密码", required = true) @RequestParam String newPassword) throws BusinessException {
        if (id == null || StringUtils.isEmpty(password) || StringUtils.isEmpty(newPassword)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        adminService.updatePassword(id, password, newPassword);
        return CommonReturnType.createSuccess(null);
    }

    @ApiOperation(value = "修改管理员信息")
    @PostMapping(value = "updateAdmin")
    public CommonReturnType updateAdmin(@ApiParam(name = "currentId", value = "当前管理员", required = true) @RequestParam Integer currentId,
                                        @ApiParam(name = "id", value = "id", required = true) @RequestParam Integer id,
                                           @ApiParam(name = "realname", value = "姓名", required = true) @RequestParam String realname,
                                           @ApiParam(name = "name", value = "账号", required = true) @RequestParam String name,
                                        @ApiParam(name = "password", value = "密码", required = true) @RequestParam String password,
                                        @ApiParam(name = "permissions", value = "权限", required = true) @RequestParam Integer permissions) throws BusinessException {
        if (permissions == null || currentId == null || id == null|| StringUtils.isEmpty(password) || StringUtils.isEmpty(realname) || StringUtils.isEmpty(name)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        adminService.updateAdmin(currentId,id,realname,name,password,permissions);
        return CommonReturnType.createSuccess(null);
    }


    @ApiOperation(value = "新增管理员")
    @PostMapping(value = "createAdmin")
    public CommonReturnType createAdmin(@ApiParam(name = "currentId", value = "当前管理员", required = true) @RequestParam Integer currentId,
                                        @ApiParam(name = "realname", value = "姓名", required = true) @RequestParam String realname,
                                        @ApiParam(name = "name", value = "账号", required = true) @RequestParam String name,
                                        @ApiParam(name = "password", value = "密码", required = true) @RequestParam String password,
                                        @ApiParam(name = "permissions", value = "权限", required = true) @RequestParam Integer permissions) throws BusinessException {
        if (currentId == null || permissions == null || StringUtils.isEmpty(realname) || StringUtils.isEmpty(name) || StringUtils.isEmpty(password)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        adminService.createAdmin(currentId,realname,name,password,permissions);
        return CommonReturnType.createSuccess(null);
    }

    @ApiOperation(value = "删除管理员")
    @PostMapping(value = "deleteAdmin")
    public CommonReturnType deleteAdmin(@ApiParam(name = "currentId", value = "当前管理员", required = true) @RequestParam Integer currentId,
                                        @ApiParam(name = "id", value = "id", required = true) @RequestParam Integer id) throws BusinessException {
        if (currentId == null || id == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        adminService.deleteAdmin(currentId,id);
        return CommonReturnType.createSuccess(null);
    }

    @ApiOperation(value = "查询管理员")
    @PostMapping(value = "getAdmin")
    public CommonReturnType getAdmin(@ApiParam(name = "id", value = "管理员id", required = true) @RequestParam Integer id) throws BusinessException {
        AdminDo adminDo = adminService.getAdmin(id);
        return CommonReturnType.createSuccess(adminDo);
    }

    @ApiOperation(value = "获取所有管理员")
    @PostMapping(value = "getAdminList")
    public CommonReturnType getAdminList(@ApiParam(name = "pageNo", value = "页码") @RequestParam(required = false,defaultValue = "1") Integer pageNo,
                                         @ApiParam(name = "pageSize", value = "每页条数") @RequestParam(required = false,defaultValue = "7") Integer pageSize,
                                         @ApiParam(name = "id", value = "管理员id", required = true) @RequestParam Integer id) throws BusinessException {
        PageInfo<AdminDo> adminDoPageInfo = adminService.getAdminList(id, pageNo, pageSize);
        return CommonReturnType.createSuccess(adminDoPageInfo);
    }

    @ApiOperation(value = "获取所有用户")
    @PostMapping(value = "getUserList")
    public CommonReturnType getUserList(@ApiParam(name = "pageNo", value = "页码") @RequestParam(required = false,defaultValue = "1") Integer pageNo,
                                         @ApiParam(name = "pageSize", value = "每页条数") @RequestParam(required = false,defaultValue = "7") Integer pageSize,
                                         @ApiParam(name = "id", value = "管理员id", required = true) @RequestParam Integer id) throws BusinessException {
        PageInfo<UserDo> userDoPageInfo = adminService.getUserList(id, pageNo, pageSize);
        return CommonReturnType.createSuccess(userDoPageInfo);
    }

    @ApiOperation(value = "删除用户")
    @PostMapping(value = "deleteUser")
    public CommonReturnType getUserList(@ApiParam(name = "currentId", value = "管理员id",required = true) @RequestParam Integer currentId,
                                        @ApiParam(name = "uid", value = "用户id", required = true) @RequestParam Integer uid) throws BusinessException {
        adminService.deleteUser(currentId,uid);
        return CommonReturnType.createSuccess(null);
    }

}
