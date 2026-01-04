package com.zxtx.hummer.insurance.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zxtx.hummer.common.domain.AbstractBaseEntity;
import com.zxtx.hummer.common.exception.BaseException;
import com.zxtx.hummer.common.lock.RedisLockService;
import com.zxtx.hummer.common.utils.*;
import com.zxtx.hummer.company.service.CompanyService;
import com.zxtx.hummer.em.domain.Employee;
import com.zxtx.hummer.em.service.EmService;
import com.zxtx.hummer.insurance.domain.DiInsuranceFixOrder;
import com.zxtx.hummer.insurance.domain.DiInsuranceFixOrderSettlement;
import com.zxtx.hummer.insurance.domain.DiInsuranceOrder;
import com.zxtx.hummer.insurance.domain.DiInsuranceUserAccount;
import com.zxtx.hummer.insurance.domain.dto.DiInsuranceFixOrderSettlementDTO;
import com.zxtx.hummer.insurance.enums.DiFixOrderStatusEnum;
import com.zxtx.hummer.insurance.enums.FixOrderCartTypeEnum;
import com.zxtx.hummer.insurance.enums.FixOrderOperationEnum;
import com.zxtx.hummer.insurance.enums.FixOrderSettlementStatusEnum;
import com.zxtx.hummer.insurance.req.DiInsuranceFixOrderSettlementQueryReq;
import com.zxtx.hummer.product.domain.dto.OrderLogDTO;
import com.zxtx.hummer.product.domain.request.OrderLogQueryReq;
import com.zxtx.hummer.product.service.OrderLogService;
import com.zxtx.hummer.withdraw.vo.ImportWithdrawVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/10/17
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Service
public class FixOrderSettlementManageService {
    @Autowired
    private DiInsuranceFixOrderSettlementService diInsuranceFixOrderSettlementService;
    @Autowired
    private OrderLogService orderLogService;
    @Autowired
    private DiInsuranceFixOrderService diInsuranceFixOrderService;
    @Autowired
    private DiInsuranceOrderService diInsuranceOrderService;
    @Autowired
    private EmService emService;
    @Autowired
    private DiOptionService diOptionService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private DiInsuranceUserAccountService insuranceUserAccountService;
    @Autowired
    private RedisLockService redisLockService;

    @Transactional(rollbackFor = Exception.class)
    public void settle(DiInsuranceFixOrder fixOrder) {
        DiInsuranceOrder order = diInsuranceOrderService.getById(fixOrder.getOrderId());
        String applyNo = "JXZ" + DateUtils.getCurDateTimeStr2() + SnowflakeIdWorker.betweenLong(100L, 999L, true);
        Employee employee = emService.getById(fixOrder.getStoreEmployeeId());
        String[] levels = employee.getAncestors().split(",");
        DiInsuranceFixOrderSettlement bean = DiInsuranceFixOrderSettlement.builder()
                .id(SnowflakeIdWorker.nextID())
                .applyNo(applyNo)
                .fixOrderId(fixOrder.getId())
                .bdId(levels.length >= 2 ? Long.valueOf(levels[1]) : null)
                .areaId(levels.length >= 3 ? Long.valueOf(levels[2]) : null)
                .ancestors(employee.getAncestors())
                .amount(fixOrder.getSettleAmount().longValue())
                .taxAmount(0L)
                .inAmount(fixOrder.getSettleAmount().longValue())
                .type(FixOrderCartTypeEnum.ALIPAY_ACCOUNT.getCode())
                .ownerName(fixOrder.getFixName())
                .accountNo(fixOrder.getFixAlipay())
                .idCard(order.getIdCard())
                .status(FixOrderSettlementStatusEnum.PENDING_PAYMENT.getCode())
                .build();
        diInsuranceFixOrderSettlementService.save(bean);
        orderLogService.addLog(
                ShiroUtils.getUserId(),
                bean.getId(),
                FixOrderSettlementStatusEnum.PENDING_PAYMENT.getCode(),
                FixOrderSettlementStatusEnum.PENDING_PAYMENT.getOperation(),
                FixOrderSettlementStatusEnum.PENDING_PAYMENT.getOperationDesc(),
                FixOrderSettlementStatusEnum.PENDING_PAYMENT.getOperationRemark());
    }

