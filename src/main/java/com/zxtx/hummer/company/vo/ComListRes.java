package com.zxtx.hummer.company.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ComListRes {

    @ApiModelProperty(value = "门店ID")
    private Long id;

    @ApiModelProperty(value = "门店名称")
    private String name;

    @ApiModelProperty(value = "门店负责人")
    private String contact;

    @ApiModelProperty(value = "门店负责人手机")
    private String contactMobile;

    @ApiModelProperty(value = "门店类型")
    private Integer type;

    @ApiModelProperty(value = "门店类型")
    private String typeName;

    @ApiModelProperty(value = "拉新模式")
    private Integer exchangeType;

    @ApiModelProperty(value = "拉新模式")
    private String exchangeTypeName;

    @ApiModelProperty(value = "门店照片")
    private String frontUrl;

    @ApiModelProperty(value = "营业执照")
    private String busLicense;

    @ApiModelProperty(value = "身份证正面")
    private String idUrlUp;

    @ApiModelProperty(value = "身份证反面")
    private String idUrlDown;

    @ApiModelProperty(value = "门店状态")
    private Integer status;

    @ApiModelProperty(value = "门店状态中文")
    private String statusName;

    @ApiModelProperty(value = "门店省份")
    private String province;

    @ApiModelProperty(value = "门店省份编码")
    private String provinceCode;

    @ApiModelProperty(value = "门店市")
    private String city;

    @ApiModelProperty(value = "门店市编码")
    private String cityCode;

    @ApiModelProperty(value = "门店区")
    private String region;

    @ApiModelProperty(value = "门店区编码")
    private String regionCode;

    @ApiModelProperty(value = "门店地址")
    private String address;

    @ApiModelProperty(value = "门店负责人ID")
    private Long employeeId;

    @ApiModelProperty(value = "负责BD")
    private String bdName;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String createTime;


}