package wiki.laona.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * @author laona
 * @description 我的评论VO
 * @since 2022-05-15 21:46
 **/
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MyCommentVO {

    private String commentId;
    private String content;
    private Date createdTime;
    private String itemId;
    private String itemName;
    private String specName;
    private String itemImg;
}
