package com.zxtx.hummer.company.req;

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
 * @Date 2024/10/22
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyBaseInfoEditReq implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("门店ID")
    @NotNull(message = "ID不能为空")
    private Long id;

    @ApiModelProperty("门店名称")
    @NotBlank(message = "名称不能为空")
    private String name;

    @ApiModelProperty(value = "门店省份")
    @NotBlank(message = "省不能为空")
    private String province;

    @ApiModelProperty(value = "门店省份编码")
    @NotBlank(message = "省编码不能为空")
    private String provinceCode;

    @ApiModelProperty(value = "门店市")
    @NotBlank(message = "市不能为空")
    private String city;

    @ApiModelProperty(value = "门店市编码")
    @NotBlank(message = "市编码不能为空")
    private String cityCode;

    @ApiModelProperty(value = "门店区")
    @NotBlank(message = "区不能为空")
    private String region;

    @ApiModelProperty(value = "门店区编码")
    @NotBlank(message = "区编码不能为空")
    private String regionCode;

    @ApiModelProperty(value = "门店地址")
    @NotBlank(message = "地址不能为空")
    private String address;

    @ApiModelProperty(value = "门店照片")
    @NotBlank(message = "门店照片不能为空")
    private String frontUrl;

    @ApiModelProperty(value = "营业执照")
    @NotBlank(message = "营业执照不能为空")
    private String busLicense;

    @ApiModelProperty(value = "拉新模式 3换机 4一键拉新")
    private Integer exchangeType;
}