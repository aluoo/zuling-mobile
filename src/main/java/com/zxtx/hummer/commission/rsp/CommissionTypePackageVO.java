package com.zxtx.hummer.commission.rsp;


import com.zxtx.hummer.common.domain.BaseEntity;
import com.zxtx.hummer.common.utils.StringUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 佣金方案表
 * </p>
 *
 * @author shenbh
 * @since 2023-03-06
 */
@Data
public class CommissionTypePackageVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long typeId;

    private String typeName;

    @ApiModelProperty("1数值2比例")
    private Integer type;

    @ApiModelProperty("方案类型名称")
    private String name;

    @ApiModelProperty("数值分佣金额上限")
    private Long maxCommissionFee;

    public String getMaxCommissionFee() {
        return StringUtils.fenToYuan(maxCommissionFee);
    }
}
