package wiki.laona.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;
import wiki.laona.utils.JsonResult;
import wiki.laona.utils.RedisOperator;

import javax.annotation.Resource;

/**
 * @author laona
 * @description redisController
 * @since 2022-05-23 15:26
 **/
@ApiIgnore
@RestController
@RequestMapping("redis")
public class RedisController {

    // @Autowired
    // private RedisTemplate redisTemplate;

    @Resource
    private RedisOperator redisOperator;

    @GetMapping("set")
    public JsonResult set(String key, String value) {
        // redisTemplate.opsForValue().set(key, value);
        redisOperator.set(key, value);
        return JsonResult.ok();
    }

    @GetMapping("get")
    public JsonResult get(String key) {
        // String value = (String) redisTemplate.opsForValue().get(key);
        String value = redisOperator.get(key);
        return JsonResult.ok(value);
    }

    @GetMapping("delete")
    public JsonResult delete(String key) {
        // redisTemplate.delete(key);
        redisOperator.del(key);
        return JsonResult.ok();
    }
}
