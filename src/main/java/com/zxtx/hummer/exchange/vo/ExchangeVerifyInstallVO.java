package com.zxtx.hummer.exchange.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zxtx.hummer.common.annotation.ExcelField;
import com.zxtx.hummer.common.annotation.IgnoreExcelField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author chenjian
 * @create 2023/5/22 13:52
 * @desc 代理对象
 **/
@Data
public class ExchangeVerifyInstallVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @IgnoreExcelField
    private Long id;

    @ApiModelProperty("安装包名称")
    @ExcelField(name = "安装包名称")
    private String name;

    @ApiModelProperty("渠道编码")
    @ExcelField(name = "渠道编码")
    private String channelNo;

    @ApiModelProperty("类型编码")
    @ExcelField(name = "类型编码")
    private String typeCode;

    @ApiModelProperty("类型名称")
    @ExcelField(name = "类型名称")
    private String typeName;


    @ExcelField(name = "下载地址")
    @ApiModelProperty("下载地址")
    private String downUrl;

    @ExcelField(name = "关联门店")
    @ApiModelProperty("关联门店")
    private Integer companyNum;

    @ApiModelProperty("状态")
    @ExcelField(name = "状态", replace = {"1_正常", "0_禁用"})
    private Integer status;

    @ApiModelProperty("创建日期")
    @ExcelField(name = "创建日期", pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("更新日期")
    @ExcelField(name = "更新日期", pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;


}