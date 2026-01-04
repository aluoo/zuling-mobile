package com.zxtx.hummer.commission.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.zxtx.hummer.account.constant.EmployAccountChangeEnum;
import com.zxtx.hummer.account.service.EmployeeAccountChangeService;
import com.zxtx.hummer.commission.dao.mapper.CommissionSettleMapper;
import com.zxtx.hummer.commission.domain.*;
import com.zxtx.hummer.commission.dto.*;
import com.zxtx.hummer.commission.enums.CommissionBizType;
import com.zxtx.hummer.commission.enums.CommissionPackage;
import com.zxtx.hummer.commission.enums.CommissionSettleGainType;
import com.zxtx.hummer.commission.enums.SettleStatus;
import com.zxtx.hummer.commission.req.SettleLogQueryReq;
import com.zxtx.hummer.common.Constants;
import com.zxtx.hummer.common.exception.BusinessException;
import com.zxtx.hummer.common.utils.ShiroUtils;
import com.zxtx.hummer.common.utils.SnowflakeIdWorker;
import com.zxtx.hummer.common.utils.StringUtils;
import com.zxtx.hummer.company.dao.mapper.CompanyMapper;
import com.zxtx.hummer.company.domain.Company;
import com.zxtx.hummer.em.dao.mapper.EmployeeMapper;
import com.zxtx.hummer.em.domain.Employee;
import com.zxtx.hummer.em.enums.CompanyType;
import com.zxtx.hummer.em.enums.EmStatus;
import com.zxtx.hummer.exchange.dao.mapper.MbExchangeOrderMapper;
import com.zxtx.hummer.exchange.domain.MbExchangeOrder;
import com.zxtx.hummer.exchange.enums.ExchangeOrderTypeEnum;
import com.zxtx.hummer.hk.dao.HkApplyOrderMapper;
import com.zxtx.hummer.hk.domain.HkApplyOrder;
import com.zxtx.hummer.insurance.dao.mapper.DiInsuranceOrderMapper;
import com.zxtx.hummer.insurance.domain.DiInsuranceOrder;
import com.zxtx.hummer.order.dao.mapper.MbOrderMapper;
import com.zxtx.hummer.order.domain.MbOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author peng can
 * @date 2022/12/1
 */
@Service
@Slf4j
public class CommissionSettleService extends ServiceImpl<CommissionSettleMapper, CommissionSettle> {

    @Autowired
    private CommissionPlanMembersService commissionPlanMemberService;
    @Autowired
    private CommissionPlanConfService commissionPlanConfService;
    @Resource
    private EmployeeMapper employeeMapper;
    @Autowired
    private OrderCommissionRuleService orderCommissionRuleService;
    @Autowired
    private EmployeeAccountChangeService employeeAccountChangeService;
    @Autowired
    private CommissionSettleCheckService  commissionSettleCheckService;
    @Autowired
    private DiInsuranceOrderMapper insuranceOrderMapper;
    @Autowired
    private MbOrderMapper mobileOrderMapper;
    @Autowired
    private MbExchangeOrderMapper exchangeOrderMapper;
    @Autowired
    private CompanyMapper companyMapper;
    @Autowired
    private HkApplyOrderMapper hkApplyOrderMapper;
    @Autowired
    private CommissionTypePackageService commissionTypePackageService;


