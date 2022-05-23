package wiki.laona.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;
import wiki.laona.utils.JsonResult;

/**
 * @author laona
 * @description redisController
 * @since 2022-05-23 15:26
 **/
@ApiIgnore
@RestController
@RequestMapping("redis")
public class RedisController {

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("set")
    public JsonResult set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
        return JsonResult.ok();
    }

    @GetMapping("get")
    public JsonResult get(String key) {
        String value = (String) redisTemplate.opsForValue().get(key);
        return JsonResult.ok(value);
    }

    @GetMapping("delete")
    public JsonResult delete(String key) {
        redisTemplate.delete(key);
        return JsonResult.ok();
    }
}
