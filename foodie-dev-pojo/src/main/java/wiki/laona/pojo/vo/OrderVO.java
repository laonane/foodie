package wiki.laona.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author laona
 * @description 订单VO
 * @since 2022-05-11 23:44
 **/
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderVO {

    private String orderId;
    private MerchantOrdersVO merchantOrdersVO;
}
