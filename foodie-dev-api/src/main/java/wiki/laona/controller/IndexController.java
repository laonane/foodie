package wiki.laona.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wiki.laona.enums.KeyEnum;
import wiki.laona.enums.YesOrNo;
import wiki.laona.pojo.Carousel;
import wiki.laona.pojo.Category;
import wiki.laona.pojo.vo.CategoryVO;
import wiki.laona.pojo.vo.NewItemsVO;
import wiki.laona.service.CarouselService;
import wiki.laona.service.CategoryService;
import wiki.laona.utils.JsonResult;
import wiki.laona.utils.JsonUtils;
import wiki.laona.utils.RedisOperator;

import java.util.ArrayList;
import java.util.List;


/**
 * @author laona
 * @description 首页 controller
 * @date 2022-04-26 19:55
 **/
@Api(value = "首页", tags = {"首页展示相关接口"})
@RestController
@RequestMapping("index")
public class IndexController {

    @Autowired
    private CarouselService carouselService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private RedisOperator redisOperator;

    @ApiOperation(value = "获取首页轮播图", notes = "获取首页轮播图", httpMethod = "GET")
    @GetMapping("/carousel")
    public JsonResult setSession() {

        List<Carousel> list = new ArrayList<>();

        // 判断 redis 之中是否有轮播图，有则直接返回
        String carouselStr = redisOperator.get("carousel");
        if (StringUtils.hasText(carouselStr)) {
            list = JsonUtils.jsonToList(carouselStr, Carousel.class);
        } else {
            // 查询数据库中的数据
            list = carouselService.queryAll(YesOrNo.YES.type);
            // 存入 redis
            redisOperator.set("carousel", JsonUtils.objectToJson(list));
        }
        return JsonResult.ok(list);
    }

    /**
     * 首页分类展示需求：
     * 1. 第一次刷新主页查询大分类，渲染展示到首页
     * 2. 如果鼠标上移到大分类，则加载其子分类的内容，如果已经存在子分类，则不需要加载（懒加载）
     */
    @ApiOperation(value = "获取商品分类（一级分类）", notes = "获取商品分类（一级分类）", httpMethod = "GET")
    @GetMapping("/cats")
    public JsonResult cats() {

        List<Category> categoryList = new ArrayList<>();

        String categoryStr = redisOperator.get(KeyEnum.CATEGORY.getKey());

        if (StringUtils.hasText(categoryStr)) {
            categoryList = JsonUtils.jsonToList(categoryStr, Category.class);
        } else {
            categoryList = categoryService.queryAllRootLevelCat();
            redisOperator.set(KeyEnum.CATEGORY.getKey(), JsonUtils.objectToJson(categoryList));
        }

        return JsonResult.ok(categoryList);
    }

    @ApiOperation(value = "获取商品子分类", notes = "获取商品子分类", httpMethod = "GET")
    @ApiParam(name = "rootCatId", value = "一级分类 id", required = true)
    @GetMapping("/subCat/{rootCatId}")
    public JsonResult cats(@PathVariable Integer rootCatId) {

        if (rootCatId == null) {
            return JsonResult.errorMsg("分类不存在");
        }
        // subCategory redis键
        final String subCategoryKey = KeyEnum.SUB_CATEGORY.getKey() + ":" + rootCatId;
        List<CategoryVO> subCatList = new ArrayList<>();

        String subCategoryJson = redisOperator.get(subCategoryKey);
        if (StringUtils.hasText(subCategoryJson)) {
            subCatList = JsonUtils.jsonToList(subCategoryJson, CategoryVO.class);
        } else {
            subCatList = categoryService.getSubCatList(rootCatId);
            // 下面的情况会导致redis缓存穿透
            if (!CollectionUtils.isEmpty(subCatList)) {
                // 查询到有数据才保存到 redis 中
                redisOperator.set(subCategoryKey, JsonUtils.objectToJson(subCatList));
            }else {
                redisOperator.set(subCategoryKey, JsonUtils.objectToJson(subCatList), 5 * 60);
            }
        }

        return JsonResult.ok(subCatList);
    }

    @ApiOperation(value = "查询每个一级分类下的最新6条商品", notes = "查询每个一级分类下的最新6条商品", httpMethod = "GET")
    @ApiParam(name = "rootCatId", value = "一级分类 id", required = true)
    @GetMapping("/sixNewItems/{rootCatId}")
    public JsonResult sixNewItems(@PathVariable Integer rootCatId) {

        if (rootCatId == null) {
            return JsonResult.errorMsg("分类不存在");
        }
        List<NewItemsVO> list = categoryService.getSixNewItemsLazy(rootCatId);
        return JsonResult.ok(list);
    }
}
