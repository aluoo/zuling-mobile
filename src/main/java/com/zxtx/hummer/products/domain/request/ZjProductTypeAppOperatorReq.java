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
@ApiModel(value = "ZjProductTypeAppOperatorReq", description = "小程序产品类型操作请求对象")
public class ZjProductTypeAppOperatorReq implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @NotNull(message = "ID不能为空", groups = {Edit.class, Delete.class})
    private  Integer id;

    @ApiModelProperty(value = "种类")
    @NotBlank(message = "种类不能为空")
    private  String productCategory;

    @ApiModelProperty(value = "产品ID集合")
    private  String productIds;

    @ApiModelProperty(value = "图片URL")
    private  String img;

    @ApiModelProperty(value = "附属图片URL")
    private  String imgs;

    @ApiModelProperty(value = "跳转页")
    private  String jumpPage;

    @ApiModelProperty(value = "排序")
    private  Integer sort;

    @ApiModelProperty(value = "配置信息")
    private  String configInfo;

    @ApiModelProperty(value = "是否隐藏")
    private  Boolean hidden;

    public interface Edit {}
    public interface Delete {}
}
