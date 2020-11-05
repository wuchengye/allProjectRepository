package com.cs.mis.controller;

import com.auth0.jwt.JWT;
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
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import sun.misc.BASE64Encoder;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
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
    @RsaSecret
    @ApiOperation(value = "登录接口", notes = "userOldPwd字段不传；返回用户名、用户类型、token")
    public Result login(@RequestBody UserRequestBody userRequestBody){
        String redisCodeValue = redisUtil.get(userRequestBody.getCodeKey());
        if(redisCodeValue == null || !redisCodeValue.toLowerCase().equals(userRequestBody.getCodeValue().toLowerCase())){
            return Result.failure("验证码错误");
        }
        UserEntity userEntity = userService.getUserByAccount(userRequestBody.getUserAccount());
        if(userEntity == null || userEntity.getUserStatus() == UserEntity.USERSTATUS_INVALID){
            return Result.failure("用户不存在");
        }
        if(!userEntity.getUserPwd().equals(userRequestBody.getUserPwd())){
            return Result.failure("密码错误");
        }
        return Result.success(userEntity);
    }

    @PostMapping("/getUsers")
    @CheckIsManager
    @ApiOperation(value = "获取用户列表接口", notes = "需管理员权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userAccount", value = "账号", required = false, dataType = "string"),
            @ApiImplicitParam(name = "pageNum", value = "开始页数", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize", value = "页面大小", required = true, dataType = "Integer")
    })
    public Result getUsers(String userAccount, int pageNum, int pageSize){
        String account = StringUtil.isNullOrEmpty(userAccount) ? null : userAccount;
        return Result.success(
                userService.getUsersByTypeAndAccount(UserEntity.USERTYPE_COMMON,account,pageNum,pageSize)
        );
    }

    @PostMapping("/addUser")
    @CheckIsManager
    @ApiOperation(value = "添加用户接口" ,notes = "需管理员权限")
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
    @ApiOperation(value = "重置密码接口" , notes = "需管理员权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userAccount", value = "账号", required = true)
    })
    public Result resetPwd(String userAccount){
        int result = userService.updatePwdByAccount(userAccount,UserEntity.DEFAULT_PWD);
        return result > 0 ? Result.success() : Result.failure();
    }

    @PostMapping("/changePwd")
    @RsaSecret
    @ApiOperation(value = "修改密码接口", notes = "传userPwd和userOldPwd字段")
    public Result changePwd(@RequestBody UserRequestBody userRequestBody){
        //获取请求头中的token
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String token = request.getHeader("token");
        String userAccount = JWT.decode(token).getAudience().get(0);
        UserEntity u = userService.getUserByAccount(userAccount);
        if(u == null || !u.getUserPwd().equals(userRequestBody.getUserOldPwd())){
            return Result.failure("密码修改失败：无效账号或密码错误");
        }
        int result = userService.updatePwdByAccount(userAccount,userRequestBody.getUserPwd());
        return result > 0 ? Result.success() : Result.failure();
    }

    @PostMapping("/invalidUser")
    @CheckIsManager
    @ApiOperation(value = "恢复或舍弃用户接口", notes = "需管理员权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userAccount", value = "账号", required = true),
            @ApiImplicitParam(name = "isValid", value = "账号状态：有效为true", required = true, dataType = "boolean")
    })
    public Result invalidUser(String userAccount, boolean isValid){
        UserEntity u = userService.getUserByAccount(userAccount);
        Boolean flag = UserEntity.USERSTATUS_VALID == u.getUserStatus() ? true : false;
        if(flag.equals(isValid)){
            return Result.success();
        }
        int newStatus = isValid ? UserEntity.USERSTATUS_VALID : UserEntity.USERSTATUS_INVALID;
        int result = userService.updateStatusByAccount(userAccount,newStatus);
        return result > 0 ? Result.success() : Result.failure();
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
    @ApiOperation(value = "获取密码加密随机公钥" , notes = "时效5分钟，有效次数1次")
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
