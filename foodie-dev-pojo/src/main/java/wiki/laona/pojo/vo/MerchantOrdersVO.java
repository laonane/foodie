package wiki.laona.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author laona
 * @description 商户订单VO
 * @since 2022-05-11 23:27
 **/
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MerchantOrdersVO {
    /**
     * 商户订单号
     */
    private String merchantOrderId;
    /**
     * 商户方的发起用户的用户主键id
     */
    private String merchantUserId;
    /**
     * 实际支付总金额(包含商户所支付的订单费、邮费)
     */
    private Integer amount;
    /**
     * 支付方式 1:微信   2:支付宝
     */
    private Integer payMethod;
    /**
     * 支付成功后的回调地址（自己设定）
     */
    private String returnUrl;
}
