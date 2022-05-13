package wiki.laona.service.center;

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
}
