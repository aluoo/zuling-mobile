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
public class DiTypeVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @IgnoreExcelField
    private Long id;

    @ApiModelProperty("产品类型")
    @ExcelField(name = "产品类型")
    private String name;

    @ApiModelProperty("状态1正常2下线3删除")
    @ExcelField(name = "状态", replace = {"1_正常", "2_下线", "3_删除"})
    private Integer status;

    @ApiModelProperty("是否套餐1是0否")
    @ExcelField(name = "状态", replace = {"1_是", "0_否"})
    private Boolean packageAble;

    @ApiModelProperty("创建日期")
    @ExcelField(name = "创建日期", pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("更新日期")
    @ExcelField(name = "更新日期", pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;


}