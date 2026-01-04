package com.zxtx.hummer.exchange.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zxtx.hummer.account.constant.EmployAccountChangeEnum;
import com.zxtx.hummer.account.service.EmployeeAccountChangeService;
import com.zxtx.hummer.commission.domain.CommissionSettle;
import com.zxtx.hummer.commission.domain.CommissionSettleCheck;
import com.zxtx.hummer.commission.enums.CommissionBizType;
import com.zxtx.hummer.commission.enums.CommissionPackage;
import com.zxtx.hummer.commission.service.CommissionSettleCheckService;
import com.zxtx.hummer.commission.service.CommissionSettleService;
import com.zxtx.hummer.common.Constants;
import com.zxtx.hummer.common.exception.BaseException;
import com.zxtx.hummer.common.exception.BusinessException;
import com.zxtx.hummer.common.lock.RedisLockService;
import com.zxtx.hummer.common.service.DictService;
import com.zxtx.hummer.common.utils.DateUtils;
import com.zxtx.hummer.common.utils.PageUtils;
import com.zxtx.hummer.common.utils.ShiroUtils;
import com.zxtx.hummer.company.domain.Company;
import com.zxtx.hummer.company.service.CompanyService;
import com.zxtx.hummer.em.dao.mapper.EmployeeMapper;
import com.zxtx.hummer.em.domain.Employee;
import com.zxtx.hummer.em.service.EmService;
import com.zxtx.hummer.exchange.dao.mapper.MbExchangeOrderMapper;
import com.zxtx.hummer.exchange.domain.MbExchangeCustom;
import com.zxtx.hummer.exchange.domain.MbExchangeDevice;
import com.zxtx.hummer.exchange.domain.MbExchangeOrder;
import com.zxtx.hummer.exchange.domain.MbExchangePic;
import com.zxtx.hummer.exchange.domain.dto.ExchangeOrderCommissionBackDTO;
import com.zxtx.hummer.exchange.domain.dto.ExchangeOrderFillDTO;
import com.zxtx.hummer.exchange.domain.dto.ExchangePhoneVerifyDTO;
import com.zxtx.hummer.exchange.enums.ExchangeOrderLogStatus;
import com.zxtx.hummer.exchange.enums.ExchangeOrderTypeEnum;
import com.zxtx.hummer.exchange.enums.ExchangeSettleStatus;
import com.zxtx.hummer.exchange.enums.ExchangeStatus;
import com.zxtx.hummer.exchange.req.ExchangeOrderReq;
import com.zxtx.hummer.exchange.vo.ExchangeOrderSummaryVO;
import com.zxtx.hummer.exchange.vo.ExchangeOrderVO;
import com.zxtx.hummer.exchange.vo.ExchangeOrderViewVO;
import com.zxtx.hummer.exchange.vo.ExchangeSubAdminOrderVO;
import com.zxtx.hummer.product.domain.OrderLog;
import com.zxtx.hummer.product.domain.dto.OrderLogDTO;
import com.zxtx.hummer.product.service.OrderLogService;
import com.zxtx.hummer.system.dao.mapper.SysUserMapper;
import com.zxtx.hummer.system.dao.mapper.UserRoleMapper;
import com.zxtx.hummer.system.domain.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 换机晒单表 服务实现类
 * </p>
 *
 * @author L
 * @since 2024-03-27
 */
@Service
@Slf4j
public class MbExchangeOrderService extends ServiceImpl<MbExchangeOrderMapper, MbExchangeOrder>{

    @Autowired
    private EmService emService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private MbExchangeCustomService mbExchangeCustomService;
    @Autowired
    private MbExchangePicService mbExchangePicService;
    @Autowired
    private OrderLogService orderLogService;
    @Autowired
    private CommissionSettleService commissionSettleService;
    @Autowired
    private CommissionSettleCheckService commissionSettleCheckService;
    @Autowired
    private MbExchangeDeviceService mbExchangeDeviceService;
    @Autowired
    DictService dictService;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private EmployeeAccountChangeService employeeAccountChangeService;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private RedisLockService redisLockService;

