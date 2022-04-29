package wiki.laona.service;

import org.springframework.stereotype.Service;
import wiki.laona.pojo.Users;
import wiki.laona.pojo.bo.UserBo;

/**
 * @author laona
 * @description 用户接口
 * @date 2022-04-27 22:23
 **/
public interface UserService {


    /**
     * 判断用户名是否存在
     * @param username 用户名
     */
    public boolean queryUsernameIsExists(String username);

    /**
     * 新建用户
     * @param userBo userBo
     */
    public Users createUser(UserBo userBo);

    /**
     * 检索用户名和密码是否匹配，用于登录；
     * @param username 用户名
     * @param password 密码
     */
    public Users queryUserForLogin(String username, String password);
}
