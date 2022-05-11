package wiki.laona.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wiki.laona.enums.PayMethod;
import wiki.laona.pojo.bo.SubmitOrderBO;
import wiki.laona.utils.JsonResult;

import java.util.Objects;

/**
 * @author laona
 * @description 用户地址controller
 * @create 2022-05-11 14:20
 **/
@Api(value = "订单相关接口", tags = {"订单相关api接口"})
@RestController
@RequestMapping("orders")
public class OrdersController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(OrdersController.class);

    @ApiOperation(value = "用户下单", notes = "用户下单", httpMethod = "POST")
    @PostMapping("/create")
    public JsonResult list(@RequestBody SubmitOrderBO submitOrderBO) {

        if (!Objects.equals(submitOrderBO.getPayMethod(), PayMethod.WEIXIN.type)
                && !Objects.equals(submitOrderBO.getPayMethod(), PayMethod.ALIPAY.type)){

            return JsonResult.errorMsg("支付方式不支持");
        }

        logger.info("用户下单：{}", submitOrderBO);

        // 1. 创建订单
        // 2. 创建爱你订单后，移除购物车中已结算（已提交）的商品
        // 3. 向支付中心发送当前订单，创建支付中心的订单数据

        return JsonResult.ok();
    }

}