package com.zxtx.hummer.common.service;

import com.zxtx.hummer.HummerApplication;
import com.zxtx.hummer.commission.req.CommissionSettleCheckReq;
import com.zxtx.hummer.commission.service.CommissionPlanService;
import com.zxtx.hummer.commission.service.CommissionSettleCheckService;
import com.zxtx.hummer.exchange.domain.dto.ExchangeOrderCommissionBackDTO;
import com.zxtx.hummer.exchange.service.MbExchangeOrderService;
import com.zxtx.hummer.mobileStat.service.CompanyDataDailyBaseService;
import com.zxtx.hummer.mobileStat.service.RecycleDataDailyBaseService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 字典管理测试类
 *
 * @author shenbh
 * @since 2023-03-08
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("production")
@SpringBootTest(classes = {HummerApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//配置启动类
@Slf4j
public class DictServiceTest {
    @Autowired
    private DictService dictService;
    @Autowired
    private CommissionPlanService commissionPlanService;
    @Autowired
    private RecycleDataDailyBaseService recycleDataDailyBaseService;
    @Autowired
    private CompanyDataDailyBaseService companyDataDailyBaseService;
    @Autowired
    private CommissionSettleCheckService commissionSettleCheckService;
    @Autowired
    private MbExchangeOrderService exchangeOrderService;


    @Test
    public void getByType() {
        commissionPlanService.create(1202668257356857345L);
    }

    @Test
    public void recycleStat() {
        recycleDataDailyBaseService.pullAllByDay("20240304", false);
    }

    @Test
    public void companyStat() {
        companyDataDailyBaseService.pullAllByDay("20240305", false);
    }

    @Test
    public void settleCheck() {
        CommissionSettleCheckReq req = new CommissionSettleCheckReq();
        req.setBdId(1250531213140168705L);
        commissionSettleCheckService.fix(req);
    }

    public void settleFix() {
        commissionSettleCheckService.fixAll();
    }

    @Test
    public void settleBack() {
        exchangeOrderService.commissionBack(ExchangeOrderCommissionBackDTO.builder().id(1820728911475625985L).build());
    }



}