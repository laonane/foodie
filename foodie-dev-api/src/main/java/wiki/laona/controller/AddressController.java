package wiki.laona.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import wiki.laona.pojo.UserAddress;
import wiki.laona.pojo.bo.AddressBO;
import wiki.laona.service.AddressService;
import wiki.laona.utils.JsonResult;
import wiki.laona.utils.MobileEmailUtils;

import java.util.List;

/**
 * @author laona
 * @description 用户地址controller
 * @create 2022-05-10 22:10
 **/
@Api(value = "地址相关", tags = {"地址相关的api接口"})
@RestController
@RequestMapping("/address")
public class AddressController extends BaseController {

    @Autowired
    private AddressService addressService;

    @ApiOperation(value = "根据用户id查询收货地址列表", notes = "根据用户id查询收货地址列表", httpMethod = "POST")
    @PostMapping("/list")
    public JsonResult list(@RequestParam String userId) {

        if (StringUtils.isBlank(userId)) {
            return JsonResult.errorMsg("");
        }

        List<UserAddress> list = addressService.queryAll(userId);

        return JsonResult.ok(list);
    }

    @ApiOperation(value = "用户新增地址", notes = "用户新增地址", httpMethod = "POST")
    @PostMapping("/add")
    public JsonResult add(@RequestBody AddressBO addressBO) {

        JsonResult checkResult = checkAddress(addressBO);
        if (!checkResult.isOK()) {
            return checkResult;
        }

        addressService.addNewUserAddress(addressBO);

        return JsonResult.ok();
    }

    /**
     * 检查地址BO有效性
     *
     * @param addressBO 地址信息
     * @return {@link JsonResult} result
     */
    private JsonResult checkAddress(AddressBO addressBO) {

        String receiver = addressBO.getReceiver();
        if (StringUtils.isBlank(receiver)) {
            return JsonResult.errorMsg("收货人不能为空");
        }
        if (receiver.length() > MAX_LEN_OF_RECEIVER_NAME) {
            return JsonResult.errorMsg("收货人姓名不能太长");
        }

        String mobile = addressBO.getMobile();
        if (StringUtils.isBlank(mobile)) {
            return JsonResult.errorMsg("收货人手机号不能为空");
        }
        if (mobile.length() > LEN_OF_PHONE_NUM) {
            return JsonResult.errorMsg("收货人手机号长度不正确");
        }
        boolean isMobileOk = MobileEmailUtils.checkMobileIsOk(mobile);
        if (!isMobileOk) {
            return JsonResult.errorMsg("收货人手机号格式不正确");
        }

        String province = addressBO.getProvince();
        String city = addressBO.getCity();
        String district = addressBO.getDistrict();
        String detail = addressBO.getDetail();
        if (StringUtils.isAnyBlank(province, city, district, detail)) {
            return JsonResult.errorMsg("收货地址信息不能为空");
        }

        return JsonResult.ok();
    }

    @ApiOperation(value = "用户修改地址", notes = "用户修改地址", httpMethod = "POST")
    @PostMapping("/update")
    public JsonResult update(@RequestBody AddressBO addressBO){

        if (StringUtils.isBlank(addressBO.getAddressId())) {
            return JsonResult.errorMsg("修改地址错误，addressId不能为空");
        }

        JsonResult checkResult = checkAddress(addressBO);
        if (!checkResult.isOK()){
            return checkResult;
        }

        addressService.updateUserAddress(addressBO);

        return JsonResult.ok();
    }

    @ApiOperation(value = "用户删除地址", notes = "用户删除地址", httpMethod = "POST")
    @PostMapping("/delete")
    public JsonResult delete(@RequestParam String userId, @RequestParam String addressId) {

        if (StringUtils.isBlank(userId)) {
            return JsonResult.errorMsg("");
        }

        addressService.deleteUserAddress(userId, addressId);

        return JsonResult.ok();
    }

    @ApiOperation(value = "用户设置默认地址", notes = "用户设置默认地址", httpMethod = "POST")
    @PostMapping("/setDefalut")
    public JsonResult setDefault(@RequestParam String userId, @RequestParam String addressId) {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(addressId)) {
            return JsonResult.errorMsg("");
        }

        addressService.updateUserAddressToBeDefault(userId,addressId);

        return JsonResult.ok();
    }
}
