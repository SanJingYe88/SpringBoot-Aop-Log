package it.com.aop.aop;

import it.com.aop.cons.Operation;
import it.com.aop.dao.UserMapper;
import it.com.aop.entity.OperateLog;
import it.com.aop.entity.User;
import it.com.aop.mongo.MonggoDao;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.Date;

@Aspect
@Component
@Slf4j
public class MapperAop {

    @Autowired
    UserMapper userMapper;

    @Autowired
    MonggoDao monggoDao;


    @Pointcut(value = "bean(*Mapper) && @annotation(org.apache.ibatis.annotations.Insert)")
    public void insertPointCut(){};

    @Pointcut(value = "bean(*Mapper) && @annotation(org.apache.ibatis.annotations.Update)")
    public void updatePointCut(){};

    @Pointcut(value = "bean(*Mapper) && @annotation(org.apache.ibatis.annotations.Delete)")
    public void deletePointCut(){};

    @Around(value = "insertPointCut() || updatePointCut() || deletePointCut()")
    public void deleteAround(ProceedingJoinPoint joinPoint){
        OperateLog operateLog = new OperateLog();
        //获取操作的类
        String className = joinPoint.getSignature().getDeclaringTypeName();
        operateLog.setClassName(className);
        //获取操作的方法名
        String methodName = joinPoint.getSignature().getName();
        operateLog.setMethodName(methodName);
        Object[] args = joinPoint.getArgs();
        //获取其方法上的注解
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Annotation[] annotations= methodSignature.getMethod().getDeclaredAnnotations();
        User user = null;
        for(int i = 0; i < annotations.length; i++){
            Annotation annotation = annotations[i];
            String type = annotation.annotationType().toString();
            //如果是修改或者删除操作
            if("interface org.apache.ibatis.annotations.Delete".equals(type)){
                operateLog.setOperateType(Operation.DELETE);
                //先获取其旧值
                user = userMapper.getById(Integer.valueOf(args[0].toString()));
                operateLog.setOldValue( user == null ? "":user.toString());
                break;
            }else if("interface org.apache.ibatis.annotations.Update".equals(type)){
                operateLog.setOperateType(Operation.UPDATE);
                user = (User) args[0];
                operateLog.setOldValue(user.toString());
                break;
            }else if("interface org.apache.ibatis.annotations.Insert".equals(type)){
                operateLog.setOperateType(Operation.INSERT);
                user = (User) args[0];
                operateLog.setOldValue("");
                break;
            }
        }

        //目标方法执行
        Object result = null;
        try {
            result = joinPoint.proceed(args);
            log.info("执行结果:{}",result);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            log.info("执行异常:{}",throwable.getMessage());
        }

        //判断操作类型
        if (operateLog.getOperateType().equals(Operation.INSERT)){
            operateLog.setNewValue(user.toString());
        }else {
            operateLog.setNewValue(result == null ? "" : result.toString());
        }
        operateLog.setOperateDate(new Date());
        monggoDao.insert(operateLog);
        log.info("log:{}",operateLog);
    }
}
