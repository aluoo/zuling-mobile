package com.zxtx.hummer.company.service;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.math.Money;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.zxtx.hummer.account.constant.EmployAccountChangeEnum;
import com.zxtx.hummer.account.service.EmployeeAccountChangeService;
import com.zxtx.hummer.common.exception.BusinessException;
import com.zxtx.hummer.common.utils.ShiroUtils;
import com.zxtx.hummer.common.utils.StringUtils;
import com.zxtx.hummer.company.dao.mapper.RecycleRechargeLogMapper;
import com.zxtx.hummer.company.domain.Company;
import com.zxtx.hummer.company.domain.RecycleRechargeLog;
import com.zxtx.hummer.company.enums.RecycleRechargeStatus;
import com.zxtx.hummer.company.req.RechargeCheckReq;
import com.zxtx.hummer.company.req.RecycleRechargeReq;
import com.zxtx.hummer.company.vo.RecycleRechargeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author peng can
 * @date 2022/12/1
 */
@Service
public class RecycleRechargeLogService extends ServiceImpl<RecycleRechargeLogMapper, RecycleRechargeLog> {

    @Autowired
    private EmployeeAccountChangeService employeeAccountChangeService;
    @Autowired
    private CompanyService companyService;

    public List<RecycleRechargeVo> selectPage(RecycleRechargeReq req) {
        PageHelper.startPage(req.getPage(), req.getPageSize());
        List<RecycleRechargeVo> resultList = this.getBaseMapper().selectByParam(req);
        if (CollUtil.isNotEmpty(resultList)) {
            resultList.stream().forEach(e -> {
                e.setRechargeAmountStr(StringUtils.fenToYuan(e.getRechargeAmount()));
                e.setStatusName(RecycleRechargeStatus.getNameByCode(e.getStatus()));
            });
        }
        return resultList;
    }

    @Transactional(rollbackFor = Exception.class)
    public void check(RechargeCheckReq req) {
        RecycleRechargeLog recycleRechargeLog = this.getById(req.getId());
        if (!recycleRechargeLog.getStatus().equals(RecycleRechargeStatus.TO_AUDIT.getCode())) {
            throw new BusinessException("订单状态不是待审核");
        }
        recycleRechargeLog.setStatus(req.getStatus());
        recycleRechargeLog.setUpdateTime(new Date());
        this.updateById(recycleRechargeLog);

        Company company = companyService.getById(recycleRechargeLog.getCompanyId());
        //钱包加钱
        if (req.getStatus().equals(RecycleRechargeStatus.PASS.getCode())) {
            employeeAccountChangeService.changeAccount(company.getEmployeeId(), EmployAccountChangeEnum.recycle_recharge,
                    recycleRechargeLog.getRechargeAmount(), recycleRechargeLog.getId(), "充值");
        }
    }

}