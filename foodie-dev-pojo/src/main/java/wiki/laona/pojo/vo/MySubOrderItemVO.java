package wiki.laona.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author laona
 * @description 用户中心 - 我的订单嵌套商品VO
 * @since 2022-05-13 14:56
 **/
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MySubOrderItemVO {

    private String itemId;
    private String itemName;
    private String itemImg;
    private String itemSpecName;
    private Integer buyCounts;
    private Integer price;
}
