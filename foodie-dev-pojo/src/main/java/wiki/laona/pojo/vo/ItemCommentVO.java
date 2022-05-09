package wiki.laona.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * @author laona
 * @description 用于展示商品评论的VO
 * @since 2022-05-09 17:52
 **/
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ItemCommentVO {

    private Integer commentLevel;
    private String content;
    private String specName;
    private Date createdTime;
    private String userFace;
    private String nickname;
}
