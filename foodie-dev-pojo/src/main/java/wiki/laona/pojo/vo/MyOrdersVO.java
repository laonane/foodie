package wiki.laona.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * @author laona
 * @description 用户中心 - 我的订单列表VO
 * @since 2022-05-13 14:54
 **/
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MyOrdersVO {

    private String orderId;
    private Date createdTime;
    private Integer payMethod;
    private Integer realPayAmount;
    private Integer postAmount;
    private Integer isComment;
    private Integer orderStatus;

    private List<MySubOrderItemVO> subOrderItemList;
}
