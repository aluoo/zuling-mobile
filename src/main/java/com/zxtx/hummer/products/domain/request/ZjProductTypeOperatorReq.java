package com.zxtx.hummer.products.domain.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "ZjProductTypeOperatorReq", description = "商品类型操作请求对象")
public class ZjProductTypeOperatorReq implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @NotNull(message = "类型ID不能为空", groups = {Edit.class, Delete.class})
    private Integer id;

    @ApiModelProperty(value = "上级ID")
    private Integer upid;

    @ApiModelProperty(value = "类型（1级分类，2级品牌）")
    private Integer type;

    @ApiModelProperty(value = "类型名称")
    @NotBlank(message = "类型名称不能为空")
    private String tit;

    @ApiModelProperty(value = "介绍")
    private String cont;

    @ApiModelProperty(value = "图标")
    private String image;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "是否隐藏")
    private Boolean hidden;

    public interface Edit {}
    public interface Delete {}

}