    public List<ExchangeOrderVO> selectPage(ExchangeOrderReq req) {
        //连锁门店的所有门店
        if(StrUtil.isNotBlank(req.getChainCompanyName())){
            Set<Long> companyIds = companyService.getByChainName(req.getChainCompanyName());
            if(CollUtil.isEmpty(companyIds)) return Collections.emptyList();
            req.setCmpIds(companyIds);
        }
        PageHelper.startPage(req.getPage(), req.getPageSize());
        List<ExchangeOrderVO> resultList = this.getBaseMapper().selectByParam(req);
        if (CollUtil.isEmpty(resultList)) {
            return new ArrayList<>();
        }
        Set<Long> parentCompanyIds = resultList.stream()
                .map(ExchangeOrderVO::getStoreCompanyPId)
                .filter(Objects::nonNull)
                .filter(pid -> !pid.equals(Constants.LAN_HAI_CMP_ID))
                .collect(Collectors.toSet());
        Map<Long, Company> chainCompanyMap = new HashMap<>(1);
        if (CollUtil.isNotEmpty(parentCompanyIds)) {
            chainCompanyMap = companyService.lambdaQuery()
                    .in(Company::getId, parentCompanyIds)
                    .list()
                    .stream()
                    .filter(Objects::nonNull)
                    .collect(Collectors.toMap(Company::getId, Function.identity()));
        }
        Set<Long> userIds = resultList.stream().map(ExchangeOrderVO::getUpdateBy).collect(Collectors.toSet());
        Map<Long, SysUser> sysUserMap = sysUserMapper.selectList(new LambdaQueryWrapper<SysUser>().in(SysUser::getUserId, userIds)).stream().filter(Objects::nonNull).collect(Collectors.toMap(SysUser::getUserId, Function.identity()));
        Map<Long, Employee> empMap = emService.getEmployeeByMap(userIds);

        for (ExchangeOrderVO o : resultList) {
            if (o.getStoreCompanyPId() != null) {
                o.setChainStoreName(Optional.ofNullable(chainCompanyMap.get(o.getStoreCompanyPId())).map(Company::getName).orElse(null));
            }
            if (o.getStatus().equals(ExchangeStatus.PASS.getCode()) && o.getSettleStatus().equals(ExchangeSettleStatus.PASS.getCode())) {
                o.setCommissionBackBtn(true);
            }
            o.setUpdaterName(Optional.ofNullable(sysUserMap.get(o.getUpdateBy())).map(SysUser::getName).orElse(Optional.ofNullable(empMap.get(o.getUpdateBy())).map(Employee::getName).orElse(null)));
        }
        return resultList;
    }

    public PageUtils<ExchangeSubAdminOrderVO> subAdminOrderList(ExchangeOrderReq req) {
        Integer employeeRoleType = ShiroUtils.getUser().getEmployeeRoleType();
        if (employeeRoleType == null) {
            return PageUtils.emptyPage();
        }
        switch (employeeRoleType) {
            case 1: {
                req.setBdId(ShiroUtils.getUser().getEmployeeId());
                break;
            }
            case 2: {
                req.setAreaId(ShiroUtils.getUser().getEmployeeId());
                break;
            }
            case 3: {
                req.setAgentId(ShiroUtils.getUser().getEmployeeId());
                break;
            }
            case 4: {
                req.setChainCompanyId(ShiroUtils.getUser().getCompanyId());
                break;
            }
            case 5: {
                req.setCmpIds(Collections.singleton(ShiroUtils.getUser().getCompanyId()));
                break;
            }
            default: {
                break;
            }
        }
        if (req.getChainCompanyId() != null) {
            Set<Long> companyIds = companyService.getByChainId(req.getChainCompanyId());
            if (CollUtil.isEmpty(companyIds)) {
                return PageUtils.emptyPage();
            }
            req.setCmpIds(companyIds);
        }
        if (StrUtil.isNotBlank(req.getChainCompanyMobile())) {
            Employee employee = emService.getByMobile(req.getChainCompanyMobile());
            if (employee == null) {
                return PageUtils.emptyPage();
            }
            Set<Long> companyIds = companyService.getByChainId(employee.getCompanyId());
            if (CollUtil.isEmpty(companyIds)) {
                return PageUtils.emptyPage();
            }
            req.setCmpIds(companyIds);
        }

        if (req.getStatus() != null && req.getStatus().equals(ExchangeStatus.TO_AUDIT.getCode())) {
            req.setSpecialStatus(Arrays.asList(ExchangeStatus.SYS_Fail.getCode(), ExchangeStatus.SYS_PASS.getCode(), ExchangeStatus.TO_AUDIT.getCode()));
            req.setStatus(null);
        }

        Page<Object> page = PageHelper.startPage(req.getPage(), req.getPageSize());
        List<ExchangeOrderVO> resultList = this.getBaseMapper().selectByParam(req);
        if (CollUtil.isEmpty(resultList)) {
            return PageUtils.emptyPage();
        }
        List<ExchangeSubAdminOrderVO> vos = BeanUtil.copyToList(resultList, ExchangeSubAdminOrderVO.class);
        Set<Long> parentCompanyIds = vos.stream()
                .map(ExchangeSubAdminOrderVO::getStoreCompanyPId)
                .filter(Objects::nonNull)
                .filter(pid -> !pid.equals(Constants.LAN_HAI_CMP_ID))
                .collect(Collectors.toSet());
        Map<Long, Company> chainCompanyMap = new HashMap<>(1);
        if (CollUtil.isNotEmpty(parentCompanyIds)) {
            chainCompanyMap = companyService.lambdaQuery()
                    .in(Company::getId, parentCompanyIds)
                    .list()
                    .stream()
                    .filter(Objects::nonNull)
                    .collect(Collectors.toMap(Company::getId, Function.identity()));
        }
        for (ExchangeSubAdminOrderVO o : vos) {
            if (o.getStoreCompanyPId() != null) {
                o.setChainStoreName(Optional.ofNullable(chainCompanyMap.get(o.getStoreCompanyPId())).map(Company::getName).orElse(null));
            }
            if (o.getStatus().equals(ExchangeStatus.SYS_Fail.getCode()) || o.getStatus().equals(ExchangeStatus.SYS_PASS.getCode())) {
                o.setStatus(ExchangeStatus.TO_AUDIT.getCode());
            }
        }
        return new PageUtils<>(vos, page.getTotal());
    }