    public PageUtils<DiInsuranceFixOrderSettlementDTO> list(DiInsuranceFixOrderSettlementQueryReq req) {
        Page<Object> page = PageHelper.startPage(req.getPage(), req.getPageSize());
        List<DiInsuranceFixOrderSettlementDTO> list = diInsuranceFixOrderSettlementService.getBaseMapper().listSettlement(req);
        if (CollUtil.isEmpty(list)) {
            return PageUtils.emptyPage();
        }

        Set<Long> userIds = list.stream().map(DiInsuranceFixOrderSettlementDTO::getFixOrderCreateBy).collect(Collectors.toSet());
        Map<Long, Employee> empMap = emService.getEmployeeByMap(userIds);
        Map<Long, DiInsuranceUserAccount> userMap = insuranceUserAccountService.getUserMap(userIds);

        list.forEach(vo -> {
            vo.setAmountStr();
            vo.setStatusStr(EnumUtil.getBy(FixOrderSettlementStatusEnum::getCode, vo.getStatus()).getDesc());
            vo.setTypeStr(EnumUtil.getBy(FixOrderCartTypeEnum::getCode, vo.getType()).getDesc());
            Optional<Employee> employee = Optional.ofNullable(empMap.get(vo.getFixOrderCreateBy()));
            Optional<DiInsuranceUserAccount> userAccount = Optional.ofNullable(userMap.get(vo.getFixOrderCreateBy()));
            vo.setFixOrderCreatorName(employee.map(Employee::getName).orElse(userAccount.map(DiInsuranceUserAccount::getName).orElse(null)));
            vo.setFixOrderCreatorPhone(employee.map(Employee::getMobileNumber).orElse(userAccount.map(DiInsuranceUserAccount::getMobile).orElse(null)));
        });

        return new PageUtils<>(list, page.getTotal());
    }

    public DiInsuranceFixOrderSettlementDTO detail(Long id) {
        List<DiInsuranceFixOrderSettlementDTO> list = diInsuranceFixOrderSettlementService.getBaseMapper().listSettlement(DiInsuranceFixOrderSettlementQueryReq.builder().id(id).build());
        if (CollUtil.isEmpty(list)) {
            throw new BaseException(-1, "订单不存在");
        }
        DiInsuranceFixOrderSettlementDTO vo = Optional.ofNullable(list.get(0)).orElseThrow(() -> new BaseException(-1, "订单不存在"));

        vo.setStatusStr(EnumUtil.getBy(FixOrderSettlementStatusEnum::getCode, vo.getStatus()).getDesc());
        vo.setTypeStr(EnumUtil.getBy(FixOrderCartTypeEnum::getCode, vo.getType()).getDesc());

        Map<Long, Employee> empMap = emService.getEmployeeByMap(Collections.singleton(vo.getFixOrderCreateBy()));
        Map<Long, DiInsuranceUserAccount> userMap = insuranceUserAccountService.getUserMap(Collections.singleton(vo.getFixOrderCreateBy()));
        Optional<Employee> employee = Optional.ofNullable(empMap.get(vo.getFixOrderCreateBy()));
        Optional<DiInsuranceUserAccount> userAccount = Optional.ofNullable(userMap.get(vo.getFixOrderCreateBy()));
        vo.setFixOrderCreatorName(employee.map(Employee::getName).orElse(userAccount.map(DiInsuranceUserAccount::getName).orElse(null)));
        vo.setFixOrderCreatorPhone(employee.map(Employee::getMobileNumber).orElse(userAccount.map(DiInsuranceUserAccount::getMobile).orElse(null)));

        List<OrderLogDTO> logs = orderLogService.listAllLogsByOrderId(OrderLogQueryReq.builder().orderId(id).build());
        logs.forEach(l -> {
            l.setStatusStr(EnumUtil.getBy(FixOrderSettlementStatusEnum::getCode, l.getStatus()).getDesc());
            l.setOperationStatusStr(EnumUtil.getBy(FixOrderSettlementStatusEnum::getOperation, l.getOperationStatus()).getDesc());
        });
        vo.setLogs(logs);
        return vo;
    }

