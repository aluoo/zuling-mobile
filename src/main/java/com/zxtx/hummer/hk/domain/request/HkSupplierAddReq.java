package com.zxtx.hummer.hk.domain.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author WangWJ
 * @Description
 * @Date 2025/8/1
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HkSupplierAddReq implements Serializable {
    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "名称")
    @NotBlank(message = "名称不能为空")
    private String name;
    @ApiModelProperty(value = "状态 0下架 1上架")
    private Integer status;
}