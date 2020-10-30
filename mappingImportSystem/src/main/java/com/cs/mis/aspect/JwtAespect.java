package com.cs.mis.aspect;

import com.cs.mis.restful.Result;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;

/**
 * @author wcy
 *
 */
@Aspect
@Component
public class JwtAespect {

    static final String LOGIN_METHOD_NAME = "login";

    /**
     * @date 2020-10-30 10:33
     * 定义切点
     */
    @Pointcut("execution(* com.cs.mis.controller.*.*(..))")
    public void Jwt(){}

    @Around("Jwt()")
    public Result JwtCheck(ProceedingJoinPoint point){
        //判断是否是登录接口
        String methodName = point.getSignature().getName();
        if(LOGIN_METHOD_NAME.equals(methodName)){

        }else {
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = requestAttributes.getRequest();
        }




    }


}
