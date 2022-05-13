package wiki.laona.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import wiki.laona.resource.FileUpload;

/**
 * @author laona
 * @description webmvc配置
 * @since 2022-05-12 00:13
 **/
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private FileUpload fileUpload;

    /**
     * 实现静态资源映射
     *
     * @param registry {@link  ResourceHandlerRegistry}
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                // 映射 swagger2
                .addResourceLocations("classpath:/META-INF/resources/")
                // 映射本地静态资源文件
                .addResourceLocations("file:/" + fileUpload.getImageUserFaceLocation());
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}