    public ExchangeOrderSummaryVO orderSummary(ExchangeOrderReq req) {
        //连锁门店的所有门店
        if(StrUtil.isNotBlank(req.getChainCompanyName())){
            Set<Long> companyIds = companyService.getByChainName(req.getChainCompanyName());
            if(CollUtil.isEmpty(companyIds)) return new ExchangeOrderSummaryVO();
            req.setCmpIds(companyIds);
        }
        // PageHelper.startPage(req.getPage(), req.getPageSize());
        ExchangeOrderSummaryVO orderSummaryVO = this.getBaseMapper().orderSummary(req);
        return orderSummaryVO;
    }

    public ExchangeOrderSummaryVO subAdminOrderSummary(ExchangeOrderReq req) {
        Integer employeeRoleType = ShiroUtils.getUser().getEmployeeRoleType();
        if (employeeRoleType == null) {
            return new ExchangeOrderSummaryVO();
        }
        switch (employeeRoleType) {
            case 1: {
                req.setBdId(ShiroUtils.getUser().getEmployeeId());
                break;
            }
            case 2: {
                req.setAreaId(ShiroUtils.getUser().getEmployeeId());
                break;
            }
            case 3: {
                req.setAgentId(ShiroUtils.getUser().getEmployeeId());
                break;
            }
            case 4: {
                req.setChainCompanyId(ShiroUtils.getUser().getCompanyId());
                break;
            }
            case 5: {
                req.setCmpIds(Collections.singleton(ShiroUtils.getUser().getCompanyId()));
                break;
            }
            default: {
                break;
            }
        }
        if (req.getChainCompanyId() != null) {
            Set<Long> companyIds = companyService.getByChainId(req.getChainCompanyId());
            if (CollUtil.isEmpty(companyIds)) {
                return new ExchangeOrderSummaryVO();
            }
            req.setCmpIds(companyIds);
        }
        if (StrUtil.isNotBlank(req.getChainCompanyMobile())) {
            Employee employee = emService.getByMobile(req.getChainCompanyMobile());
            if (employee == null) {
                return new ExchangeOrderSummaryVO();
            }
            Set<Long> companyIds = companyService.getByChainId(employee.getCompanyId());
            if (CollUtil.isEmpty(companyIds)) {
                return new ExchangeOrderSummaryVO();
            }
            req.setCmpIds(companyIds);
        }
        // PageHelper.startPage(req.getPage(), req.getPageSize());
        ExchangeOrderSummaryVO orderSummaryVO = this.getBaseMapper().orderSummary(req);
        return orderSummaryVO;
    }

    public ExchangeOrderViewVO view(Long id){
        ExchangeOrderViewVO resultVo = new ExchangeOrderViewVO();
        MbExchangeOrder order = this.getById(id);
        List<Long> emIds = Arrays.asList(order.getStoreEmployeeId(),order.getBdId(),order.getAgentId(),order.getAreaId());
        Map<Long, Employee> employeeMap =  emService.getEmployeeByMap(emIds);

        Company company = companyService.getById(order.getStoreCompanyId());

        resultVo.setStoreName(company.getName());
        resultVo.setStoreEmployee(Optional.ofNullable(employeeMap.get(order.getStoreEmployeeId())).map(Employee::getName).orElse(""));
        resultVo.setStoreMobile(Optional.ofNullable(employeeMap.get(order.getStoreEmployeeId())).map(Employee::getMobileNumber).orElse(""));
        resultVo.setAgentName(Optional.ofNullable(employeeMap.get(order.getAgentId())).map(Employee::getName).orElse(""));
        resultVo.setAreaName(Optional.ofNullable(employeeMap.get(order.getAreaId())).map(Employee::getName).orElse(""));
        resultVo.setBdName(Optional.ofNullable(employeeMap.get(order.getBdId())).map(Employee::getName).orElse(""));
        resultVo.setSysMobile(order.getSysMobile());
        resultVo.setImeiNo(order.getImeiNo());
        resultVo.setExchangePhoneNo(order.getExchangePhoneNo());
        resultVo.setCreateTime(order.getCreateTime());
        resultVo.setTrialTime(order.getTrialTime());
        resultVo.setStatus(order.getStatus());
        resultVo.setSettleStatus(order.getSettleStatus());
        resultVo.setOaid(order.getOaid());
        resultVo.setRemark(order.getRemark());

        List<MbExchangeCustom> installList = getInstallList(id);
        if(CollUtil.isNotEmpty(installList)){
            resultVo.setCustomMobile(installList.get(0).getCustomPhone());
        }
        resultVo.setInstallList(installList);

        resultVo.setPicList(getPicList(id));

        //开机时长
        MbExchangeDevice device = mbExchangeDeviceService.lambdaQuery()
                .eq(MbExchangeDevice::getOrderId,order.getId())
                .last("limit 1").one();

        resultVo.setOpenTime(Optional.ofNullable(device).map(MbExchangeDevice::getOpenTime).orElse(0));
        resultVo.setBrand(Optional.ofNullable(device).map(MbExchangeDevice::getBrand).orElse(""));


        //门店晒单数
        Long companyNum = companyNum(order);
        resultVo.setCompanyNum(companyNum);
        //店员晒单书
        Long storeNum = storeNum(order);
        resultVo.setStoreNum(storeNum);

        Long sysCompanyNum = Long.valueOf(dictService.getByNameWithCache("exchange_company_num"));
        Long sysStoreNum = Long.valueOf(dictService.getByNameWithCache("exchange_store_num"));

        resultVo.setRedFlag(false);
        if(sysCompanyNum-companyNum<=0 || sysStoreNum-storeNum<=0){
            resultVo.setRedFlag(true);
        }

        List<OrderLog> logList =  orderLogService.list(Wrappers.lambdaQuery(OrderLog.class)
                .eq(OrderLog::getOrderId,order.getId()));
        resultVo.setOrderLogList(getLogs(logList));

        // set history orders
        setHistoryOrder(resultVo, order);

        return resultVo;
    }

