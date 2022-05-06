package wiki.laona.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author laona
 * @description 跨域配置
 * @date 2022-05-06 11:46
 **/
@Configuration
public class CorsConfig {

    public CorsConfig() {
    }

    @Bean
    public CorsFilter corsFilter() {
        // 1. 添加cors配置信息
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:8080");
        // config.addAllowedOrigin("http://172.16.23.224:8080");
        // 设置允许的header
        config.addAllowedHeader("*");
        // 设置允许请求的方式
        config.addAllowedMethod("*");
        // 设置是否发送cookie信息
        config.setAllowCredentials(true);

        // 2. 为url添加映射路径
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", config);

        // 3. 返回重新定义好的corsSource
        return new CorsFilter(urlBasedCorsConfigurationSource);
    }
}
