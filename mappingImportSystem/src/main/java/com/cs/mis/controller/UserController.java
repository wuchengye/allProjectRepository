package com.cs.mis.controller;

import com.cs.mis.annotation.CheckIsManager;
import com.cs.mis.annotation.PassToken;
import com.cs.mis.entity.UserEntity;
import com.cs.mis.restful.Result;
import com.cs.mis.service.UserService;
import com.cs.mis.utils.CodeUtil;
import com.cs.mis.utils.RedisUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Encoder;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wcy
 */

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RedisUtil redisUtil;

    @PostMapping("/login")
    @ApiOperation(value = "登录接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "account", value = "登录账号", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "pwd", value = "登录密码", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "codeKey", value = "验证码的key", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "codeValue", value = "验证码", required = true, paramType = "query",dataType = "String")
    })
    public Result login(String account,String pwd,String codeKey,String codeValue){
        /*String redisCodeValue = redisUtil.get(codeKey);
        if(redisCodeValue == null || !redisCodeValue.toLowerCase().equals(codeValue.toLowerCase())){
            return Result.failure("验证码错误");
        }*/
        UserEntity userEntity = userService.getUserByAccount(account);
        if(userEntity == null || userEntity.getUserStatus() == UserEntity.USERSTATUS_INVALID){
            return Result.failure("用户不存在");
        }
        if(!userEntity.getUserPwd().equals(pwd)){
            return Result.failure("密码错误");
        }
        return Result.success(userEntity);
    }

    @PostMapping("/addUser")
    @CheckIsManager
    public Result addUser(){
        return null;
    }

    @PostMapping("/resetPwd")
    @CheckIsManager
    public Result resetPwd(){
        return null;
    }

    @PostMapping("/changePwd")
    public Result changePwd(){
        return null;
    }

    @PostMapping("/invalidUser")
    @CheckIsManager
    public Result invalidUser(){
        return null;
    }

    @PostMapping("/getCode")
    @PassToken
    public Result getCode(){
        Map map = CodeUtil.generateCodeAndPic();
        String code = (String) map.get("code");
        BufferedImage img = (BufferedImage) map.get("codePic");
        //存入缓存
        String codeKey =(String.valueOf(Math.random())) + (String.valueOf(System.currentTimeMillis()));
        redisUtil.set(codeKey,code,300L);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(img, "png", baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] bytes = baos.toByteArray();
        BASE64Encoder encoder = new BASE64Encoder();
        String base64 = encoder.encodeBuffer(bytes).trim();
        base64 = base64.replaceAll("\n", "").replaceAll("\r", "");
        Map resultMap = new HashMap();
        resultMap.put("codeKey",codeKey);
        resultMap.put("codePic",base64);
        return Result.success(resultMap);
    }
}
