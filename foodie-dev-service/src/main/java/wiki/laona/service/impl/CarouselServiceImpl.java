package wiki.laona.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import wiki.laona.mapper.CarouselMapper;
import wiki.laona.pojo.Carousel;
import wiki.laona.service.CarouselService;

import java.util.List;

/**
 * @author laona
 * @description 轮播图实现类
 * @create 2022-05-08 13:58
 **/
@Service
public class CarouselServiceImpl implements CarouselService {

    @Autowired
    private CarouselMapper carouselMapper;

    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    @Override
    public List<Carousel> queryAll(Integer isShow) {

        Example example = new Example(Carousel.class);
        // 降序
        example.orderBy("sort").desc();
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isShow", isShow);

        List<Carousel> carouselList = carouselMapper.selectByExample(example);

        return carouselList;
    }
}
