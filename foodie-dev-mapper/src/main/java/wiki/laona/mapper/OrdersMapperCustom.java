package wiki.laona.mapper;

import org.apache.ibatis.annotations.Param;
import wiki.laona.pojo.vo.MyOrdersVO;

import java.util.List;
import java.util.Map;

/**
 * @author laona
 * @description 自定义分类mapper
 * @create 2022-05-08 18:30
 **/
public interface OrdersMapperCustom {

    /**
     * 查询我的订单
     *
     * @param map 参数
     * @return 订单信息
     */
    public List<MyOrdersVO> queryMyOrders(@Param("paramsMap") Map<String, Object> map);
}
