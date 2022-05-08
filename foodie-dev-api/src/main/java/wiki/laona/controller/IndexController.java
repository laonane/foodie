package wiki.laona.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wiki.laona.enums.YesOrNo;
import wiki.laona.pojo.Carousel;
import wiki.laona.service.CarouselService;
import wiki.laona.utils.JsonResult;

import java.util.List;


/**
 * @author laona
 * @description hello controller
 * @date 2022-04-26 19:55
 **/
@Api(value = "首页", tags = {"首页展示相关接口"})
@RestController
@RequestMapping("index")
public class IndexController {

    @Autowired
    private CarouselService carouselService;

    @ApiOperation(value = "获取首页轮播图", notes = "获取首页轮播图", httpMethod = "GET")
    @GetMapping("/carousel")
    public JsonResult setSession() {
        List<Carousel> result = carouselService.queryAll(YesOrNo.YES.type);
        return JsonResult.ok(result);
    }
}
