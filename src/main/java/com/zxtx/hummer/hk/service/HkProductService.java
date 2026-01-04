package com.zxtx.hummer.hk.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zxtx.hummer.common.domain.AbstractBaseEntity;
import com.zxtx.hummer.common.exception.BaseException;
import com.zxtx.hummer.common.exception.BusinessException;
import com.zxtx.hummer.common.lock.RedisLockService;
import com.zxtx.hummer.common.utils.PageUtils;
import com.zxtx.hummer.common.utils.ShiroUtils;
import com.zxtx.hummer.common.utils.SnowflakeIdWorker;
import com.zxtx.hummer.common.utils.StringUtils;
import com.zxtx.hummer.company.dao.mapper.CompanyMapper;
import com.zxtx.hummer.company.domain.Company;
import com.zxtx.hummer.company.enums.CompanyStatus;
import com.zxtx.hummer.company.req.CompanyExchangeDTO;
import com.zxtx.hummer.company.service.CompanyChainInfoService;
import com.zxtx.hummer.company.vo.CompanyChainInfoDTO;
import com.zxtx.hummer.company.vo.CompanyExchangeVo;
import com.zxtx.hummer.em.dao.mapper.EmployeeMapper;
import com.zxtx.hummer.em.domain.Employee;
import com.zxtx.hummer.em.enums.CompanyType;
import com.zxtx.hummer.em.enums.EmStatus;
import com.zxtx.hummer.hk.domain.HkOperator;
import com.zxtx.hummer.hk.domain.HkProduct;
import com.zxtx.hummer.hk.domain.HkProductEmployee;
import com.zxtx.hummer.hk.domain.HkSupplier;
import com.zxtx.hummer.hk.domain.enums.HkApplyOrderStatusEnum;
import com.zxtx.hummer.hk.domain.enums.HkOperationEnum;
import com.zxtx.hummer.hk.domain.request.*;
import com.zxtx.hummer.hk.domain.response.HkProductVO;
import com.zxtx.hummer.hk.domain.response.HkRelationCompanyVO;
import com.zxtx.hummer.product.domain.dto.OrderLogDTO;
import com.zxtx.hummer.product.domain.request.OrderLogQueryReq;
import com.zxtx.hummer.product.service.OrderLogService;
import com.zxtx.hummer.system.dao.mapper.SysUserMapper;
import com.zxtx.hummer.system.domain.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author WangWJ
 * @Description
 * @Date 2025/8/1
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Service
public class HkProductService {
    @Autowired
    private HkProductRepository hkProductRepository;
    @Autowired
    private HkOperatorRepository hkOperatorRepository;
    @Autowired
    private HkSupplierRepository hkSupplierRepository;
    @Autowired
    private OrderLogService orderLogService;
    @Autowired
    private RedisLockService redisLockService;
    @Autowired
    private HkProductEmployeeRepository hkProductEmployeeRepository;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private CompanyMapper companyMapper;
    @Autowired
    private CompanyChainInfoService companyChainInfoService;

