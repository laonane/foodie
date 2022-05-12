package wiki.laona.controller.center;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wiki.laona.pojo.Users;
import wiki.laona.service.center.CenterUserService;
import wiki.laona.utils.JsonResult;

/**
 * @author laona
 * @description 用户中心
 * @since 2022-05-12 16:56
 **/
@Api(value = "center-用户中心", tags = {"用户中心展示的相关接口"})
@RestController
@RequestMapping("center")
public class CenterController {

    @Autowired
    private CenterUserService centerUserService;

    @ApiOperation(value = "获取登录用户信息", tags = {"获取登录用户信息"}, httpMethod = "GET")
    @GetMapping("/userInfo")
    public JsonResult userInfo(@ApiParam(name = "userId", value = "用户id", required = true)
                                   @RequestParam String userId){

        Users userInfo = centerUserService.queryUserInfo(userId);
        return JsonResult.ok(userInfo);
    }
}
