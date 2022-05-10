package wiki.laona.pojo.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author laona
 * @description 购物车 BO
 * @since 2022-05-10 15:44
 **/
@ApiModel(value = "购物车BO", description = "购物车数据BO")
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ShopcartBO {
    @ApiModelProperty("商品ID")
    private String itemId;
    @ApiModelProperty("商品图片链接")
    private String itemImgUrl;
    private String itemName;
    private String specId;
    private String specName;
    private Integer buyCounts;
    private String priceDiscount;
    private String priceNormal;
}
