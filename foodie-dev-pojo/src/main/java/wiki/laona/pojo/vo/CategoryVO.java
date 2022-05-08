package wiki.laona.pojo.vo;

import java.util.List;

/**
 * @author laona
 * @description 二级分类VO
 * @create 2022-05-08 18:56
 **/
public class CategoryVO {

    private Integer id;
    private String name;
    private String type;
    private Integer fatherId;

    /**
     * 三级分类vo list
     */
    private List<SubCategoryVO> subCatList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getFatherId() {
        return fatherId;
    }

    public void setFatherId(Integer fatherId) {
        this.fatherId = fatherId;
    }

    public List<SubCategoryVO> getSubCatList() {
        return subCatList;
    }

    public void setSubCatList(List<SubCategoryVO> subCatList) {
        this.subCatList = subCatList;
    }

    @Override
    public String toString() {
        return "CategoryVO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", fatherId=" + fatherId +
                ", subCatList=" + subCatList +
                '}';
    }
}