    @Transactional(rollbackFor = Exception.class)
    public String importData(MultipartFile file) {
        redisLockService.redisLock(StrUtil.format("pc_insurance_fix_order_settle_import_lock_{}",file.getOriginalFilename()), 20L, TimeUnit.SECONDS);
        String resultMessage = "";
        List<ImportWithdrawVO> importList = null;
        try {
            importList = ExcelImportUtil.importFromExcel(file.getOriginalFilename(), file.getInputStream(),
                    20, ImportWithdrawVO.class);
        } catch (Exception e) {
            throw new BaseException(-1, "导入失败");
        }
        if (CollectionUtils.isEmpty(importList)) {
            throw new BaseException(-1, "导入失败");
        }
        //订单号对应的申请记录
        List<String> applyNos = importList.stream().map(ImportWithdrawVO::getApplyNo).collect(Collectors.toList());
        Map<String, DiInsuranceFixOrderSettlement> collect = diInsuranceFixOrderSettlementService.lambdaQuery()
                .eq(AbstractBaseEntity::getDeleted, false)
                .eq(DiInsuranceFixOrderSettlement::getStatus, FixOrderSettlementStatusEnum.PENDING_PAYMENT.getCode())
                .in(DiInsuranceFixOrderSettlement::getApplyNo, applyNos)
                .list().stream().collect(Collectors.toMap(DiInsuranceFixOrderSettlement::getApplyNo, Function.identity(), (a, b) -> b));

        List<DiInsuranceFixOrderSettlement> updateList = new ArrayList<>();
        List<DiInsuranceFixOrderSettlement> successList = new ArrayList<>();
        List<DiInsuranceFixOrderSettlement> failList = new ArrayList<>();
        for (ImportWithdrawVO vo : importList) {
            DiInsuranceFixOrderSettlement apply = collect.get(vo.getApplyNo());
            if (apply == null) {
                throw new BaseException(-1, StrUtil.format("存在单号{}在系统中不存在或状态不是待打款", vo.getApplyNo()));
            }
            if (vo.getPayStatus().equals("已完成")) {
                apply.setPayTime(new Date());
                apply.setStatus(FixOrderSettlementStatusEnum.PAYED.getCode());
                successList.add(apply);
            } else {
                apply.setStatus(FixOrderSettlementStatusEnum.FAILED.getCode());
                apply.setRemark(vo.getPayStatus());
                failList.add(apply);
            }
            updateList.add(apply);
        }

        if (CollUtil.isEmpty(updateList)) {
            return resultMessage;
        }

        successList.forEach(o -> {
            // 打款成功
            diInsuranceFixOrderService.lambdaUpdate()
                    .set(DiInsuranceFixOrder::getStatus, DiFixOrderStatusEnum.PAY_PASS.getCode())
                    .eq(DiInsuranceFixOrder::getId, o.getFixOrderId())
                    .update(new DiInsuranceFixOrder());
            orderLogService.addLog(
                    ShiroUtils.getUserId(),
                    o.getFixOrderId(),
                    DiFixOrderStatusEnum.PAY_PASS.getCode(),
                    FixOrderOperationEnum.PAY_PASS.getCode(),
                    FixOrderOperationEnum.PAY_PASS.getDesc(),
                    FixOrderOperationEnum.PAY_PASS.getDesc() + StringUtils.fenToYuan(o.getInAmount()) + "元"
            );

            orderLogService.addLog(
                    ShiroUtils.getUserId(),
                    o.getId(),
                    FixOrderSettlementStatusEnum.PAYED.getCode(),
                    FixOrderSettlementStatusEnum.PAYED.getOperation(),
                    FixOrderSettlementStatusEnum.PAYED.getOperationDesc(),
                    FixOrderSettlementStatusEnum.PAYED.getOperationRemark()
            );
        });

        failList.forEach(o -> {
            // 打款失败
            diInsuranceFixOrderService.lambdaUpdate()
                    .set(DiInsuranceFixOrder::getStatus, DiFixOrderStatusEnum.DATA_EDIT.getCode())
                    .eq(DiInsuranceFixOrder::getId, o.getFixOrderId())
                    .update(new DiInsuranceFixOrder());
            orderLogService.addLog(
                    ShiroUtils.getUserId(),
                    o.getFixOrderId(),
                    DiFixOrderStatusEnum.DATA_EDIT.getCode(),
                    FixOrderOperationEnum.DATA_EDIT.getCode(),
                    FixOrderOperationEnum.DATA_EDIT.getDesc(),
                    FixOrderOperationEnum.DATA_EDIT.getDesc()
            );

            orderLogService.addLog(
                    ShiroUtils.getUserId(),
                    o.getId(),
                    FixOrderSettlementStatusEnum.FAILED.getCode(),
                    FixOrderSettlementStatusEnum.FAILED.getOperation(),
                    FixOrderSettlementStatusEnum.FAILED.getOperationDesc(),
                    StrUtil.isNotBlank(o.getRemark()) ? o.getRemark() : FixOrderSettlementStatusEnum.FAILED.getOperationRemark()
            );
        });

        diInsuranceFixOrderSettlementService.updateBatchById(updateList);

        return resultMessage;
    }
}