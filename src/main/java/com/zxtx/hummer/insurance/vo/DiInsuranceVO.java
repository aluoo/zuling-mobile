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
public class DiInsuranceVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @IgnoreExcelField
    private Long id;

    @IgnoreExcelField
    private Long typeId;

    @IgnoreExcelField
    private Long packageId;

    @ApiModelProperty("产品名称")
    @ExcelField(name = "产品名称")
    private String name;

    @ApiModelProperty("产品描述")
    @ExcelField(name = "产品描述")
    private String description;

    @ApiModelProperty("所属套餐")
    @ExcelField(name = "所属套餐")
    private String packageName;

    @ApiModelProperty("所属类型")
    @ExcelField(name = "所属类型")
    private String typeName;

    @ApiModelProperty("服务期限")
    @ExcelField(name = "服务期限")
    private Integer period;

    @ApiModelProperty("状态")
    @ExcelField(name = "状态", replace = {"1_正常", "2_下线"})
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