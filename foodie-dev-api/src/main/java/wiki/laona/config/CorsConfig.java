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
        config.addAllowedOrigin("http://localhost:8088");

        // 设置是否允许发送 cookie
        config.setAllowCredentials(true);

        // 设置允许请求的方式
        config.addAllowedMethod("*");

        // 设置允许的 header
        config.addAllowedMethod("*");

        // 2. url 添加映射路径
        UrlBasedCorsConfigurationSource corsSource = new UrlBasedCorsConfigurationSource();
        corsSource.registerCorsConfiguration("/**", config);

        // 3. 返回重新定义好的 corsSource
        return new CorsFilter(corsSource);
    }
}
