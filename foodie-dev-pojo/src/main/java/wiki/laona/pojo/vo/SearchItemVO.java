package wiki.laona.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author laona
 * @description 用于展示商品搜索列表的VO
 * @since 2022-05-09 22:00
 **/
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SearchItemVO {

    private String itemId;
    private String itemName;
    private Integer sellCounts;
    private String imgUrl;
    private Integer price;
}
