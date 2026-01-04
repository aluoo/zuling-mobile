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
public class ExchangeOrderSummaryVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("晒单总计")
    private Integer totalNum;

    @ApiModelProperty("换机晒单总计")
    private Integer exchangeNum;

    @ApiModelProperty("一键更新晒单总计")
    private Integer onekeyNum;

    @ApiModelProperty("快手绿洲晒单总计")
    private Integer lvzhouNum;

    @ApiModelProperty("苹果抖音晒单总计")
    private Integer pgdouyinNum;





}