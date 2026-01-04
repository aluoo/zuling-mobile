package com.zxtx.hummer.hk.domain.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * @author WangWJ
 * @Description
 * @Date 2025/8/1
 * @Copyright
 * @Version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HkCommonPropertiesVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("运营商")
    private Map<Long, String> operator;
    @ApiModelProperty("供应商")
    private Map<Long, String> supplier;
    @ApiModelProperty("订单状态（结算条件）")
    private Map<Integer, String> orderStatus;
    @ApiModelProperty("套餐状态")
    private Map<Integer, String> productStatus;
}