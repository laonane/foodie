package wiki.laona.service.impl;


import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;
import wiki.laona.enums.YesOrNo;
import wiki.laona.mapper.UserAddressMapper;
import wiki.laona.pojo.UserAddress;
import wiki.laona.pojo.bo.AddressBO;
import wiki.laona.service.AddressService;

import java.util.Date;
import java.util.List;

/**
 * @author laona
 * @description 用户地址服务
 * @create 2022-05-10 21:36
 **/
@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private Sid sid;
    @Autowired
    private UserAddressMapper addressMapper;

    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    @Override
    public List<UserAddress> queryAll(String userId) {

        Example userAddressExample = new Example(UserAddress.class);
        Example.Criteria criteria = userAddressExample.createCriteria();
        criteria.andEqualTo("userId", userId);

        return addressMapper.selectByExample(userAddressExample);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void addNewUserAddress(AddressBO addressBO) {

        // 判断当前用户是否存在默认地址，如果无则当前地址为默认地址
        int isDefault = 0;
        List<UserAddress> addressList = this.queryAll(addressBO.getAddressId());
        if (CollectionUtils.isEmpty(addressList)) {
            isDefault = 1;
        }

        // 设置地址主键
        String addressId = sid.nextShort();

        // 保存地址
        UserAddress userAddress = new UserAddress();

        BeanUtils.copyProperties(addressBO, userAddress);
        userAddress.setId(addressId);
        userAddress.setIsDefault(isDefault);
        userAddress.setCreatedTime(new Date());
        userAddress.setUpdatedTime(new Date());

        addressMapper.insert(userAddress);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void updateUserAddress(AddressBO addressBO) {

        String addressId = addressBO.getAddressId();

        UserAddress pendingAddress = new UserAddress();
        BeanUtils.copyProperties(addressBO, pendingAddress);

        pendingAddress.setId(addressId);
        pendingAddress.setUpdatedTime(new Date());

        addressMapper.updateByPrimaryKeySelective(pendingAddress);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void deleteUserAddress(String userId, String addressId) {

        UserAddress toDeleteUserAddress = new UserAddress();
        toDeleteUserAddress.setId(addressId);
        toDeleteUserAddress.setUserId(userId);

        addressMapper.delete(toDeleteUserAddress);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void updateUserAddressToBeDefault(String userId, String addressId) {

        // 1. 查找默认地址，设置为不默认
        UserAddress oldDefaultUserAddress = new UserAddress();
        oldDefaultUserAddress.setUserId(userId);
        oldDefaultUserAddress.setIsDefault(YesOrNo.YES.type);
        List<UserAddress> oldDefaultUserAddressList = addressMapper.select(oldDefaultUserAddress);
        for (UserAddress defaultItem : oldDefaultUserAddressList) {
            defaultItem.setIsDefault(YesOrNo.NO.type);
            addressMapper.updateByPrimaryKeySelective(defaultItem);
        }

        // 2. 根据地址id修改为默认的地址
        UserAddress defaultAddress = new UserAddress();
        defaultAddress.setId(addressId);
        defaultAddress.setUserId(userId);
        defaultAddress.setIsDefault(YesOrNo.YES.type);
        addressMapper.updateByPrimaryKeySelective(defaultAddress);
    }

    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    @Override
    public UserAddress queryUserAddress(String userId, String addressId) {

        UserAddress singleUserAddress = new UserAddress();
        singleUserAddress.setUserId(userId);
        singleUserAddress.setId(addressId);

        return addressMapper.selectOne(singleUserAddress);
    }
}
