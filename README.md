# foodie

## 一、单体项目开发与上线(1-5周)

### 第1周 万丈高楼，地基首要

#### 1. 初始化项目

#### 2. 跨域请求

```java
package wiki.laona.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author laona
 * @description 跨域配置
 * @create 2022-05-02 15:32
 **/
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        // 1. 添加 cors 配置
        CorsConfiguration config = new CorsConfiguration();
        // 此处是前端的请求地址，tomcat 默认 8080
        config.addAllowedOrigin("http://localhost:8080");

        // 设置是否发送cookie信息
        config.setAllowCredentials(true);

        // 设置允许请求的方式
        config.addAllowedMethod("*");

        // 设置允许的header
        config.addAllowedHeader("*");

        // 2. 为url添加映射路径
        UrlBasedCorsConfigurationSource corsSource = new UrlBasedCorsConfigurationSource();
        corsSource.registerCorsConfiguration("/**", config);

        // 3. 返回重新定义好的corsSource
        return new CorsFilter(corsSource);
    }
}

```



#### 3. 整合 log4j

```properties
log4j.rootLogger=DEBUG,stdout,file
log4j.additivity.org.apache=true

log4j.appender.stdout=org.apache.log4j.ConsoleAppender
log4j.appender.stdout.threshold=INFO
log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
log4j.appender.stdout.layout.ConversionPattern=%-5p %c{1}:%L - %m%n

log4j.appender.file=org.apache.log4j.DailyRollingFileAppender
log4j.appender.file.layout=org.apache.log4j.PatternLayout
log4j.appender.file.DatePattern='.'yyyy-MM-dd-HH-mm
log4j.appender.file.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n
log4j.appender.file.Threshold=INFO
log4j.appender.file.append=true
log4j.appender.file.File=logs/foodie-api/foodie.log
```



#### 4. 监控日志执行时间

- 添加切面依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
```

- 编写 Aspect

```java
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
```



#### 5. 首页轮播图

#### 6. 首页分类实现

- 一级分类查询
- 子分类查询

#### 7. 商品推荐

- 查询每一级分类下的最新 6 条商品信息

#### 8. 商品详情

- 商品信息展示相关接口

#### 9. 商品评论

- 商品评论详情
- 商品评论分页
- 评论用户信息脱敏

#### 10. 商品搜索

- 关键字搜索

- 按分类搜索

#### 11. 购物车

- 常见解决方案
- Cookies 购物车实现
- 未登录 -> 登录购物车处理
- 渲染（刷新购物车）
- 删除购物车

#### 12. 订单

- 确认订单
- 支付流程
- 订单回调
- 支付中心订单测试
```shell
# 修改: merchantOrderId\merchantUserId 即可
curl --location --request POST 'http://payment.t.mukewang.com/foodie-payment/payment/getPaymentCenterOrderInfo?merchantOrderId=2205120DK1RGXZTC&merchantUserId=220511G4M62YZ0X4' --header 'imoocUserId: imooc' --header 'password: imooc'


curl --location --request POST 'http://payment.t.mukewang.com/foodie-payment/payment/getPaymentCenterOrderInfo?merchantOrderId=220512A9BFT3F2RP&merchantUserId=220511AYF42TCWSW' \
--header 'imoocUserId: imooc' \
--header 'password: imooc'
```
- 支付成功后的回调
