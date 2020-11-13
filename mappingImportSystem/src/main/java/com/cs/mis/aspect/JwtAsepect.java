package com.cs.mis.aspect;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.cs.mis.entity.UserEntity;
import com.cs.mis.restful.Result;
import com.cs.mis.service.UserService;
import com.cs.mis.utils.RedisUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wcy
 */
@Aspect
@Component
@Order(1)
public class JwtAsepect {

    public static final String LOGIN_METHOD_NAME = "login";

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private UserService userService;

    /**
     * 定义切点:扫描controller包下所有类中方法，但排除加了PassToken注解的方法.
     */
    @Pointcut("execution(* com.cs.mis.controller.*.*(..)) && !@annotation(com.cs.mis.annotation.PassToken)")
    public void jwt(){}

    @Around("jwt()")
    public Result jwtCheck(ProceedingJoinPoint point) throws Throwable {

        //判断是否是登录接口
        String methodName = point.getSignature().getName();
        if(LOGIN_METHOD_NAME.equals(methodName)){
            Result loginResult = (Result) point.proceed();
            if (loginResult.getRespCode().equals(Result.FAILURE_RESPCODE)){
                return loginResult;
            }else {
                UserEntity userEntity = (UserEntity) loginResult.getObject();
                String token= JWT.create().withAudience(userEntity.getUserAccount(),String.valueOf(userEntity.getUserType()),String.valueOf(System.currentTimeMillis()))
                        .sign(Algorithm.HMAC256(userEntity.getUserPwd()));
                //存入缓存
                redisUtil.set(userEntity.getUserAccount(),token,1800L);
                Map map = new HashMap();
                map.put("userName",userEntity.getUserName());
                map.put("userType",userEntity.getUserType());
                map.put("token",token);
                return Result.success(map);
            }
        }else {
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = requestAttributes.getRequest();
            String token = request.getHeader("token");
            if(token == null){
                return Result.failure("403","无token");
            }
            String userAccount;
            try {
                userAccount = JWT.decode(token).getAudience().get(0);
            } catch (JWTDecodeException j) {
                return Result.failure("403","无效token");
            }
            UserEntity userEntity = userService.getUserByAccount(userAccount);
            if(userEntity == null || userEntity.getUserStatus() == UserEntity.USERSTATUS_INVALID){
                return Result.failure("403","用户不存在");
            }
            // 验证 token
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(userEntity.getUserPwd())).build();
            try {
                jwtVerifier.verify(token);
            } catch (JWTVerificationException e) {
                return Result.failure("403","无效token");
            }
            String redisToken = redisUtil.get(userAccount);
            if(redisToken == null || !redisToken.equals(token)){
                return Result.failure("403","token过期请重新登录");
            }
            redisUtil.expire(userAccount,1800L);
            return (Result) point.proceed();
        }
    }


}
