package wiki.laona.mapper;

import wiki.laona.pojo.vo.CategoryVO;

import java.util.List;

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
}