    /**
     * 固定值分佣规则绑定
     *
     * @param orderId
     * @param employeeId
     * @see CommissionBizType 方案ID
     * @see CommissionPackage 套餐ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void orderBindSettleRule(Long orderId, CommissionBizType typeEnum, CommissionPackage packageEnum, Long employeeId) {
        Boolean existsFlag = orderCommissionRuleService.checkOrderRuleExists(orderId,typeEnum,packageEnum);
        if (existsFlag) {
            log.info("订单{}的佣金结算规则已锁定", orderId);
            return;
        }
        //  结算佣金开始
        // 1. 查找员工的层级关系，由上而下查找套餐对应的佣金方案及对应数值配置
        Employee employee = employeeMapper.selectByPrimaryKey(employeeId);
        //当前员工的人员层级编码
        String ancestorsCode = employee.getAncestors();
        List<Employee> superEmployees = getSuperEmployees(ancestorsCode);
        List<RuleVersionDTO> ruleVersions = convertToRuleVersion(superEmployees);
        // 转化为待入库的规则描述存储对象
        // 由上而下查找套餐对应的佣金方案及对应数值配置
        if (ruleVersions.size() > 1) {
            for (int i = 1; i < superEmployees.size(); i++) {
                Employee superEmployee = superEmployees.get(i);
                // 根据成员id获取被分配的方案
                CommissionPlanMembers commissionPlanMember = commissionPlanMemberService.getCommissionPlanMemberByMemberId(superEmployee.getId(), typeEnum.getType().longValue());
                RuleVersionDTO ruleVersionDTO = ruleVersions.get(i - 1);
                if (commissionPlanMember == null) {
                    continue;
                } else {
                    Long planId = commissionPlanMember.getPlanId();
                    // 根据方案id和套餐id获取套餐方案配置详情
                    CommissionPlanConf commissionPlanIssueConf = commissionPlanConfService.getCommissionConfByPlanIdAndPackageId(planId, packageEnum.getType().longValue());
                    if (commissionPlanIssueConf == null) {
                        continue;
                    } else {
                        // 添加层级
                        PlanConfDTO planConfDTO = convertToPlanConfDTO(commissionPlanIssueConf);
                        ruleVersionDTO.setPlanConf(planConfDTO);
                    }
                }
            }

            // 2. 由上而下计算分成比例，生成多条待结算记录
            if (CollectionUtil.isNotEmpty(ruleVersions)) {
                RuleVersionDTO topPlanIssueConf = ruleVersions.get(0);
                if (topPlanIssueConf.getLevel() == 0) {
                    long all = 0L;
                    // 配置的下级分成金额
                    long remove = 0L;
                    // 配置的自留分成金额
                    long remain = 0L;
                    // 实际自留佣金
                    long actualRemain = 0L;
                    // 实际下级分成金额
                    long actualRemove = 0L;

                    for (int i = 0; i < ruleVersions.size(); i++) {
                        //  一级一级往下分
                        RuleVersionDTO ruleVersionDTO = ruleVersions.get(i);
                        PlanConfDTO issueConf = ruleVersionDTO.getPlanConf();
                        if (i == 0 && issueConf != null) {
                            all = issueConf.getChildDivide() + issueConf.getSelfDivide();
                        }
                        CommissionDTO commissionDTO = new CommissionDTO();
                        if (issueConf == null) {
                            // 分成方案为空则不往下分
                            commissionDTO.setAll(all);
                            commissionDTO.setActualRemain(all);
                            actualRemove = 0L;
                            commissionDTO.setActualRemove(actualRemove);
                        } else {
                            remove = issueConf.getChildDivide();
                            actualRemove = (all >= remove ? remove : all);
                            actualRemain = all - actualRemove;
                            commissionDTO.setAll(all);
                            commissionDTO.setActualRemove(actualRemove);
                            commissionDTO.setActualRemain(actualRemain);
                        }
                        ruleVersionDTO.setCommission(commissionDTO);
                        // 实际下级分成金额赋值到下一级的上级分配
                        all = actualRemove;
                    }
                }
            }

        } else {
            // 只有最顶层方案规则则不结算
            log.info("订单{} 的各层级方案为空", orderId);
        }

        // 3. 订单绑定规则入库

//        String ruleVersionJson = null;
//        if (CollectionUtil.isNotEmpty(ruleVersions)) {
//            ruleVersionJson = JSONUtil.toJsonStr(ruleVersions);
//        }

        OrderCommissionRule rule = new OrderCommissionRule();

        rule.setOrderId(orderId);
        rule.setCommissionType(typeEnum.getType().longValue());
        rule.setCommissionPackage(packageEnum.getType());
        rule.setRuleVersion(ruleVersions);

        if (log.isInfoEnabled()) {
            log.info("订单{} 绑定的佣金方案规则为:{}", orderId, JSONUtil.toJsonStr(ruleVersions));
        }
        orderCommissionRuleService.save(rule);
        //  绑定规则结束
    }

    /**
     * 比例分佣绑定规则
     *
     * @param amount     分佣金额
     * @param orderId    订单ID
     * @param employeeId 员工ID
     * @see CommissionBizType     方案ID
     * @see CommissionPackage     套餐ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void orderScaleBindSettleRule(Long amount, Long orderId, CommissionBizType typeEnum, CommissionPackage packageEnum, Long employeeId) {
        Boolean existsFlag = orderCommissionRuleService.checkOrderRuleExists(orderId,typeEnum,packageEnum);
        if (existsFlag) {
            log.info("订单{}的佣金结算规则已锁定", orderId);
            return;
        }
        //  结算佣金开始
        // 1. 查找员工的层级关系，由上而下查找套餐对应的佣金方案及对应数值配置
        Employee employee = employeeMapper.selectByPrimaryKey(employeeId);
        //当前员工的人员层级编码
        String ancestorsCode = employee.getAncestors();
        List<Employee> superEmployees = new ArrayList<>();

        if (CommissionPackage.PHONE_DOWN.getType().equals(packageEnum.getType()) || CommissionPackage.INSURANCE_COMPANY_SERVICE.getType().equals(packageEnum.getType())) {
            superEmployees = getSuperEmployeesByDown(ancestorsCode);
        } else {
            superEmployees = getSuperEmployees(ancestorsCode);
        }
        List<RuleVersionDTO> ruleVersions = convertToRuleVersion(superEmployees);
        // 转化为待入库的规则描述存储对象
        // 由上而下查找套餐对应的佣金方案及对应数值配置
        if (ruleVersions.size() > 1) {
            for (int i = 1; i < superEmployees.size(); i++) {
                Employee superEmployee = superEmployees.get(i);
                // 根据成员id获取被分配的方案
                CommissionPlanMembers commissionPlanMember = commissionPlanMemberService.getCommissionPlanMemberByMemberId(superEmployee.getId(), typeEnum.getType().longValue());
                RuleVersionDTO ruleVersionDTO = ruleVersions.get(i - 1);
                if (commissionPlanMember == null) {
                    continue;
                } else {
                    Long planId = commissionPlanMember.getPlanId();
                    // 根据方案id和套餐id获取套餐方案配置详情
                    CommissionPlanConf commissionPlanIssueConf = commissionPlanConfService.getCommissionConfByPlanIdAndPackageId(planId, packageEnum.getType());
                    if (commissionPlanIssueConf == null) {
                        continue;
                    } else {
                        // 添加层级
                        PlanConfDTO planConfDTO = convertToPlanConfDTO(commissionPlanIssueConf);
                        ruleVersionDTO.setPlanConf(planConfDTO);
                    }
                }
            }

            // 2. 由上而下计算分成比例，生成多条待结算记录
            if (CollectionUtil.isNotEmpty(ruleVersions)) {
                RuleVersionDTO topPlanIssueConf = ruleVersions.get(0);
                if (topPlanIssueConf.getLevel() == 0) {
                    long all = amount;
                    // 配置的下级分成金额
                    long remove = 0L;
                    // 配置的自留分成金额
                    long remain = 0L;
                    // 实际自留佣金
                    long actualRemain = 0L;
                    // 实际下级分成金额
                    long actualRemove = 0L;

                    for (int i = 0; i < ruleVersions.size(); i++) {
                        //  一级一级往下分
                        RuleVersionDTO ruleVersionDTO = ruleVersions.get(i);
                        PlanConfDTO issueConf = ruleVersionDTO.getPlanConf();
                        CommissionDTO commissionDTO = new CommissionDTO();
                        if (issueConf == null) {
                            // 分成方案为空则不往下分
                            commissionDTO.setAll(all);
                            commissionDTO.setActualRemain(all);
                            actualRemove = 0L;
                            commissionDTO.setActualRemove(actualRemove);
                        } else {
                            BigDecimal childScale = issueConf.getChildScale();
                            remove = childScale.multiply(new BigDecimal(all)).longValue();
                            actualRemove = (all >= remove ? remove : all);
                            actualRemain = all - actualRemove;
                            commissionDTO.setAll(all);
                            commissionDTO.setActualRemove(actualRemove);
                            commissionDTO.setActualRemain(actualRemain);
                        }
                        ruleVersionDTO.setCommission(commissionDTO);
                        // 实际下级分成金额赋值到下一级的上级分配
                        all = actualRemove;
                    }
                }
            }

        } else {
            // 只有最顶层方案规则则不结算
            log.info("订单{} 的各层级方案为空", orderId);
        }

        // 3. 订单绑定规则入库

//        String ruleVersionJson = null;
//        if (CollectionUtil.isNotEmpty(ruleVersions)) {
//            ruleVersionJson = JSONUtil.toJsonStr(ruleVersions);
//        }
        OrderCommissionRule rule = new OrderCommissionRule();
        rule.setOrderId(orderId);
        rule.setCommissionType(typeEnum.getType().longValue());
        rule.setCommissionPackage(packageEnum.getType());
        rule.setRuleVersion(ruleVersions);

        if (log.isInfoEnabled()) {
            log.info("订单{} 绑定的佣金方案规则为:{}", orderId, JSONUtil.toJsonStr(ruleVersions));
        }
        orderCommissionRuleService.save(rule);
        //  绑定规则结束
    }

    /**
     * 根据人员层级编码获取上级人员列表，按层级升序排序
     *
     * @param ancestorsCode
     * @return
     */
    public static List<Employee> getSuperEmployees(String ancestorsCode) {
        String[] ancestors = ancestorsCode.split(",");

        List<Employee> superEmployees = new ArrayList<>();
        for (int i = 0; i < ancestors.length; i++) {
            Long empId = Long.parseLong(ancestors[i]);
            int empLevel = i;

            StringBuilder ancestorsSb = new StringBuilder();
            for (int j = 0; j <= i; j++) {

                ancestorsSb.append(ancestors[j]);
                if (j < i) {
                    ancestorsSb.append(",");
                }
            }
            String empAncestors = ancestorsSb.toString();


            Employee superEmployee = new Employee();
            superEmployee.setId(empId);
            superEmployee.setAncestors(empAncestors);
            superEmployee.setLevel(empLevel);

            superEmployees.add(superEmployee);
        }

        return superEmployees;
    }

