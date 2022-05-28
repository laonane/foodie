package wiki.laona.controller;

import org.springframework.beans.factory.annotation.Autowired;
import wiki.laona.pojo.Orders;
import wiki.laona.service.center.MyOrdersService;
import wiki.laona.utils.JsonResult;

import java.io.File;

/**
 * @author laona
 * @description 通用 controller
 * @since 2022-05-09 18:51
 **/
public class BaseController {

    /**
     * 分页-每页的条数
     */
    public static final Integer COMMON_PAGE_SIZE = 10;
    /**
     * 分页-通用每页条数
     */
    public static final Integer PAGE_SIZE = 20;
    /**
     * 收货人姓名的最大长度
     */
    public static final Integer MAX_LEN_OF_RECEIVER_NAME = 12;
    /**
     * 手机号长度
     */
    public static final Integer LEN_OF_PHONE_NUM = 11;
    /**
     * 成功回调码
     */
    public static final Integer SUCCESS_CODE = 200;
    /**
     * 用户cookie名
     */
    public static final String USER_INFO = "user";
    /**
     * 购物车cookie名
     */
    public static final String FOODIE_SHOPCART = "shopcart";
    /**
     * redis中用户唯一token
     */
    public static final String REDIS_USER_TOKEN = "user_unique_token";
    /**
     * 回调通知url
     * <p>
     * 流程： 微信支付成功 -> 支付中信 -> 电商平台
     */
    public static final String RETURN_URL = "http://api.foodie.laona.wiki:8088/foodie-dev-api/orders/notifyMerchantOrderPaid";
    /**
     * 支付中心的调用地址(生产环境)
     */
    public static final String PAYMENT_URL = "http://payment.t.mukewang.com/foodie-payment/payment/createMerchantOrder";
    /**
     * 用户上传头像位置
     * File.separator 文件分隔符(自适应不同系统)
     */
    public static final String IMAGE_USER_FACE_LOCATION
            = String.format("E:%sfoodieUpload%sfoodie%sfaces", File.separator, File.separator, File.separator);



    @Autowired
    protected MyOrdersService myOrdersService;

    /**
     * 查询订单信息是否有效
     *
     * @param userId  用户id
     * @param orderId 订单id
     * @return JsonResult
     */
    protected JsonResult checkUserOrder(String userId, String orderId) {

        Orders checkResult = myOrdersService.queryMyOrder(userId, orderId);

        if (checkResult == null) {
            return JsonResult.errorMsg("订单不存在!");
        }

        return JsonResult.ok(checkResult);
    }
}
