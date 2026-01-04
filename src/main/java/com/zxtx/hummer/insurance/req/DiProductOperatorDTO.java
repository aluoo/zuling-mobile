package com.zxtx.hummer.insurance.req;

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
public class DiProductOperatorDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品ID编辑传")
    private Long id;

    @NotNull(message = "状态不能为空")
    @ApiModelProperty(value = "产品类型")
    private Long typeId;

    @ApiModelProperty(value = "产品类型")
    private Long packageId;

    @ApiModelProperty(value = "名称")
    @NotBlank(message = "商品名称不能为空")
    private String name;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "服务期限")
    private Integer period;


    @ApiModelProperty("选中的选项ID列表")
    private List<Long> optionIds;
}