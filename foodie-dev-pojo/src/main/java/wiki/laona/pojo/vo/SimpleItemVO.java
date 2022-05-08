package wiki.laona.pojo.vo;

/**
 * @author laona
 * @description 6个商品的简单数据类型
 * @create 2022-05-09 00:41
 **/
public class SimpleItemVO {
    private String itemId;
    private String itemName;
    private String itemUrl;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemUrl() {
        return itemUrl;
    }

    public void setItemUrl(String itemUrl) {
        this.itemUrl = itemUrl;
    }

    @Override
    public String toString() {
        return "SimpleItemVO{" +
                "itemId=" + itemId +
                ", itemName='" + itemName + '\'' +
                ", itemUrl='" + itemUrl + '\'' +
                '}';
    }
}
