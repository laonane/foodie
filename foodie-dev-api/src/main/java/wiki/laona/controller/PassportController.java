package wiki.laona.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import wiki.laona.enums.KeyEnum;
import wiki.laona.pojo.Users;
import wiki.laona.pojo.bo.ShopcartBO;
import wiki.laona.pojo.bo.UserBo;
import wiki.laona.pojo.vo.UsersVO;
import wiki.laona.service.UserService;
import wiki.laona.utils.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author laona
 * @description
 * @date 2022-04-28 14:50
 **/
@Api(value = "注册登录", tags = {"用于注册登录的相关接口"})
@RestController
@RequestMapping("passport")
public class PassportController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private RedisOperator redisOperator;

    @ApiOperation(value = "用户名是否存在", notes = "用户名是否存在", httpMethod = "GET")
    @GetMapping("/usernameIsExist")
    public JsonResult usernameIsExist(
            @ApiParam(name = "username", value = "用户名", required = false) @RequestParam String username) {
        // 判空
        if (StringUtils.isBlank(username)) {
            return JsonResult.errorMsg("用户名不能为空");
        }

        // 查找注册的用户名是否存在
        boolean isExists = userService.queryUsernameIsExists(username);
        if (isExists) {
            return JsonResult.errorMsg("用户名已经存在");
        }

        // 请求成功，用户名没有重复
        return JsonResult.ok();
    }


    @ApiOperation(value = "用户注册", notes = "用户注册", httpMethod = "POST")
    @PostMapping("/regist")
    public JsonResult regist(@RequestBody UserBo userBo, HttpServletRequest req, HttpServletResponse resp) throws Exception {

        String username = userBo.getUsername().trim();
        String password = userBo.getPassword().trim();
        String confirmPassword = userBo.getConfirmPassword().trim();
        // 1. 查询用户名是否存在
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password) || StringUtils.isBlank(confirmPassword)) {
            return JsonResult.errorMsg("用户名或密码不能为空");
        }
        // 2. 查询用户是否存在
        boolean isExists = userService.queryUsernameIsExists(username);
        if (isExists) {
            return JsonResult.errorMsg("用户名已经存在");
        }
        // 3. 密码长度不能少于6位
        if (password.length() < 6) {
            return JsonResult.errorMsg("密码长度不能少于6位");
        }
        // 4. 两次密码不一样
        if (!password.equals(confirmPassword)) {
            return JsonResult.errorMsg("两次输入的密码不一样");
        }
        // 5. 注册用户
        Users userResult = userService.createUser(userBo);

        // 去除隐私信息
        // userResult = setNullProperty(userResult);

        // 生成用户token，存入 Redis 会话
        // 实现用户的 redis 会话
        UsersVO usersVO = conventUserVO(userResult);

        // 6. 保存 cookie
        CookieUtils.setCookie(req, resp, USER_INFO, JsonUtils.objectToJson(usersVO), true);

        // 同步购物车数据
        syncShopcartData(userResult.getId(), req, resp);

        return JsonResult.ok();
    }

    /**
     * 将 Users 实体转换成 UsersVO
     *
     * @param user 用户信息
     * @return UsersVO
     */
    private UsersVO conventUserVO(Users user) {
        // 实现用户的 redis 会话
        String uniqueToken = UUID.randomUUID().toString().trim();
        redisOperator.set(REDIS_USER_TOKEN + ":" + user.getId(), uniqueToken);

        UsersVO usersVO = new UsersVO();
        BeanUtils.copyProperties(user, usersVO);
        usersVO.setUserUniqueToken(uniqueToken);
        return usersVO;
    }

    /**
     * 注册登录成功后，同步redis和cookie中的购物车数据
     * 1. redis无数据，如果cookie中为空，不做处理
     * 2. redis无数据，cookie有数据，同步redis
     * 3. redis有数据，cookie无数据，同步cookie
     * 4. redis有数据，cookie有数据。如果两者包含同一商品，以cookie为准，直接将cookie的该商品信息覆盖redis
     * 5. 同步到redis中后，需要覆盖本地cookie的数据，保证cookie的数据是最新的
     */
    private void syncShopcartData(String userId, HttpServletRequest request, HttpServletResponse response) {

        final String shopcartKey = KeyEnum.FOODIE_SHOPCART.getKey() + ":" + userId;

        // redis中的购物车数据
        String shopcartJsonRedis = redisOperator.get(shopcartKey);

        // redis中cookie数据
        String shopcartJsonCookie = CookieUtils.getCookieValue(request, FOODIE_SHOPCART, true);

        if (!org.springframework.util.StringUtils.hasText(shopcartJsonRedis)) {
            // 2. redis无数据，cookie有数据，同步redis
            if (org.springframework.util.StringUtils.hasText(shopcartJsonCookie)) {
                redisOperator.set(shopcartKey, shopcartJsonCookie);
            }
        } else {
            // redis有数据，cookie无数据，同步cookie
            if (!org.springframework.util.StringUtils.hasText(shopcartJsonCookie)) {
                CookieUtils.setCookie(request, response, FOODIE_SHOPCART, shopcartJsonRedis, true);
            } else {
                // redis有数据，cookie有数据。如果两者包含同一商品，以cookie为准，直接将cookie的该商品信息覆盖redis
                List<ShopcartBO> cookieShopcartList = JsonUtils.jsonToList(shopcartJsonCookie, ShopcartBO.class);
                List<ShopcartBO> redisShopcartList = JsonUtils.jsonToList(shopcartJsonRedis, ShopcartBO.class);

                // 通过Map过滤重复元素
                Map<String, ShopcartBO> shopcartMap = new HashMap<>(1 << 4);
                for (ShopcartBO cartItem : redisShopcartList) {
                    shopcartMap.put(cartItem.getItemId() + cartItem.getSpecId(), cartItem);
                }

                // 如果redis、cookie两者包含同一商品，以cookie为准，直接将cookie的该商品信息覆盖redis
                for (ShopcartBO cartItem : cookieShopcartList) {
                    shopcartMap.put(cartItem.getItemId() + cartItem.getSpecId(), cartItem);
                }

                // 最新购物车数据
                List<ShopcartBO> cartResultList = new ArrayList<>();
                for (String key : shopcartMap.keySet()) {
                    cartResultList.add(shopcartMap.get(key));
                }

                // 重新写入cookie、redis中
                String cartResultJson = JsonUtils.objectToJson(cartResultList);
                redisOperator.set(shopcartKey, cartResultJson);
                CookieUtils.setCookie(request, response, FOODIE_SHOPCART, cartResultJson, true);
            }
        }
    }


    private Users setNullProperty(Users userResult) {
        userResult.setPassword(null);
        userResult.setMobile(null);
        userResult.setEmail(null);
        userResult.setCreatedTime(null);
        userResult.setUpdatedTime(null);
        userResult.setBirthday(null);
        return userResult;
    }


    @ApiOperation(value = "用户登录", notes = "用户登录", httpMethod = "POST")
    @PostMapping("/login")
    public JsonResult login(@RequestBody UserBo userBo, HttpServletRequest req, HttpServletResponse resp) throws Exception {

        String username = userBo.getUsername().trim();
        String password = userBo.getPassword().trim();

        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return JsonResult.errorMsg("用户名或密码不能为空");
        }

        Users userResult = userService.queryUserForLogin(username, MD5Utils.getMD5Str(password));

        if (ObjectUtils.isEmpty(userResult)) {
            return JsonResult.errorMsg("用户名或密码不正确");
        }

        // 将用户信息转换成 UsersVO，并保存到 redis 中
        UsersVO usersVO = conventUserVO(userResult);

        // 用户信息设置到 cookie
        CookieUtils.setCookie(req, resp, USER_INFO, JsonUtils.objectToJson(usersVO), true);

        // 同步购物车数据
        syncShopcartData(userResult.getId(), req, resp);

        return JsonResult.ok(userResult);
    }

    @ApiOperation(value = "退出登录", notes = "退出登录", httpMethod = "POST")
    @PostMapping("/logout")
    public JsonResult logout(@RequestParam String userId, HttpServletRequest req, HttpServletResponse resp) {

        // 清除用户相关的 cookie 信息
        CookieUtils.deleteCookie(req, resp, USER_INFO);

        // 用户退出，清理redis中的user会话信息
        redisOperator.del(REDIS_USER_TOKEN + ":" + userId);

        // 分布式会话中需要清除用户数据
        CookieUtils.deleteCookie(req, resp, FOODIE_SHOPCART);

        return JsonResult.ok();
    }
}
