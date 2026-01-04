package com.zxtx.hummer.insurance.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.binarywang.wxpay.bean.ecommerce.RefundsResult;
import com.zxtx.hummer.account.constant.EmployAccountChangeEnum;
import com.zxtx.hummer.commission.enums.CommissionBizType;
import com.zxtx.hummer.commission.enums.CommissionPackage;
import com.zxtx.hummer.commission.service.CommissionSettleService;
import com.zxtx.hummer.common.domain.AbstractBaseEntity;
import com.zxtx.hummer.common.exception.BaseException;
import com.zxtx.hummer.common.lock.RedisLockService;
import com.zxtx.hummer.common.utils.ShiroUtils;
import com.zxtx.hummer.common.wx.CommonWxPayService;
import com.zxtx.hummer.common.wx.CommonWxUtils;
import com.zxtx.hummer.common.wx.dto.CommonRefundReq;
import com.zxtx.hummer.company.domain.Company;
import com.zxtx.hummer.company.service.CompanyService;
import com.zxtx.hummer.em.dao.mapper.EmployeeMapper;
import com.zxtx.hummer.em.domain.Employee;
import com.zxtx.hummer.em.service.EmService;
import com.zxtx.hummer.insurance.domain.DiInsuranceOrder;
import com.zxtx.hummer.insurance.domain.DiInsuranceOrderPayment;
import com.zxtx.hummer.insurance.domain.DiInsuranceOrderPicture;
import com.zxtx.hummer.insurance.enums.*;
import com.zxtx.hummer.insurance.req.DiInsuranceOrderAuditReq;
import com.zxtx.hummer.insurance.req.DiInsuranceOrderEditReq;
import com.zxtx.hummer.insurance.req.DiInsuranceOrderReq;
import com.zxtx.hummer.insurance.vo.DiInsuranceOrderVO;
import com.zxtx.hummer.insurance.vo.DiInsuranceOrderViewVO;
import com.zxtx.hummer.product.domain.OrderLog;
import com.zxtx.hummer.product.domain.dto.OrderLogDTO;
import com.zxtx.hummer.product.domain.enums.OrderOperationEnum;
import com.zxtx.hummer.product.service.OrderLogService;
import com.zxtx.hummer.system.dao.mapper.SysUserMapper;
import com.zxtx.hummer.system.domain.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 手机-数保产品关联表 服务实现类
 * </p>
 *
 * @author aycx
 * @since 2024-05-21
 */
@Service
@Slf4j
public class DiInsuranceOrderManageService {
    @Autowired
    private DiInsuranceOrderService insuranceOrderService;
    @Autowired
    private DiInsuranceOrderPaymentService insuranceOrderPaymentService;
    @Autowired
    private DiInsuranceOrderPictureService insuranceOrderPictureService;
    @Autowired
    private OrderLogService orderLogService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private EmService emService;
    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private CompanyAccountChangeService companyAccountChangeService;
    @Autowired
    private DiInsuranceUserAccountService insuranceUserAccountService;
    @Autowired
    private CommissionSettleService commissionSettleService;
    @Autowired
    private CommonWxPayService commonWxPayService;
    @Autowired
    private RedisLockService redisLockService;
    @Autowired
    private SysUserMapper sysUserMapper;


    public List<DiInsuranceOrderVO> selectPage(DiInsuranceOrderReq req) {
        return insuranceOrderService.selectPage(req);
    }

