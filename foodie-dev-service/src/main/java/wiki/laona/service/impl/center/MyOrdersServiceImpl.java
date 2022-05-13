package wiki.laona.service.impl.center;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import wiki.laona.enums.OrderStatusEnum;
import wiki.laona.mapper.OrderStatusMapper;
import wiki.laona.mapper.OrdersMapperCustom;
import wiki.laona.pojo.OrderStatus;
import wiki.laona.pojo.vo.MyOrdersVO;
import wiki.laona.service.center.MyOrdersService;
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
public class MyOrdersServiceImpl implements MyOrdersService {

    @Autowired
    private OrdersMapperCustom ordersMapperCustom;
    @Autowired
    private OrderStatusMapper orderStatusMapper;

    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    @Override
    public PagedGridResult queryMyOrders(String userId, Integer orderStatus, Integer page, Integer pageSize) {

        Map<String, Object> paramsMap = new HashMap<>(1 << 4);
        paramsMap.put("userId", userId);
        if (orderStatus != null) {
            paramsMap.put("orderStatus", orderStatus);
        }

        PageHelper.startPage(page, pageSize);

        List<MyOrdersVO> list = ordersMapperCustom.queryMyOrders(paramsMap);

        return setterPageGrid(list, page);
    }

    /**
     * 分页操作
     *
     * @param list 需要分页的列表
     * @param page 当前页码
     * @return 分页查询结果
     */
    private PagedGridResult setterPageGrid(List<?> list, Integer page) {
        PageInfo<?> pageInfo = new PageInfo<>(list);
        PagedGridResult grid = new PagedGridResult();
        grid.setPage(page);
        grid.setRows(list);
        grid.setTotal(pageInfo.getPages());
        grid.setRecords(pageInfo.getTotal());
        return grid;
    }


    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void updateDeliverOrderStatus(String orderId) {

        OrderStatus waitReceiveOrderStatus = new OrderStatus();
        waitReceiveOrderStatus.setOrderId(orderId);
        waitReceiveOrderStatus.setOrderStatus(OrderStatusEnum.WAIT_RECEIVE.type);
        waitReceiveOrderStatus.setDeliverTime(new Date());

        Example example = new Example(OrderStatus.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderId", orderId);
        criteria.andEqualTo("orderStatus", OrderStatusEnum.WAIT_DELIVER.type);

        orderStatusMapper.updateByExampleSelective(waitReceiveOrderStatus, example);

    }
}
