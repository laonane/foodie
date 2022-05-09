package wiki.laona.service;

import wiki.laona.pojo.Items;
import wiki.laona.pojo.ItemsImg;
import wiki.laona.pojo.ItemsParam;
import wiki.laona.pojo.ItemsSpec;

import java.util.List;

/**
 * @author laona
 * @description 商品信息接口
 * @date 2022-05-09 15:50
 **/
public interface ItemService {

    /**
     * 根据商品 id 查询商品详情
     * @param itemId 商品 id
     * @return 商品详情
     */
    public Items queryItemById(String itemId);

    /**
     * 根据商品 id 查询商品图片列表
     * @param itemId 商品 id
     * @return 商品图片列表
     */
    public List<ItemsImg> queryItemImgList(String itemId);

    /**
     * 根据商品 id 查询商品规格
     * @param itemId 商品 id
     * @return 商品规格
     */
    public List<ItemsSpec> queryItemSpecList(String itemId);

    /**
     * 根据商品 id 查询商品参数
     * @param itemId 商品 id
     * @return 商品参数
     */
    public ItemsParam queryItemParam(String itemId);
}
