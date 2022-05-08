package wiki.laona.service;

import wiki.laona.pojo.Category;
import wiki.laona.pojo.vo.CategoryVO;

import java.util.List;

/**
 * @author laona
 * @description 首页分类
 * @create 2022-05-08 16:56
 **/
public interface CategoryService {

    /**
     * 查询所有一级分类
     *
     * @return 轮播图列表
     */
    public List<Category> queryAllRootLevelCat();

    /**
     * 根据一级分类 id 查询子分类信息
     * @param rootCatId 一级分类 id
     * @return 子分类列表
     */
    public List<CategoryVO> getSubCatList(Integer rootCatId);
}
