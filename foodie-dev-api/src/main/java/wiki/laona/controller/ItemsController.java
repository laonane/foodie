package wiki.laona.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import wiki.laona.pojo.Items;
import wiki.laona.pojo.ItemsImg;
import wiki.laona.pojo.ItemsParam;
import wiki.laona.pojo.ItemsSpec;
import wiki.laona.pojo.vo.CommentLevelCountsVO;
import wiki.laona.pojo.vo.ItemInfoVO;
import wiki.laona.service.ItemService;
import wiki.laona.utils.JsonResult;
import wiki.laona.utils.PagedGridResult;

import java.util.List;

/**
 * @author laona
 * @description 商品 controller
 * @date 2022-05-09 16:12
 **/
@Api(value = "商品接口", tags = {"商品信息展示相关接口"})
@RestController
@RequestMapping("items")
public class ItemsController extends BaseController{

    @Autowired
    private ItemService itemService;

    @ApiOperation(value = "查询商品详情", notes = "查询商品详情", httpMethod = "GET")
    @GetMapping("info/{itemId}")
    public JsonResult info(@ApiParam(name = "itemId", value = "商品id", required = false) @PathVariable String itemId) {

        if (StringUtils.isBlank(itemId)) {
            return JsonResult.errorMsg(null);
        }

        Items item = itemService.queryItemById(itemId);
        List<ItemsImg> itemImgList = itemService.queryItemImgList(itemId);
        List<ItemsSpec> itemSpecList = itemService.queryItemSpecList(itemId);
        ItemsParam itemParam = itemService.queryItemParam(itemId);

        ItemInfoVO itemInfo = new ItemInfoVO();
        itemInfo.setItem(item);
        itemInfo.setItemImgList(itemImgList);
        itemInfo.setItemSpecList(itemSpecList);
        itemInfo.setItemParams(itemParam);

        return JsonResult.ok(itemInfo);
    }

    @ApiOperation(value = "查询商品评论等级", notes = "查询商品评论等级", httpMethod = "GET")
    @GetMapping("/commentLevel")
    public JsonResult commentLevel(
            @ApiParam(name = "itemId", value = "商品id", required = false) @RequestParam String itemId) {

        if (StringUtils.isBlank(itemId)) {
            return JsonResult.errorMsg(null);
        }

        CommentLevelCountsVO countsVO = itemService.queryCommentCounts(itemId);

        return JsonResult.ok(countsVO);
    }


    @ApiOperation(value = "查询商品评论详情", notes = "查询商品评论详情", httpMethod = "GET")
    @GetMapping("/comments")
    public JsonResult comments(
            @ApiParam(name = "itemId", value = "商品id", required = true) @RequestParam String itemId,
            @ApiParam(name = "level", value = "评论等级", required = false) @RequestParam(defaultValue = "0") Integer level,
            @ApiParam(name = "page", value = "查询下一页是第几页", required = false) @RequestParam(defaultValue = "1") Integer page,
            @ApiParam(name = "pageSize", value = "分页的每一页显示的条数", required = false) @RequestParam Integer pageSize) {

        if (StringUtils.isBlank(itemId)) {
            return JsonResult.errorMsg(null);
        }
        // 没有设置每页条数，则设置默认条数
        if (pageSize == null) {
            pageSize = COMMENT_PAGE_SIZE;
        }
        /*  bug fix: 解决查看全部评论时，level为空的命令 */
        if (level == 0) {
            level = null;
        }
        PagedGridResult result = itemService.queryPagedComments(itemId, level, page, pageSize);

        return JsonResult.ok(result);
    }


    @ApiOperation(value = "搜索商品列表", notes = "搜索商品列表", httpMethod = "GET")
    @GetMapping("/search")
    public JsonResult comments(
            @ApiParam(name = "keywords", value = "搜索关键字", required = true) @RequestParam String keywords,
            @ApiParam(name = "sort", value = "排序规则", required = false) @RequestParam(defaultValue = "k") String sort,
            @ApiParam(name = "page", value = "查询下一页是第几页", required = false) @RequestParam(defaultValue = "1") Integer page,
            @ApiParam(name = "pageSize", value = "分页的每一页显示的条数", required = false) @RequestParam Integer pageSize) {

        if (StringUtils.isBlank(keywords)) {
            return JsonResult.errorMsg(null);
        }
        // 没有设置每页条数，则设置默认条数
        if (pageSize == null) {
            pageSize = PAGE_SIZE;
        }
        PagedGridResult result = itemService.searchItems(keywords, sort, page, pageSize);

        return JsonResult.ok(result);
    }
}
