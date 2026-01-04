package com.zxtx.hummer.mobileStat.task;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.zxtx.hummer.commission.service.CommissionSettleDataService;
import com.zxtx.hummer.commission.service.CommissionSettleService;
import com.zxtx.hummer.mobileStat.service.CompanyDataDailyBaseService;
import com.zxtx.hummer.mobileStat.service.RecycleDataDailyBaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 佣金结算定时任务
 *
 * @author ：shenbh
 * @since ：2023.03.10
 */
@Component
@Slf4j
public class MobileStatTask {

    @Autowired
    private CompanyDataDailyBaseService companyDataDailyBaseService;
    @Autowired
    private RecycleDataDailyBaseService recycleDataDailyBaseService;


    @XxlJob("pullMobileCompanyStatByDay")
    public void pullMobileCompanyStatByDay() {
        XxlJobHelper.log("==========门店日统计开始========");
        String dateStr = XxlJobHelper.getJobParam();
        companyDataDailyBaseService.pullAllByDay(dateStr, false);
        XxlJobHelper.log("==========门店日统计结束========");
    }

    @XxlJob("pullMobileRecycleStatByDay")
    public void pullMobileRecycleStatByDay() {
        XxlJobHelper.log("==========回收商日统计开始========");
        String dateStr = XxlJobHelper.getJobParam();
        recycleDataDailyBaseService.pullAllByDay(dateStr, false);
        XxlJobHelper.log("==========回收商日统计结束========");
    }
}