    /**
     * 压价方案的分佣人员不是从顶层开始
     *
     * @param ancestorsCode
     * @return
     */
    public List<Employee> getSuperEmployeesByDown(String ancestorsCode) {
        List<String> employeeIdStrs = Arrays.asList(ancestorsCode.split(","));
        List<Long> employeeIds = employeeIdStrs.stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
        List<Employee> employeeList = employeeMapper.selectList(Wrappers.lambdaQuery(Employee.class)
                .in(Employee::getId, employeeIds)
                .in(Employee::getCompanyType, Arrays.asList(CompanyType.CHAIN.getCode(), CompanyType.STORE.getCode()))
                .orderByAsc(Employee::getLevel)
        );
        List<Employee> superEmployees = new ArrayList<>();
        Employee superEmployee = new Employee();
        superEmployee.setId(1L);
        superEmployee.setAncestors("1");
        superEmployee.setLevel(0);
        superEmployees.add(superEmployee);
        for (Employee employee : employeeList) {
            Employee empLevel = new Employee();
            empLevel.setId(employee.getId());
            empLevel.setAncestors(employee.getAncestors());
            empLevel.setLevel(employee.getLevel());
            superEmployees.add(empLevel);
        }
        return superEmployees;
    }

    private List<RuleVersionDTO> convertToRuleVersion(List<Employee> superEmployees) {
        if (CollectionUtil.isEmpty(superEmployees)) {
            return null;
        }
        List<RuleVersionDTO> list = new ArrayList<>();
        int size = superEmployees.size();
        for (int i = 0; i < size; i++) {
            RuleVersionDTO ruleVersionDTO = employeeMapToRuleVersionDTO(superEmployees.get(i));
            if (i != size - 1) {
                ruleVersionDTO.setChildEmployeeId(superEmployees.get(i + 1).getId());
            }
            list.add(ruleVersionDTO);
        }
        return list;
    }

