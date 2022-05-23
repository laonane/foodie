package wiki.laona.enums;

/**
 * @author laona
 * @description Redis key
 * @since 2022-05-23 16:39
 **/
public enum KeyEnum {
    /**
     * 购物车
     */
    CAROUSEL("carousel"),
    /**
     * 商品分类（一级分类）
     */
    CATEGORY("cats"),
    /**
     * 商品子分类
     */
    SUB_CATEGORY("subCat")
    ;
    private String key;

    KeyEnum(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
