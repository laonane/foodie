package wiki.laona.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import wiki.laona.my.mapper.MyMapper;
import wiki.laona.pojo.Items;
import wiki.laona.pojo.vo.ItemCommentVO;

import java.util.List;
import java.util.Map;

@Repository
public interface ItemsMapperCustom extends MyMapper<Items> {

    /**
     * 查询商品的评论详情
     * @param paramsMap 参数
     * @return 评论详情
     */
    public List<ItemCommentVO> queryItemComments(@Param("paramsMap") Map<String, Object> map);
}
