package com.zxtx.hummer.account.service;

import cn.hutool.core.util.StrUtil;
import com.zxtx.hummer.account.constant.EmployAccountChangeEnum;
import com.zxtx.hummer.account.domain.EmployeeAccount;
import com.zxtx.hummer.account.domain.EmployeeAccountLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class EmployeeAccountChangeService {


    @Autowired
    private IEmployeeAccountService employeeAccountService;

    @Autowired
    private IEmployeeAccountLogService employeeAccountLogService;

    /**
     * 账户金币变更 统一入口方法
     *
     * @param employeeId        员工id
     * @param accountChangeEnum 变更类型枚举类
     * @param changeBalance     变更账户值
     * @param correlationId     关联对象ID
     * @return
     */
    public boolean changeAccount(Long employeeId, EmployAccountChangeEnum accountChangeEnum, long changeBalance, Long correlationId, String remark) {


        EmployeeAccount employeeAccount = employeeAccountService.lambdaQuery().eq(EmployeeAccount::getEmployeeId, employeeId).last("for update").one();

        if (employeeAccount == null) {
            return false;
        }

        long changeAbleBalance = changeBalance * accountChangeEnum.getChangeAbleBalance(),
                changeTempFrozenBalance = changeBalance * accountChangeEnum.getChangeTempFrozenBalance(),
                changeFrozenBalance = changeBalance * accountChangeEnum.getChangeFrozenBalance(),
                changeAccumulateIncome = changeBalance * accountChangeEnum.getChangeAccumulateIncome(),
                changeAccAwardIncome = changeBalance * accountChangeEnum.getChangeAccAwardIncome(),
                changeAccWithdraw = changeBalance * accountChangeEnum.getChangeAccWithdraw();


        EmployeeAccountLog accountLog = new EmployeeAccountLog();
        accountLog.setEmployeeId(employeeAccount.getEmployeeId());
        accountLog.setEmployeeAccountId(employeeAccount.getId());
        accountLog.setAncestors(employeeAccount.getAncestors());
        accountLog.setChangeMainType(accountChangeEnum.getChangeMainType());
        accountLog.setChangeDetailType(accountChangeEnum.getChangeDetailType());
        accountLog.setUserFocus(accountChangeEnum.getIsUserFocusType());
        accountLog.setChangeBalance(changeBalance);


        accountLog.setAbleBalanceBefore(employeeAccount.getAbleBalance());
        accountLog.setAbleBalanceChange(changeAbleBalance);
        accountLog.setAbleBalanceAfter(employeeAccount.getAbleBalance() + changeAbleBalance);

        accountLog.setTempFrozenBalanceBefore(employeeAccount.getTempFrozenBalance());
        accountLog.setTempFrozenBalanceChange(changeTempFrozenBalance);
        accountLog.setTempFrozenBalanceAfter(employeeAccount.getTempFrozenBalance() + changeTempFrozenBalance);

        accountLog.setFrozenBalanceBefore(employeeAccount.getFrozenBalance());
        accountLog.setFrozenBalanceChange(changeFrozenBalance);
        accountLog.setFrozenBalanceAfter(employeeAccount.getFrozenBalance() + changeFrozenBalance);

        accountLog.setAccumulateIncomeBefore(employeeAccount.getAccumulateIncome());
        accountLog.setAccumulateIncomeChange(changeAccumulateIncome);
        accountLog.setAccumulateIncomeAfter(employeeAccount.getAccumulateIncome() + changeAccumulateIncome);

        accountLog.setAccAwardIncomeBefore(employeeAccount.getAccAwardIncome());
        accountLog.setAccAwardIncomeChange(changeAccAwardIncome);
        accountLog.setAccAwardIncomeAfter(employeeAccount.getAccAwardIncome() + changeAccAwardIncome);

        accountLog.setAccWithdrawBefore(employeeAccount.getAccWithdraw());
        accountLog.setAccWithdrawChange(changeAccWithdraw);
        accountLog.setAccWithdrawAfter(employeeAccount.getAccWithdraw() + changeAccWithdraw);


        if (correlationId != null) {
            accountLog.setCorrelationId(correlationId);
        }

        accountLog.setRemark(StrUtil.isBlank(remark) ? accountChangeEnum.getRemark() : remark);
        accountLog.setCreateTime(LocalDateTime.now());
//        int addDate = Integer.parseInt(DateUtil.getCurDateStr2());
//        log.set("addDate", addDate);
        if (employeeAccountLogService.save(accountLog)) {
            boolean changeSuccess = employeeAccountService.changeAccountBalance(accountLog);
            return changeSuccess;
        }
        return false;
    }


}
