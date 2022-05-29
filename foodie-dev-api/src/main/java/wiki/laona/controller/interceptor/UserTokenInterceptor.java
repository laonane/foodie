package wiki.laona.controller.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import wiki.laona.utils.JsonResult;
import wiki.laona.utils.JsonUtils;
import wiki.laona.utils.RedisOperator;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author laona
 * @description 用户token拦截器
 * @since 2022-05-29 01:22
 **/
public class UserTokenInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(UserTokenInterceptor.class);

    /**
     * redis中用户唯一token
     */
    public static final String REDIS_USER_TOKEN = "user_unique_token";

    @Resource
    private RedisOperator redisOperator;

    /**
     * 拦截请求，在访问Controller之前
     *
     * @param request  request
     * @param response response
     * @param handler  handler
     * @return false: 请求被拦截，被驳回，验证出现问题； true：请求在校验之后通过，可以放行
     * @throws Exception 抛出异常
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 获取用户token、用户id
        String headerUserToken = request.getHeader("headerUserToken");
        String headerUserId = request.getHeader("headerUserId");

        // 请求用户信息或者token不存在则表明当前请求为非法访问
        if (StringUtils.isEmpty(headerUserId) || StringUtils.isEmpty(headerUserToken)) {
            returnErrorResponse(response, JsonResult.errorMsg("请进行登录"));
            return false;
        }

        // redis中用户信息或者token不存在则表明未登录
        String uniqueToken = redisOperator.get(REDIS_USER_TOKEN + ":" + headerUserId);
        if (StringUtils.isEmpty(uniqueToken)){
            returnErrorResponse(response, JsonResult.errorMsg("请进行登录"));
            return false;
        }

        // redis中用户信息或者token不存在则表明账号在异地登录了
        if (!ObjectUtils.nullSafeEquals(uniqueToken, headerUserToken)) {
            returnErrorResponse(response, JsonResult.errorMsg("账号在异地登录"));
            return false;
        }

        // 信息校验通过
        return true;
    }

    public void returnErrorResponse(HttpServletResponse response, JsonResult result){
        OutputStream out = null;
        try {
            response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
            response.setContentType("text/json");
            out = response.getOutputStream();
            out.write(JsonUtils.objectToJson(result).getBytes(StandardCharsets.UTF_8.displayName()));
            out.flush();
        } catch (IOException e) {
            logger.error("发生异常", e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 请求访问controller之后，渲染视图之前
     *
     * @param request  request
     * @param response response
     * @param handler  handler
     * @throws Exception 抛出异常
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {

    }

    /**
     * 请求访问controller之后，渲染视图之后
     *
     * @param request  request
     * @param response response
     * @param handler  handler
     * @throws Exception 抛出异常
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception ex) throws Exception {

    }
}