    private void setHistoryOrder(ExchangeOrderViewVO resultVo, MbExchangeOrder order) {
        resultVo.setHistoryOrders(new ArrayList<>());
        if (StrUtil.isBlank(order.getCustomPhone())) {
            return;
        }
        List<MbExchangeOrder> historyOrders = this.lambdaQuery()
                .eq(MbExchangeOrder::getCustomPhone, order.getCustomPhone())
                .eq(StrUtil.isNotBlank(order.getOaid()), MbExchangeOrder::getOaid, order.getOaid())
                .ne(MbExchangeOrder::getId, order.getId())
                .list();
        if (CollUtil.isEmpty(historyOrders)) {
            return;
        }
        List<OrderLog> logList = new ArrayList<>();
        historyOrders.forEach(o -> {
            OrderLog l = orderLogService.getOne(Wrappers.lambdaQuery(OrderLog.class)
                    .eq(OrderLog::getOrderId, o.getId()).orderByDesc(OrderLog::getCreateTime).last("limit 1"));
            if (l != null) {
                logList.add(l);
            }
        });
        if (CollUtil.isEmpty(logList)) {
            return;
        }
        List<OrderLogDTO> logs = getLogs(logList);
        resultVo.setHistoryOrders(logs);
    }

    public ExchangeOrderViewVO subAdminOrderView(Long id) {
        ExchangeOrderViewVO resultVo = new ExchangeOrderViewVO();
        MbExchangeOrder order = this.getById(id);
        List<Long> emIds = Arrays.asList(order.getStoreEmployeeId(),order.getBdId(),order.getAgentId(),order.getAreaId());
        Map<Long, Employee> employeeMap =  emService.getEmployeeByMap(emIds);

        Company company = companyService.getById(order.getStoreCompanyId());

        resultVo.setStoreName(company.getName());
        resultVo.setStoreEmployee(Optional.ofNullable(employeeMap.get(order.getStoreEmployeeId())).map(Employee::getName).orElse(""));
        resultVo.setStoreMobile(Optional.ofNullable(employeeMap.get(order.getStoreEmployeeId())).map(Employee::getMobileNumber).orElse(""));
        resultVo.setAgentName(Optional.ofNullable(employeeMap.get(order.getAgentId())).map(Employee::getName).orElse(""));
        resultVo.setAreaName(Optional.ofNullable(employeeMap.get(order.getAreaId())).map(Employee::getName).orElse(""));
        resultVo.setBdName(Optional.ofNullable(employeeMap.get(order.getBdId())).map(Employee::getName).orElse(""));
        resultVo.setSysMobile(order.getSysMobile());
        resultVo.setImeiNo(order.getImeiNo());
        resultVo.setExchangePhoneNo(order.getExchangePhoneNo());
        resultVo.setCreateTime(order.getCreateTime());
        resultVo.setTrialTime(order.getTrialTime());
        resultVo.setStatus(order.getStatus());
        resultVo.setSettleStatus(order.getSettleStatus());


        List<MbExchangeCustom> installList = getInstallList(id);

        if(CollUtil.isNotEmpty(installList)){
            resultVo.setCustomMobile(installList.get(0).getCustomPhone());
        }
        resultVo.setInstallList(installList);

        resultVo.setPicList(getPicList(id));

        //开机时长
        MbExchangeDevice device = mbExchangeDeviceService.lambdaQuery()
                .eq(MbExchangeDevice::getOrderId,order.getId())
                .last("limit 1").one();

        resultVo.setOpenTime(Optional.ofNullable(device).map(MbExchangeDevice::getOpenTime).orElse(0));
        resultVo.setBrand(Optional.ofNullable(device).map(MbExchangeDevice::getBrand).orElse(""));


        //门店晒单数
        Long companyNum = companyNum(order);
        resultVo.setCompanyNum(companyNum);
        //店员晒单书
        Long storeNum = storeNum(order);
        resultVo.setStoreNum(storeNum);

        Long sysCompanyNum = Long.valueOf(dictService.getByNameWithCache("exchange_company_num"));
        Long sysStoreNum = Long.valueOf(dictService.getByNameWithCache("exchange_store_num"));

        resultVo.setRedFlag(false);
        if(sysCompanyNum-companyNum<=0 || sysStoreNum-storeNum<=0){
            resultVo.setRedFlag(true);
        }

        List<OrderLog> logList =  orderLogService.list(Wrappers.lambdaQuery(OrderLog.class)
                .eq(OrderLog::getOrderId,order.getId()).notIn(OrderLog::getStatus, ExchangeStatus.SYS_Fail.getCode(), ExchangeStatus.SYS_PASS.getCode()));

        resultVo.setOrderLogList(getLogs(logList));

        return resultVo;
    }

