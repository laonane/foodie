package wiki.laona.controller.center;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import wiki.laona.controller.BaseController;
import wiki.laona.service.center.MyOrdersService;
import wiki.laona.utils.JsonResult;
import wiki.laona.utils.PagedGridResult;

/**
 * @author laona
 * @description 用户中心 - 我的订单
 * @since 2022-05-13 15:52
 **/
@Api(value = "用户中心-我的订单信息", tags = {"我的订单信息的相关接口"})
@RestController
@RequestMapping("myorders")
public class MyOrdersController extends BaseController {

    @Autowired
    private MyOrdersService myOrdersService;

    @ApiOperation(value = "查询订单列表", notes = "查询订单列表", httpMethod = "POST")
    @PostMapping("/query")
    public JsonResult query(
            @ApiParam(name = "userId", value = "用户id", required = true) @RequestParam String userId,
            @ApiParam(name = "orderStatus", value = "订单状态", required = false) @RequestParam Integer orderStatus,
            @ApiParam(name = "page", value = "查询下一页是第几页", required = false) @RequestParam(defaultValue = "1") Integer page,
            @ApiParam(name = "pageSize", value = "分页的每一页显示的条数", required = false) @RequestParam Integer pageSize) {

        if (userId == null) {
            return JsonResult.errorMsg(null);
        }
        // 没有设置每页条数，则设置默认条数
        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }

        PagedGridResult result = myOrdersService.queryMyOrders(userId, orderStatus, page, pageSize);

        return JsonResult.ok(result);
    }
}
