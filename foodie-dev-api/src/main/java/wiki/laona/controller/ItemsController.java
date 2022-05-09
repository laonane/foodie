package wiki.laona.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wiki.laona.pojo.Items;
import wiki.laona.pojo.ItemsImg;
import wiki.laona.pojo.ItemsParam;
import wiki.laona.pojo.ItemsSpec;
import wiki.laona.pojo.vo.ItemInfoVO;
import wiki.laona.service.ItemService;
import wiki.laona.utils.JsonResult;

import java.util.List;

/**
 * @author laona
 * @description 商品 controller
 * @date 2022-05-09 16:12
 **/
@Api(value = "商品接口", tags = {"商品信息展示相关接口"})
@RestController
@RequestMapping("items")
public class ItemsController {

    @Autowired
    private ItemService itemService;

    @ApiOperation(value = "查询商品详情", notes = "查询商品详情", httpMethod = "GET")
    @GetMapping("info/{itemId}")
    @ApiParam(name = "itemId", value = "商品id")
    public JsonResult info(@PathVariable String itemId) {

        if (StringUtils.isBlank(itemId)) {
            return JsonResult.errorMsg(null);
        }

        Items item = itemService.queryItemById(itemId);
        List<ItemsImg> itemImgList = itemService.queryItemImgList(itemId);
        List<ItemsSpec> itemSpecList = itemService.queryItemSpecList(itemId);
        ItemsParam itemParam = itemService.queryItemParam(itemId);

        return JsonResult.ok(new ItemInfoVO(item, itemImgList, itemSpecList, itemParam));
    }
}
