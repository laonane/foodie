package wiki.laona.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import wiki.laona.pojo.Users;
import wiki.laona.pojo.vo.UsersVO;
import wiki.laona.service.UserService;
import wiki.laona.utils.JsonResult;
import wiki.laona.utils.JsonUtils;
import wiki.laona.utils.MD5Utils;
import wiki.laona.utils.RedisOperator;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @author laona
 * @description sso 控制器
 * @since 2022-05-29 16:17
 **/
@Controller
public class SSOController {

    private static final Logger logger = LoggerFactory.getLogger(SSOController.class);
    /**
     * redis中用户唯一token
     */
    public static final String REDIS_USER_TOKEN = "user_unique_token";
    /**
     * redis中用户全局门票
     */
    public static final String REDIS_USER_TICKET = "redis_user_ticket";
    /**
     * redis中用户临时门票
     */
    public static final String REDIS_TMP_TICKET = "redis_tmp_ticket";
    /**
     * cookie中用户全局门票
     */
    public static final String COOKIE_USER_TICKET = "cookie_user_ticket";

    @Resource
    private UserService userService;
    @Resource
    private RedisOperator redisOperator;

    @GetMapping("/login")
    public String login(String returnUrl, Model model, HttpServletRequest request, HttpServletResponse response) {

        model.addAttribute("returnUrl", returnUrl);

        // todo 后续完善是否登录

        // 用户从未登录，第一次进入则跳转到cas的同一登录页面
        return "login";
    }


    /**
     * CAS 的统一登录接口
     * 目的：
     * 1. 创建登录用户的全局会话                  -> uniqueToken
     * 2. 创建用户全局门票, 用于表示CAS端是够登录   -> userTicket
     * 3. 创建用户的临时票据、用户回调回传         -> tmpTicket
     */
    @PostMapping("/doLogin")
    public String doLogin(String username,
                          String password,
                          String returnUrl,
                          Model model,
                          HttpServletRequest request,
                          HttpServletResponse response) throws Exception {

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            model.addAttribute("errmsg", "账号密码不能为空");
            return "login";
        }

        Users userResult = userService.queryUserForLogin(username, MD5Utils.getMD5Str(password));
        if (ObjectUtils.isEmpty(userResult)) {
            model.addAttribute("errmsg", "账号或密码不正确");
            return "login";
        }

        // 实现用户的 redis 会话
        String uniqueToken = UUID.randomUUID().toString().trim();

        UsersVO usersVO = new UsersVO();
        BeanUtils.copyProperties(userResult, usersVO);
        usersVO.setUserUniqueToken(uniqueToken);

        redisOperator.set(REDIS_USER_TOKEN + ":" + userResult.getId(), JsonUtils.objectToJson(usersVO));

        // 生成ticket门票，全局门票，代表用户在CAS端登录过
        String userTicket = UUID.randomUUID().toString().trim();

        // 用户cookie需要放到redis中
        setCookie(COOKIE_USER_TICKET, userTicket, response);

        // userTicket关联用户id，并且放入到redis中，代表这个用户有门票了，
        redisOperator.set(REDIS_USER_TICKET + ":" + userTicket, userResult.getId());

        // 生成临时票据，回调到调用端网站，是由CAS端所签发的一个一次性临时ticket
        String tmpTicket = createTmpTicket();

        // 重定向
        // return "login";
        return "redirect:" + returnUrl + "?tmpTicket=" + tmpTicket;

    }


    @PostMapping("/verifyTmpTicket")
    @ResponseBody
    public JsonResult verifyTmpTicket(String tmpTicket,
                                      HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {
        // 使用一次性票据去验证用户是否登录，如果登录过就把用户会话信息返回给站点
        // 使用完毕后，需要销毁临时票据
        String tmpTicketValue = redisOperator.get(REDIS_TMP_TICKET + ":" + tmpTicket);
        if (StringUtils.isEmpty(tmpTicketValue)) {
            return JsonResult.errorUserTicket("用户票据异常");
        }

        // 验证临时票据，需要销毁，并且回去CAS端中的全局userTicket，以此来获取用户登录信息
        if (!ObjectUtils.nullSafeEquals(tmpTicketValue, MD5Utils.getMD5Str(tmpTicket))) {
            return JsonResult.errorUserTicket("用户票据异常");
        }

        // 销毁临时票据
        redisOperator.del(REDIS_TMP_TICKET + ":" + tmpTicket);

        // 1. 验证并获取用户的userTicket
        String userTicket = getCookie(request, COOKIE_USER_TICKET);

        // 2. 获取用户信息
        String userId = redisOperator.get(REDIS_USER_TICKET + ":" + userTicket);
        if (StringUtils.isEmpty(userId)) {
            return JsonResult.errorUserTicket("用户票据异常");
        }

        // 3. 校验门票对应的user会话是否存在
        String userRedis = redisOperator.get(REDIS_USER_TOKEN + ":" + userId);
        if (StringUtils.isEmpty(userRedis)) {
            return JsonResult.errorUserTicket("用户票据异常");
        }
        UsersVO usersVO = JsonUtils.jsonToPojo(userRedis, UsersVO.class);
        if (ObjectUtils.isEmpty(usersVO)) {
            return JsonResult.errorUserTicket("用户票据异常");
        }

        return JsonResult.ok(usersVO);
    }

    private String getCookie(HttpServletRequest request, String key) {

        Cookie[] cookieArr = request.getCookies();
        if (StringUtils.isEmpty(key) || cookieArr == null) {
            return null;
        }

        String cookieValue = null;
        String cookieKey;
        for (Cookie cookie : cookieArr) {
            cookieKey = cookie.getName();
            if (ObjectUtils.nullSafeEquals(cookieKey, key)) {
                cookieValue = cookieKey;
            }
        }

        return cookieValue;
    }

    private void setCookie(String key, String val, HttpServletResponse response) {
        Cookie cookie = new Cookie(key, val);
        cookie.setDomain("sso.com");
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    /**
     * 生成临时票据
     *
     * @return 临时票据
     */
    private String createTmpTicket() {
        String tmpTicket = UUID.randomUUID().toString().trim();

        try {
            redisOperator.set(REDIS_TMP_TICKET + ":" + tmpTicket, MD5Utils.getMD5Str(tmpTicket), 600);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tmpTicket;
    }
}
