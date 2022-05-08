package wiki.laona.service.impl;

import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;
import wiki.laona.enums.Sex;
import wiki.laona.mapper.UsersMapper;
import wiki.laona.pojo.Users;
import wiki.laona.pojo.bo.UserBo;
import wiki.laona.service.UserService;
import wiki.laona.utils.DateUtil;
import wiki.laona.utils.MD5Utils;

import java.util.Date;

/**
 * @author laona
 * @description 用户 service
 * @date 2022-04-28 14:20
 **/
@Service
public class UserServiceImpl implements UserService {

    public UsersMapper usersMapper;

    @Autowired
    public void setUsersMapper(UsersMapper usersMapper) {
        this.usersMapper = usersMapper;
    }

    private Sid sid;

    @Autowired
    public void setSid(Sid sid) {
        this.sid = sid;
    }

    private static final String USER_FACE = "http://122.152.205.72:88/group1/M00/00/05/CpoxxFw_8_qAIlFXAAAcIhVPdSg994.png";

    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    @Override
    public boolean queryUsernameIsExists(String username) {

        Example userExample = new Example(Users.class);
        Example.Criteria usersCriteria = userExample.createCriteria();

        usersCriteria.andEqualTo("username", username);

        Users result = usersMapper.selectOneByExample(userExample);

        return !ObjectUtils.isEmpty(result);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public Users createUser(UserBo userBo) {

        String userId = sid.nextShort();

        Users user = new Users();
        user.setId(userId);
        user.setUsername(userBo.getUsername());
        try {
            user.setPassword(MD5Utils.getMD5Str(userBo.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 默认用户昵称同用户名
        user.setNickname(userBo.getUsername());
        // 默认头像
        user.setFace(USER_FACE);
        // 默认生日
        user.setBirthday(DateUtil.stringToDate("1900-01-01"));
        // 默认性别：保密
        user.setSex(Sex.secret.type);
        // 设置日期
        user.setCreatedTime(new Date());
        user.setUpdatedTime(new Date());
        // 保存用户
        usersMapper.insert(user);
        return user;
    }

    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    @Override
    public Users queryUserForLogin(String username, String password) {
        Example userExample = new Example(Users.class);
        Example.Criteria userCriteria = userExample.createCriteria();

        userCriteria.andEqualTo("username", username);
        userCriteria.andEqualTo("password", password);

        return usersMapper.selectOneByExample(userExample);
    }
}
