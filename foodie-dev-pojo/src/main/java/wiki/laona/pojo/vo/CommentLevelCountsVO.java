package wiki.laona.pojo.vo;

import lombok.Data;

/**
 * @author laona
 * @description 商品的评论等级 VO
 * @since 2022-05-09 16:56
 **/
@Data
public class CommentLevelCountsVO {
    private  Integer totalCounts;
    private Integer goodCounts;
    private Integer normalCounts;
    private Integer badCounts;
}
