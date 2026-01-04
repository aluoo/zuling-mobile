package com.zxtx.hummer.commission.req;

import com.zxtx.hummer.common.domain.AbstractBaseQueryEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import java.io.Serializable;

/**
 * @author chenjian
 * @Description
 * @Date 2025/16/44
 * @Copyright
 * @Version 1.0
 */
@Data
public class CommissionQueryReq extends AbstractBaseQueryEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("分佣方案ID")
    private Long typeId;
}