package wiki.laona.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import wiki.laona.enums.CommentLevel;
import wiki.laona.enums.YesOrNo;
import wiki.laona.mapper.*;
import wiki.laona.pojo.*;
import wiki.laona.pojo.vo.CommentLevelCountsVO;
import wiki.laona.pojo.vo.ItemCommentVO;
import wiki.laona.pojo.vo.SearchItemVO;
import wiki.laona.pojo.vo.ShopcartVO;
import wiki.laona.service.ItemService;
import wiki.laona.utils.DesensitizationUtil;
import wiki.laona.utils.PagedGridResult;

import java.util.*;

/**
 * @author laona
 * @description 商品信息服务实现类
 * @date 2022-05-09 15:50
 **/
@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemsMapper itemsMapper;
    @Autowired
    private ItemsImgMapper itemsImgMapper;
    @Autowired
    private ItemsSpecMapper itemsSpecMapper;
    @Autowired
    private ItemsParamMapper itemsParamMapper;
    @Autowired
    private ItemsCommentsMapper itemsCommentsMapper;
    @Autowired
    private ItemsMapperCustom itemsMapperCustom;

    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    @Override
    public Items queryItemById(String itemId) {
        return itemsMapper.selectByPrimaryKey(itemId);
    }

    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    @Override
    public List<ItemsImg> queryItemImgList(String itemId) {
        Example itemsImgExample = new Example(ItemsImg.class);
        Example.Criteria criteria = itemsImgExample.createCriteria();
        criteria.andEqualTo("itemId", itemId);

        return itemsImgMapper.selectByExample(itemsImgExample);
    }

    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    @Override
    public List<ItemsSpec> queryItemSpecList(String itemId) {
        Example itemsSpecExample = new Example(ItemsImg.class);
        Example.Criteria criteria = itemsSpecExample.createCriteria();
        criteria.andEqualTo("itemId", itemId);

        return itemsSpecMapper.selectByExample(itemsSpecExample);
    }

    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    @Override
    public ItemsParam queryItemParam(String itemId) {
        Example itemsParamExample = new Example(ItemsImg.class);
        Example.Criteria criteria = itemsParamExample.createCriteria();
        criteria.andEqualTo("itemId", itemId);

        return itemsParamMapper.selectOneByExample(itemsParamExample);
    }

    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    @Override
    public CommentLevelCountsVO queryCommentCounts(String itemId) {

        Integer goodCounts = getCommentCounts(itemId, CommentLevel.GOOD.type);
        Integer normalCounts = getCommentCounts(itemId, CommentLevel.NORMAL.type);
        Integer badCounts = getCommentCounts(itemId, CommentLevel.BAD.type);
        Integer totalCounts = goodCounts + normalCounts + badCounts;

        CommentLevelCountsVO countsVO = new CommentLevelCountsVO();
        countsVO.setTotalCounts(totalCounts);
        countsVO.setGoodCounts(goodCounts);
        countsVO.setNormalCounts(normalCounts);
        countsVO.setBadCounts(badCounts);

        return countsVO;
    }

    /**
     * 根据itemId 评论等级查询评论数量
     *
     * @param itemId 商品id
     * @param level  评论等级
     * @return 评论数量
     */
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    Integer getCommentCounts(String itemId, Integer level) {
        ItemsComments condition = new ItemsComments();
        condition.setItemId(itemId);
        if (level != null) {
            condition.setCommentLevel(level);
        }
        return itemsCommentsMapper.selectCount(condition);
    }

    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    @Override
    public PagedGridResult queryPagedComments(String itemId, Integer level, Integer page, Integer pageSize) {
        Map<String, Object> paramsMap = new HashMap<>(1 << 4);
        paramsMap.put("itemId", itemId);
        paramsMap.put("level", level);

        PageHelper.startPage(page, pageSize);
        List<ItemCommentVO> list = itemsMapperCustom.queryItemComments(paramsMap);
        for (ItemCommentVO vo : list) {
            vo.setNickname(DesensitizationUtil.commonDisplay(vo.getNickname()));
        }
        return setterPageGrid(list, page);
    }

    /**
     * 分页操作
     *
     * @param list 需要分页的列表
     * @param page 当前页码
     * @return 分页查询结果
     */
    private PagedGridResult setterPageGrid(List<?> list, Integer page) {
        PageInfo<?> pageInfo = new PageInfo<>(list);
        PagedGridResult grid = new PagedGridResult();
        grid.setPage(page);
        grid.setRows(list);
        grid.setTotal(pageInfo.getPages());
        grid.setRecords(pageInfo.getTotal());
        return grid;
    }

    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    @Override
    public PagedGridResult searchItems(String keywords, String sort, Integer page, Integer pageSize) {
        Map<String, Object> paramsMap = new HashMap<>(1 << 4);
        paramsMap.put("keywords", keywords);
        paramsMap.put("sort", sort);

        PageHelper.startPage(page, pageSize);
        List<SearchItemVO> list = itemsMapperCustom.searchItems(paramsMap);

        return setterPageGrid(list, page);
    }

    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    @Override
    public PagedGridResult searchItemsByThirdCat(Integer catId, String sort, Integer page, Integer pageSize) {
        Map<String, Object> paramsMap = new HashMap<>(1 << 4);
        paramsMap.put("catId", catId);
        paramsMap.put("sort", sort);

        PageHelper.startPage(page, pageSize);
        List<SearchItemVO> list = itemsMapperCustom.searchItemsByThirdCat(paramsMap);

        return setterPageGrid(list, page);
    }

    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    @Override
    public List<ShopcartVO> queryItemsBySpecIds(String specIds) {

        String[] ids = specIds.split(",");
        List<String> specIdList = new ArrayList<>();
        Collections.addAll(specIdList, ids);

        return itemsMapperCustom.queryItemsBySpecIds(specIdList);
    }

    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    @Override
    public ItemsSpec queryItemSpecById(String itemSpecId) {
        return itemsSpecMapper.selectByPrimaryKey(itemSpecId);
    }

    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    @Override
    public String queryItemMainImgById(String itemId) {

        ItemsImg itemImg = new ItemsImg();
        itemImg.setItemId(itemId);
        itemImg.setIsMain(YesOrNo.YES.type);

        ItemsImg result = itemsImgMapper.selectOne(itemImg);

        return ObjectUtils.isNotEmpty(result) ? result.getUrl() : "";
    }

    /**
     * synchronized: 不推荐是用，集群下无用，性能低
     * 锁数据库：不推荐，导致数据库性能低下
     * 分布式锁：zookeeper  redis
     *
     * ---
     * 这里使用数据库锁进行
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void decreaseItemSpecStock(String specId, int buyCounts) {
        // 1. 查询库存，

        // 2. 判断库存，是否能够减少

        int result = itemsMapperCustom.decreaseItemSpecStock(specId, buyCounts);
        if (result != 1) {
            throw new RuntimeException("订单创建失败，原因：库存不足");
        }
    }
}
