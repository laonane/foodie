package wiki.laona.controller;

/**
 * @author laona
 * @description 通用 controller
 * @since 2022-05-09 18:51
 **/
public class BaseController {

    /**
     * 评论每页的条数
     */
    public static final Integer COMMENT_PAGE_SIZE = 10;
    /**
     * 通用每页条数
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
    public static final Integer success_code = 200;
}
