package wiki.laona.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author laona
 * @description 订单状态统计VO
 * @since 2022-05-15 23:00
 **/
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatusCountsVO {

    Integer waitPayCounts;
    Integer waitDeliverCounts;
    Integer waitReceiveCounts;
    Integer waitCommentCounts;
}
