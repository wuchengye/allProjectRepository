package com.jingdong.webmagic.Controller;

import com.jingdong.webmagic.Annotation.PassToken;
import com.jingdong.webmagic.Annotation.UserLoginToken;
import com.jingdong.webmagic.Model.UserEntity;
import com.jingdong.webmagic.RESTful.Result;
import com.jingdong.webmagic.RESTful.ResultCode;
import com.jingdong.webmagic.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/User")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    @PassToken
    @ResponseBody
    public Result login(UserEntity userEntity){
        UserEntity user = userService.findUserByName(userEntity.getUserName());
        if(user == null){
            return Result.failure(ResultCode.USER_NOT_EXIST);
        }
        if(!user.getPassWord().equals(userEntity.getPassWord())){
            return Result.failure(ResultCode.USER_PASSWORD_ERROR);
        }
        String token = userService.getToken(user);
        user.setPassWord("");
        return Result.success(token,user);
    }

    @PostMapping("/register")
    @PassToken
    @ResponseBody
    public Result register(UserEntity userEntity){
        List<UserEntity> list = userService.repeatUserByName(userEntity.getUserName());
        if(list.size() > 0){
            return Result.failure(ResultCode.USER_EXIST);
        }
        UserEntity u = userService.insertUser(userEntity);
        if(u.getUserId() == null){
            return Result.failure();
        }
        return Result.success();
    }

    @PostMapping("/getRecord")
    @UserLoginToken
    @ResponseBody
    public Result getRecord(Long userId){
        UserEntity userEntity = userService.findUserById(userId);
        if(userEntity == null){
            return Result.failure();
        }
        return Result.success(userEntity.getRecord());
    }


    @PostMapping("/setRecord")
    @UserLoginToken
    @ResponseBody
    public Result setRecord(UserEntity userEntity){
        UserEntity u = userService.findUserById(userEntity.getUserId());
        u.setRecord(userEntity.getRecord());
        userService.insertUser(u);
        return Result.success();
    }
}
