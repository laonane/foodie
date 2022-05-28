package wiki.laona.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author laona
 * @description 用户VO
 * @since 2022-05-28 18:15
 **/
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UsersVO {

    /**
     * 主键id，用户id
     */
    private String id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 头像
     */
    private String face;
    /**
     * 性别：0:女 1:男 2:保密
     */
    private Integer sex;
    /**
     * 用户会话token
     */
    private String userUniqueToken;
}
