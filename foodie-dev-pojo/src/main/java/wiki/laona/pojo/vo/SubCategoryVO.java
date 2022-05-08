package wiki.laona.pojo.vo;

import lombok.Data;

/**
 * @author laona
 * @description 三级分类VO
 * @create 2022-05-08 18:56
 **/
@Data
public class SubCategoryVO {

    private Integer subId;
    private String subName;
    private String subType;
    private Integer subFatherId;
}
