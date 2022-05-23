package wiki.laona.service;

import wiki.laona.pojo.OrderStatus;
import wiki.laona.pojo.bo.ShopcartBO;
import wiki.laona.pojo.bo.SubmitOrderBO;
import wiki.laona.pojo.vo.OrderVO;

import java.util.List;

/**
 * @author laona
 * @description 订单service
 * @since 2022-05-11 15:21
 **/
public interface OrderService {

    /**
     * 创建订单信息
     *
     * @param shopcartList 购物车列表
     * @param submitOrderBO 订单信息
     * @return {@link OrderVO} 订单VO
     */
    public OrderVO createOrder(List<ShopcartBO> shopcartList, SubmitOrderBO submitOrderBO);

    /**
     * 根据订单id修改订单状态
     *
     * @param orderId     订单id
     * @param orderStatus 订单状态
     */
    public void updateOrderStatus(String orderId, Integer orderStatus);


    /**
     * 根据订单id查询订单状态
     *
     * @param orderId 订单id
     * @return 订单状态
     */
    public OrderStatus queryOrderStatusInfo(String orderId);

    /**
     * 关闭支付超时未支付订单
     */
    public void closeOrder();

}
