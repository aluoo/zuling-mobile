package com.zxtx.hummer.insurance.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author chenjian
 * @create 2023/5/22 13:52
 * @desc 代理对象
 **/
@Data
public class DiProductInsuranceDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("手机产品ID")
    @NotNull(message = "手机产品ID不能为空")
    private Long productId;

    @ApiModelProperty("数保产品ID列表")
    private List<Long> insuranceIds;

    @ApiModelProperty("产品数组")
    List<InsuranceProductDetailDTO> insuranceList;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class InsuranceProductDetailDTO implements Serializable {
        private static final long serialVersionUID = 1L;

        @ApiModelProperty("手机产品ID")
        // @NotNull(message = "手机产品ID不能为空")
        private Long productId;

        @ApiModelProperty("数保产品ID")
        @NotNull(message = "数保产品ID不能为空")
        private Long insuranceId;

    }


}