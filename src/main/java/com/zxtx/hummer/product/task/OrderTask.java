package com.zxtx.hummer.product.task;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.zxtx.hummer.product.service.OrderTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/3/18
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Component
public class OrderTask {
    @Autowired
    private OrderTaskService orderTaskService;

    @XxlJob("autoCloseOverdueOrder")
    public void autoCloseOverdueOrder() {
        XxlJobHelper.log("==========自动关闭报价订单任务开始========");
        orderTaskService.autoCloseOverdueOrder();
        XxlJobHelper.log("==========自动关闭报价订单任务结束========");
    }

    @XxlJob("autoCloseOverdueOrderQuote")
    public void autoCloseOverdueOrderQuote() {
        XxlJobHelper.log("==========自动关闭报价订单报价功能任务开始========");
        orderTaskService.autoCloseOverdueOrderQuote();
        XxlJobHelper.log("==========自动关闭报价订单报价功能任务结束========");
    }

    @XxlJob("autoCloseOverdueShippingOrder")
    public void autoCloseOverdueShippingOrder() {
        XxlJobHelper.log("==========自动关闭发货订单任务开始========");
        orderTaskService.autoCloseOverdueShippingOrder();
        XxlJobHelper.log("==========自动关闭发货订单任务结束========");
    }
}