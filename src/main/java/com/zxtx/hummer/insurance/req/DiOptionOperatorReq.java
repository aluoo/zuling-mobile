package com.zxtx.hummer.insurance.req;

import com.zxtx.hummer.product.domain.validator.ProductValidatorGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/1/26
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiOptionOperatorReq implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @NotNull(message = "选项ID不能为空", groups = {ProductValidatorGroup.Edit.class, ProductValidatorGroup.Switch.class, ProductValidatorGroup.Delete.class})
    private Long id;
    @ApiModelProperty(value = "名称")
    @NotBlank(message = "选项名称不能为空", groups = {ProductValidatorGroup.Add.class, ProductValidatorGroup.Edit.class})
    private String name;
    @ApiModelProperty(value = "描述")
    private String description;
    @ApiModelProperty(value = "排序")
    private Integer sort;
    @ApiModelProperty(value = "1单选 2多选 3图片")
    @NotNull(message = "类型不能为空", groups = {ProductValidatorGroup.Add.class, ProductValidatorGroup.Edit.class})
    private Integer type;
    @ApiModelProperty(value = "是否必填0否1是")
    private Boolean required;
    @ApiModelProperty(value = "上级ID")
    private Long parentId;

    @ApiModelProperty(value = "示例图")
    private String sampleImage;

    @ApiModelProperty(value = "CODE")
    //@NotBlank(message = "code不能为空", groups = {ProductValidatorGroup.Add.class})
    private String code;
}