    private List<MbExchangePic> getPicList(Long id) {
        if (id == null) {
            return new ArrayList<>();
        }
        List<MbExchangePic> list = mbExchangePicService.list(Wrappers.lambdaQuery(MbExchangePic.class)
                .eq(MbExchangePic::getOrderId, id));
        if (CollUtil.isEmpty(list)) {
            return new ArrayList<>();
        }
        String format = "?x-oss-process=image/format,jpg";
        list.forEach(img -> {
            // 转换成jpg格式显示，防止怕苹果HEIC格式的图片页面上显示不出来
            img.setImageUrl(img.getImageUrl() + format);
        });
        return list;
    }

    private List<MbExchangeCustom> getInstallList(Long id) {
        if (id == null) {
            return new ArrayList<>();
        }
        return mbExchangeCustomService.list(Wrappers.lambdaQuery(MbExchangeCustom.class)
                .eq(MbExchangeCustom::getOrderId, id));
    }

    private List<OrderLogDTO> getLogs(List<OrderLog> logList) {
        if (CollUtil.isEmpty(logList)) {
            return new ArrayList<>();
        }
        List<OrderLogDTO> logDTOList = BeanUtil.copyToList(logList, OrderLogDTO.class);
        Set<Long> userIds = logDTOList.stream().map(OrderLogDTO::getCreateBy).collect(Collectors.toSet());

        Map<Long, SysUser> sysUserMap = sysUserMapper.selectList(new LambdaQueryWrapper<SysUser>().in(SysUser::getUserId, userIds)).stream().filter(Objects::nonNull).collect(Collectors.toMap(SysUser::getUserId, Function.identity()));
        Map<Long, Employee> empMap = emService.getEmployeeByMap(userIds);

        logDTOList.forEach(e ->{
            e.setStatusStr(ExchangeStatus.getNameByCode(e.getStatus()));
            e.setOperationStatusStr(ExchangeOrderLogStatus.getNameByCode(e.getOperationStatus()));
            e.setCreator(Optional.ofNullable(sysUserMap.get(e.getCreateBy())).map(SysUser::getName).orElse(Optional.ofNullable(empMap.get(e.getCreateBy())).map(Employee::getName).orElse(null)));
        });
        return logDTOList;
    }

    @Transactional(rollbackFor = Exception.class)
    public void check(ExchangePhoneVerifyDTO dto){
        redisLockService.redisLock("pc_exchange_order_audit_lock", dto.getId());
        MbExchangeOrder order = this.lambdaQuery()
                .eq(MbExchangeOrder::getId, dto.getId())
                .last("for update")
                .one();
        if(!Arrays.asList(ExchangeStatus.TO_AUDIT.getCode(),ExchangeStatus.SYS_PASS.getCode(),ExchangeStatus.SYS_Fail.getCode())
                .contains(order.getStatus())){
            throw new BusinessException("订单不是待审核状态");
        }

        //换机的单数判断
        if(ExchangeOrderTypeEnum.HUAN_JI.getCode().equals(order.getType())){
            //当前登录人角色
            List<Long> roleIds = userRoleMapper.listRoleId(ShiroUtils.getUser().getUserId());
            //门店晒单数
            Long companyNum = companyNum(order);
            //店员晒单数
            Long storeNum = storeNum(order);
            Long sysCompanyNum = Long.valueOf(dictService.getByNameWithCache("exchange_company_num"));
            Long sysStoreNum = Long.valueOf(dictService.getByNameWithCache("exchange_store_num"));
            if((sysCompanyNum-companyNum<=0 || sysStoreNum-storeNum<=0) && !roleIds.contains(2L)){
                throw new BusinessException("晒单超过限制");
            }
        }

        CommissionPackage commissionPackage = getCommissionPackage(order.getType());
        EmployAccountChangeEnum employAccountChangeEnum = getAccountChangeEnum(order.getType());

        if(dto.getStatus().equals(ExchangeOrderLogStatus.PASS.getCode())){

            if(ExchangeOrderTypeEnum.HUAN_JI.getCode().equals(order.getType())){
                undoCommit(order);
            }

            if(StrUtil.isNotBlank(dto.getImeiNo())){
                List<MbExchangeOrder> existList = this.lambdaQuery().eq(MbExchangeOrder::getImeiNo,dto.getImeiNo())
                        .eq(MbExchangeOrder::getStatus,ExchangeStatus.PASS.getCode())
                        .eq(MbExchangeOrder::getType,order.getType()).list();
                if(CollUtil.isNotEmpty(existList)){
                    throw new BusinessException("IMEI已经审核过");
                }
            }

            List<MbExchangeOrder> existList = this.lambdaQuery()
                    .eq(MbExchangeOrder::getCustomPhone,order.getCustomPhone())
                    .eq(MbExchangeOrder::getStatus,ExchangeStatus.PASS.getCode())
                    .eq(MbExchangeOrder::getType,order.getType()).list();
            if(CollUtil.isNotEmpty(existList)){
                throw new BusinessException("客户已经晒单");
            }

            order.setStatus(ExchangeStatus.PASS.getCode());
            order.setSettleStatus(ExchangeSettleStatus.PASS.getCode());
            order.setPassRemark(dto.getRemark());
            //系统审核失败且不是跳过晒单
            if(ExchangeOrderTypeEnum.HUAN_JI.getCode().equals(order.getType()) && order.getSysStatus().equals(ExchangeStatus.SYS_Fail.getCode()) && order.getType().intValue()!=2){
                order.setIllegal(true);
            }else{
                order.setIllegal(false);
            }
            order.setImeiNo(dto.getImeiNo());
            order.setTrialTime(new Date());
            this.updateById(order);
            orderLogService.addLog(ShiroUtils.getUserId(),order.getId(),ExchangeStatus.PASS.getCode(),ExchangeOrderLogStatus.PASS.getCode(),dto.getReason(),dto.getRemark());
            //拉新绑定订单
            commissionSettleService.orderBindSettleRule(dto.getId(), CommissionBizType.APP_NEW, commissionPackage,order.getStoreEmployeeId());
            commissionSettleService.waitSettleOrder(dto.getId(), CommissionBizType.APP_NEW, commissionPackage.getType(),order.getStoreEmployeeId(),null,employAccountChangeEnum.getRemark());
            commissionSettleService.settleOrder(dto.getId(), CommissionBizType.APP_NEW, commissionPackage.getType(),employAccountChangeEnum,employAccountChangeEnum.getRemark());
        }

        if(dto.getStatus().equals(ExchangeOrderLogStatus.FAIL.getCode())){
            order.setStatus(ExchangeStatus.FAIL.getCode());
            order.setTrialTime(new Date());
            order.setImeiNo(dto.getImeiNo());
            order.setRemark(StrUtil.format("{} {}", dto.getReason(), dto.getRemark()));
            this.updateById(order);
            // orderLogService.addLog(ShiroUtils.getUserId(),order.getId(),ExchangeStatus.FAIL.getCode(),ExchangeOrderLogStatus.FAIL.getCode(),dto.getReason(),dto.getRemark());
            orderLogService.addLog(
                    ShiroUtils.getUserId(),
                    order.getId(),
                    ExchangeStatus.FAIL.getCode(),ExchangeOrderLogStatus.FAIL.getCode(),
                    ExchangeOrderLogStatus.FAIL.getName(),
                    StrUtil.format("{} {}", dto.getReason(), dto.getRemark()));
        }

        //订单晒单图片更新
        if(CollUtil.isNotEmpty(dto.getPicList())){
            dto.getPicList().stream().forEach(e -> {
                MbExchangePic mbExchangePic = mbExchangePicService.getById(e.getId());
                mbExchangePic.setUid(e.getUid());
                mbExchangePic.setDid(e.getDid());
                mbExchangePicService.updateById(mbExchangePic);
            });
        }

    }

