package com.zxtx.hummer.hk.domain.request;

import com.zxtx.hummer.hk.domain.enums.HkApplyOrderStatusEnum;
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
 * @Date 2025/8/1
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HkProductEditReq implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @NotNull(message = "ID不能为空")
    private Long id;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "运营商ID")
    @NotNull(message = "运营商不能为空")
    private Long operatorId;
    @ApiModelProperty(value = "供应商ID")
    @NotNull(message = "供应商不能为空")
    private Long supplierId;
    @ApiModelProperty(value = "名称")
    @NotBlank(message = "名称不能为空")
    private String name;
    @ApiModelProperty(value = "编码")
    @NotBlank(message = "编码不能为空")
    private String code;
    @ApiModelProperty(value = "状态 0下架 1上架")
    private Integer status;

    @ApiModelProperty(value = "价格")
    @NotBlank(message = "价格不能为空")
    private String price;
    @ApiModelProperty(value = "是否选号 0否1是")
    @NotNull(message = "是否选号不能为空")
    private Boolean requireSelectMobile;
    /**
     * @see HkApplyOrderStatusEnum
     */
    @ApiModelProperty(value = "分佣条件（订单状态）")
    private Integer commissionStatus;

    @ApiModelProperty(value = "列表主图")
    private String masterImage;
    @ApiModelProperty(value = "详情图")
    private String detailImage;
    @ApiModelProperty(value = "卖点1")
    private String sellPointOne;
    @ApiModelProperty(value = "卖点2")
    private String sellPointTwo;
    @ApiModelProperty(value = "卖点3")
    private String sellPointThree;
    @ApiModelProperty(value = "标签1")
    private String tagOne;
    @ApiModelProperty(value = "标签2")
    private String tagTwo;
    @ApiModelProperty(value = "标签3")
    private String tagThree;

    @ApiModelProperty(value = "归属地-省")
    private String province;
    @ApiModelProperty(value = "归属地-市")
    private String city;
}