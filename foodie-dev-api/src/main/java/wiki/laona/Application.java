package wiki.laona;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author laona
 * @description api application
 * @date 2022-04-26 19:53
 **/
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        try {
            SpringApplication.run(Application.class, args);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
