package wiki.laona.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;
import wiki.laona.utils.JsonResult;
import wiki.laona.utils.RedisOperator;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * @author laona
 * @description redisController
 * @since 2022-05-23 15:26
 **/
// @Api("redis")
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

    /**
     * 批量查询 mget
     *
     * @param keys 查询keys
     * @return Result
     */
    @GetMapping("mget")
    public JsonResult mget(String... keys) {

        List<String> keyList = Arrays.asList(keys);
        List<String> result = redisOperator.mget(keyList);

        return JsonResult.ok(result);
    }

    /**
     * 批量查询 batch
     * @param keys
     * @return
     */
    @GetMapping("batchGet")
    public JsonResult batchGet(String... keys) {

        List<String> keyList = Arrays.asList(keys);
        List<Object> result = redisOperator.batchGet(keyList);

        return JsonResult.ok(result);
    }

}
