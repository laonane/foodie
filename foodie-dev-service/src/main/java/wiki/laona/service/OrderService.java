package wiki.laona.service;

import wiki.laona.pojo.bo.SubmitOrderBO;
import wiki.laona.pojo.vo.OrderVO;

/**
 * @author laona
 * @description 订单service
 * @since 2022-05-11 15:21
 **/
public interface OrderService {

    /**
     * 创建订单信息
     *
     * @param submitOrderBO 订单信息
     * @return {@link OrderVO} 订单VO
     */
    public OrderVO createOrder(SubmitOrderBO submitOrderBO);

    /**
     * 根据订单id更新订单状态
     *
     * @param orderId     订单id
     * @param orderStatus 订单状态
     */
    public void updateOrderStatus(String orderId, Integer orderStatus);
}
