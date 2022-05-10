package wiki.laona.service;

import wiki.laona.pojo.Items;
import wiki.laona.pojo.ItemsImg;
import wiki.laona.pojo.ItemsParam;
import wiki.laona.pojo.ItemsSpec;
import wiki.laona.pojo.vo.CommentLevelCountsVO;
import wiki.laona.pojo.vo.ShopcartVO;
import wiki.laona.utils.PagedGridResult;

import java.util.List;

/**
 * @author laona
 * @description 商品信息接口
 * @date 2022-05-09 15:50
 **/
public interface ItemService {

    /**
     * 根据商品 id 查询商品详情
     *
     * @param itemId 商品 id
     * @return 商品详情
     */
    public Items queryItemById(String itemId);

    /**
     * 根据商品 id 查询商品图片列表
     *
     * @param itemId 商品 id
     * @return 商品图片列表
     */
    public List<ItemsImg> queryItemImgList(String itemId);

    /**
     * 根据商品 id 查询商品规格
     *
     * @param itemId 商品 id
     * @return 商品规格
     */
    public List<ItemsSpec> queryItemSpecList(String itemId);

    /**
     * 根据商品 id 查询商品参数
     *
     * @param itemId 商品 id
     * @return 商品参数
     */
    public ItemsParam queryItemParam(String itemId);

    /**
     * 查询当前商品的评论等级数量
     *
     * @param itemId 商品 id
     * @return 评论数量
     */
    public CommentLevelCountsVO queryCommentCounts(String itemId);

    /**
     * 根据商品id 查询商品的评价（分页）
     *
     * @param itemId   商品 id
     * @param level    评论等级
     * @param page     当前页码
     * @param pageSize 每页条数
     * @return 评价
     */
    public PagedGridResult queryPagedComments(String itemId, Integer level, Integer page, Integer pageSize);

    /**
     * 搜索商品列表
     *
     * @param keywords 搜索关键字
     * @param sort     排序规则：k: 默认，代表默认排序，根据name进行排序
     *                 c: 根据销量排序
     *                 p: 根据价格排序
     * @param page     当前页码
     * @param pageSize 每页条数
     * @return 商品列表
     */
    public PagedGridResult searchItems(String keywords, String sort, Integer page, Integer pageSize);

    /**
     * 搜索商品列表（按照三级分类id）
     *
     * @param catId    三级分类id
     * @param sort     排序规则：k: 默认，代表默认排序，根据name进行排序
     *                 c: 根据销量排序
     *                 p: 根据价格排序
     * @param page     当前页码
     * @param pageSize 每页条数
     * @return 商品列表
     */
    public PagedGridResult searchItemsByThirdCat(Integer catId, String sort, Integer page, Integer pageSize);

    /**
     * 根据规格ids查询最新购物车中商品数据（用于渲染刷新购物车中的商品数据）
     *
     * @param specIds 拼接的规格ids
     * @return 购物车中的商品数据
     */
    public List<ShopcartVO> queryItemsBySpecIds(String specIds);
}
