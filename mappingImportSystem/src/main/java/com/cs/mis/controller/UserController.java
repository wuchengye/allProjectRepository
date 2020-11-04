package com.cs.mis.controller;

import com.cs.mis.annotation.CheckIsManager;
import com.cs.mis.annotation.PassToken;
import com.cs.mis.annotation.RsaSecret;
import com.cs.mis.entity.UserEntity;
import com.cs.mis.restful.Result;
import com.cs.mis.restful.UserRequestBody;
import com.cs.mis.service.UserService;
import com.cs.mis.utils.CodeUtil;
import com.cs.mis.utils.RedisUtil;
import com.cs.mis.utils.RsaEncrypt;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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
    //@RsaSecret
    @ApiOperation(value = "登录接口", notes = "userOldPwd字段不传；返回用户名、用户类型、token")
    public Result login(@RequestBody UserRequestBody userRequestBody){
        /*String redisCodeValue = redisUtil.get(userRequestBody.getCodeKey());
        if(redisCodeValue == null || !redisCodeValue.toLowerCase().equals(userRequestBody.getCodeValue().toLowerCase())){
            return Result.failure("验证码错误");
        }*/
        UserEntity userEntity = userService.getUserByAccount(userRequestBody.getUserAccount());
        if(userEntity == null || userEntity.getUserStatus() == UserEntity.USERSTATUS_INVALID){
            return Result.failure("用户不存在");
        }
        if(!userEntity.getUserPwd().equals(userRequestBody.getUserPwd())){
            return Result.failure("密码错误");
        }
        return Result.success(userEntity);
    }

    @PostMapping("/addUser")
    @CheckIsManager
    @ApiOperation(value = "添加用户接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userAccount", value = "账号", required = true),
            @ApiImplicitParam(name = "userName", value = "用户名", required = true)
    })
    public Result addUser(String userAccount,String userName){
        UserEntity u = userService.getUserByAccount(userAccount);
        if(u != null){
            return Result.failure("账号已存在");
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setUserAccount(userAccount);
        userEntity.setUserName(userName);
        userEntity.setUserPwd(UserEntity.DEFAULT_PWD);
        userEntity.setUserType(UserEntity.USERTYPE_COMMON);
        userEntity.setUserStatus(UserEntity.USERSTATUS_VALID);
        int result = userService.insertUser(userEntity);
        return result > 0 ? Result.success() : Result.failure();
    }

    @PostMapping("/resetPwd")
    @CheckIsManager
    public Result resetPwd(){
        return null;
    }

    @PostMapping("/changePwd")
    @RsaSecret
    @ApiOperation(value = "修改密码接口", notes = "传userPwd和userOldPwd字段")
    public Result changePwd(@RequestBody UserRequestBody userRequestBody){
        return null;
    }

    @PostMapping("/invalidUser")
    @CheckIsManager
    public Result invalidUser(){
        return null;
    }

    @GetMapping("/getCode")
    @PassToken
    @ApiOperation(value = "获取验证码接口", notes = "时效5分钟")
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

    @GetMapping("/getCipher")
    @PassToken
    @ApiOperation(value = "获取密码加密随机公钥" , notes = "时效5分钟")
    public Result getCipher(){
        try {
            Map map = RsaEncrypt.genKeyPair();
            //生成私钥的随机key，存入缓存
            String priKey = "pri" + (String.valueOf(Math.random())) + (String.valueOf(System.currentTimeMillis()));
            redisUtil.set(priKey, (String) map.get("rsaPri"),300L);
            map.remove("rsaPri");
            map.put("rsaKey",priKey);
            return Result.success(map);
        } catch (Exception e) {
            return Result.failure("公钥获取失败");
        }
    }
}
