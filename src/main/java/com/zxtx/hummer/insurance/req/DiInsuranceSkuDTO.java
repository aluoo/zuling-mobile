package com.zxtx.hummer.insurance.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/1/25
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiInsuranceSkuDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "产品ID")
    private Long skuId;

    private List<priceDTO> priseList;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class priceDTO implements Serializable {
        private static final long serialVersionUID = 1L;

        @ApiModelProperty("产品ID")
        private Long insuranceId;
        @ApiModelProperty("底价(单位：分)")
        private Long downPrice;


    }
}