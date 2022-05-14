package wiki.laona.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import wiki.laona.enums.OrderStatusEnum;
import wiki.laona.enums.PayMethod;
import wiki.laona.pojo.OrderStatus;
import wiki.laona.pojo.bo.SubmitOrderBO;
import wiki.laona.pojo.vo.MerchantOrdersVO;
import wiki.laona.pojo.vo.OrderVO;
import wiki.laona.service.OrderService;
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

    @Autowired
    private OrderService orderService;

    @Autowired
    private RestTemplate restTemplate;

    @ApiOperation(value = "用户下单", notes = "用户下单", httpMethod = "POST")
    @PostMapping("/create")
    public JsonResult create(@RequestBody SubmitOrderBO submitOrderBO) {

        if (!Objects.equals(submitOrderBO.getPayMethod(), PayMethod.WEIXIN.type)
                && !Objects.equals(submitOrderBO.getPayMethod(), PayMethod.ALIPAY.type)) {

            return JsonResult.errorMsg("支付方式不支持");
        }

        // 1. 创建订单
        OrderVO orderVO = orderService.createOrder(submitOrderBO);
        String orderId = orderVO.getOrderId();

        // 2. 创建订单后，移除购物车中已结算（已提交）的商品
        // TODO 整合 redis之后，移除购物车已经结算的订单数据
        // CookieUtils.setCookie(request, response, FOODIE_SHOPCART, "");

        // 3. 向支付中心发送当前订单，创建支付中心的订单数据
        MerchantOrdersVO merchantOrdersVO = orderVO.getMerchantOrdersVO();
        merchantOrdersVO.setReturnUrl(RETURN_URL);
        // 为了方便测试购买，所有的订单金额都写成 0.01 元
        merchantOrdersVO.setAmount(1);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("imoocUserId", "imooc");
        headers.add("password", "imooc");

        HttpEntity<MerchantOrdersVO> httpEntity = new HttpEntity<>(merchantOrdersVO, headers);

        ResponseEntity<JsonResult> responseEntity
                = restTemplate.postForEntity(PAYMENT_URL, httpEntity, JsonResult.class);
        JsonResult paymentResult = responseEntity.getBody();
        if (ObjectUtils.isEmpty(paymentResult) || !ObjectUtils.nullSafeEquals(paymentResult.getStatus(), SUCCESS_CODE)) {
            return JsonResult.errorMsg("支付中心订单创建失败，请联系管理员");
        }

        return JsonResult.ok(orderId);
    }

    @ApiOperation(value = "更改订单状态为已支付，待发送", notes = "更改订单状态为已支付，待发送", httpMethod = "POST")
    @PostMapping("notifyMerchantOrderPaid")
    public Integer notifyMerchantOrderPaid(String merchantOrderId) {

        orderService.updateOrderStatus(merchantOrderId, OrderStatusEnum.WAIT_DELIVER.type);

        return HttpStatus.OK.value();
    }


    @ApiOperation(value = "发起支付请求后查询当前订单状态", notes = "发起支付请求后查询当前订单状态", httpMethod = "POST")
    @PostMapping("getPaidOrderInfo")
    public JsonResult getPaidOrderInfo(String orderId){
        if (StringUtils.isBlank(orderId)) {
            return JsonResult.errorMsg("订单不存在");
        }

        OrderStatus orderStatus = orderService.queryOrderStatusInfo(orderId);

        return JsonResult.ok(orderStatus);
    }

}
