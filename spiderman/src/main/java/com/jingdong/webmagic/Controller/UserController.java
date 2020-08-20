package com.jingdong.webmagic.Controller;

import com.jingdong.webmagic.Annotation.PassToken;
import com.jingdong.webmagic.Annotation.UserLoginToken;
import com.jingdong.webmagic.Model.UserEntity;
import com.jingdong.webmagic.RESTful.Result;
import com.jingdong.webmagic.RESTful.ResultCode;
import com.jingdong.webmagic.Service.UserService;
import com.jingdong.webmagic.Utils.CodeUtil;
import com.jingdong.webmagic.Utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/User")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RedisUtil redisUtil;

    @PostMapping("/login")
    @PassToken
    @ResponseBody
    public Result login(UserEntity userEntity,String codeKey,String code){
        if(code == null){
            return Result.failure(ResultCode.CODE_NULL);
        }
        UserEntity user = userService.findUserByName(userEntity.getUserName());
        if(user == null){
            return Result.failure(ResultCode.USER_NOT_EXIST);
        }
        if(!user.getPassWord().equals(userEntity.getPassWord())){
            return Result.failure(ResultCode.USER_PASSWORD_ERROR);
        }
        //取缓存
        String redisCode = redisUtil.get(codeKey);
        if(redisCode == null){
            return Result.failure(ResultCode.CODE_TIMEOUT);
        }
        if(!redisCode.toLowerCase().equals(code.toLowerCase())){
            return Result.failure(ResultCode.USER_PASSWORD_ERROR);
        }
        String token = userService.getToken(user);
        //存入缓存
        redisUtil.set(user.getUserName(),token,600L);
        user.setPassWord("");
        return Result.success(token,user);
    }

    @RequestMapping("/getCode")
    @PassToken
    @ResponseBody
    public Result getCode(){
        Map map = CodeUtil.generateCodeAndPic();
        String code = (String) map.get("code");
        BufferedImage img = (BufferedImage) map.get("codePic");
        //存入缓存
        String codeKey =(String.valueOf(Math.random())) + (String.valueOf(new Date().getTime()));
        redisUtil.set(codeKey,code,300L);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();//io流
        try {
            ImageIO.write(img, "png", baos);//写入流中
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] bytes = baos.toByteArray();//转换成字节
        BASE64Encoder encoder = new BASE64Encoder();
        String base64 = encoder.encodeBuffer(bytes).trim();//转换成base64串
        base64 = base64.replaceAll("\n", "").replaceAll("\r", "");//删除 \r\n
        return Result.success(base64,codeKey);
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
