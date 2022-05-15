package wiki.laona.service.impl;

import com.github.pagehelper.PageInfo;
import wiki.laona.utils.PagedGridResult;

import java.util.List;

/**
 * @author laona
 * @description 通用方法service
 * @since 2022-05-15 22:08
 **/
public class BaseService {

    /**
     * 分页操作
     *
     * @param list 需要分页的列表
     * @param page 当前页码
     * @return 分页查询结果
     */
    protected PagedGridResult setterPageGrid(List<?> list, Integer page) {
        PageInfo<?> pageInfo = new PageInfo<>(list);
        PagedGridResult grid = new PagedGridResult();
        grid.setPage(page);
        grid.setRows(list);
        grid.setTotal(pageInfo.getPages());
        grid.setRecords(pageInfo.getTotal());
        return grid;
    }
}
