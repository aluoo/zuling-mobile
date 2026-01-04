package com.zxtx.hummer.mobileStat.dto;

import cn.hutool.core.util.NumberUtil;
import com.zxtx.hummer.common.annotation.ExcelField;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/8/12
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IndexStatisticsVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(name = "开始时间", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty("结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(name = "结束时间", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @ApiModelProperty("拉新总晒单数")
    @ExcelField(name = "拉新总晒单数")
    @Builder.Default
    private Long exchangeOrderAllTotal = 0L;
    @ApiModelProperty("拉新总通过数")
    @ExcelField(name = "拉新总通过数")
    @Builder.Default
    private Long exchangeOrderAllPassTotal = 0L;
    @ApiModelProperty("拉新总通过率")
    @ExcelField(name = "拉新总通过率")
    @Builder.Default
    private String exchangeOrderAllPassRateTotal = "0.00";

    @ApiModelProperty("换机晒单数")
    @ExcelField(name = "换机晒单数")
    @Builder.Default
    private Long exchangeOrderHJTotal = 0L;
    @ApiModelProperty("换机通过数")
    @ExcelField(name = "换机通过数")
    @Builder.Default
    private Long exchangeOrderHJPassTotal = 0L;
    @ApiModelProperty("换机通过率")
    @ExcelField(name = "换机通过率")
    @Builder.Default
    private String exchangeOrderHJPassRate = "0.00";

    @ApiModelProperty("快手绿洲晒单数")
    @ExcelField(name = "快手绿洲晒单数")
    @Builder.Default
    private Long exchangeOrderKSLZTotal = 0L;
    @ApiModelProperty("快手绿洲通过数")
    @ExcelField(name = "快手绿洲通过数")
    @Builder.Default
    private Long exchangeOrderKSLZPassTotal = 0L;
    @ApiModelProperty("快手绿洲通过率")
    @ExcelField(name = "快手绿洲通过率")
    @Builder.Default
    private String exchangeOrderKSLZPassRate = "0.00";

    @ApiModelProperty("苹果抖音晒单数")
    @ExcelField(name = "苹果抖音晒单数")
    @Builder.Default
    private Long exchangeOrderPGDYTotal = 0L;
    @ApiModelProperty("苹果抖音通过数")
    @ExcelField(name = "苹果抖音通过数")
    @Builder.Default
    private Long exchangeOrderPGDYPassTotal = 0L;
    @ApiModelProperty("苹果抖音通过率")
    @ExcelField(name = "苹果抖音通过率")
    @Builder.Default
    private String exchangeOrderPGDYPassRate = "0.00";

    @ApiModelProperty("数保投保总数")
    @ExcelField(name = "数保投保总数")
    @Builder.Default
    private Long insuranceOrderAllTotal = 0L;
    @ApiModelProperty("数保优享Care+(单)")
    @ExcelField(name = "数保优享Care+(单)")
    @Builder.Default
    private Long insuranceOrderCareTotal = 0L;
    @ApiModelProperty("数保碎屏(单)")
    @ExcelField(name = "数保碎屏(单)")
    @Builder.Default
    private Long insuranceOrderSPBTotal = 0L;
    @ApiModelProperty("数保延保(单)")
    @ExcelField(name = "数保延保(单)")
    @Builder.Default
    private Long insuranceOrderYBTotal = 0L;

    public String getExchangeOrderAllPassRateTotal() {
        if (exchangeOrderAllTotal == null || exchangeOrderAllPassTotal == 0) {
            return "0.00";
        }
        BigDecimal rate = NumberUtil.div(new BigDecimal(exchangeOrderAllPassTotal),new BigDecimal(exchangeOrderAllTotal),2, RoundingMode.HALF_UP)
                .multiply(new BigDecimal(100));
        return rate.toString();
    }

    public String getExchangeOrderHJPassRate() {
        if (exchangeOrderHJTotal == null || exchangeOrderHJTotal == 0) {
            return "0.00";
        }
        BigDecimal rate = NumberUtil.div(new BigDecimal(exchangeOrderHJPassTotal),new BigDecimal(exchangeOrderHJTotal),2, RoundingMode.HALF_UP)
                .multiply(new BigDecimal(100));
        return rate.toString();
    }

    public String getExchangeOrderKSLZPassRate() {
        if (exchangeOrderKSLZTotal == null || exchangeOrderKSLZTotal == 0) {
            return "0.00";
        }
        BigDecimal rate = NumberUtil.div(new BigDecimal(exchangeOrderKSLZPassTotal),new BigDecimal(exchangeOrderKSLZTotal),2, RoundingMode.HALF_UP)
                .multiply(new BigDecimal(100));
        return rate.toString();
    }

    public String getExchangeOrderPGDYPassRate() {
        if (exchangeOrderPGDYTotal == null || exchangeOrderPGDYTotal == 0) {
            return "0.00";
        }
        BigDecimal rate = NumberUtil.div(new BigDecimal(exchangeOrderPGDYPassTotal),new BigDecimal(exchangeOrderPGDYTotal),2, RoundingMode.HALF_UP)
                .multiply(new BigDecimal(100));
        return rate.toString();
    }
}