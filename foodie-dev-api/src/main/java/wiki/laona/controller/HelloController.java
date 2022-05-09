package wiki.laona.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import wiki.laona.utils.JsonResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author laona
 * @description hello controller
 * @date 2022-04-26 19:55
 **/
@ApiIgnore
@Api(value = "Hello 测试", tags = {"用于测试的相关接口"})
@RestController
public class HelloController {

    final static Logger logger = LoggerFactory.getLogger(HelloController.class);

    @ApiOperation(value = "测试系统连通性", tags = {"用户测试springboot启动情况"}, httpMethod = "GET")
    @GetMapping("/hello")
    public String hello() {

        logger.debug("debug: hello~");
        logger.info("info: hello~");
        logger.warn("warn: hello~");
        logger.error("error: hello~");
        return "hello world!";
    }

    @ApiOperation(value = "设置session", tags = {"设置session"}, httpMethod = "get")
    @GetMapping("setSession")
    public JsonResult setSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        // 设置存储的键值
        session.setAttribute("userInfo", "laona");
        // 设置过期时间
        session.setMaxInactiveInterval(3600);
        Object info = session.getAttribute("userInfo");
        System.out.println(info);
        return JsonResult.ok();
    }
}
