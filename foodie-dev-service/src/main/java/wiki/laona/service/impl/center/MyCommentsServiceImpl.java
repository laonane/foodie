package wiki.laona.service.impl.center;

import com.github.pagehelper.PageHelper;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wiki.laona.enums.YesOrNo;
import wiki.laona.mapper.ItemsCommentsMapperCustom;
import wiki.laona.mapper.OrderItemsMapper;
import wiki.laona.mapper.OrderStatusMapper;
import wiki.laona.mapper.OrdersMapper;
import wiki.laona.pojo.OrderItems;
import wiki.laona.pojo.OrderStatus;
import wiki.laona.pojo.Orders;
import wiki.laona.pojo.bo.center.OrderItemsCommentBO;
import wiki.laona.pojo.vo.MyCommentVO;
import wiki.laona.service.center.MyCommentsService;
import wiki.laona.service.impl.BaseService;
import wiki.laona.utils.PagedGridResult;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author laona
 * @description 用户中心 - 我的订单信息 service
 * @since 2022-05-13 15:26
 **/
@Service
public class MyCommentsServiceImpl extends BaseService implements MyCommentsService {

    @Autowired
    private Sid sid;

    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private OrderStatusMapper orderStatusMapper;
    @Autowired
    private OrderItemsMapper orderItemsMapper;
    @Autowired
    private ItemsCommentsMapperCustom itemsCommentsMapperCustom;

    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    @Override
    public List<OrderItems> queryPendingComment(String orderId) {

        OrderItems orderItems = new OrderItems();
        orderItems.setOrderId(orderId);

        return orderItemsMapper.select(orderItems);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void saveComments(String orderId, String userId, List<OrderItemsCommentBO> commentList) {

        // 1. 保存评价
        for (OrderItemsCommentBO commentBO : commentList) {
            commentBO.setCommentId(sid.nextShort());
        }
        Map<String, Object> map = new HashMap<>(1 << 4);
        map.put("userId", userId);
        map.put("commentList", commentList);
        itemsCommentsMapperCustom.saveComments(map);

        // 2. 修改订单评价
        Orders order = new Orders();
        order.setId(orderId);
        order.setIsComment(YesOrNo.YES.type);
        ordersMapper.updateByPrimaryKeySelective(order);

        // 3. 修改订单状态的留言时间
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(orderId);
        orderStatus.setCommentTime(new Date());
        orderStatusMapper.updateByPrimaryKeySelective(orderStatus);
    }

    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    @Override
    public PagedGridResult queryMyComments(String userId, Integer page, Integer pageSize) {

        Map<String, Object> paramsMap = new HashMap<>(1 << 4);
        paramsMap.put("userId", userId);

        PageHelper.startPage(page, pageSize);
        List<MyCommentVO> list = itemsCommentsMapperCustom.queryMyComments(paramsMap);

        return setterPageGrid(list, page);
    }

    // /**
    //  * 分页操作
    //  *
    //  * @param list 需要分页的列表
    //  * @param page 当前页码
    //  * @return 分页查询结果
    //  */
    // private PagedGridResult setterPageGrid(List<?> list, Integer page) {
    //     PageInfo<?> pageInfo = new PageInfo<>(list);
    //     PagedGridResult grid = new PagedGridResult();
    //     grid.setPage(page);
    //     grid.setRows(list);
    //     grid.setTotal(pageInfo.getPages());
    //     grid.setRecords(pageInfo.getTotal());
    //     return grid;
    // }
}
