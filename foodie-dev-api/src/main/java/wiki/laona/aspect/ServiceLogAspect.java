package wiki.laona.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author laona
 * @description service日志切面
 * @create 2022-05-08 12:40
 **/
@Aspect
@Component
public class ServiceLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(ServiceLogAspect.class);
    /**
     * 错误超时
     */
    final long ERROR_TIME = 3000L;
    /**
     * 警告超时
     */
    final long WARM_TIME = 2000L;

    /**
     * AOP通知：
     * 1. 前置通知：在方法调用之前执行
     * 2. 后置通知：在方法正常调用之后执行
     * 3. 环绕通知：在方法调用之前和之后，都分别可以执行的通知
     * 4. 异常通知：如果在方法调用过程中发生异常，则通知
     * 5. 最终通知：在方法调用之后执行
     *
     *
     * 切面表达式：
     * execution 代表所要执行的表达式主体
     * 第一处 * 代表方法返回类型 *代表所有类型
     * 第二处 包名代表aop监控的类所在的包
     * 第三处 .. 代表该包以及其子包下的所有类方法
     * 第四处 * 代表类名，*代表所有类
     * 第五处 *(..) *代表类中的方法名，(..)表示方法中的任何参数
     *
     * @param joinPoint @{ProceedingJoinPoint}
     * @return 执行结果
     * @throws Throwable 抛出异常
     */
    @Around("execution(* wiki.laona.service..*.*(..))")
    public Object recordTimeLog(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Class<?> aClass = joinPoint.getTarget().getClass();
        String methodName = joinPoint.getSignature().getName();
        logger.info("------------- start execute method {}.{} -----------", aClass, methodName);
        Object result = joinPoint.proceed();

        long endTime = System.currentTimeMillis();
        long takeTime = endTime - startTime;

        if (takeTime > ERROR_TIME) {
            logger.error("----------------- execute done，take time: {} ms ---------------", takeTime);
        }else if (takeTime > WARM_TIME) {
            logger.warn("----------------- execute done，take time: {} ms ---------------", takeTime);
        }else {
            logger.info("----------------- execute done，take time: {} ms ---------------", takeTime);
        }

        return result;
    }
}
