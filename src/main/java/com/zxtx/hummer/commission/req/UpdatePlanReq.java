package com.zxtx.hummer.commission.req;

import cn.hutool.core.util.NumberUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Data
public class UpdatePlanReq {

    @NotNull(message = "planId，不能为空")
    private Long planId;

    @NotNull(message = "佣金方案类型Id不能为空")
    private Long bizTypeId;

    private String planName;

    @Valid
    private List<Conf> planConf;


    private Long employeeId;

    private List<Long> members;


    @Data
    public static class Conf {
        //@NotNull
        private Long confId;

        private Long packageInfoId;

        @DecimalMin(value = "0.00", message = "childDivide：必须大于0")
        private BigDecimal childDivide;

        @ApiModelProperty("方案类型比例的时候传0,范围0-100")
        private BigDecimal childScale;

        public Long getChildDivide() {
            return NumberUtil.mul(childDivide, 100).toBigInteger().longValue();
        }

        public BigDecimal getChildScale() {
            return NumberUtil.div(childScale, 100);
        }

    }
}
