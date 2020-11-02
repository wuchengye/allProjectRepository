package com.cs.mis.controller;

import com.cs.mis.entity.UserEntity;
import com.cs.mis.restful.Result;
import com.cs.mis.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wcy
 */

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    @ApiOperation(value = "登录接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "account", value = "登录账号", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "pwd", value = "登录密码", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "code", value = "验证码", required = true, paramType = "query", dataType = "String")
    })
    public Result login(String account,String pwd,String code){
        UserEntity userEntity = userService.getUserByAccount(account);
        return Result.success(userEntity);

    }

    @PostMapping("/addUser")
    public Result addUser(){
        return null;
    }

    @PostMapping("/resetPwd")
    public Result resetPwd(){
        return null;
    }

    @PostMapping("/changePwd")
    public Result changePwd(){
        return null;
    }

    @PostMapping("/invalidUser")
    public Result invalidUser(){
        return null;
    }
}
