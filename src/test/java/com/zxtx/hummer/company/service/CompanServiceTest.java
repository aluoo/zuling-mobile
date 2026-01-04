package com.zxtx.hummer.company.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zxtx.hummer.HummerApplication;
import com.zxtx.hummer.HummerApplicationTest;
import com.zxtx.hummer.account.constant.EmployAccountChangeEnum;
import com.zxtx.hummer.commission.enums.CommissionBizType;
import com.zxtx.hummer.commission.enums.CommissionPackage;
import com.zxtx.hummer.commission.service.CommissionPlanService;
import com.zxtx.hummer.commission.service.CommissionSettleService;
import com.zxtx.hummer.common.Constants;
import com.zxtx.hummer.company.domain.Company;
import com.zxtx.hummer.packageInfo.service.PackageInfoService;
import com.zxtx.hummer.product.dao.BrandMapper;
import com.zxtx.hummer.product.service.BrandService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("production")
@SpringBootTest(classes = {HummerApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//配置启动类
@Slf4j
public class CompanServiceTest  {
    @Autowired
    private CompanyService companyService;
    @Autowired
    private CommissionPlanService commissionPlanService;

    @Autowired
    CommissionSettleService commissionSettleService;

    @Autowired
    PackageInfoService packageInfoService;


    @Test
    public void test() {
        List<Company> companyList =  companyService.lambdaQuery()
                .eq(Company::getPId, Constants.LAN_HAI_CMP_ID).eq(Company::getStatus,2).list();

        for(Company company:companyList){
            commissionPlanService.insuranceCreate(company.getEmployeeId());
        }
    }

    @Test
    public void test2() {
        Long id = 20240628170302L;
        Long shopAmount = 20000L;
        commissionSettleService.orderScaleBindSettleRule(Long.valueOf(shopAmount),id, CommissionBizType.INSURANCE_SERVICE, CommissionPackage.INSURANCE_SERVICE,1247910694330961920L);
        commissionSettleService.waitSettleOrder(id, CommissionBizType.INSURANCE_SERVICE, CommissionPackage.INSURANCE_SERVICE.getType(),1247910694330961920L,null, EmployAccountChangeEnum.insurance_commission.getRemark());
    }

    @Test
    public void test3() {
        List<Company> companyList = companyService.lambdaQuery().eq(Company::getStatus,2).list();
        for(Company company:companyList){
            packageInfoService.saveDefault(company.getId());
        }

    }

}