    public DiInsuranceOrderViewVO detail(Long id){

        DiInsuranceOrderViewVO resultVo = new DiInsuranceOrderViewVO();

        DiInsuranceOrder order = insuranceOrderService.getById(id);
        BeanUtil.copyProperties(order,resultVo);

        DiInsuranceOrderPayment payment = insuranceOrderPaymentService.lambdaQuery()
                .eq(DiInsuranceOrderPayment::getInsuranceOrderId,order.getId())
                .one();
        if(ObjectUtil.isNotNull(payment)){
            BeanUtil.copyProperties(payment,resultVo,"id");
        }

        List<DiInsuranceOrderPicture> pictureList = insuranceOrderPictureService.lambdaQuery()
                .eq(DiInsuranceOrderPicture::getInsuranceOrderId,id)
                .eq(AbstractBaseEntity::getDeleted, false)
                .list();
        resultVo.setPicList(pictureList);

        //日志
        List<OrderLog> logList =  orderLogService.list(Wrappers.lambdaQuery(OrderLog.class)
                .eq(OrderLog::getOrderId,order.getId()));
        if(CollUtil.isNotEmpty(logList)){
            List<OrderLogDTO> logDTOList = BeanUtil.copyToList(logList, OrderLogDTO.class);
            Set<Long> creatorIds = logList.stream().map(OrderLog::getCreateBy).filter(Objects::nonNull).collect(Collectors.toSet());
            List<Employee> employees = employeeMapper.selectList(new LambdaQueryWrapper<Employee>().in(Employee::getId, creatorIds));
            List<SysUser> sysUsers = sysUserMapper.selectList(new LambdaQueryWrapper<SysUser>().in(SysUser::getUserId, creatorIds));
            Map<Long, Employee> empMap = employees.stream().collect(Collectors.toMap(Employee::getId, Function.identity()));
            Map<Long, SysUser> sysUserMap = sysUsers.stream().collect(Collectors.toMap(SysUser::getUserId, Function.identity()));
            logDTOList.forEach(e -> {
                e.setStatusStr(EnumUtil.getBy(DiOrderStatusEnum::getCode,e.getStatus()).getDesc());
                e.setOperationStatusStr(EnumUtil.getBy(DiOrderStatusEnum::getCode,e.getStatus()).getDesc());
                e.setCreator(Optional.ofNullable(empMap.get(e.getCreateBy())).map(Employee::getName).orElse(Optional.ofNullable(sysUserMap.get(e.getCreateBy())).map(SysUser::getName).orElse(null)));
            });
            resultVo.setOrderLogList(logDTOList);
        }

        Company company = companyService.getById(order.getStoreCompanyId());
        resultVo.setStoreName(Optional.ofNullable(company).map(Company::getName).orElse(null));

        Employee storeEmployee = emService.getById(order.getStoreEmployeeId());
        resultVo.setStoreEmployeePhone(Optional.ofNullable(storeEmployee).map(Employee::getMobileNumber).orElse(null));
        resultVo.setStoreEmployeeName(Optional.ofNullable(storeEmployee).map(Employee::getName).orElse(null));

        Employee agentEmployee = emService.getById(company.getAplId());
        resultVo.setAgentName(Optional.ofNullable(agentEmployee).map(Employee::getName).orElse(null));
        resultVo.setAgentPhone(Optional.ofNullable(agentEmployee).map(Employee::getMobileNumber).orElse(null));

        Employee bdEmployee = employeeMapper.selectBdByAncestors(storeEmployee.getAncestors());
        resultVo.setBdName(Optional.ofNullable(bdEmployee).map(Employee::getName).orElse(null));
        resultVo.setBdPhone(Optional.ofNullable(bdEmployee).map(Employee::getMobileNumber).orElse(null));

        return resultVo;
    }

