package com.zxtx.hummer.insurance.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zxtx.hummer.common.exception.BaseException;
import com.zxtx.hummer.common.lock.RedisLockService;
import com.zxtx.hummer.common.utils.DateUtils;
import com.zxtx.hummer.common.utils.PageUtils;
import com.zxtx.hummer.common.utils.ShiroUtils;
import com.zxtx.hummer.common.utils.StringUtils;
import com.zxtx.hummer.company.domain.Company;
import com.zxtx.hummer.company.service.CompanyService;
import com.zxtx.hummer.em.domain.Employee;
import com.zxtx.hummer.em.service.EmService;
import com.zxtx.hummer.insurance.dao.mapper.DiInsuranceFixOrderMapper;
import com.zxtx.hummer.insurance.domain.*;
import com.zxtx.hummer.insurance.domain.dto.DiFixOrderCountDTO;
import com.zxtx.hummer.insurance.domain.dto.DiFixOrderInfoDTO;
import com.zxtx.hummer.insurance.enums.*;
import com.zxtx.hummer.insurance.req.DiInsuranceFixOrderAmountReq;
import com.zxtx.hummer.insurance.req.DiInsuranceFixOrderAuditReq;
import com.zxtx.hummer.insurance.req.DiInsuranceFixReq;
import com.zxtx.hummer.insurance.vo.DiInsuranceFixOrderDetailVO;
import com.zxtx.hummer.insurance.vo.DiInsuranceFixOrderVO;
import com.zxtx.hummer.insurance.vo.DiOptionVO;
import com.zxtx.hummer.insurance.vo.InsuranceDemoImageVO;
import com.zxtx.hummer.product.domain.Product;
import com.zxtx.hummer.product.domain.ProductSku;
import com.zxtx.hummer.product.domain.dto.OrderLogDTO;
import com.zxtx.hummer.product.domain.request.OrderLogQueryReq;
import com.zxtx.hummer.product.service.OrderLogService;
import com.zxtx.hummer.product.service.ProductService;
import com.zxtx.hummer.product.service.ProductSkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * 数保报修订单 服务实现类
 * </p>
 *
 * @author L
 * @since 2024-06-06
 */
@Service
public class DiInsuranceFixOrderService extends ServiceImpl<DiInsuranceFixOrderMapper, DiInsuranceFixOrder>{

    @Autowired
    private OrderLogService orderLogService;
    @Autowired
    private DiInsuranceOrderService insuranceOrderService;
    @Autowired
    private EmService emService;
    @Autowired
    private DiInsuranceUserAccountService insuranceUserAccountService;
    @Autowired
    private DiOptionService diOptionService;
    @Autowired
    private RedisLockService redisLockService;
    @Autowired
    private ProductSkuService productSkuService;
    @Autowired
    private ProductService productService;
    @Autowired
    private DiInsuranceOptionService insuranceOptionService;
    @Autowired
    private DiInsuranceFixOrderOptionService fixOrderOptionService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private FixOrderSettlementManageService fixOrderSettlementManageService;

