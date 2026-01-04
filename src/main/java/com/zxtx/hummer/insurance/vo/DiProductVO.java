package com.zxtx.hummer.insurance.vo;

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
public class DiProductVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("编号")
    @ExcelField(name = "编号")
    private Long id;

    @ApiModelProperty("手机型号")
    @ExcelField(name = "手机型号")
    private String name;

    @ApiModelProperty(value = "系列")
    @ExcelField(name = "系列")
    private String categoryFullName;

    @ApiModelProperty(value = "是否关联产品")
    @IgnoreExcelField
    private Boolean productContact;

    @ApiModelProperty(value = "是否基础配置")
    @IgnoreExcelField
    private Boolean baseConfig;



}