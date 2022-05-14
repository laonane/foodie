package wiki.laona.service.center;

import wiki.laona.pojo.Users;
import wiki.laona.pojo.bo.center.CenterUsersBO;

/**
 * @author laona
 * @description 用户中心相关服务
 * @since 2022-05-12 16:58
 **/
public interface CenterUserService {

    /**
     * 根据用户id查询用户信息
     *
     * @param userId 用户id
     * @return 用户信息
     */
    public Users queryUserInfo(String userId);

    /**
     * 修改用户信息
     *
     * @param userId        userId
     * @param centerUsersBO centerUsersBO
     * @return 用户信息
     */
    public Users updateUserInfo(String userId, CenterUsersBO centerUsersBO);

    /**
     * 更新用户头像
     *
     * @param userId  用户id
     * @param faceUrl 用户头像
     * @return 用户头像
     */
    public Users updateUserFace(String userId, String faceUrl);
}