    @Transactional(rollbackFor = Exception.class)
    public void rollbackOrderStatus(ExchangePhoneVerifyDTO dto) {
        MbExchangeOrder order = this.getById(dto.getId());
        if (!Objects.equals(ExchangeStatus.FAIL.getCode(), order.getStatus())) {
            throw new BusinessException("当前订单状态不可进行操作");
        }

        order.setStatus(ExchangeStatus.SYS_Fail.getCode());
        order.setTrialTime(new Date());
        this.updateById(order);
        orderLogService.addLog(
                ShiroUtils.getUserId(),
                order.getId(),
                ExchangeStatus.SYS_Fail.getCode(),
                ExchangeOrderLogStatus.ROLLBACK.getCode(),
                ExchangeOrderLogStatus.ROLLBACK.getName(),
                StrUtil.format("{} {}", StrUtil.isBlank(dto.getReason())?"":dto.getReason(), dto.getRemark()));
    }

    private Long companyNum(MbExchangeOrder order){
        return this.lambdaQuery().eq(MbExchangeOrder::getStoreCompanyId,order.getStoreCompanyId()).eq(MbExchangeOrder::getStatus,ExchangeStatus.PASS.getCode()).between(MbExchangeOrder::getTrialTime, DateUtil.beginOfDay(new Date()),DateUtil.endOfDay(new Date())).count();
    }

    private Long storeNum(MbExchangeOrder order){
        return this.lambdaQuery().eq(MbExchangeOrder::getStoreEmployeeId,order.getStoreEmployeeId()).eq(MbExchangeOrder::getStatus,ExchangeStatus.PASS.getCode()).between(MbExchangeOrder::getTrialTime, DateUtil.beginOfDay(new Date()),DateUtil.endOfDay(new Date())).count();
    }

    private CommissionPackage getCommissionPackage(Integer orderType){
        CommissionPackage commissionPackage = null;
        switch (EnumUtil.getBy(ExchangeOrderTypeEnum::getCode, orderType)) {
            case HUAN_JI: {
                commissionPackage = CommissionPackage.HUAN_JI;
                break;
            }
            case LV_ZHOU: {
                commissionPackage = CommissionPackage.KUAI_SHOU_LV_SHOU;
                break;
            }
            case IPHONE_DOUYIN: {
                commissionPackage = CommissionPackage.IPHONE_DOUYIN;
                break;
            }
            default: {
                commissionPackage = CommissionPackage.YJLX;
                break;
            }
        }
        return commissionPackage;
    }

    private EmployAccountChangeEnum getAccountChangeEnum(Integer orderType){
        EmployAccountChangeEnum employAccountChangeEnum = null;
        switch (EnumUtil.getBy(ExchangeOrderTypeEnum::getCode, orderType)) {
            case HUAN_JI: {
                employAccountChangeEnum = EmployAccountChangeEnum.app_lx_hjzs;
                break;
            }
            case LV_ZHOU: {
                employAccountChangeEnum = EmployAccountChangeEnum.app_lx_kslz;
                break;
            }
            case IPHONE_DOUYIN: {
                employAccountChangeEnum = EmployAccountChangeEnum.app_lx_pgdy;
                break;
            }
            default: {
                employAccountChangeEnum = EmployAccountChangeEnum.app_lx_yjlx;
                break;
            }
        }
        return employAccountChangeEnum;
    }

