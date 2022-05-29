package wiki.laona.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import wiki.laona.controller.interceptor.UserTokenInterceptor;
import wiki.laona.resource.FileUpload;

import javax.annotation.Resource;

/**
 * @author laona
 * @description webmvc配置
 * @since 2022-05-12 00:13
 **/
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private FileUpload fileUpload;

    /**
     * 实现静态资源映射
     *
     * @param registry {@link  ResourceHandlerRegistry}
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String resourceLocation = "file:" + fileUpload.getImageUserFaceLocation();
        // 映射 swagger2
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/META-INF/resources/")
                // 映射本地静态资源文件
                .addResourceLocations(resourceLocation);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public UserTokenInterceptor userTokenInterceptor() {
        return new UserTokenInterceptor();
    }

    /**
     * 注册拦截器
     * @param registry 注册
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // 用户token拦截器
        registry.addInterceptor(userTokenInterceptor())
                .addPathPatterns("/hello")
                .addPathPatterns("/shopcart/add")
                .addPathPatterns("/shopcart/del")
                .addPathPatterns("/address/list")
                .addPathPatterns("/address/add")
                .addPathPatterns("/address/update")
                .addPathPatterns("/address/setDefalut")
                .addPathPatterns("/address/delete")
                .addPathPatterns("/orders/*")
                .addPathPatterns("/center/*")
                .addPathPatterns("/userInfo/*")
                .addPathPatterns("/myorders/*")
                .addPathPatterns("/mycomments/*")
                .excludePathPatterns("/myorders/deliver")
                .excludePathPatterns("/orders/notifyMerchantOrderPaid");

        WebMvcConfigurer.super.addInterceptors(registry);
    }
}
