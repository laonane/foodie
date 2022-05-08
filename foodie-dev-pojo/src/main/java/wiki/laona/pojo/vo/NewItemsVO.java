package wiki.laona.pojo.vo;

import lombok.Data;

import java.util.List;

/**
 * @author laona
 * @description 最新商品VO
 * @create 2022-05-09 00:37
 **/
@Data
public class NewItemsVO {
    private Integer rootCatId;
    private String rootCatName;
    private String slogan;
    private String catImage;
    private String bgColor;
    /**
     * 简单商品的最新商品推荐6条商品
     */
    private List<SimpleItemVO> simpleItemList;
}
