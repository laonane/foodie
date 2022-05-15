package wiki.laona.service.center;

import wiki.laona.pojo.OrderItems;
import wiki.laona.pojo.bo.center.OrderItemsCommentBO;
import wiki.laona.utils.PagedGridResult;

import java.util.List;

/**
 * @author laona
 * @description 用户中心 - 我的订单信息service
 * @since 2022-05-14
 **/
public interface MyCommentsService {

    /**
     * 根据订单id查询关联的商品评论
     *
     * @param orderId 订单id
     * @return 我的订单
     */
    public List<OrderItems> queryPendingComment(String orderId);

    /**
     * 保存评论列表
     *
     * @param orderId     订单id
     * @param userId      用户id
     * @param commentList 评论列表
     */
    public void saveComments(String orderId, String userId, List<OrderItemsCommentBO> commentList);

    /**
     * 查询我的评论列表
     *
     * @param userId   用户id
     * @param page     当前页码
     * @param pageSize 每页条数
     * @return 评论列表
     */
    public PagedGridResult queryMyComments(String userId, Integer page, Integer pageSize);
}
