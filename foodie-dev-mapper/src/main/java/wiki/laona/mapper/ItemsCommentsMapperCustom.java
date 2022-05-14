package wiki.laona.mapper;

import org.apache.ibatis.annotations.Param;
import wiki.laona.my.mapper.MyMapper;
import wiki.laona.pojo.ItemsComments;

import java.util.Map;

/**
 * @author huaian
 */
public interface ItemsCommentsMapperCustom extends MyMapper<ItemsComments> {

    public void saveComments(@Param("paramsMap") Map<String, Object> map );
}