    @Transactional(rollbackFor = Exception.class)
    public void add(HkProductAddReq req) {
        HkProduct bean = BeanUtil.copyProperties(req, HkProduct.class);
        bean.setId(SnowflakeIdWorker.nextID());

        Integer p = StringUtils.yuanToFen(req.getPrice());
        bean.setPrice(p.longValue());
        boolean success = hkProductRepository.save(bean);

        if (success) {
            orderLogService.addLog(ShiroUtils.getUserId(), bean.getId(), HkOperationEnum.CREATE.getCode(), HkOperationEnum.CREATE.getCode(), HkOperationEnum.CREATE.getDesc(), HkOperationEnum.CREATE.getRemark());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void edit(HkProductEditReq req) {
        redisLockService.redisLock("pc_hk_product_edit_lk", req.getId());
        Optional.ofNullable(hkProductRepository.lambdaQuery()
                .eq(AbstractBaseEntity::getDeleted, false)
                .eq(HkProduct::getId, req.getId())
                .one()).orElseThrow(() -> new BaseException(-1, "数据不存在"));
        HkProduct bean = BeanUtil.copyProperties(req, HkProduct.class);
        Integer p = StringUtils.yuanToFen(req.getPrice());
        bean.setPrice(p.longValue());
        boolean success = hkProductRepository.updateById(bean);

        if (success) {
            orderLogService.addLog(ShiroUtils.getUserId(), bean.getId(), HkOperationEnum.EDIT.getCode(), HkOperationEnum.EDIT.getCode(), HkOperationEnum.EDIT.getDesc(), HkOperationEnum.EDIT.getRemark());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void switchStatus(Long id) {
        redisLockService.redisLock("pc_hk_product_switch_status_lk", id);
        HkProduct bean = Optional.ofNullable(hkProductRepository.lambdaQuery()
                .eq(HkProduct::getId, id)
                .eq(AbstractBaseEntity::getDeleted, false)
                .one()).orElseThrow(() -> new BaseException(-1, "数据不存在"));
        int newStatus = bean.getStatus() == 1 ? 0 : 1;
        boolean success = hkProductRepository.lambdaUpdate()
                .set(HkProduct::getStatus, newStatus)
                .eq(HkProduct::getId, id)
                .eq(HkProduct::getStatus, bean.getStatus())
                .eq(AbstractBaseEntity::getDeleted, false)
                .update(new HkProduct());
        if (success) {
            HkOperationEnum op = newStatus == 1 ? HkOperationEnum.ONLINE : HkOperationEnum.OFFLINE;
            orderLogService.addLog(ShiroUtils.getUserId(), id, op.getCode(), op.getCode(), op.getDesc(), op.getRemark());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void remove(Long id) {
        redisLockService.redisLock("pc_hk_product_remove_lk", id);
        Long userId = ShiroUtils.getUserId();
        boolean success = hkProductRepository.lambdaUpdate()
                .set(AbstractBaseEntity::getDeleted, true)
                .eq(HkProduct::getId, id)
                .eq(AbstractBaseEntity::getDeleted, false)
                .update(new HkProduct());
        if (success) {
            orderLogService.addLog(userId, id, HkOperationEnum.DELETE.getCode(), HkOperationEnum.DELETE.getCode(), HkOperationEnum.DELETE.getDesc(), HkOperationEnum.DELETE.getRemark());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void editCommissionStatus(HkProductCommissionStatusReq req) {
        redisLockService.redisLock("pc_hk_product_edit_commission_status_lk", req.getId());
        boolean success = hkProductRepository.lambdaUpdate()
                .set(HkProduct::getCommissionStatus, req.getCommissionStatus())
                .set(HkProduct::getCommissionTypePackageId, req.getCommissionTypePackageId())
                .eq(HkProduct::getId, req.getId())
                .eq(AbstractBaseEntity::getDeleted, false)
                .update(new HkProduct());
        if (success) {
            orderLogService.addLog(ShiroUtils.getUserId(), req.getId(), HkOperationEnum.COMMISSION_STATUS.getCode(), HkOperationEnum.COMMISSION_STATUS.getCode(), HkOperationEnum.COMMISSION_STATUS.getDesc(), HkOperationEnum.COMMISSION_STATUS.getRemark());
        }
    }

    public PageUtils<HkProductVO> list(HkProductQueryReq req) {
        Page<Object> page = PageHelper.startPage(req.getPage(), req.getPageSize());
        List<HkProduct> list = hkProductRepository.lambdaQuery()
                .eq(req.getSupplierId() != null, HkProduct::getSupplierId, req.getSupplierId())
                .eq(req.getOperatorId() != null, HkProduct::getOperatorId, req.getOperatorId())
                .eq(req.getStatus() != null, HkProduct::getStatus, req.getStatus())
                .eq(req.getCommissionStatus() != null, HkProduct::getCommissionStatus, req.getCommissionStatus())
                .gt(req.getCreateStartTime() != null, HkProduct::getCreateTime, req.getCreateStartTime())
                .lt(req.getCreateEndTime() != null, HkProduct::getCreateTime, req.getCreateEndTime())
                .gt(req.getUpdateStartTime() != null, HkProduct::getUpdateTime, req.getUpdateStartTime())
                .lt(req.getUpdateEndTime() != null, HkProduct::getUpdateTime, req.getUpdateEndTime())
                .like(StrUtil.isNotBlank(req.getName()), HkProduct::getName, req.getName())
                .eq(StrUtil.isNotBlank(req.getCode()), HkProduct::getCode, req.getCode())
                .eq(AbstractBaseEntity::getDeleted, false)
                .orderByDesc(HkProduct::getSort)
                .orderByDesc(HkProduct::getCreateTime)
                .orderByDesc(HkProduct::getUpdateTime)
                .list();
        if (CollUtil.isEmpty(list)) {
            return PageUtils.emptyPage();
        }
        List<HkProductVO> resp = BeanUtil.copyToList(list, HkProductVO.class, CopyOptions.create().ignoreNullValue());
        Set<Long> sysUserIds = HkProductVO.extractIds(resp, HkProductVO::getCreateBy, HkProductVO::getUpdateBy);
        Map<Long, SysUser> sysUserMap = sysUserMapper.selectList(new LambdaQueryWrapper<SysUser>().in(SysUser::getUserId, sysUserIds)).stream().filter(Objects::nonNull).collect(Collectors.toMap(SysUser::getUserId, Function.identity()));
        Set<Long> supplierIds = resp.stream().map(HkProductVO::getSupplierId).collect(Collectors.toSet());
        Set<Long> operatorIds = resp.stream().map(HkProductVO::getOperatorId).collect(Collectors.toSet());
        Map<Long, HkSupplier> supplierMap = hkSupplierRepository.lambdaQuery().in(HkSupplier::getId, supplierIds).eq(AbstractBaseEntity::getDeleted, false)
                .eq(HkSupplier::getStatus, 1).list().stream().filter(Objects::nonNull).collect(Collectors.toMap(HkSupplier::getId, Function.identity()));
        Map<Long, HkOperator> operatorMap = hkOperatorRepository.lambdaQuery().in(HkOperator::getId, operatorIds).eq(AbstractBaseEntity::getDeleted, false)
                .eq(HkOperator::getStatus, 1).list().stream().filter(Objects::nonNull).collect(Collectors.toMap(HkOperator::getId, Function.identity()));
        resp.forEach(o -> {
            o.setCreator(Optional.ofNullable(sysUserMap.get(o.getCreateBy())).map(SysUser::getName).orElse(null));
            o.setUpdater(Optional.ofNullable(sysUserMap.get(o.getUpdateBy())).map(SysUser::getName).orElse(null));
            o.setSupplier(Optional.ofNullable(supplierMap.get(o.getSupplierId())).map(HkSupplier::getName).orElse(null));
            o.setOperator(Optional.ofNullable(operatorMap.get(o.getOperatorId())).map(HkOperator::getName).orElse(null));
            o.setPriceStr(StringUtils.fenToYuan(o.getPrice()));
            o.setCommissionStatusStr(o.getCommissionStatus() != null ? EnumUtil.getBy(HkApplyOrderStatusEnum::getCode, o.getCommissionStatus()).getDesc() : null);
            o.setArea(StringUtils.buildArea(o.getProvince(), o.getCity(), null, "/"));
            Long companyNum = hkProductEmployeeRepository.countByProductId(o.getId());
            o.setCompanyNum(companyNum);
        });

        return new PageUtils<>(resp, page.getTotal());
    }

    public HkProductVO detail(Long id) {
        HkProduct bean = Optional.ofNullable(hkProductRepository.lambdaQuery()
                .eq(HkProduct::getId, id)
                .eq(AbstractBaseEntity::getDeleted, false)
                .one()).orElseThrow(() -> new BaseException(-1, "数据不存在"));
        HkProductVO resp = BeanUtil.copyProperties(bean, HkProductVO.class);
        resp.setPriceStr(StringUtils.fenToYuan(bean.getPrice()));

        List<OrderLogDTO> logs = orderLogService.listAllLogsByOrderId(OrderLogQueryReq.builder().orderId(id).build());
        if (CollUtil.isNotEmpty(logs)) {
            logs.forEach(o-> {
                o.setStatusStr(EnumUtil.getBy(HkOperationEnum::getCode, o.getStatus()).getDesc());
                o.setOperationStatusStr(EnumUtil.getBy(HkOperationEnum::getCode, o.getOperationStatus()).getDesc());
            });
        }
        resp.setLogs(logs);

        resp.setArea(StringUtils.buildArea(resp.getProvince(), resp.getCity(), null, "/"));
        return resp;
    }

    public PageUtils<HkRelationCompanyVO> listRelationCompany(HkRelationCompanyReq req) {
        List<HkRelationCompanyVO> resp = new ArrayList<>();
        if (req.getEmployeeId() == null) {
            return PageUtils.emptyPage();
        }
        Employee employee = employeeMapper.selectById(req.getEmployeeId());
        if (employee == null) {
            return PageUtils.emptyPage();
        }
        List<Employee> employeeList = employeeMapper.selectList(Wrappers.lambdaQuery(Employee.class)
                .likeRight(Employee::getAncestors, employee.getAncestors())
                .eq(Employee::getDeptType, 1)
                .eq(Employee::getType, 1)
                .in(Employee::getCompanyType, Arrays.asList(CompanyType.STORE.getCode(), CompanyType.CHAIN.getCode()))
                .eq(Employee::getStatus, EmStatus.NORMAL.getCode()));
        if (CollUtil.isEmpty(employeeList)) {
            return PageUtils.emptyPage();
        }
        List<Long> emIds = employeeList.stream().map(Employee::getId).collect(Collectors.toList());

        Page<Object> page = PageHelper.startPage(req.getPage(), req.getPageSize());

        List<Company> companyList = companyMapper.selectList(Wrappers.lambdaQuery(Company.class)
                .in(Company::getEmployeeId,emIds)
                .eq(Company::getStatus, CompanyStatus.NORMAL.getCode())
                .in(Company::getType,Arrays.asList(CompanyType.STORE.getCode(),CompanyType.CHAIN.getCode())));
        if (CollUtil.isEmpty(companyList)) {
            return PageUtils.emptyPage();
        }

        Map<Long, CompanyChainInfoDTO> companyChainInfoMap = companyChainInfoService.buildCompanyChainInfo(companyList);

        for (Company company : companyList) {
            HkRelationCompanyVO companyVO = new HkRelationCompanyVO();
            companyVO.setEmployeeId(company.getEmployeeId());
            companyVO.setCompanyId(company.getId());
            companyVO.setCompanyName(company.getName());
            companyVO.setContact(company.getContact());
            companyVO.setContactMobile(company.getContactMobile());
            Optional<CompanyChainInfoDTO> chainInfo = Optional.ofNullable(companyChainInfoMap.get(company.getEmployeeId()));
            companyVO.setBdId(chainInfo.map(CompanyChainInfoDTO::getBdId).orElse(null));
            companyVO.setAreaId(chainInfo.map(CompanyChainInfoDTO::getAreaId).orElse(null));
            companyVO.setAgentId(company.getEmployeeId());
            companyVO.setBdName(chainInfo.map(CompanyChainInfoDTO::getBdName).orElse(null));
            companyVO.setAreaName(chainInfo.map(CompanyChainInfoDTO::getAreaName).orElse(null));
            companyVO.setAgentName(chainInfo.map(CompanyChainInfoDTO::getAgentName).orElse(null));

            HkProductEmployee relation = hkProductEmployeeRepository.getByEmployeeId(company.getEmployeeId(), req.getProductId());
            companyVO.setVerifyFlag(ObjectUtil.isNotNull(relation) ? true : false);
            companyVO.setProductId(Optional.ofNullable(relation).map(HkProductEmployee::getProductId).orElse(null));

            resp.add(companyVO);
        }

        return new PageUtils<>(resp, page.getTotal());
    }

    public List<HkRelationCompanyVO> listRelationCompanyInfo(HkRelationCompanyReq req) {
        List<HkRelationCompanyVO> resp = new ArrayList<>();
        List<Long> cmpIds;
        List<HkProductEmployee> relations = hkProductEmployeeRepository.listByProductId(req.getProductId());
        if (CollUtil.isEmpty(relations)) {
            return resp;
        }
        cmpIds = relations.stream().map(HkProductEmployee::getCompanyId).collect(Collectors.toList());

        List<Company> companyList = companyMapper.selectList(Wrappers.lambdaQuery(Company.class)
                .in(Company::getId,cmpIds)
                .eq(Company::getStatus,CompanyStatus.NORMAL.getCode())
                .in(Company::getType,Arrays.asList(CompanyType.STORE.getCode(),CompanyType.CHAIN.getCode())));

        if (CollUtil.isEmpty(companyList)) {
            return resp;
        }

        Map<Long, CompanyChainInfoDTO> companyChainInfoMap = companyChainInfoService.buildCompanyChainInfo(companyList);

        for (Company company : companyList) {
            HkRelationCompanyVO companyVO = new HkRelationCompanyVO();
            companyVO.setEmployeeId(company.getEmployeeId());
            companyVO.setCompanyId(company.getId());
            companyVO.setCompanyName(company.getName());
            companyVO.setContact(company.getContact());
            companyVO.setContactMobile(company.getContactMobile());

            Optional<CompanyChainInfoDTO> chainInfo = Optional.ofNullable(companyChainInfoMap.get(company.getEmployeeId()));
            companyVO.setBdId(chainInfo.map(CompanyChainInfoDTO::getBdId).orElse(null));
            companyVO.setAreaId(chainInfo.map(CompanyChainInfoDTO::getAreaId).orElse(null));
            companyVO.setAgentId(company.getEmployeeId());
            companyVO.setBdName(chainInfo.map(CompanyChainInfoDTO::getBdName).orElse(null));
            companyVO.setAreaName(chainInfo.map(CompanyChainInfoDTO::getAreaName).orElse(null));
            companyVO.setAgentName(chainInfo.map(CompanyChainInfoDTO::getAgentName).orElse(null));
            resp.add(companyVO);
        }

        return resp;
    }

    @Transactional(rollbackFor = Exception.class)
    public void editRelationCompany(HkRelationCompanyEditReq req) {
        redisLockService.redisLock("pc_hk_product_relation_company_edit_lk", req.getProductId());

        hkProductEmployeeRepository.remove(new LambdaQueryWrapper<HkProductEmployee>().eq(HkProductEmployee::getProductId, req.getProductId()));

        if (CollUtil.isEmpty(req.getCompanyList())) {
            return;
        }

        List<HkProductEmployee> saveList = new ArrayList<>();

        req.getCompanyList().forEach(e -> {
            HkProductEmployee rel = new HkProductEmployee();
            BeanUtil.copyProperties(e, rel);
            rel.setProductId(req.getProductId());
            saveList.add(rel);
        });

        hkProductEmployeeRepository.saveBatch(saveList);
    }

    public CompanyExchangeVo companyHkProductRelationInfo(Long companyId) {
        List<HkProductEmployee> list = hkProductEmployeeRepository.listByCompanyId(companyId);
        return CompanyExchangeVo.builder()
                .hkProductId(CollUtil.isEmpty(list) ? new ArrayList<>() : list.stream().map(HkProductEmployee::getProductId).collect(Collectors.toList()))
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    public void hkCompanyProductRelationEdit(CompanyExchangeDTO req) {
        redisLockService.redisLock("pc_hk_company_product_relation_edit_lk", req.getCompanyId());

        hkProductEmployeeRepository.remove(new LambdaQueryWrapper<HkProductEmployee>().eq(HkProductEmployee::getCompanyId, req.getCompanyId()));

        if (CollUtil.isEmpty(req.getHkProductId())) {
            return;
        }

        List<HkProductEmployee> saveList = new ArrayList<>();

        Company company = Optional.ofNullable(companyMapper.selectById(req.getCompanyId())).orElseThrow(() -> new BusinessException("门店信息不存在"));

        CompanyChainInfoDTO companyChainInfoDTO = Optional.ofNullable(companyChainInfoService.buildCompanyChainInfo(Collections.singletonList(company)).get(company.getEmployeeId()))
                .orElseThrow(() -> new BusinessException("门店信息不存在"));

        req.getHkProductId().forEach(e -> {
            HkProductEmployee rel = HkProductEmployee.builder()
                    .id(SnowflakeIdWorker.nextID())
                    .productId(e)
                    .employeeId(companyChainInfoDTO.getEmployeeId())
                    .companyId(companyChainInfoDTO.getCompanyId())
                    .bdId(companyChainInfoDTO.getBdId())
                    .bdName(companyChainInfoDTO.getBdName())
                    .areaId(companyChainInfoDTO.getAreaId())
                    .areaName(companyChainInfoDTO.getAreaName())
                    .agentId(companyChainInfoDTO.getAgentId())
                    .agentName(companyChainInfoDTO.getAgentName())
                    .build();
            saveList.add(rel);
        });

        hkProductEmployeeRepository.saveBatch(saveList);
    }
}