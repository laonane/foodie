package wiki.laona;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import tk.mybatis.spring.annotation.MapperScan;

import java.net.InetAddress;

/**
 * @author laona
 * @description api application
 * @date 2022-04-26 19:53
 **/
@SpringBootApplication
@MapperScan(basePackages = {"wiki.laona.mapper"})
@ComponentScan(basePackages = {"wiki.laona", "org.n3r.idworker"})
public class Application {

    public static void main(String[] args) {
        try {
            ConfigurableApplicationContext application = SpringApplication.run(Application.class, args);
            Environment env = application.getEnvironment();
            String ip = InetAddress.getLocalHost().getHostAddress();
            String port = env.getProperty("server.port");
            String path = env.getProperty("server.servlet.context-path");
            if (StringUtils.isBlank(path)) {
                path = "";
            }
            System.out.println(
                    "\n----------------------------------------------------------\n\t" +
                            "Application Jeecg-Boot is running! Access URLs:\n\t" +
                            "Local: \t\thttp://localhost:" + port + path + "/\n\t" +
                            "External: \thttp://" + ip + ":" + port + path + "/\n\t" +
                            "前端地址: \thttp://localhost:8080/foodie-shop/\n\t" +
                            "Swagger文档: \thttp://" + ip + ":" + port + path + "/doc.html\n" +
                            "----------------------------------------------------------");
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
