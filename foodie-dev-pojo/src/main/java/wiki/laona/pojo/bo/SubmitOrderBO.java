package wiki.laona.pojo.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author laona
 * @description 用于创建订单BO
 * @since 2022-05-11 14:22
 **/
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SubmitOrderBO {

    private String userId;
    private String itemSpecIds;
    private String addressId;
    private Integer payMethod;
    private String leftMsg;

}