    public PageUtils<DiInsuranceFixOrderVO> listFixOrders(DiInsuranceFixReq req) {
        Page<Object> page = PageHelper.startPage(req.getPage(), req.getPageSize());
        List<DiFixOrderInfoDTO> list = this.getBaseMapper().listFixOrderInfo(req);
        if (CollUtil.isEmpty(list)) {
            return PageUtils.emptyPage();
        }
        Set<Long> userIds = list.stream().map(DiFixOrderInfoDTO::getCreateBy).collect(Collectors.toSet());
        Map<Long, Employee> empMap = emService.getEmployeeByMap(userIds);
        Map<Long, DiInsuranceUserAccount> userMap = insuranceUserAccountService.getUserMap(userIds);
        Set<Long> optionIds = list.stream().flatMap(o -> Stream.concat(
                Optional.ofNullable(o.getServiceType()).map(Stream::of).orElseGet(Stream::empty),
                Optional.ofNullable(o.getClaimItem()).map(Stream::of).orElseGet(Stream::empty)
                )).collect(Collectors.toSet());
        Map<Long, DiOption> optionsMap = diOptionService.getOptionsMap(optionIds);
        Set<Long> orderIds = list.stream().map(DiFixOrderInfoDTO::getOrderId).collect(Collectors.toSet());
        Map<Long, Integer> orderCount = this.getBaseMapper().getFixOrderCount(DiFixOrderCountDTO.builder().orderIds(orderIds).build()).stream().collect(Collectors.toMap(DiFixOrderCountDTO::getOrderId, DiFixOrderCountDTO::getCount));
        List<DiInsuranceFixOrderVO> resp = new ArrayList<>();
        for (DiFixOrderInfoDTO dto : list) {
            DiInsuranceFixOrderVO vo = BeanUtil.copyProperties(dto, DiInsuranceFixOrderVO.class);
            vo.setInsuranceNo(dto.getOrderInsuranceNo());
            vo.setFixCount(Optional.ofNullable(orderCount.get(dto.getOrderId())).orElse(0));
            vo.setInsuranceType(dto.getOrderInsuranceType());
            vo.setInsuranceName(dto.getOrderInsuranceName());
            vo.setInsurancePeriod(dto.getOrderInsurancePeriod());
            DiOption serviceTypeOp = optionsMap.get(vo.getServiceType());
            vo.setServiceTypeStr(Optional.ofNullable(serviceTypeOp).map(DiOption::getName).orElse(null));
            DiOption claimItemOp = optionsMap.get(vo.getClaimItem());
            vo.setClaimItemStr(Optional.ofNullable(claimItemOp).map(DiOption::getName).orElse(null));
            vo.setProductName(dto.getOrderProductName());
            vo.setProductSpec(dto.getOrderProductSpec());
            vo.setImeiNo(dto.getOrderImeiNo());
            vo.setOldSkuRetailPriceStr(StringUtils.convertMoneyDefaultNull(vo.getOldSkuRetailPrice()));
            vo.setSettleAmountStr(StringUtils.convertMoneyDefaultNull(vo.getSettleAmount()));
            vo.setCustomName(dto.getOrderCustomName());
            vo.setCustomPhone(dto.getOrderCustomPhone());
            Optional<Employee> employee = Optional.ofNullable(empMap.get(vo.getCreateBy()));
            Optional<DiInsuranceUserAccount> userAccount = Optional.ofNullable(userMap.get(vo.getCreateBy()));
            vo.setCreatorName(employee.map(Employee::getName).orElse(userAccount.map(DiInsuranceUserAccount::getName).orElse(null)));
            vo.setCreatorPhone(employee.map(Employee::getMobileNumber).orElse(userAccount.map(DiInsuranceUserAccount::getMobile).orElse(null)));
            vo.setStatusStr(EnumUtil.getBy(DiFixOrderStatusEnum::getCode, vo.getStatus()).getDesc());
            resp.add(vo);
        }
        return new PageUtils<>(resp, page.getTotal());
    }

