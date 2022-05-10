package wiki.laona.service;

import wiki.laona.pojo.UserAddress;
import wiki.laona.pojo.bo.AddressBO;

import java.util.List;

/**
 * @author laona
 * @description 地址service
 * @create 2022-05-10 21:24
 **/
public interface AddressService {

    /**
     * 根据用户id查询用户的收货地址列表
     * @param userId 用户id
     * @return
     */
    public List<UserAddress> queryAll(String userId);

    /**
     * 用户新增地址
     * @param addressBO 地址BO
     */
    public void addNewUserAddress(AddressBO addressBO);

    /**
     * 用户修改地址
     * @param addressBO 地址BO
     */
    public void updateUserAddress(AddressBO addressBO);

    /**
     * 根据用户id和地址id，删除对应的用户地址信息
     * @param userId 用户id
     * @param addressId 地址id
     */
    public void deleteUserAddress(String userId, String addressId);

    /**
     * 修改默认地址
     * @param userId 用户id
     * @param addressId 地址id
     */
    public void updateUserAddressToBeDefault(String userId, String addressId);

    /**
     * 根据用户id和地址id，查询具体的用户地址对象信息
     * @param userId 用户id
     * @param addressId 地址id
     * @return {@linkplain  UserAddress 用户地址}
     */
    public UserAddress queryUserAddress(String userId, String addressId);
}
