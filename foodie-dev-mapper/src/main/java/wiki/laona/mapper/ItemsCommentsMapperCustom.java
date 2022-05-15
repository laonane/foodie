package wiki.laona.mapper;

import org.apache.ibatis.annotations.Param;
import wiki.laona.my.mapper.MyMapper;
import wiki.laona.pojo.ItemsComments;
import wiki.laona.pojo.vo.MyCommentVO;

import java.util.List;
import java.util.Map;

/**
 * @author huaian
 */
public interface ItemsCommentsMapperCustom extends MyMapper<ItemsComments> {

    /**
     * 保存评论
     *
     * @param map 参数
     */
    public void saveComments(@Param("paramsMap") Map<String, Object> map);

    /**
     * 查询用户的评论
     *
     * @param map 参数
     * @return 评论列表
     */
    public List<MyCommentVO> queryMyComments(@Param("paramsMap") Map<String, Object> map);
}