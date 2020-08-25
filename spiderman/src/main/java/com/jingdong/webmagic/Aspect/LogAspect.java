package com.jingdong.webmagic.Aspect;

import com.auth0.jwt.JWT;
import com.jingdong.webmagic.Annotation.LogOperator;
import com.jingdong.webmagic.Model.LogEntity;
import com.jingdong.webmagic.Service.LogService;
import com.jingdong.webmagic.Utils.IpUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Aspect
@Component
public class LogAspect {

    @Autowired
    private LogService logService;

    //上下文获取requset
    //ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    //HttpServletRequest request = requestAttributes.getRequest();


    //定义切点
    //@Pointcut("execution(* com.jingdong.webmagic.Controller.*.*(..))")
    @Pointcut("@annotation(com.jingdong.webmagic.Annotation.LogOperator)")
    public void LogOperator(){}

    /*@Before("LogOperator()")
    public void doBeforeAdvice(JoinPoint joinPoint){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        System.out.println("前置通知");
        Object[] obj = joinPoint.getArgs();
        for(Object o : obj){
            System.out.println("前置1" + o.toString());
        }
        System.out.println("ip" + IpUtil.getIpAddr(request));
    }*/

    @AfterReturning(value = "LogOperator()")
    public void doAfterAdvice(JoinPoint joinPoint){
        LogEntity logEntity = new LogEntity();
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String token = request.getHeader("token");
        try {
            logEntity.setUserName(JWT.decode(token).getAudience().get(0));
        }catch (Exception e){
        }
        logEntity.setIp(IpUtil.getIpAddr(request));
        StringBuffer sb = new StringBuffer();
        Object[] obj = joinPoint.getArgs();
        for (Object o : obj){
            if(o != null){
                sb.append(o.toString() + " ; ");
            }
        }
        logEntity.setParams(sb.toString());
        LogOperator annotation = null;
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        Method method = signature.getMethod();
        try {
            method = joinPoint.getTarget().getClass().getMethod(method.getName(),method.getParameterTypes());
        } catch (Exception e) {}
        annotation = method.getAnnotation(LogOperator.class);
        if(annotation != null){
            logEntity.setMethod(annotation.method());
        }
        logService.insertLog(logEntity);
    }
}
