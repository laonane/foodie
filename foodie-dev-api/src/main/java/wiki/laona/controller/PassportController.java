package wiki.laona.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import wiki.laona.pojo.Users;
import wiki.laona.pojo.bo.UserBo;
import wiki.laona.service.UserService;
import wiki.laona.utils.CookieUtils;
import wiki.laona.utils.JsonResult;
import wiki.laona.utils.JsonUtils;
import wiki.laona.utils.MD5Utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author laona
 * @description
 * @date 2022-04-28 14:50
 **/
@Api(value = "注册登录", tags = {"用于注册登录的相关接口"})
@RestController
@RequestMapping("passport")
public class PassportController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "用户名是否存在", notes = "用户名是否存在", httpMethod = "GET")
    @GetMapping("/usernameIsExist")
    public JsonResult usernameIsExist(@RequestParam String username) {
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
        setNullProperty(userResult);

        // 6. 保存 cookie
        CookieUtils.setCookie(req, resp, "user", JsonUtils.objectToJson(userResult), true);

        // TODO 生成用户token，存入redis会话
        // TODO 同步购物车数据

        return JsonResult.ok();
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

        setNullProperty(userResult);

        // 用户信息设置到 cookie
        CookieUtils.setCookie(req, resp, "user", JsonUtils.objectToJson(userResult), true);
        return JsonResult.ok(userResult);
    }

    @ApiOperation(value = "退出登录", notes = "退出登录", httpMethod = "POST")
    @PostMapping("/logout")
    public JsonResult logout(@RequestParam String userId, HttpServletRequest req, HttpServletResponse resp) {
        // 清除用户相关的 cookie 信息
        CookieUtils.deleteCookie(req, resp, "user");

        // TODO 用户退出登录，就需要清空购物车
        // TODO 分布式绘画中需要清除用户数据

        return JsonResult.ok();
    }
}
