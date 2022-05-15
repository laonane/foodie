package wiki.laona.controller.center;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import wiki.laona.controller.BaseController;
import wiki.laona.enums.YesOrNo;
import wiki.laona.pojo.OrderItems;
import wiki.laona.pojo.Orders;
import wiki.laona.pojo.bo.center.OrderItemsCommentBO;
import wiki.laona.service.center.MyCommentsService;
import wiki.laona.utils.JsonResult;
import wiki.laona.utils.PagedGridResult;

import java.util.List;

/**
 * @author laona
 * @description 我的评价
 * @since 2022-05-14 15:25
 **/
@Api(value = "用户中心-评价管理", tags = {"我的评价管理的相关接口"})
@RestController
@RequestMapping("mycomments")
public class MyCommentsController extends BaseController {

    @Autowired
    private MyCommentsService myCommentsService;

    @ApiOperation(value = "查询评价信息", notes = "查询评价信息", httpMethod = "POST")
    @PostMapping("/pending")
    public JsonResult pending(
            @ApiParam(name = "userId", value = "用户id", required = true) @RequestParam String userId,
            @ApiParam(name = "orderId", value = "订单id", required = false) @RequestParam String orderId) {

        // 判断用户和订单是否关联
        JsonResult checkResult = checkUserOrder(userId, orderId);
        if (checkResult.getStatus() != HttpStatus.OK.value()){
            return checkResult;
        }
        // 判断每笔订单是否已经评论过，评价过就不在评论
        Orders myOrders = (Orders) checkResult.getData();
        if (ObjectUtils.nullSafeEquals(myOrders.getIsComment(), YesOrNo.YES.type)){
            return JsonResult.errorMsg("当前商品已经评价!");
        }

        List<OrderItems> result = myCommentsService.queryPendingComment(orderId);

        return JsonResult.ok(result);
    }

    @ApiOperation(value = "保存评价列表", notes = "保存评价列表", httpMethod = "POST")
    @PostMapping("/saveList")
    public JsonResult saveList(
            @ApiParam(name = "userId", value = "用户id", required = true) @RequestParam String userId,
            @ApiParam(name = "orderId", value = "订单id", required = false) @RequestParam String orderId,
            @RequestBody List<OrderItemsCommentBO> commentList ) {


        // 判断用户和订单是否关联
        JsonResult checkResult = checkUserOrder(userId, orderId);
        if (checkResult.getStatus() != HttpStatus.OK.value()){
            return checkResult;
        }
        // 判断评论内容list不能为空
        if (CollectionUtils.isEmpty(commentList)) {
            return JsonResult.errorMsg("评论内容不能为空");
        }

        myCommentsService.saveComments(orderId, userId, commentList);

        return JsonResult.ok();
    }


    @ApiOperation(value = "查询我的评论", notes = "查询我的评论", httpMethod = "POST")
    @PostMapping("/query")
    public JsonResult query(
            @ApiParam(name = "userId", value = "用户id", required = true) @RequestParam String userId,
            @ApiParam(name = "page", value = "查询下一页是第几页", required = false) @RequestParam(defaultValue = "1") Integer page,
            @ApiParam(name = "pageSize", value = "分页的每一页显示的条数", required = false) @RequestParam Integer pageSize) {

        if (userId == null) {
            return JsonResult.errorMsg(null);
        }
        // 没有设置每页条数，则设置默认条数
        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }

        PagedGridResult result = myCommentsService.queryMyComments(userId, page, pageSize);

        return JsonResult.ok(result);
    }
}