    @Transactional(rollbackFor = Exception.class)
    public void auditOrder(DiInsuranceOrderAuditReq req) {
        redisLockService.redisLock("pc_insurance_order_audit_lock", req.getId());
        DiInsuranceOrder order = Optional.ofNullable(insuranceOrderService.lambdaQuery()
                        .eq(AbstractBaseEntity::getDeleted, false)
                        .eq(DiInsuranceOrder::getId, req.getId()).one())
                .orElseThrow(() -> new BaseException(-1, "订单不存在"));
        if (!order.getStatus().equals(DiOrderStatusEnum.PENDING_AUDIT.getCode())) {
            throw new BaseException(-1, "当前订单状态发送变化，不可操作");
        }
        if (req.getPass()) {
            // 通过
            Integer invoicePrice = null;
            if (req.getProductSkuPurchaseInvoicePrice() != null) {
                invoicePrice = NumberUtil.mul(req.getProductSkuPurchaseInvoicePrice(), new BigDecimal("100")).setScale(0, RoundingMode.UNNECESSARY).intValue();
            }
            Date purchaseDate = null;
            if (req.getProductSkuPurchaseInvoiceTime() != null && StrUtil.isNotBlank(req.getProductSkuPurchaseInvoiceTime())) {
                purchaseDate = DateUtil.parse(req.getProductSkuPurchaseInvoiceTime()).toJdkDate();
            }
            // 更新订单为已完成-待上传
            insuranceOrderService.lambdaUpdate()
                    .set(DiInsuranceOrder::getStatus, DiOrderStatusEnum.FINISHED.getCode())
                    .set(DiInsuranceOrder::getSubStatus, DiOrderSubStatusEnum.PENDING_UPLOAD_INSURANCE.getCode())
                    .set(invoicePrice != null, DiInsuranceOrder::getProductSkuPurchaseInvoicePrice, invoicePrice)
                    .set(purchaseDate != null, DiInsuranceOrder::getProductSkuPurchaseInvoiceTime, purchaseDate)
                    .eq(DiInsuranceOrder::getId, order.getId())
                    .eq(AbstractBaseEntity::getDeleted, false)
                    .update(new DiInsuranceOrder());
            orderLogService.addLog(
                    ShiroUtils.getUserId(),
                    order.getId(),
                    DiOrderStatusEnum.FINISHED.getCode(),
                    OrderOperationEnum.DI_ORDER_AUDIT_PASSED.getCode(),
                    OrderOperationEnum.DI_ORDER_AUDIT_PASSED.getDesc(),
                    OrderOperationEnum.DI_ORDER_AUDIT_PASSED.getRemark()
            );
            // 创建用户账号
            insuranceUserAccountService.createUser(order.getCustomName(), order.getCustomPhone(), order.getIdCard());
            //佣金待结算
            Long amount = order.getInsuranceNormalPrice()-order.getInsuranceDownPrice();
            commissionSettleService.orderScaleBindSettleRule(amount,order.getId(), CommissionBizType.INSURANCE_SERVICE, CommissionPackage.INSURANCE_SERVICE,order.getStoreEmployeeId());
            commissionSettleService.waitSettleOrder(order.getId(), CommissionBizType.INSURANCE_SERVICE, CommissionPackage.INSURANCE_SERVICE.getType(),order.getStoreEmployeeId(),null, EmployAccountChangeEnum.insurance_commission.getRemark());

            //门店佣金
            Integer shopAmount = order.getPrice()-order.getInsuranceNormalPrice();
            commissionSettleService.orderScaleBindSettleRule(Long.valueOf(shopAmount),order.getId(), CommissionBizType.INSURANCE_SERVICE, CommissionPackage.INSURANCE_COMPANY_SERVICE,order.getStoreEmployeeId());
            commissionSettleService.waitSettleOrder(order.getId(), CommissionBizType.INSURANCE_SERVICE, CommissionPackage.INSURANCE_COMPANY_SERVICE.getType(),order.getStoreEmployeeId(),null, EmployAccountChangeEnum.insurance_commission.getRemark());

        } else {
            // 不通过
            if (StrUtil.isBlank(req.getAuditRemark())) {
                throw new BaseException(-1, "请填写审核备注");
            }
            insuranceOrderService.lambdaUpdate()
                    .set(DiInsuranceOrder::getSubStatus, DiOrderSubStatusEnum.AUDIT_ORDER_FAILED.getCode())
                    .eq(DiInsuranceOrder::getId, order.getId())
                    .eq(AbstractBaseEntity::getDeleted, false)
                    .update(new DiInsuranceOrder());
            orderLogService.addLog(
                    ShiroUtils.getUserId(),
                    order.getId(),
                    DiOrderStatusEnum.PENDING_AUDIT.getCode(),
                    OrderOperationEnum.DI_ORDER_AUDIT_FAILED.getCode(),
                    OrderOperationEnum.DI_ORDER_AUDIT_FAILED.getDesc(),
                    req.getAuditRemark()
            );
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void auditRefund(DiInsuranceOrderAuditReq req) {
        redisLockService.redisLock("pc_insurance_order_refund_audit_lock", req.getId());
        DiInsuranceOrder order = Optional.ofNullable(insuranceOrderService.lambdaQuery()
                        .eq(AbstractBaseEntity::getDeleted, false)
                        .eq(DiInsuranceOrder::getId, req.getId()).one())
                .orElseThrow(() -> new BaseException(-1, "订单不存在"));
        if (!order.getStatus().equals(DiOrderStatusEnum.REFUNDING.getCode())) {
            throw new BaseException(-1, "当前订单状态发送变化，不可操作");
        }
        DiInsuranceOrderPayment payment = Optional.ofNullable(insuranceOrderPaymentService.lambdaQuery()
                        .eq(DiInsuranceOrderPayment::getStatus, DiOrderPayStatusEnum.PENDING_REFUND.getCode())
                        .eq(AbstractBaseEntity::getDeleted,false)
                        .eq(DiInsuranceOrderPayment::getInsuranceOrderId, order.getId()).one())
                .orElseThrow(() -> new BaseException(-1, "订单不存在"));

        if (!req.getPass()) {
            // 不通过
            if (StrUtil.isBlank(req.getAuditRemark())) {
                throw new BaseException(-1, "请填写审核备注");
            }
            // 更新订单为待审核-退款审核失败
            insuranceOrderService.lambdaUpdate()
                    .set(DiInsuranceOrder::getStatus, DiOrderStatusEnum.PENDING_AUDIT.getCode())
                    .set(DiInsuranceOrder::getSubStatus, DiOrderSubStatusEnum.AUDIT_REFUND_FAILED.getCode())
                    .eq(DiInsuranceOrder::getId, order.getId())
                    .eq(AbstractBaseEntity::getDeleted, false)
                    .update(new DiInsuranceOrder());
            // 更新账单为已支付
            insuranceOrderPaymentService.lambdaUpdate()
                    .set(DiInsuranceOrderPayment::getStatus, DiOrderPayStatusEnum.PAYED.getCode())
                    .eq(DiInsuranceOrderPayment::getId, payment.getId())
                    .eq(AbstractBaseEntity::getDeleted, false)
                    .eq(DiInsuranceOrderPayment::getStatus, DiOrderPayStatusEnum.PENDING_REFUND.getCode())
                    .update(new DiInsuranceOrderPayment());
            orderLogService.addLog(
                    ShiroUtils.getUserId(),
                    order.getId(),
                    DiOrderStatusEnum.PENDING_AUDIT.getCode(),
                    OrderOperationEnum.DI_ORDER_REFUND_AUDIT_FAILED.getCode(),
                    OrderOperationEnum.DI_ORDER_REFUND_AUDIT_FAILED.getDesc(),
                    req.getAuditRemark()
            );
            return;
        }
        // 通过
        boolean refundSuccess;
        String refundNo = null;
        String refundId = null;
        switch (EnumUtil.getBy(DiOrderPayTypeEnum::getCode, payment.getType())) {
            case WECHAT: {
                refundNo = CommonWxUtils.createOrderNo();
                RefundsResult res = wechatRefund(payment, refundNo);
                refundId = res.getRefundId();
                refundSuccess = true;
                break;
            }
            case ACCOUNT: {
                refundSuccess = accountRefund(order, payment);
                break;
            }
            default: {
                refundSuccess = false;
                break;
                // throw new BaseException(-1, "未知支付渠道");
            }
        }
        if (!refundSuccess) {
            throw new BaseException(-1, "退款失败");
        }
        DateTime refundTime = DateUtil.date();
        // 更新订单为已关闭-已退款
        insuranceOrderService.lambdaUpdate()
                .set(DiInsuranceOrder::getStatus, DiOrderStatusEnum.CANCELED.getCode())
                .set(DiInsuranceOrder::getSubStatus, DiOrderSubStatusEnum.REFUNDED.getCode())
                .eq(DiInsuranceOrder::getId, order.getId())
                .eq(AbstractBaseEntity::getDeleted, false)
                .update(new DiInsuranceOrder());
        // 更新账单为已退款
        insuranceOrderPaymentService.lambdaUpdate()
                .set(DiInsuranceOrderPayment::getStatus, DiOrderPayStatusEnum.REFUNDED.getCode())
                .set(DiInsuranceOrderPayment::getRefundTime, refundTime.toJdkDate())
                .set(DiInsuranceOrderPayment::getRefundNo, refundNo)
                .set(DiInsuranceOrderPayment::getRefundId, refundId)
                .eq(DiInsuranceOrderPayment::getId, payment.getId())
                .eq(AbstractBaseEntity::getDeleted, false)
                .eq(DiInsuranceOrderPayment::getStatus, DiOrderPayStatusEnum.PENDING_REFUND.getCode())
                .update(new DiInsuranceOrderPayment());
        // 退款审核通过
        orderLogService.addLog(
                ShiroUtils.getUserId(),
                order.getId(),
                DiOrderStatusEnum.CANCELED.getCode(),
                OrderOperationEnum.DI_ORDER_REFUND_AUDIT_PASSED.getCode(),
                OrderOperationEnum.DI_ORDER_REFUND_AUDIT_PASSED.getDesc(),
                OrderOperationEnum.DI_ORDER_REFUND_AUDIT_PASSED.getRemark()
        );
        // 退款成功
        orderLogService.addLog(
                ShiroUtils.getUserId(),
                order.getId(),
                DiOrderStatusEnum.CANCELED.getCode(),
                OrderOperationEnum.DI_ORDER_REFUND.getCode(),
                OrderOperationEnum.DI_ORDER_REFUND.getDesc(),
                OrderOperationEnum.DI_ORDER_REFUND.getRemark()
        );
    }

    private RefundsResult wechatRefund(DiInsuranceOrderPayment payment, String refundNo) {
        CommonRefundReq req = CommonRefundReq.builder()
                .refundNo(refundNo)
                .transactionId(payment.getTransactionId())
                .tradeNo(payment.getOutTradeNo())
                .fee(payment.getAmount().intValue())
                .refundFee(payment.getAmount().intValue())
                .build();
        return commonWxPayService.refundPartnerTransactions(req);
    }

    private boolean accountRefund(DiInsuranceOrder order, DiInsuranceOrderPayment payment) {
        return companyAccountChangeService.changeAccount(order.getStoreCompanyId(),
                CompanyAccountChangeEnum.insurance_refund,
                payment.getAmount(),
                payment.getId(),
                CompanyAccountChangeEnum.insurance_refund.getRemark());
    }

    @Transactional(rollbackFor = Exception.class)
    public void uploadInsurance(DiInsuranceOrderAuditReq req) {
        redisLockService.redisLock("pc_insurance_order_upload_lock", req.getId());
        DiInsuranceOrder order = Optional.ofNullable(insuranceOrderService.lambdaQuery()
                        .eq(AbstractBaseEntity::getDeleted, false)
                        .eq(DiInsuranceOrder::getId, req.getId()).one())
                .orElseThrow(() -> new BaseException(-1, "订单不存在"));
        if (!order.getSubStatus().equals(DiOrderSubStatusEnum.PENDING_UPLOAD_INSURANCE.getCode())) {
            throw new BaseException(-1, "当前订单状态发送变化，不可操作");
        }
        // 更新订单为已完成-待出保
        insuranceOrderService.lambdaUpdate()
                .set(DiInsuranceOrder::getSubStatus, DiOrderSubStatusEnum.PENDING_EFFECTIVE.getCode())
                .set(DiInsuranceOrder::getInsuranceStatus, DiOrderInsuranceStatusEnum.PENDING_EFFECTIVE.getCode())
                .eq(DiInsuranceOrder::getId, order.getId())
                .eq(AbstractBaseEntity::getDeleted, false)
                .update(new DiInsuranceOrder());
        orderLogService.addLog(
                ShiroUtils.getUserId(),
                order.getId(),
                DiOrderStatusEnum.FINISHED.getCode(),
                OrderOperationEnum.DI_ORDER_UPLOAD_PASSED.getCode(),
                OrderOperationEnum.DI_ORDER_UPLOAD_PASSED.getDesc(),
                OrderOperationEnum.DI_ORDER_UPLOAD_PASSED.getRemark()
        );
        //结算佣金
        commissionSettleService.settleOrder(order.getId(), CommissionBizType.INSURANCE_SERVICE, CommissionPackage.INSURANCE_SERVICE.getType(),EmployAccountChangeEnum.insurance_commission,EmployAccountChangeEnum.insurance_commission.getRemark());
        commissionSettleService.settleOrder(order.getId(), CommissionBizType.INSURANCE_SERVICE, CommissionPackage.INSURANCE_COMPANY_SERVICE.getType(),EmployAccountChangeEnum.insurance_commission,EmployAccountChangeEnum.insurance_commission.getRemark());
    }

    @Deprecated
    @Transactional(rollbackFor = Exception.class)
    public void editOrder(DiInsuranceOrderEditReq req) {
        DiInsuranceOrder order = Optional.ofNullable(insuranceOrderService.lambdaQuery()
                        .eq(AbstractBaseEntity::getDeleted, false)
                        .eq(DiInsuranceOrder::getId, req.getId()).one())
                .orElseThrow(() -> new BaseException(-1, "订单不存在"));
        if (!order.getSubStatus().equals(DiOrderSubStatusEnum.PENDING_EFFECTIVE.getCode())) {
            throw new BaseException(-1, "当前订单状态发送变化，不可操作");
        }
        // 更新订单资料
        insuranceOrderService.lambdaUpdate()
                .set(DiInsuranceOrder::getCustomName, req.getCustomName())
                .set(DiInsuranceOrder::getCustomPhone, req.getCustomPhone())
                .set(DiInsuranceOrder::getIdCard, req.getIdCard())
                .eq(DiInsuranceOrder::getId, order.getId())
                .eq(AbstractBaseEntity::getDeleted, false)
                .update(new DiInsuranceOrder());
        orderLogService.addLog(
                ShiroUtils.getUserId(),
                order.getId(),
                DiOrderStatusEnum.FINISHED.getCode(),
                OrderOperationEnum.DI_ORDER_EDIT_INFO_ADMIN.getCode(),
                OrderOperationEnum.DI_ORDER_EDIT_INFO_ADMIN.getDesc(),
                OrderOperationEnum.DI_ORDER_EDIT_INFO_ADMIN.getRemark()
        );
    }

    public void manualRefund(DiInsuranceOrderAuditReq req) {
        DiInsuranceOrder order = Optional.ofNullable(insuranceOrderService.lambdaQuery()
                        .eq(AbstractBaseEntity::getDeleted, false)
                        .eq(DiInsuranceOrder::getId, req.getId()).one())
                .orElseThrow(() -> new BaseException(-1, "订单不存在"));
        if (!order.getStatus().equals(DiOrderStatusEnum.REFUNDING.getCode())) {
            throw new BaseException(-1, "当前订单状态发送变化，不可操作");
        }
        DiInsuranceOrderPayment payment = Optional.ofNullable(insuranceOrderPaymentService.lambdaQuery()
                        .eq(DiInsuranceOrderPayment::getStatus, DiOrderPayStatusEnum.PENDING_REFUND.getCode())
                        .eq(AbstractBaseEntity::getDeleted,false)
                        .eq(DiInsuranceOrderPayment::getInsuranceOrderId, order.getId()).one())
                .orElseThrow(() -> new BaseException(-1, "订单不存在"));

        // 更新订单为已关闭-已退款
        insuranceOrderService.lambdaUpdate()
                .set(DiInsuranceOrder::getStatus, DiOrderStatusEnum.CANCELED.getCode())
                .set(DiInsuranceOrder::getSubStatus, DiOrderSubStatusEnum.REFUNDED.getCode())
                .eq(DiInsuranceOrder::getId, order.getId())
                .eq(AbstractBaseEntity::getDeleted, false)
                .update(new DiInsuranceOrder());
        // 更新账单为已退款
        insuranceOrderPaymentService.lambdaUpdate()
                .set(DiInsuranceOrderPayment::getStatus, DiOrderPayStatusEnum.REFUNDED.getCode())
                .eq(DiInsuranceOrderPayment::getId, payment.getId())
                .eq(AbstractBaseEntity::getDeleted, false)
                .eq(DiInsuranceOrderPayment::getStatus, DiOrderPayStatusEnum.PENDING_REFUND.getCode())
                .update(new DiInsuranceOrderPayment());
        // 退款审核通过
        orderLogService.addLog(
                ShiroUtils.getUserId(),
                order.getId(),
                DiOrderStatusEnum.CANCELED.getCode(),
                OrderOperationEnum.DI_ORDER_REFUND_AUDIT_PASSED.getCode(),
                OrderOperationEnum.DI_ORDER_REFUND_AUDIT_PASSED.getDesc(),
                OrderOperationEnum.DI_ORDER_REFUND_AUDIT_PASSED.getRemark()
        );
        // 退款成功
        orderLogService.addLog(
                ShiroUtils.getUserId(),
                order.getId(),
                DiOrderStatusEnum.CANCELED.getCode(),
                OrderOperationEnum.DI_ORDER_REFUND.getCode(),
                OrderOperationEnum.DI_ORDER_REFUND.getDesc(),
                OrderOperationEnum.DI_ORDER_REFUND.getRemark()
        );
    }
}