    private RuleVersionDTO employeeMapToRuleVersionDTO(Employee employee) {
        if (employee == null) {
            return null;
        }
        RuleVersionDTO ruleVersionDTO = new RuleVersionDTO();
        ruleVersionDTO.setEmployeeId(employee.getId());
        ruleVersionDTO.setAncestors(employee.getAncestors());
        ruleVersionDTO.setLevel(employee.getLevel());
        return ruleVersionDTO;
    }

    private PlanConfDTO convertToPlanConfDTO(CommissionPlanConf commissionPlanIssueConf) {
        if (commissionPlanIssueConf == null) {
            return null;
        }
        PlanConfDTO planConfDTO = new PlanConfDTO();
        planConfDTO.setPlanId(commissionPlanIssueConf.getPlanId());
        planConfDTO.setSuperDivide(commissionPlanIssueConf.getSuperDivide());
        planConfDTO.setChildDivide(commissionPlanIssueConf.getChildDivide());
        planConfDTO.setSelfDivide(commissionPlanIssueConf.getSelfDivide());
        planConfDTO.setSuperScale(commissionPlanIssueConf.getSuperScale());
        planConfDTO.setChildScale(commissionPlanIssueConf.getChildScale());
        planConfDTO.setSelfScale(commissionPlanIssueConf.getSelfScale());
        return planConfDTO;
    }