    public void fillSn(ExchangeOrderFillDTO dto){
        MbExchangeOrder order = this.getById(dto.getId());
        if(ObjectUtil.isNull(order)){
            throw new BaseException(99999,"订单不存在");
        }

        List<MbExchangeCustom> customInstallList = mbExchangeCustomService.list(Wrappers.lambdaQuery(MbExchangeCustom.class)
                .eq(MbExchangeCustom::getOrderSn,dto.getOrderSn()));

        if(CollUtil.isEmpty(customInstallList)){
            throw new BaseException(99999,"单号安装记录不存在");
        }

        String customPhone = customInstallList.get(0).getCustomPhone();
        String employeePhone = customInstallList.get(0).getEmployeePhone();
        String oaid = customInstallList.get(0).getOaid();

        order.setOaid(StrUtil.isNotBlank(oaid)?oaid:"");
        order.setCustomPhone(customPhone);

        //可提交判断
        checkExchangeApply(order);

        this.updateById(order);

        //更新客户安装记录,回填订单号
        mbExchangeCustomService.lambdaUpdate()
                .set(MbExchangeCustom::getOrderId, order.getId())
                .eq(MbExchangeCustom::getOaid, oaid)
                .update(new MbExchangeCustom());

        //更新客户安装设备记录,回填订单号
        mbExchangeDeviceService.lambdaUpdate()
                .set(MbExchangeDevice::getOrderId,order.getId())
                .eq(MbExchangeDevice::getOaid, oaid)
                .update(new MbExchangeDevice());

        undoCommit(order);
    }

    public void undoCommit(MbExchangeOrder order){

        if(StrUtil.isBlank(order.getOaid())){
            throw new BaseException(99999,"设备OAID不存在");
        }

        Employee employee = emService.getById(order.getStoreEmployeeId());

        List<MbExchangeOrder> customList = this.list(Wrappers.lambdaQuery(MbExchangeOrder.class)
                .eq(MbExchangeOrder::getCustomPhone,order.getCustomPhone())
                .isNotNull(MbExchangeOrder::getBdId)
                .eq(MbExchangeOrder::getStatus,ExchangeStatus.PASS.getCode()));

        if(CollUtil.isNotEmpty(customList)){
            throw new BaseException(99999,"该账户已参加过换机");
        }

        long applyNum = this.count(Wrappers.lambdaQuery(MbExchangeOrder.class)
                .eq(MbExchangeOrder::getStoreEmployeeId,order.getStoreEmployeeId())
                .isNotNull(MbExchangeOrder::getBdId)
                .eq(MbExchangeOrder::getType,ExchangeOrderTypeEnum.HUAN_JI.getCode())
                .in(MbExchangeOrder::getStatus,
                        Arrays.asList(ExchangeStatus.SYS_Fail.getCode(),ExchangeStatus.TO_AUDIT.getCode(),ExchangeStatus.SYS_PASS.getCode())));

        int downNum = mbExchangeCustomService.list(Wrappers.lambdaQuery(MbExchangeCustom.class)
                .eq(MbExchangeCustom::getStoreEmployeeId,order.getStoreEmployeeId())
                .groupBy(MbExchangeCustom::getCustomPhone)).size();

        List<MbExchangeCustom> installList = mbExchangeCustomService.list(Wrappers.lambdaQuery(MbExchangeCustom.class)
                .eq(MbExchangeCustom::getCustomPhone,order.getCustomPhone()));

        if(CollUtil.isEmpty(installList)){
            throw new BaseException(99999,"客户安装记录不存在");
        }

        if(applyNum-downNum>0){
            throw new BaseException(99999,"晒单数大于下载数");
        }
        Date applyDate = installList.get(0).getCreateTime();
        String nowDate = DateUtils.getDateStr(order.getCreateTime());
        String applyTime = DateUtils.dateToStr(applyDate,"yyyy-MM-dd");

        //日期当天判断
        if(!applyTime.equals(nowDate)){
            throw new BaseException(99999,"换机日期不是当天的");
        }

        //OAID判断
        List<MbExchangeOrder> oaidList =  this.list(Wrappers.lambdaQuery(MbExchangeOrder.class)
                .eq(MbExchangeOrder::getOaid,order.getOaid())
                .eq(MbExchangeOrder::getStatus,ExchangeStatus.PASS.getCode()));
        if(CollUtil.isNotEmpty(oaidList)){
            throw new BaseException(99999,"设备信息已经存在");
        }


        //打开应用市场判断
        if(installList.stream().anyMatch(e -> Boolean.TRUE.equals(e.getMarketFlag()))){
            throw new BaseException(99999,"去应用商店安装改订单无效");
        }

        //晒单员工和下载员工不一致
        if(!employee.getMobileNumber().equals(installList.get(0).getEmployeePhone())){
            throw new BaseException(99999,"晒单员工和下载员工不一致");
        }

        //IMEI判断
        if(StrUtil.isNotBlank(order.getImeiNo())){
            List<MbExchangeOrder> imeiList =  this.list(Wrappers.lambdaQuery(MbExchangeOrder.class)
                    .eq(MbExchangeOrder::getImeiNo,order.getImeiNo())
                    .ne(MbExchangeOrder::getStatus,ExchangeStatus.FAIL.getCode()));
            if(CollUtil.isNotEmpty(imeiList)){
                throw new BaseException(99999,"该设备已参加过换机");
            }
        }

        //打开换机助手时间和做单时间差1个小时以上
        MbExchangeDevice device = mbExchangeDeviceService.lambdaQuery()
                .eq(MbExchangeDevice::getOaid,order.getOaid())
                .last("limit 1").one();

        //大于1小时
        if(ObjectUtil.isNull(device) || DateUtils.compareDifferMins(device.getCreateTime(),installList.get(0).getCreateTime())>60){
            throw new BaseException(99999,"做单耗时超过规定时间");
        }

    }

