package wiki.laona.pojo.vo;

import lombok.Data;

import java.util.List;

/**
 * @author laona
 * @description 二级分类VO
 * @create 2022-05-08 18:56
 **/
@Data
public class CategoryVO {

    private Integer id;
    private String name;
    private String type;
    private Integer fatherId;

    /**
     * 三级分类vo list
     */
    private List<SubCategoryVO> subCatList;
}
