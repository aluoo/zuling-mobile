package com.zxtx.hummer.commission.task;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.zxtx.hummer.commission.service.CommissionSettleDataService;
import com.zxtx.hummer.commission.service.CommissionSettleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 佣金结算定时任务
 *
 * @author ：shenbh
 * @since ：2023.03.10
 */
@Component
@Slf4j
public class CommissionSettleTask {

    @Autowired
    private CommissionSettleService settleService;
    @Autowired
    private CommissionSettleDataService commissionSettleDataService;


    @XxlJob("pullMobileSettleDataByDay")
    public void pullAllCommissionSettleDataByDay() {
        XxlJobHelper.log("==========佣金统计任务开始========");
        String dateStr = XxlJobHelper.getJobParam();
        commissionSettleDataService.pullAllByDay(dateStr, false);
        XxlJobHelper.log("==========佣金统计任务结束========");
    }
}