    private Boolean checkExchangeApply(MbExchangeOrder order){
        Boolean flag = true;
        StringJoiner remark = new StringJoiner(",");

        log.info("换机订单可提交判断{}",order.getCustomPhone());

       /* if(!employee.getMobileNumber().equals(employeePhone)){
            remark.add("晒单员工和下载员工不一致");
            flag=false;
        }*/

        /*if(StrUtil.isNotBlank(order.getImeiNo())){
            //IMEI判断
            List<MbExchangeOrder> imeiList =  this.list(Wrappers.lambdaQuery(MbExchangeOrder.class)
                    .eq(MbExchangeOrder::getImeiNo,order.getImeiNo())
                    .eq(MbExchangeOrder::getStatus,ExchangeStatus.PASS.getCode()));
            if(CollUtil.isNotEmpty(imeiList)){
                remark.add("该设备已参加过换机");
                flag=false;
            }
        }*/

        //开机时长判断
        MbExchangeDevice device = mbExchangeDeviceService.lambdaQuery()
                .eq(MbExchangeDevice::getOaid,order.getOaid())
                .last("limit 1").one();

        //大于3小时
        if(ObjectUtil.isNull(device) || ObjectUtil.isNull(device.getOpenTime()) || device.getOpenTime()>10800){
            remark.add("开机时长超出范围");
            flag=false;
        }

        //IMEI匹配判断
        /*List<MbExchangePic> picList =  mbExchangePicService.list(Wrappers.lambdaQuery(MbExchangePic.class)
                .eq(MbExchangePic::getOrderId,order.getId()));

        if(picList.size()!=2 || StrUtil.isBlank(picList.get(0).getImeiNo()) || StrUtil.isBlank(picList.get(1).getImeiNo())
                || !picList.get(0).getImeiNo().equals(picList.get(1).getImeiNo())){
            remark.add("图二IMEI不匹配");
            flag=false;
        }*/

        order.setRemark(remark.toString());
        return flag;
    }

    @Transactional(rollbackFor = Exception.class)
    public void commissionBack(ExchangeOrderCommissionBackDTO req) {
        Long orderId = req.getId();
        redisLockService.redisLock("exchange_order_commission_back_lock", orderId);
        MbExchangeOrder exchangeOrder = Optional.ofNullable(this.getById(orderId)).orElseThrow(() -> new BusinessException("订单不存在"));
        if (exchangeOrder.getSettleStatus().equals(ExchangeSettleStatus.COMMISSION_BACK.getCode())) {
            throw new BusinessException("已追回");
        }

        List<CommissionSettle> resultList = commissionSettleService.lambdaQuery()
                .eq(CommissionSettle::getCorrelationId,orderId).list();
        if (CollUtil.isNotEmpty(resultList)) {
            for (CommissionSettle settle : resultList) {
                String remark = "系统佣金追回";
                if (StrUtil.isNotBlank(req.getRemark())) {
                    remark = StrUtil.format("{}: {}", remark, req.getRemark());
                }
                employeeAccountChangeService.changeAccount(settle.getEmployeeId(), EmployAccountChangeEnum.sys_commission_back, settle.getSettleBalance(), settle.getCorrelationId(), remark);
            }
            //删除结算
            commissionSettleService.remove(Wrappers.lambdaQuery(CommissionSettle.class).eq(CommissionSettle::getCorrelationId,orderId));
            //删除合伙人账单
            commissionSettleCheckService.remove(Wrappers.lambdaQuery(CommissionSettleCheck.class).eq(CommissionSettleCheck::getCorrelationId,orderId));
        }

        //订单状态修改
        exchangeOrder.setStatus(ExchangeStatus.FAIL.getCode());
        exchangeOrder.setTrialTime(new Date());
        exchangeOrder.setSettleStatus(ExchangeSettleStatus.COMMISSION_BACK.getCode());
        this.updateById(exchangeOrder);

        String remark = "超管佣金追回";
        if (StrUtil.isNotBlank(req.getRemark())) {
            remark = StrUtil.format("{}: {}", remark, req.getRemark());
        }
        orderLogService.addLog(
                ShiroUtils.getUserId() != null ? ShiroUtils.getUserId() : 2L,
                orderId,
                ExchangeStatus.FAIL.getCode(),ExchangeOrderLogStatus.FAIL.getCode(),
                ExchangeOrderLogStatus.FAIL.getName(),
                remark);
    }

}