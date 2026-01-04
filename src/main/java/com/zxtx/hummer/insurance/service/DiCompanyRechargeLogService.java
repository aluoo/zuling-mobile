package com.zxtx.hummer.insurance.service;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.zxtx.hummer.common.exception.BusinessException;
import com.zxtx.hummer.common.utils.StringUtils;
import com.zxtx.hummer.company.domain.Company;
import com.zxtx.hummer.company.enums.RecycleRechargeStatus;
import com.zxtx.hummer.company.req.RechargeCheckReq;
import com.zxtx.hummer.company.req.RecycleRechargeReq;
import com.zxtx.hummer.company.service.CompanyService;
import com.zxtx.hummer.company.vo.RecycleRechargeVo;
import com.zxtx.hummer.insurance.dao.mapper.DiCompanyRechargeLogMapper;
import com.zxtx.hummer.insurance.domain.DiCompanyAccount;
import com.zxtx.hummer.insurance.domain.DiCompanyRechargeLog;
import com.zxtx.hummer.insurance.enums.CompanyAccountChangeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 数保门店账户充值记录表 服务实现类
 * </p>
 *
 * @author aycx
 * @since 2024-05-21
 */
@Service
public class DiCompanyRechargeLogService extends ServiceImpl<DiCompanyRechargeLogMapper, DiCompanyRechargeLog> {

    @Autowired
    private CompanyAccountChangeService companyAccountChangeService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private DiCompanyAccountService companyAccountService;

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
        DiCompanyRechargeLog recycleRechargeLog = this.getById(req.getId());
        if (!recycleRechargeLog.getStatus().equals(RecycleRechargeStatus.TO_AUDIT.getCode())) {
            throw new BusinessException("订单状态不是待审核");
        }
        recycleRechargeLog.setStatus(req.getStatus());
        recycleRechargeLog.setUpdateTime(new Date());
        this.updateById(recycleRechargeLog);

        Company company = companyService.getById(recycleRechargeLog.getCompanyId());
        //钱包加钱
        if (req.getStatus().equals(RecycleRechargeStatus.PASS.getCode())) {

            //第一次门店充值账户不存在，创建账户
            DiCompanyAccount employeeAccount = companyAccountService.lambdaQuery().eq(DiCompanyAccount::getCompanyId, recycleRechargeLog.getCompanyId()).one();
            if (employeeAccount == null) {
                employeeAccount = new DiCompanyAccount();
                employeeAccount.setCompanyId(recycleRechargeLog.getCompanyId());
                employeeAccount.setCreateTime(LocalDateTime.now());
                employeeAccount.setUpdateTime(LocalDateTime.now());
                companyAccountService.save(employeeAccount);
            }

            companyAccountChangeService.changeAccount(company.getId(), CompanyAccountChangeEnum.recycle_recharge,
                    recycleRechargeLog.getRechargeAmount(), recycleRechargeLog.getId(), "充值");
        }
    }

}
