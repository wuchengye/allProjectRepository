package com.cs.mis.aspect;

import com.cs.mis.restful.Result;
import com.cs.mis.restful.UserRequestBody;
import com.cs.mis.utils.RedisUtil;
import com.cs.mis.utils.RsaEncrypt;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.awt.geom.RectangularShape;

/**
 * @author wcy
 */
@Aspect
@Component
@Order(3)
public class RsaAsepect {

    public static final String CHANGE_PWD_METHOD_NAME = "changePwd";

    @Autowired
    private RedisUtil redisUtil;

    @Pointcut("@annotation(com.cs.mis.annotation.RsaSecret)")
    public void rsa(){}

    @Around("rsa()")
    public Result rsaSecret(ProceedingJoinPoint point) throws Throwable {
        Object[] objects = point.getArgs();
        UserRequestBody userRequestBody = (UserRequestBody) objects[0];
        String rsaPri = redisUtil.get(userRequestBody.getRsaKey());
        if(rsaPri == null){
            return Result.failure("RSA公钥过期");
        }
        String userPwd = "";
        String userOldPwd = "";
        try {
            userPwd = RsaEncrypt.decrypt(userRequestBody.getUserPwd(),rsaPri);
        } catch (Exception e) {
            return Result.failure("密码解密错误");
        }
        String methodName = point.getSignature().getName();
        if(CHANGE_PWD_METHOD_NAME.equals(methodName)){
            try {
                userOldPwd = RsaEncrypt.decrypt(userRequestBody.getUserOldPwd(),rsaPri);
            }catch (Exception e){
                return Result.failure("旧密码解密错误");
            }
        }
        redisUtil.del(userRequestBody.getRsaKey());
        userRequestBody.setUserPwd(userPwd);
        userRequestBody.setUserOldPwd(userOldPwd);
        objects[0] = userRequestBody;
        return (Result) point.proceed(objects);
    }
}
