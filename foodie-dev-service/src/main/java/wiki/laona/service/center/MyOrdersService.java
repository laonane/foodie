package wiki.laona.service.center;

import wiki.laona.pojo.Orders;
import wiki.laona.pojo.vo.OrderStatusCountsVO;
import wiki.laona.utils.PagedGridResult;

/**
 * @author laona
 * @description 用户中心 - 我的订单信息service
 * @since 2022-05-13 15:25
 **/
public interface MyOrdersService {

    /**
     * 查询我的订单（分页）
     *
     * @param userId      用户id
     * @param orderStatus 订单状态
     * @param page        当前页码
     * @param pageSize    每页多少条
     * @return 我的订单
     */
    public PagedGridResult queryMyOrders(String userId, Integer orderStatus, Integer page, Integer pageSize);

    /**
     * 更改订单id的订单为发货状态
     *
     * @param orderId 订单id
     */
    public void updateDeliverOrderStatus(String orderId);


    /**
     * 查询我的订单
     *
     * @param userId  用户id
     * @param orderId 订单id
     * @return orders
     */
    public Orders queryMyOrder(String userId, String orderId);

    /**
     * 更改订单id的订单为收货状态
     *
     * @param orderId 订单id
     * @return 更改成功
     */
    public boolean updateReceiveOrderStatus(String orderId);

    /**
     * 删除用户订单
     *
     * @param userId  用户id
     * @param orderId 订单id
     * @return 删除成功
     */
    public boolean delete(String userId, String orderId);

    /**
     * 根据用户id查询订单状态数量
     *
     * @param userId 用户id
     * @return 订单状态统计VO
     */
    public OrderStatusCountsVO getOrderStatusCounts(String userId);

    /**
     * 查询用户订单动向
     *
     * @param userId   用户id
     * @param page     当前页码
     * @param pageSize 每页多少条
     * @return 订单动向
     */
    PagedGridResult getMyOrdersTrend(String userId, Integer page, Integer pageSize);
}
