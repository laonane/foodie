package wiki.laona.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import wiki.laona.pojo.bo.ShopcartBO;
import wiki.laona.utils.JsonResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author laona
 * @description 购物车接口
 * @since 2022-05-10 15:40
 **/
@Api(value = "购物车相关接口", tags = {"购物车相关接口"})
@RestController
@RequestMapping("shopcart")
public class ShopcartController {

    private static final Logger logger = LoggerFactory.getLogger(ShopcartController.class);

    @ApiOperation(value = "添加商品到购物车", notes = "添加商品到购物车", httpMethod = "POST")
    @PostMapping("/add")
    public JsonResult add(@RequestParam String userId,
                          @RequestBody ShopcartBO shopcartBO,
                          HttpServletRequest request,
                          HttpServletResponse response) {
        if (StringUtils.isBlank(userId)) {
            return  JsonResult.errorMsg("");
        }

        logger.info("购物车信息：{}", shopcartBO);
        // TODO 前端用户在登录的情况下，添加商品到购物车，会同时同步商品到 Redis 缓存

        return JsonResult.ok();
    }

}
