package com.zxtx.hummer.hk.domain.request;

import com.zxtx.hummer.common.domain.AbstractBaseQueryEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
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
public class HkRelationCompanyReq extends AbstractBaseQueryEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("员工ID")
    private Long employeeId;
    @ApiModelProperty("号卡套餐ID")
    @NotNull(message = "号卡套餐ID不能为空")
    private Long productId;
}