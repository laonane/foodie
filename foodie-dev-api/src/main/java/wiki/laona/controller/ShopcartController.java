package wiki.laona.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import wiki.laona.enums.KeyEnum;
import wiki.laona.pojo.bo.ShopcartBO;
import wiki.laona.utils.JsonResult;
import wiki.laona.utils.JsonUtils;
import wiki.laona.utils.RedisOperator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

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

    @Autowired
    private RedisOperator redisOperator;

    @ApiOperation(value = "添加商品到购物车", notes = "添加商品到购物车", httpMethod = "POST")
    @PostMapping("/add")
    public JsonResult add(@RequestParam String userId,
                          @RequestBody ShopcartBO shopcartBO,
                          HttpServletRequest request,
                          HttpServletResponse response) {
        if (StringUtils.isBlank(userId)) {
            return JsonResult.errorMsg("");
        }

        logger.info("购物车信息：{}", shopcartBO);
        // TODO 前端用户在登录的情况下，添加商品到购物车，会同时同步商品到 Redis 缓存
        final String shopcartKey = KeyEnum.FOODIE_SHOPCART.getKey() + ":" + userId;
        List<ShopcartBO> shopcartList = new ArrayList<>();

        String shopcartJson = redisOperator.get(shopcartKey);
        if (org.springframework.util.StringUtils.hasText(shopcartJson)) {
            // redis中已经有购物车
            shopcartList = JsonUtils.jsonToList(shopcartJson, ShopcartBO.class);
            // 判断购物车种是否存在已有商品，如果有的话counts累加
            boolean isHaving = false;
            for (ShopcartBO sc : shopcartList) {
                String tmpSpecId = sc.getSpecId();
                if (ObjectUtils.nullSafeEquals(tmpSpecId, shopcartBO.getSpecId())) {
                    sc.setBuyCounts(sc.getBuyCounts() + shopcartBO.getBuyCounts());
                    isHaving = true;
                }
            }
            if (!isHaving) {
                shopcartList.add(shopcartBO);
            }
        } else {
            // redis中没有购物车
            shopcartList = new ArrayList<>();
            // 直接添加到购物车中
            shopcartList.add(shopcartBO);
        }

        // 直接添加到 redis
        redisOperator.set(shopcartKey, JsonUtils.objectToJson(shopcartList));

        return JsonResult.ok();
    }


    @ApiOperation(value = "删除购物车的商品", notes = "删除购物车的商品", httpMethod = "POST")
    @PostMapping("/del")
    public JsonResult del(@RequestParam String userId,
                          @RequestParam String itemSpecId,
                          HttpServletRequest request,
                          HttpServletResponse response) {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(itemSpecId)) {
            return JsonResult.errorMsg("参数不能为空");
        }

        // TODO 前端用户在登录的情况下，删除购物车的商品，同时删除 Redis 缓存中的商品

        return JsonResult.ok();
    }

}
