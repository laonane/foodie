package wiki.laona.service;

import wiki.laona.pojo.Carousel;

import java.util.List;

/**
 * @author laona
 * @description 轮播图service
 * @create 2022-05-08 13:56
 **/
public interface CarouselService {

    /**
     * 查询所有轮播图
     *
     * @param isShow 是否展示
     * @return 轮播图列表
     */
    public List<Carousel> queryAll(Integer isShow);
}
