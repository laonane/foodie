package wiki.laona.controller;

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
     * 回调通知url
     * <p>
     * 流程： 微信支付成功 -> 支付中信 -> 电商平台
     */
    public static final String RETURN_URL = "http://5afk4s.natappfree.cc/orders/notifyMerchantOrderPaid";
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
}
