package wiki.laona;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author laona
 * @description war 包启动类
 * @since 2022-05-16 16:17
 **/
public class WarStarterApplication extends SpringBootServletInitializer {


    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        // 指向 application 这个 springboot 启动类
        return builder.sources(Application.class);
    }
}
