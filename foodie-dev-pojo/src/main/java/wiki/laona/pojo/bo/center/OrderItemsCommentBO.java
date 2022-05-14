package wiki.laona.pojo.bo.center;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author laona
 * @description 订单商品评论详情
 * @since 2022-05-14 15:57
 **/
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemsCommentBO {

    private String commentId;
    private String itemId;
    private String itemName;
    private String itemSpecId;
    private String itemSpecName;
    private Integer commentLevel;
    private String content;
}