    /**
     * 生成待结算
     * @param orderId
     * @param typeEnum
     * @param commissionPackageId
     * @param employeeId
     * @param employAccountChangeEnum
     * @param remark
     */
    @Transactional(rollbackFor = Exception.class)
    public void waitSettleOrder(Long orderId, CommissionBizType typeEnum, Long commissionPackageId, Long employeeId, EmployAccountChangeEnum employAccountChangeEnum, String remark) {
        //  结算佣金开始
        // 1. 查找员工的层级关系，由上而下查找套餐对应的佣金方案及对应数值配置
        Employee employee = employeeMapper.selectByPrimaryKey(employeeId);
        //当前员工的人员层级编码
        String ancestorsCode = employee.getAncestors();
        List<Employee> superEmployees = new ArrayList<>();
        if (CommissionPackage.PHONE_DOWN.getType().equals(commissionPackageId) || CommissionPackage.INSURANCE_COMPANY_SERVICE.getType().equals(commissionPackageId)) {
            superEmployees = getSuperEmployeesByDown(ancestorsCode);
        } else {
            superEmployees = getSuperEmployees(ancestorsCode);
        }
        OrderCommissionRule rule = orderCommissionRuleService.getRuleVersionByOrderId(orderId,typeEnum,commissionPackageId);
        if (rule == null) {
            return;
        }
        // 由上而下查找套餐对应的佣金方案及对应数值配置
        String versionJSON = JSONUtil.toJsonStr(rule.getRuleVersion());
        rule.setRuleVersion(JSONUtil.toList(versionJSON, RuleVersionDTO.class));
        List<RuleVersionDTO> commissionPlanIssueConfList = rule.getRuleVersion();
        // 2. 由上而下计算分成比例，生成多条待结算记录
        List<CommissionSettle> commissionSettleList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(commissionPlanIssueConfList)) {
            Date settleTime = new Date();
            int settleLevel = 0;
            for (int i = settleLevel; i < commissionPlanIssueConfList.size(); i++) {
                //  一级一级往下分
                RuleVersionDTO issueConf = commissionPlanIssueConfList.get(i);
                CommissionDTO commissionDTO = issueConf.getCommission();
                if (commissionDTO != null) {
                    // 被结算人
                    Employee settleEmp = superEmployees.get(i);
                    CommissionSettle commissionSettle = buildCommissionSettle(orderId, remark, employee, typeEnum.getType().longValue(), commissionPackageId, commissionDTO.getActualRemain().intValue(), settleTime, settleEmp, superEmployees,i);
                    commissionSettleList.add(commissionSettle);
                }
                settleLevel++;
            }
        }
        // 3. 保存入库
        if (CollectionUtil.isNotEmpty(commissionSettleList)) {
            List<CommissionSettle> commissionSettleListToSave = commissionSettleList.stream().filter(item -> item.getSettleBalance() > 0).collect(Collectors.toList());
            //有些绑定分拥规则不需要资金变化
            if(employAccountChangeEnum != null){
                //钱包资金变化
                for (CommissionSettle settle : commissionSettleListToSave) {
                    employeeAccountChangeService.changeAccount(settle.getEmployeeId(), employAccountChangeEnum, settle.getSettleBalance(), settle.getCorrelationId(), remark);
                }
            }
            this.saveBatch(commissionSettleListToSave);
        }
    }


    private CommissionSettle buildCommissionSettle(Long orderId, String remark, Employee employee, Long commissionTypeId, Long commissionPackageId, int actualRemain, Date settleTime, Employee settleEmp, List<Employee> superEmployees,int i) {
        CommissionSettle commissionSettle = new CommissionSettle();
        commissionSettle.setEmployeeId(settleEmp.getId());
//        commissionSettle.setBatchNo(settleBatchNo);
        commissionSettle.setCommissionType(commissionTypeId);
        commissionSettle.setCommissionPackage(commissionPackageId);
        commissionSettle.setSettleBalance(actualRemain);
        commissionSettle.setSettleStatus(SettleStatus.WAIT_TO_SETTLE.getStatus());
        // 关联类型 、 关联ID
        commissionSettle.setCorrelationId(orderId);
        if (employee.getLevel().compareTo(settleEmp.getLevel()) == 0) {
            commissionSettle.setGainType(CommissionSettleGainType.BY_MYSELF.getType());
        } else {
            commissionSettle.setGainType(CommissionSettleGainType.CHILD_CONTRIBUTE.getType());
            int childLevel = i + 1;
            if (childLevel < superEmployees.size()) {
                Employee settleEmpChild = superEmployees.get(childLevel);
                commissionSettle.setChildEmployeeId(settleEmpChild.getId());
            }
        }

        commissionSettle.setGainTime(settleTime);
        commissionSettle.setRemark(remark);
        commissionSettle.setLevel(settleEmp.getLevel());
        commissionSettle.setAncestors(settleEmp.getAncestors());
        commissionSettle.setCreateTime(settleTime);
        return commissionSettle;
    }

    /**
     * 订单结算
     *
     * @param orderId
     * @param typeEnum
     * @param commissionPackageId
     * @param employAccountChangeEnum
     * @param remark
     */
    @Transactional(rollbackFor = Exception.class)
    public void settleOrder(Long orderId, CommissionBizType typeEnum, Long commissionPackageId, EmployAccountChangeEnum employAccountChangeEnum, String remark) {
        LambdaQueryWrapper<CommissionSettle> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CommissionSettle::getCorrelationId, orderId);
        queryWrapper.eq(CommissionSettle::getSettleStatus, SettleStatus.WAIT_TO_SETTLE.getStatus());
        queryWrapper.eq(CommissionSettle::getCommissionType, typeEnum.getType());
        queryWrapper.eq(CommissionSettle::getCommissionPackage, commissionPackageId);
        List<CommissionSettle> settleList = this.baseMapper.selectList(queryWrapper);
        if (CollUtil.isEmpty(settleList)) return;
        for (CommissionSettle settle : settleList) {
            settle.setBatchNo(SnowflakeIdWorker.nextID());
            settle.setSettleStatus(SettleStatus.HAVING_SETTLE.getStatus());
            settle.setSettleTime(new Date());
            this.updateById(settle);
            employeeAccountChangeService.changeAccount(settle.getEmployeeId(), employAccountChangeEnum, settle.getSettleBalance(), settle.getCorrelationId(), remark);
        }
        if(!Arrays.asList(CommissionPackage.PHONE_DOWN.getType(),CommissionPackage.INSURANCE_COMPANY_SERVICE.getType()).contains(commissionPackageId)){
            //合伙人账单数据
            commissionCheck(settleList);
        }
    }

    /**
     * 待结算取消
     *
     * @param orderId
     * @param typeEnum
     * @param packageEnum
     * @param employAccountChangeEnum
     * @param remark
     */
    @Transactional(rollbackFor = Exception.class)
    public void waitSettleCancel(Long orderId, CommissionBizType typeEnum, CommissionPackage packageEnum, EmployAccountChangeEnum employAccountChangeEnum, String remark) {
        LambdaQueryWrapper<CommissionSettle> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CommissionSettle::getCorrelationId, orderId);
        queryWrapper.eq(CommissionSettle::getSettleStatus, SettleStatus.WAIT_TO_SETTLE.getStatus());
        queryWrapper.eq(CommissionSettle::getCommissionType, typeEnum.getType());
        queryWrapper.eq(CommissionSettle::getCommissionPackage, packageEnum.getType());
        List<CommissionSettle> settleList = this.baseMapper.selectList(queryWrapper);
        if (CollUtil.isEmpty(settleList)) return;
        for (CommissionSettle settle : settleList) {
            //旧的已结算
            settle.setBatchNo(SnowflakeIdWorker.nextID());
            settle.setSettleStatus(SettleStatus.HAVING_SETTLE.getStatus());
            this.updateById(settle);
            //新插入一条负数已经结算
            CommissionSettle cancelSettle = new CommissionSettle();
            BeanUtil.copyProperties(settle, cancelSettle, "id");
            cancelSettle.setSettleBalance(0 - settle.getSettleBalance());
            cancelSettle.setBatchNo(SnowflakeIdWorker.nextID());
            cancelSettle.setSettleStatus(SettleStatus.HAVING_SETTLE.getStatus());
            this.save(cancelSettle);
            employeeAccountChangeService.changeAccount(settle.getEmployeeId(), employAccountChangeEnum, settle.getSettleBalance(), settle.getCorrelationId(), remark);
        }
    }

    public List<CommissionSettleDTO> pageList(SettleLogQueryReq req) {
        if (StrUtil.isNotBlank(req.getBdMobile())) {
            LambdaQueryWrapper<Employee> qw = new LambdaQueryWrapper<>();
            if (StrUtil.isNotBlank(req.getBdMobile())) {
                qw.eq(Employee::getMobileNumber, req.getBdMobile());
            }
            qw.eq(Employee::getStatus, EmStatus.NORMAL.getCode());
            Employee bd = employeeMapper.selectOne(qw);
            if (bd == null) {
                return new ArrayList<>();
            }
            req.setAncestors(Optional.ofNullable(bd).map(Employee::getAncestors).orElse(null));
        }
        PageHelper.startPage(req.getPage(), req.getPageSize());
        List<CommissionSettleDTO> list = this.baseMapper.selectBySearch(req);
        return buildCommissionSettleList(list);
    }

    private Employee getEmployeeByCondition(SettleLogQueryReq req) {
        Employee emp = null;
        if (req.getAreaId() != null) {
            emp = employeeMapper.selectById(req.getAreaId());
            if (emp == null) {
                throw new BusinessException("not found employee");
            }
        }
        if (req.getAgentId() != null) {
            emp = employeeMapper.selectById(req.getAreaId());
            if (emp == null) {
                throw new BusinessException("not found employee");
            }
        }
        if (req.getChainCompanyId() != null) {
            Company cmp = companyMapper.selectById(req.getChainCompanyId());
            if (cmp == null) {
                throw new BusinessException("not found company");
            }
            emp = employeeMapper.selectById(cmp.getEmployeeId());
            if (emp == null) {
                throw new BusinessException("not found employee");
            }
        }
        if (StrUtil.isNotBlank(req.getChainCompanyMobile())) {
            emp = employeeMapper.selectOne(new LambdaQueryWrapper<Employee>()
                    .eq(Employee::getMobileNumber, req.getChainCompanyMobile())
                    .eq(Employee::getStatus, EmStatus.NORMAL.getCode())
                    .last("limit 1"));
            if (emp == null) {
                throw new BusinessException("not found employee");
            }
        }
        if (StrUtil.isNotBlank(req.getCompanyMobile())) {
            emp = employeeMapper.selectOne(new LambdaQueryWrapper<Employee>()
                    .eq(Employee::getMobileNumber, req.getCompanyMobile())
                    .eq(Employee::getStatus, EmStatus.NORMAL.getCode())
                    .last("limit 1"));
            if (emp == null) {
                throw new BusinessException("not found employee");
            }
        }
        return emp;
    }

    public List<CommissionSettleDTO> subAdminCommissionSettleList(SettleLogQueryReq req) {
        Integer employeeRoleType = ShiroUtils.getUser().getEmployeeRoleType();
        if (employeeRoleType == null) {
            return new ArrayList<>();
        }
        req.setAncestors(ShiroUtils.getUser().getEmployeeAncestors());
        try {
            Employee emp = getEmployeeByCondition(req);
            if (emp != null) {
                req.setAncestors(emp.getAncestors());
            }
        } catch (BusinessException e) {
            return new ArrayList<>();
        }

        PageHelper.startPage(req.getPage(), req.getPageSize());
        List<CommissionSettleDTO> list = this.baseMapper.selectBySearch(req);

        List<CommissionSettleDTO> result = buildCommissionSettleList(list);
        if (CollUtil.isNotEmpty(result) && employeeRoleType.equals(1)) {
            // 合伙人
            Set<Long> empIds = result.stream().map(CommissionSettleDTO::getEmployeeId).filter(Objects::nonNull).collect(Collectors.toSet());
            List<Employee> employees = employeeMapper.selectList(new LambdaQueryWrapper<Employee>().in(Employee::getId, empIds));
            // empId, areaEmpId
            Map<Long, Long> empAreaIdMap = new HashMap<>(1);
            for (Employee employee : employees) {
                List<String> levels = StrUtil.split(employee.getAncestors(), ",");
                if (levels.size() < 3) {
                    continue;
                }
                String areaIdStr = levels.get(2);
                empAreaIdMap.put(employee.getId(), Long.valueOf(areaIdStr));
            }
            Collection<Long> areaEmpIds = empAreaIdMap.values();
            Map<Long, Employee> areaMap = employeeMapper.selectList(new LambdaQueryWrapper<Employee>().in(Employee::getId, areaEmpIds)).stream().collect(Collectors.toMap(Employee::getId, Function.identity()));

            for (CommissionSettleDTO dto : result) {
                Long areaId = empAreaIdMap.get(dto.getEmployeeId());
                if (areaId == null) {
                    continue;
                }
                dto.setAreaName(Optional.ofNullable(areaMap.get(areaId)).map(Employee::getName).orElse(null));
            }
        }
        return result;
    }

    public CommissionSettleCheckSumVO sumByParam(SettleLogQueryReq req) {
        CommissionSettleCheckSumVO defaultResult = CommissionSettleCheckSumVO.builder().appTotal(0L).insuranceTotal(0L).mobileTotal(0L).build();
        Integer employeeRoleType = ShiroUtils.getUser().getEmployeeRoleType();
        if (employeeRoleType == null) {
            return defaultResult;
        }
        req.setAncestors(ShiroUtils.getUser().getEmployeeAncestors());
        try {
            Employee emp = getEmployeeByCondition(req);
            if (emp != null) {
                req.setAncestors(emp.getAncestors());
            }
        } catch (BusinessException e) {
            return defaultResult;
        }
        CommissionSettleCheckSumVO resultVo = this.baseMapper.sumByParam(req);
        if (ObjectUtil.isNull(resultVo)) {
            return defaultResult;
        }
        return resultVo;
    }

    private List<CommissionSettleDTO> buildCommissionSettleList(List<CommissionSettleDTO> list) {
        if (CollUtil.isEmpty(list)) {
            return new ArrayList<>();
        }

        Set<Long> correlationIds = list.stream().map(CommissionSettleDTO::getCorrelationId).filter(Objects::nonNull).collect(Collectors.toSet());
        // 找数保订单
        Map<Long, DiInsuranceOrder> insuranceOrderMap = insuranceOrderMapper.selectList(new LambdaQueryWrapper<DiInsuranceOrder>().in(DiInsuranceOrder::getId, correlationIds)).stream().collect(Collectors.toMap(DiInsuranceOrder::getId, Function.identity()));
        // 找拉新订单
        Map<Long, MbExchangeOrder> exchangeOrderMap = exchangeOrderMapper.selectList(new LambdaQueryWrapper<MbExchangeOrder>().in(MbExchangeOrder::getId, correlationIds)).stream().collect(Collectors.toMap(MbExchangeOrder::getId, Function.identity()));
        // 找二手机订单
        Map<Long, MbOrder> mobileOrderMap = mobileOrderMapper.selectList(new LambdaQueryWrapper<MbOrder>().in(MbOrder::getId, correlationIds)).stream().collect(Collectors.toMap(MbOrder::getId, Function.identity()));
        // 找号卡订单
        Map<Long, HkApplyOrder> hkOrderMap = hkApplyOrderMapper.selectList(new LambdaQueryWrapper<HkApplyOrder>().in(HkApplyOrder::getId, correlationIds)).stream().collect(Collectors.toMap(HkApplyOrder::getId, Function.identity()));
        //佣金套餐
        Map<Long, CommissionTypePackage> packageMap = commissionTypePackageService.list().stream().collect(Collectors.toMap(CommissionTypePackage::getId, Function.identity()));

        for (CommissionSettleDTO dto : list) {
            Optional<CommissionTypePackage> typePackage = Optional.ofNullable(packageMap.get(dto.getCommissionType()));
            dto.setCommissionTypeStr(typePackage.map(CommissionTypePackage::getName).orElse(null));
            if (insuranceOrderMap.containsKey(dto.getCorrelationId())) {
                Optional<DiInsuranceOrder> o = Optional.ofNullable(insuranceOrderMap.get(dto.getCorrelationId()));
                dto.setProductName(o.map(DiInsuranceOrder::getInsuranceName).orElse(null));
                dto.setCorrelationOrderEmployeeId(o.map(DiInsuranceOrder::getStoreEmployeeId).orElse(null));
                dto.setCorrelationOrderCompanyId(o.map(DiInsuranceOrder::getStoreCompanyId).orElse(null));
                dto.setCreateTime(o.map(DiInsuranceOrder::getCreateTime).orElse(null));
            }
            if (exchangeOrderMap.containsKey(dto.getCorrelationId())) {
                MbExchangeOrder o = exchangeOrderMap.get(dto.getCorrelationId());
                dto.setProductName(Optional.ofNullable(EnumUtil.getBy(ExchangeOrderTypeEnum::getCode, o.getType())).map(ExchangeOrderTypeEnum::getDesc).orElse(null));

                dto.setCorrelationOrderEmployeeId(Optional.of(o).map(MbExchangeOrder::getStoreEmployeeId).orElse(null));
                dto.setCorrelationOrderCompanyId(Optional.of(o).map(MbExchangeOrder::getStoreCompanyId).orElse(null));
                dto.setCreateTime(Optional.of(o).map(MbExchangeOrder::getCreateTime).orElse(null));
            }
            if (mobileOrderMap.containsKey(dto.getCorrelationId())) {
                Optional<MbOrder> o = Optional.ofNullable(mobileOrderMap.get(dto.getCorrelationId()));
                dto.setProductName(o.map(MbOrder::getProductName).orElse(null));
                dto.setCorrelationOrderEmployeeId(o.map(MbOrder::getStoreEmployeeId).orElse(null));
                dto.setCorrelationOrderCompanyId(o.map(MbOrder::getStoreCompanyId).orElse(null));
                dto.setCreateTime(o.map(MbOrder::getCreateTime).orElse(null));
            }
            if (hkOrderMap.containsKey(dto.getCorrelationId())) {
                Optional<HkApplyOrder> o = Optional.ofNullable(hkOrderMap.get(dto.getCorrelationId()));
                dto.setProductName(o.map(HkApplyOrder::getFetchName).orElse(null));
                dto.setCorrelationOrderEmployeeId(o.map(HkApplyOrder::getEmployeeId).orElse(null));
                dto.setCorrelationOrderCompanyId(o.map(HkApplyOrder::getCompanyId).orElse(null));
                dto.setCreateTime(o.map(HkApplyOrder::getCreateTime).orElse(null));
            }
            if (dto.getCompanyType().equals(1) && dto.getDeptType().equals(2)) {
                // 管理层级
                if (dto.getEmployeeLevel().equals(1)) {
                    dto.setEmployeeRole("合伙人");
                } else if (dto.getEmployeeLevel().equals(2)) {
                    dto.setEmployeeRole("区域经理");
                } else {
                    dto.setEmployeeRole("代理");
                }
            }
            if (dto.getCompanyType().equals(2)) {
                // 门店层级
                if (dto.getEmployeeType().equals(1) || dto.getEmployeeType().equals(3)) {
                    dto.setEmployeeRole("门店负责人");
                }
                if (dto.getEmployeeType().equals(2) || dto.getEmployeeType().equals(4)) {
                    dto.setEmployeeRole("店员");
                }
            }
            if (dto.getCompanyType().equals(3)) {
                // 门店层级
                if (dto.getEmployeeType().equals(1) || dto.getEmployeeType().equals(3)) {
                    dto.setEmployeeRole("连锁店负责人");
                }
                if (dto.getEmployeeType().equals(2) || dto.getEmployeeType().equals(4)) {
                    dto.setEmployeeRole("店员");
                }
            }

            dto.setGainTypeStr(CommissionSettleGainType.getDescByCode(dto.getGainType()));
            dto.setSettleBalanceStr(StringUtils.fenToYuan(dto.getSettleBalance()));
            dto.setSettleStatusStr(SettleStatus.getDescByCode(dto.getSettleStatus()));
        }

        Set<Long> correlationOrderCompanyId = list.stream().map(CommissionSettleDTO::getCorrelationOrderCompanyId).filter(Objects::nonNull).collect(Collectors.toSet());
        if (CollUtil.isEmpty(correlationOrderCompanyId)) {
            return list;
        }
        List<Company> correlationOrderCompanies = companyMapper.selectList(new LambdaQueryWrapper<Company>().in(Company::getId, correlationOrderCompanyId));
        if (CollUtil.isEmpty(correlationOrderCompanies)) {
            return list;
        }
        Map<Long, Company> correlationOrderCompanyMap = correlationOrderCompanies.stream().collect(Collectors.toMap(Company::getId, Function.identity()));
        list.forEach(dto -> {
            dto.setCorrelationOrderCompanyName(Optional.ofNullable(correlationOrderCompanyMap.get(dto.getCorrelationOrderCompanyId())).map(Company::getName).orElse(null));
        });

        Set<Long> parentCompanyIds = correlationOrderCompanies.stream().map(Company::getPId).filter(Objects::nonNull).filter(pid -> !pid.equals(Constants.LAN_HAI_CMP_ID)).collect(Collectors.toSet());
        if (CollUtil.isEmpty(parentCompanyIds)) {
            return list;
        }
        Map<Long, Company> parentCompanyMap = companyMapper.selectList(new LambdaQueryWrapper<Company>().in(Company::getId, parentCompanyIds)).stream().collect(Collectors.toMap(Company::getId, Function.identity()));
        list.forEach(dto -> {
            Company company = correlationOrderCompanyMap.get(dto.getCorrelationOrderCompanyId());
            if (company == null || company.getPId() == null) {
                return;
            }
            dto.setCorrelationOrderParentCompanyName(Optional.ofNullable(parentCompanyMap.get(company.getPId())).map(Company::getName).orElse(null));
        });

        return list;
    }

    private void commissionCheck(List<CommissionSettle> settleList){
        //总得分佣金额
        Integer allAmount = settleList.stream().mapToInt(CommissionSettle::getSettleBalance).sum();
        //最后层级
        CommissionSettle maxEmployee = settleList.stream()
                .max(Comparator.comparingInt(CommissionSettle::getLevel)).get();
        String [] levels = maxEmployee.getAncestors().split(",");
        Employee region = null;
        if(levels.length>2){
            Long regionId = Long.valueOf(levels[2]);
            region = employeeMapper.selectById(regionId);
        }
        Integer regionAmount = 0;
        //合伙人直营门店的
        if(ObjectUtil.isNotNull(region) && CompanyType.COMPANY.getCode()!=region.getCompanyType().intValue()){
            List<CommissionSettle> regionList =  settleList.stream().filter(e->e.getLevel()>=2).collect(Collectors.toList());
            if(CollUtil.isNotEmpty(regionList)){
                regionAmount = regionList.stream().mapToInt(CommissionSettle::getSettleBalance).sum();
            }
        }else{
            List<CommissionSettle> regionList =  settleList.stream().filter(e->e.getLevel()>=3).collect(Collectors.toList());
            if(CollUtil.isNotEmpty(regionList)){
                regionAmount = regionList.stream().mapToInt(CommissionSettle::getSettleBalance).sum();
            }
        }

        CommissionSettleCheck settleCheck = new CommissionSettleCheck();
        settleCheck.setBdId(Long.valueOf(levels[1]));
        if(levels.length>2){
            settleCheck.setRegionId(Long.valueOf(levels[2]));
        }
        settleCheck.setCommissionType(maxEmployee.getCommissionType());
        settleCheck.setCommissionPackage(maxEmployee.getCommissionPackage());
        settleCheck.setSettleBalance(allAmount-regionAmount);
        settleCheck.setAllAmount(allAmount);
        settleCheck.setPayAmount(regionAmount);
        settleCheck.setCorrelationId(maxEmployee.getCorrelationId());
        settleCheck.setSettleTime(maxEmployee.getSettleTime());
        settleCheck.setRemark(maxEmployee.getRemark());
        commissionSettleCheckService.save(settleCheck);
    }


}