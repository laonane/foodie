package wiki.laona.mapper;

import org.apache.ibatis.annotations.Param;
import wiki.laona.pojo.vo.CategoryVO;
import wiki.laona.pojo.vo.NewItemsVO;

import java.util.List;
import java.util.Map;

/**
 * @author laona
 * @description 自定义分类mapper
 * @create 2022-05-08 18:30
 **/
public interface CategoryMapperCustom {

    /**
     * 获取一级分类下面的所有子分类
     *
     * @param rootCatId 父类分类id
     * @return 子分类列表
     */

    public List<CategoryVO> getSubCatList(Integer rootCatId);

    /**
     * 获取首页推荐的商品信息
     * @param map map
     * @return 商品信息
     */
    public List<NewItemsVO> getSixNewItemsLazy(@Param("paramMap") Map<String, Object> map);
}
