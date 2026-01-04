package com.zxtx.hummer.insurance.req;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
public class DiInsurancePriceDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "产品ID")
    private Long insuranceId;

    private List<priceDTO> priseList;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class priceDTO implements Serializable {
        private static final long serialVersionUID = 1L;

        @ApiModelProperty("编辑时候给")
        private Long id;

        @ApiModelProperty("名称")
        private String name;

        @ApiModelProperty("进店价格(单位：分)")
        private Integer normalPrice;

        @ApiModelProperty("门店初始售价(单位：分)")
        private Integer salePrice;

        @ApiModelProperty("价格区间下限(单位：分)")
        private Integer priceLow;

        @ApiModelProperty("价格区间上限(单位：分)")
        private Integer priceHigh;

    }
}