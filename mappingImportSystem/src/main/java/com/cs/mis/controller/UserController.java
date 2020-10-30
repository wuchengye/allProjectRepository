package com.cs.mis.controller;

import com.cs.mis.restful.Result;
import com.cs.mis.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wcy
 */
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public Result login(){

    }

    @PostMapping
    public Result addUser(){

    }

    @PostMapping
    public Result resetPwd(){

    }

    @PostMapping
    public Result changePwd(){

    }

    @PostMapping
    public Result invalidUser(){

    }
}
