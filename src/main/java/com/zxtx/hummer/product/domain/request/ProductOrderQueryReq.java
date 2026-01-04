package com.zxtx.hummer.product.domain.request;

import com.zxtx.hummer.common.domain.AbstractBaseQueryEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/5/21
 * @Copyright
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ApiModel(value = "手机订单查询对象")
public class ProductOrderQueryReq extends AbstractBaseQueryEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "订单ID")
    private Long id;
    @ApiModelProperty(value = "订单编码")
    private String orderNo;
    @ApiModelProperty(value = "IMEI号")
    private String imeiNo;
}