    @Transactional(rollbackFor = Exception.class)
    public void fixCheck(DiInsuranceFixOrderAuditReq req) {
        redisLockService.redisLock("pc_insurance_fix_order_fix_check_lock", req.getId());
        DiInsuranceFixOrder fixOrder = Optional.ofNullable(this.getById(req.getId())).orElseThrow(() -> new BaseException(-1, "订单不存在"));
        if (!DiFixOrderStatusEnum.WAIT.getCode().equals(fixOrder.getStatus())) {
            throw new BaseException(99999, "不支持审核的订单状态");
        }
        // 通过
        if (req.getStatus() == 2) {
            fixOrder.setStatus(DiFixOrderStatusEnum.ONE_PASS.getCode());
            this.updateById(fixOrder);
            orderLogService.addLog(
                    ShiroUtils.getUserId(),
                    fixOrder.getId(),
                    DiFixOrderStatusEnum.ONE_PASS.getCode(),
                    FixOrderOperationEnum.ONE_PASS.getCode(),
                    FixOrderOperationEnum.ONE_PASS.getDesc(),
                    FixOrderOperationEnum.ONE_PASS.getDesc());
        } else {
            fixOrder.setStatus(DiFixOrderStatusEnum.ONE_FAIL.getCode());
            this.updateById(fixOrder);
            orderLogService.addLog(ShiroUtils.getUserId(),
                    fixOrder.getId(),
                    DiFixOrderStatusEnum.ONE_FAIL.getCode(),
                    FixOrderOperationEnum.ONE_FAIL.getCode(),
                    FixOrderOperationEnum.ONE_FAIL.getDesc(),
                    StrUtil.format("{} {}", req.getReason(), req.getRemark()));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void dataCheck(DiInsuranceFixOrderAuditReq req) {
        redisLockService.redisLock("pc_insurance_fix_order_data_check_lock", req.getId());
        DiInsuranceFixOrder fixOrder = Optional.ofNullable(this.getById(req.getId())).orElseThrow(() -> new BaseException(-1, "订单不存在"));
        if (!DiFixOrderStatusEnum.DATA_WAIT.getCode().equals(fixOrder.getStatus())) {
            throw new BaseException(99999, "不支持审核的订单状态");
        }
        // 通过
        if (req.getStatus() == 2) {
            if (fixOrder.getSettleAmount() == null) {
                throw new BaseException(-1, "理赔金额不能为空");
            }
            fixOrder.setStatus(DiFixOrderStatusEnum.DATA_PASS.getCode());
            this.updateById(fixOrder);
            orderLogService.addLog(
                    ShiroUtils.getUserId(),
                    fixOrder.getId(),
                    DiFixOrderStatusEnum.DATA_PASS.getCode(),
                    FixOrderOperationEnum.DATA_PASS.getCode(),
                    FixOrderOperationEnum.DATA_PASS.getDesc(),
                    FixOrderOperationEnum.DATA_PASS.getDesc());
            // 添加待打款记录
            fixOrderSettlementManageService.settle(fixOrder);
        } else {
            fixOrder.setStatus(DiFixOrderStatusEnum.DATA_EDIT.getCode());
            this.updateById(fixOrder);
            orderLogService.addLog(
                    ShiroUtils.getUserId(),
                    fixOrder.getId(),
                    DiFixOrderStatusEnum.DATA_EDIT.getCode(),
                    FixOrderOperationEnum.DATA_EDIT.getCode(),
                    FixOrderOperationEnum.DATA_EDIT.getDesc(),
                    StrUtil.format("{} {}", req.getReason(), req.getRemark()));
        }
    }

    public DiInsuranceFixOrderDetailVO detailFixOrder(Long id) {
        List<InsuranceDemoImageVO> fixDataList = new ArrayList<>();
        List<InsuranceDemoImageVO> suppleDataList = new ArrayList<>();
        List<InsuranceDemoImageVO> settleDataList = new ArrayList<>();

        DiInsuranceFixOrderDetailVO resultVo = new DiInsuranceFixOrderDetailVO();
        DiInsuranceFixOrder fixOrder = this.getById(id);

        resultVo.setId(fixOrder.getId());
        resultVo.setCreateBy(fixOrder.getCreateBy());
        resultVo.setOrderId(fixOrder.getOrderId());
        resultVo.setOrderNo(fixOrder.getOrderNo());
        resultVo.setStatus(fixOrder.getStatus());
        resultVo.setStatusStr(EnumUtil.getBy(DiFixOrderStatusEnum::getCode, resultVo.getStatus()).getDesc());
        resultVo.setFixName(fixOrder.getFixName());
        resultVo.setFixAlipay(fixOrder.getFixAlipay());
        resultVo.setNewProductSkuStallPrice(fixOrder.getNewProductSkuStallPrice());
        resultVo.setContactMobile(fixOrder.getContactMobile());
        resultVo.setNewImei(fixOrder.getNewImei());

        Map<Long, Integer> orderCount = this.getBaseMapper().getFixOrderCount(DiFixOrderCountDTO.builder().orderIds(Collections.singleton(fixOrder.getOrderId())).build()).stream().collect(Collectors.toMap(DiFixOrderCountDTO::getOrderId, DiFixOrderCountDTO::getCount));
        resultVo.setFixCount(Optional.ofNullable(orderCount.get(fixOrder.getOrderId())).orElse(0));

        DiInsuranceOrder insuranceOrder = insuranceOrderService.getById(fixOrder.getOrderId());

        ProductSku productSku = Optional.ofNullable(productSkuService.getById(insuranceOrder.getProductSkuId())).orElse(new ProductSku());
        Product product = productService.getById(productSku.getProductId());
        // 机器类型
        resultVo.setMobileType(Optional.ofNullable(product).map(Product::getType).orElse(0));
        resultVo.setOldSkuRetailPrice(productSku.getRetailPrice());
        resultVo.setOldSkuPurchaseInvoicePrice(insuranceOrder.getProductSkuPurchaseInvoicePrice());
        // 服务类型
        resultVo.setServiceType(diOptionService.getById(fixOrder.getServiceType()));

        // 数保服务类型对应的理赔项目
        List<DiOption> optionList = insuranceOptionService.getOptionIdsByPid(fixOrder.getServiceType());

        // 保险材料
        List<DiInsuranceFixOrderOption> fixOrderOptionList = fixOrderOptionService.list(Wrappers.lambdaQuery(DiInsuranceFixOrderOption.class)
                .eq(DiInsuranceFixOrderOption::getOrderId, fixOrder.getId()));

        Map<Long, DiInsuranceFixOrderOption> fixOptionMap = fixOrderOptionList.stream().collect(Collectors.toMap(DiInsuranceFixOrderOption::getOptionId, Function.identity()));


        if (CollUtil.isNotEmpty(optionList)) {
            for (DiOption option : optionList) {
                // 报险资料
                List<DiOption> fixDataImages = insuranceOptionService.getCodeImages(option.getAncestors(), DiOptionCodeEnum.FIXDATA.getCode());
                if (CollUtil.isNotEmpty(fixDataImages)) {
                    List<DiOptionVO> fixDataImageVo = BeanUtil.copyToList(fixDataImages, DiOptionVO.class);
                    InsuranceDemoImageVO fixDataVo = new InsuranceDemoImageVO();
                    fixDataVo.setItemName(option.getName());
                    // 示例图用上传的图片替换
                    fixDataImageVo.stream().forEach(e -> {
                        if (fixOptionMap != null && fixOptionMap.get(e.getId()) != null) {
                            e.setValue(fixOptionMap.get(e.getId()).getValue());
                        }

                    });
                    fixDataVo.setImageList(fixDataImageVo.stream().filter(e -> StrUtil.isNotBlank(e.getValue()) && e.getType() == 3).collect(Collectors.toList()));
                    fixDataList.add(fixDataVo);
                }

                // 补充资料
                List<DiOption> suppleImages = insuranceOptionService.getCodeImages(option.getAncestors(), DiOptionCodeEnum.SUPPLEDATA.getCode());
                if (CollUtil.isNotEmpty(suppleImages)) {
                    List<DiOptionVO> suppleImageVo = BeanUtil.copyToList(suppleImages, DiOptionVO.class);
                    InsuranceDemoImageVO fixDataVo = new InsuranceDemoImageVO();
                    fixDataVo.setItemName(option.getName());
                    // 示例图用上传的图片替换
                    suppleImageVo.stream().forEach(e -> {
                        if (fixOptionMap != null && fixOptionMap.get(e.getId()) != null) {
                            e.setValue(fixOptionMap.get(e.getId()).getValue());
                        }

                    });
                    fixDataVo.setImageList(suppleImageVo.stream().filter(e -> StrUtil.isNotBlank(e.getValue()) && e.getType() == 3).collect(Collectors.toList()));
                    suppleDataList.add(fixDataVo);
                }

                // 理赔资料
                List<DiOption> settleImages = insuranceOptionService.getCodeImages(option.getAncestors(), DiOptionCodeEnum.SETTLEDATA.getCode());
                if (CollUtil.isNotEmpty(settleImages)) {
                    List<DiOptionVO> settleImageVo = BeanUtil.copyToList(settleImages, DiOptionVO.class);
                    InsuranceDemoImageVO fixDataVo = new InsuranceDemoImageVO();
                    fixDataVo.setItemName(option.getName());
                    // 示例图用上传的图片替换
                    settleImageVo.stream().forEach(e -> {
                        if (fixOptionMap != null && fixOptionMap.get(e.getId()) != null) {
                            e.setValue(fixOptionMap.get(e.getId()).getValue());
                        }
                    });
                    fixDataVo.setImageList(settleImageVo.stream().filter(e -> StrUtil.isNotBlank(e.getValue()) && (e.getType() == 3 || e.getType() == 4)).collect(Collectors.toList()));
                    settleDataList.add(fixDataVo);
                }

            }
        }

        resultVo.setFixItemList(optionList);
        resultVo.setFixDataList(fixDataList);
        resultVo.setSuppleDataList(suppleDataList);
        resultVo.setSettleDataList(settleDataList);

        //折抵金额计算
        int days = 0;
        try {
            days = DateUtils.daysBetween(new Date(), insuranceOrder.getEffectiveDate());
        } catch (ParseException e) {
            throw new BaseException(-1, "日期转换异常");
        }

        if (days <= 365) {
            resultVo.setDiscountAmount(new BigDecimal(productSku.getRetailPrice()).multiply(new BigDecimal("0.60")).intValue());
            resultVo.setDiscountAmountDesc("(第一年按价格60%赔付)");
        } else {
            resultVo.setDiscountAmount(new BigDecimal(productSku.getRetailPrice()).multiply(new BigDecimal("0.50")).intValue());
            resultVo.setDiscountAmountDesc("(第二年按价格50%赔付)");
        }

        resultVo.setProductName(insuranceOrder.getProductName());
        resultVo.setProductSpec(insuranceOrder.getProductSpec());
        resultVo.setImeiNo(insuranceOrder.getImeiNo());
        resultVo.setCustomName(insuranceOrder.getCustomName());
        resultVo.setCustomPhone(insuranceOrder.getCustomPhone());
        resultVo.setIdCard(insuranceOrder.getIdCard());
        resultVo.setInsuranceName(insuranceOrder.getInsuranceName());
        resultVo.setInsuranceType(insuranceOrder.getInsuranceType());
        resultVo.setInsurancePeriod(insuranceOrder.getInsurancePeriod());

        resultVo.setNewProductName(fixOrder.getProductName());
        resultVo.setNewProductSpec(fixOrder.getProductSpec());
        resultVo.setNewProductSkuRetailPrice(fixOrder.getProductSkuRetailPrice());
        resultVo.setProductSkuId(fixOrder.getProductSkuId());
        resultVo.setSuppleAmount(fixOrder.getSuppleAmount());
        resultVo.setSettleAmount(fixOrder.getSettleAmount());
        resultVo.setDiscountAmount(fixOrder.getDiscountAmount());
        resultVo.setCreateTime(fixOrder.getCreateTime());
        resultVo.setUpProduct(fixOrder.getUpProduct());
        resultVo.setImeiRead(fixOrder.getImeiRead());
        resultVo.setBreakDown(fixOrder.getBreakDown());
        resultVo.setFixCity(fixOrder.getFixCity());
        resultVo.setRemark(fixOrder.getRemark());

        resultVo.setExpiredTime(DateUtils.adjustYear(DateUtils.adjustDay(resultVo.getCreateTime(), 1), insuranceOrder.getInsurancePeriod()));


        if (resultVo.getUpProduct() != null) {
            resultVo.setUpProductName(EnumUtil.getBy(DiUpProductEnum::getCode, resultVo.getUpProduct()).getDesc());
        }
        if (resultVo.getImeiRead() != null) {
            resultVo.setImeiReadName(EnumUtil.getBy(DiReadImeiEnum::getCode, resultVo.getImeiRead()).getDesc());
        }

        DiOption serviceTypeOption = diOptionService.getById(fixOrder.getServiceType());
        DiOption claimItemOption = diOptionService.getById(fixOrder.getClaimItem());


        resultVo.setServiceTypeName(serviceTypeOption.getName());
        resultVo.setClaimItemName(claimItemOption.getName());


        List<OrderLogDTO> logs = orderLogService.listAllLogsByOrderId(OrderLogQueryReq.builder().orderId(fixOrder.getId()).build());
        if (CollUtil.isNotEmpty(logs)) {
            logs.forEach(o -> {
                o.setStatusStr(EnumUtil.getBy(DiFixOrderStatusEnum::getCode, o.getStatus()).getDesc());
                o.setOperationStatusStr(EnumUtil.getBy(FixOrderOperationEnum::getCode, o.getOperationStatus()).getDesc());
            });
        }
        resultVo.setLogs(logs);

        Map<Long, Employee> empMap = emService.getEmployeeByMap(Collections.singleton(resultVo.getCreateBy()));
        Map<Long, DiInsuranceUserAccount> userMap = insuranceUserAccountService.getUserMap(Collections.singleton(resultVo.getCreateBy()));
        Optional<Employee> employee = Optional.ofNullable(empMap.get(resultVo.getCreateBy()));
        Optional<DiInsuranceUserAccount> userAccount = Optional.ofNullable(userMap.get(resultVo.getCreateBy()));
        resultVo.setCreatorName(employee.map(Employee::getName).orElse(userAccount.map(DiInsuranceUserAccount::getName).orElse(null)));
        resultVo.setCreatorPhone(employee.map(Employee::getMobileNumber).orElse(userAccount.map(DiInsuranceUserAccount::getMobile).orElse(null)));

        resultVo.setStoreCompanyId(insuranceOrder.getStoreCompanyId());
        Company company = companyService.getById(insuranceOrder.getStoreCompanyId());
        resultVo.setStoreCompanyName(company.getName());
        resultVo.setStoreCompanyManageName(company.getContact());
        resultVo.setStoreCompanyManageMobile(company.getContactMobile());
        return resultVo;
    }

    @Transactional(rollbackFor = Exception.class)
    public void close(DiInsuranceFixOrderAuditReq req) {
        redisLockService.redisLock("pc_insurance_fix_order_close_audit_lock", req.getId());
        DiInsuranceFixOrder fixOrder = Optional.ofNullable(this.getById(req.getId())).orElseThrow(() -> new BaseException(-1, "订单不存在"));
        if (fixOrder.getStatus().equals(DiFixOrderStatusEnum.CANCEL.getCode())) {
            throw new BaseException(-1, "订单已关闭");
        }
        fixOrder.setStatus(DiFixOrderStatusEnum.CANCEL.getCode());
        this.updateById(fixOrder);
        orderLogService.addLog(
                ShiroUtils.getUserId(),
                fixOrder.getId(),
                DiFixOrderStatusEnum.CANCEL.getCode(),
                FixOrderOperationEnum.CANCEL.getCode(),
                FixOrderOperationEnum.CANCEL.getDesc(),
                StrUtil.format("{} {}", req.getReason(), req.getRemark()));
    }

    @Transactional(rollbackFor = Exception.class)
    public void editNewProductSkuStallPrice(DiInsuranceFixOrderAmountReq req) {
        redisLockService.redisLock("pc_insurance_fix_order_edit_stall_price_lock", req.getId());
        DiInsuranceFixOrder fixOrder = Optional.ofNullable(this.getById(req.getId())).orElseThrow(() -> new BaseException(-1, "订单不存在"));
        if (fixOrder.getStatus().equals(DiFixOrderStatusEnum.CANCEL.getCode())) {
            throw new BaseException(-1, "订单已关闭");
        }
        // todo other status check
        fixOrder.setNewProductSkuStallPrice(StringUtils.yuanToFen(req.getAmount()));
        this.updateById(fixOrder);
        orderLogService.addLog(
                ShiroUtils.getUserId(),
                fixOrder.getId(),
                fixOrder.getStatus(),
                FixOrderOperationEnum.EDIT_STALL_PRICE.getCode(),
                FixOrderOperationEnum.EDIT_STALL_PRICE.getDesc(),
                FixOrderOperationEnum.EDIT_STALL_PRICE.getRemark());
    }

    @Transactional(rollbackFor = Exception.class)
    public void editSettleAmount(DiInsuranceFixOrderAmountReq req) {
        redisLockService.redisLock("pc_insurance_fix_order_edit_settle_amount_lock", req.getId());
        DiInsuranceFixOrder fixOrder = Optional.ofNullable(this.getById(req.getId())).orElseThrow(() -> new BaseException(-1, "订单不存在"));
        if (fixOrder.getStatus().equals(DiFixOrderStatusEnum.CANCEL.getCode())) {
            throw new BaseException(-1, "订单已关闭");
        }
        // todo other status check
        fixOrder.setSettleAmount(StringUtils.yuanToFen(req.getAmount()));
        this.updateById(fixOrder);
        orderLogService.addLog(
                ShiroUtils.getUserId(),
                fixOrder.getId(),
                fixOrder.getStatus(),
                FixOrderOperationEnum.EDIT_SETTLE_PRICE.getCode(),
                FixOrderOperationEnum.EDIT_SETTLE_PRICE.getDesc(),
                FixOrderOperationEnum.EDIT_SETTLE_PRICE.getRemark());
    }
}