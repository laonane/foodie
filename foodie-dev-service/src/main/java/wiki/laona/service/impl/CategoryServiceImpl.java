package wiki.laona.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import wiki.laona.mapper.CategoryMapper;
import wiki.laona.mapper.CategoryMapperCustom;
import wiki.laona.pojo.Category;
import wiki.laona.pojo.vo.CategoryVO;
import wiki.laona.pojo.vo.NewItemsVO;
import wiki.laona.service.CategoryService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author laona
 * @description 首页分类实现
 * @create 2022-05-08 16:48
 **/
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private CategoryMapperCustom categoryMapperCustom;

    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    @Override
    public List<Category> queryAllRootLevelCat() {
        Example example = new Example(Category.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("type", 1);
        List<Category> categories = categoryMapper.selectByExample(example);
        return categories;
    }

    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    @Override
    public List<CategoryVO> getSubCatList(Integer rootCatId) {
        List<CategoryVO> subCatList = categoryMapperCustom.getSubCatList(rootCatId);
        return subCatList;
    }

    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    @Override
    public List<NewItemsVO> getSixNewItemsLazy(Integer rootCatId) {

        Map<String, Object> map = new HashMap<>(1 << 4);
        map.put("rootCatId", rootCatId);

        return categoryMapperCustom.getSixNewItemsLazy(map);
    }
}
