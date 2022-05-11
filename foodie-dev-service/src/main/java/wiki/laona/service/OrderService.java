package wiki.laona.service;

import wiki.laona.pojo.bo.SubmitOrderBO;

/**
 * @author laona
 * @description 订单service
 * @since 2022-05-11 15:21
 **/
public interface OrderService {

    /**
     * 创建订单信息
     * @param submitOrderBO 订单信息
     * @return 订单号
     */
    public String createOrder(SubmitOrderBO submitOrderBO);
}
