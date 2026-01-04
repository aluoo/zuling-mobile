package com.zxtx.hummer.hk.domain.request;

import com.zxtx.hummer.common.domain.AbstractBaseQueryEntity;
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
public class HkSupplierQueryReq extends AbstractBaseQueryEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "状态 0下架 1上架")
    private Integer status;
}