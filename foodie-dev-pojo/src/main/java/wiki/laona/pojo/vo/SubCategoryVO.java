package wiki.laona.pojo.vo;

/**
 * @author laona
 * @description 三级分类VO
 * @create 2022-05-08 18:56
 **/
public class SubCategoryVO {

    private Integer subId;
    private String subName;
    private String subType;
    private Integer subFatherId;

    public Integer getSubId() {
        return subId;
    }

    public void setSubId(Integer subId) {
        this.subId = subId;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public Integer getSubFatherId() {
        return subFatherId;
    }

    public void setSubFatherId(Integer subFatherId) {
        this.subFatherId = subFatherId;
    }

    @Override
    public String toString() {
        return "SubCategoryVO{" +
                "subId=" + subId +
                ", subName='" + subName + '\'' +
                ", subType='" + subType + '\'' +
                ", subFatherId=" + subFatherId +
                '}';
    }
}
