package com.zxtx.hummer.em.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.zxtx.hummer.account.dao.mapper.EmployeeAccountMapper;
import com.zxtx.hummer.account.domain.EmployeeAccount;
import com.zxtx.hummer.common.Constants;
import com.zxtx.hummer.common.config.Constant;
import com.zxtx.hummer.common.enums.UserType;
import com.zxtx.hummer.common.exception.BaseException;
import com.zxtx.hummer.common.lock.RedisLockService;
import com.zxtx.hummer.common.utils.PageUtils;
import com.zxtx.hummer.common.utils.PasswordUtil;
import com.zxtx.hummer.common.utils.ShiroUtils;
import com.zxtx.hummer.common.utils.SnowflakeIdWorker;
import com.zxtx.hummer.common.vo.PageReq;
import com.zxtx.hummer.company.domain.Company;
import com.zxtx.hummer.company.service.CompanyService;
import com.zxtx.hummer.dept.dao.mapper.DeptMapper;
import com.zxtx.hummer.dept.domain.Dept;
import com.zxtx.hummer.dept.enums.DeptType;
import com.zxtx.hummer.dept.service.DeptService;
import com.zxtx.hummer.em.dao.EmDao;
import com.zxtx.hummer.em.dao.EmployeeHistoryDao;
import com.zxtx.hummer.em.dao.EmployeeLoginDao;
import com.zxtx.hummer.em.dao.mapper.EmployeeMapper;
import com.zxtx.hummer.em.dao.mapper.ExtEmMapper;
import com.zxtx.hummer.em.domain.*;
import com.zxtx.hummer.em.enums.*;
import com.zxtx.hummer.em.vo.EmLsReq;
import com.zxtx.hummer.em.vo.EmLsRps;
import com.zxtx.hummer.em.vo.EmployeeParentInfoDTO;
import com.zxtx.hummer.system.shiro.LoginUser;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
public class EmService {

    @Autowired
    private EmployeeMapper mapper;

    @Autowired
    private ExtEmMapper extEmMapper;

    @Autowired
    private EmDao emDao;
    //    @Autowired
//    private SmsSender smsSender;
    @Autowired
    private DeptMapper deptMapper;

    @Autowired
    private DeptService deptService;

    @Autowired
    private CompanyService companyService;
/*
    @Autowired
    private SysUserService sysUserService;*/

    @Autowired
    private EmployeeLoginDao loginDao;

    @Autowired
    private EmployeeHistoryDao employeeHistoryDao;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    //    @Autowired
//    private WalletService walletService;
    @Autowired
    private EmployeeAccountMapper employeeAccountMapper;

    @Autowired
    private EmployeeOperateLogService employeeOperateLogService;
    @Autowired
    private RedisLockService redisLockService;


    private Logger log = LoggerFactory.getLogger(EmService.class);

    public Employee getById(Long id) {
        return mapper.selectByPrimaryKey(id);
    }

    public Employee getByMobile(String mobile) {
        return emDao.getByMobile(mobile);
    }

    public Employee getByMobile(String mobile, byte status) {
        return emDao.getByMobile(mobile, status);
    }

    public Employee getByMobile(String mobile, List<Byte> lsStatus) {
        return emDao.getByMobile(mobile, lsStatus);
    }

    @Transactional(rollbackFor = Exception.class)
    public void resetPwd(Long employeeId){
        redisLockService.redisLock("pc_employee_reset_password_lock", employeeId);
        logout(employeeId);
        //更改员工状态
        Employee employee = this.getById(employeeId);
        employee.setPassword(PasswordUtil.buildDefaultPassword(employee.getMobileNumber()));
        mapper.updateById(employee);
    }

    public Integer getEmployeeRoleType(Long employeeId) {
        if (employeeId == null) {
            return null;
        }
        Employee employee = mapper.selectById(employeeId);
        if (employee == null) {
            return null;
        }
        if (employee.getCompanyType().equals(1) && employee.getDeptType().equals(2)) {
            if (employee.getLevel().equals(1)) {
                // 合伙人
                return 1;
            }
            if (employee.getLevel().equals(2)) {
                // 区域经理
                return 2;
            }
            if (employee.getLevel() > 2) {
                // 代理
                return 3;
            }
        }
        if (employee.getCompanyType().equals(3)) {
            if (employee.getType().equals(1) || employee.getType().equals(3)) {
                // 连锁店长
                return 4;
            }
        }
        if (employee.getCompanyType().equals(2)) {
            if (employee.getType().equals(1) || employee.getType().equals(3)) {
                // 单店店长
                return 5;
            }
        }
        return null;
    }

    public void createEm(Dept dept, Employee employee, Company company, Employee parentEmployee) {
        // 发短信
        Employee employeeNew = buildEm(dept, employee.getName(), company.getContact(), company.getContactMobile(),
                EmType.MANGER_MANGER.getCode(), parentEmployee);
        save(employeeNew);
        updateParentIsLeaf(parentEmployee);
        company.setEmployeeId(employeeNew.getId());
//        smsSender.send(SmsMessage.create().mobile(company.getContactMobile()).templateCode("SMS_167180378"));
    }

    public void updateIsLeaf(Employee employee, Boolean isLeaf) {
        mapper.updateIsLeaf(employee.getId(), isLeaf);
    }

    private void updateParentIsLeaf(Employee parentEmployee) {
        if (parentEmployee.getIsLeaf()) {
            this.updateIsLeaf(parentEmployee, Boolean.FALSE);
        }
    }


    public void createManEm(String mobile, String name, Dept dept, int type, byte status, Employee parentEmployee) {

        String creator = ShiroUtils.getUser().getName();
        Employee employee = buildEm(dept, creator, name, mobile, type, status, parentEmployee);
        save(employee);
        updateParentIsLeaf(parentEmployee);

        // TODO 创建后台管理账号
        /*   createManAccount(employee);*/
        // 发短信
//        smsSender.send(SmsMessage.create().mobile(mobile).templateCode(SMSEnum.EM_Register.getTemplateCode()));
    }

    /*    */

    /**
     * 新增后台管理的用户-只针对公司的管理部门人员开通账号
     *//*
    private void createManAccount(Employee emp) {
        if (emp.getCompanyType().equals(CompanyType.COMPANY.getCode()) && emp.getDeptType().equals(DeptType.MANGER.getCode())) {
            sysUserService.insert(emp.getMobileNumber(), emp.getName(), emp.getId());
        }
    }*/
    private Employee buildEm(Dept dept, String creator, String name, String mobile, int type, Employee parent) {

        return buildEm(dept, creator, name, mobile, type, EmStatus.NORMAL.getCode(), parent);
    }


    private Employee buildEm(Dept dept, String creator, String name, String mobile, int type, byte status, Employee parent) {

        Employee data = new Employee();
        data.setId(SnowflakeIdWorker.nextID());
        data.setCompanyId(dept.getCompanyId());
        data.setCompanyType(dept.getCompanyType());
        data.setCreateTime(new Date());
        data.setCreator(creator);
        data.setDeptCode(dept.getCode());
        data.setDeptType(dept.getType());
        data.setType(type);
        data.setUpdator(creator);
        data.setStatus(EmStatus.NORMAL.getCode());
        data.setUpdateTime(new Date());
        data.setMobileNumber(mobile);
        data.setName(name);
        data.setDeptId(dept.getId());
        data.setStatus(status);

        // 设置人员组织关系
        data.setAncestors(parent.getAncestors() + "," + data.getId());
        data.setIsLeaf(Boolean.TRUE);
        data.setLevel(parent.getLevel() + 1);

        data.setPassword(PasswordUtil.buildDefaultPassword(mobile));
        return data;
    }

  /*  public Employee getManManager(Long id,int status) {

        return getManManager(id,status);
    }
*/

    public Employee getManManager(Long id, byte status) {
        EmployeeExample example = new EmployeeExample();
        example.createCriteria().andCompanyIdEqualTo(id)
                .andStatusEqualTo(status)
                .andTypeEqualTo(EmType.MANGER_MANGER.getCode());
        List<Employee> employees = mapper.selectByExample(example);
        if (employees.size() > 0) {
            return employees.get(0);
        }
        return null;
    }

    public Employee getManManager(Long id, List<Byte> lsStatus) {
        EmployeeExample example = new EmployeeExample();
        example.createCriteria().andCompanyIdEqualTo(id)
                .andStatusIn(lsStatus)
                .andTypeEqualTo(EmType.MANGER_MANGER.getCode());
        List<Employee> employees = mapper.selectByExample(example);
        if (employees.size() > 0) {
            return employees.get(0);
        }
        return null;
    }


    public String getDeptManager(Long deptId) {

        Employee employee = getDeptManagerInf(deptId);
        if (null != employee) {
            return employee.getName();
        }
        return null;
    }


    public Employee getDeptManagerInf(Long deptId) {

        if (deptId == null) {
            return null;
        }
        EmployeeExample employeeExample = new EmployeeExample();
        employeeExample.createCriteria().andDeptIdEqualTo(deptId)
                .andStatusEqualTo(EmStatus.NORMAL.getCode()).
                andTypeIn(Arrays.asList(EmType.MANGER_MANGER.getCode(), EmType.CM_MANGER.getCode()));
        List<Employee> lsEmployee = mapper.selectByExample(employeeExample);
        if (null == lsEmployee || lsEmployee.size() < 1) {
            Dept dept = deptService.getDeptById(deptId);
            if (dept == null) {
                return null;
            }
            Long pDeptId = dept.getpDeptId();
            if (-1 == pDeptId) {
                return null;
            }
            return getDeptManagerInf(pDeptId);
        }
        return lsEmployee.get(0);
    }


    public Employee getManager(Long deptId, int emType) {
        return emDao.getManager(deptId, emType);
    }

    public Employee getManager(Dept dept) {
        return emDao.getManager(dept.getId(), dept.getType() == DeptType.MANGER.getCode() ? EmType.MANGER_MANGER.getCode() : EmType.CM_MANGER.getCode());
    }


    @Transactional
    public void save(Employee data) {
        mapper.insert(data);
        EmployeeAccount employeeAccount = new EmployeeAccount();
        employeeAccount.setId(SnowflakeIdWorker.nextID());
        employeeAccount.setEmployeeId(data.getId());
        employeeAccount.setAncestors(data.getAncestors());
        Company company = companyService.getById(data.getCompanyId());
        company.setEmployeeId(data.getId());
        companyService.updateById(company);
        // 判断公司是否可开票
        if (company.getInvoiceAble()) {
            // 可开票,判断是否一级渠道下的员工
            if (data.getType() != 1) {
                employeeAccount.setInventedWithdrawFlag(Boolean.TRUE);
            } else {
                employeeAccount.setInventedWithdrawFlag(Boolean.FALSE);
            }
        } else {
            // 不可开票
            employeeAccount.setInventedWithdrawFlag(company.getInvoiceAble());
        }
        employeeAccount.setCreateTime(LocalDateTime.now());
        employeeAccount.setUpdateTime(employeeAccount.getCreateTime());
        employeeAccountMapper.insert(employeeAccount);

    }

    public void updateEm(Employee employee) {
        mapper.updateByPrimaryKeySelective(employee);
    }


    public void offlineChannelEms(String code, LoginUser user) {
        emDao.offlineChannelEms(code, user);
    }

    public void cancelChannelEms(String code, LoginUser user) {
        emDao.cancelChannelEms(code, user);
    }


    public void onlineChannelEms(String code, LoginUser user) {

        emDao.onlineChannelEms(code, user);
    }


    /**
     * 清除员工token让其退出登录
     *
     * @param empId
     */
    @Transactional
    public void logout(Long empId) {
        loginDao.delete(empId);
        EmployeeHistory history = employeeHistoryDao.getLatest(empId);
        if (history != null) {
            history.setOutTime(new Date());
            history.setOutResaon(OutResaonType.LOGOUT.getCode());
            employeeHistoryDao.update(history);
            //清除缓存
            removeCacheToken(history.getToken());
        }
    }


    @Transactional
    public void logoutEmpsByDeptCode(String deptCode) {
        List<EmployeeLogin> records = loginDao.selectByCode(deptCode);
        if (CollectionUtils.isNotEmpty(records)) {
            for (EmployeeLogin login : records) {
                removeCacheToken(login.getToken());
            }
        }
        loginDao.deleteByDeptCode(deptCode);
    }


    /**
     * 删除缓存
     *
     * @param token
     */
    private void removeCacheToken(String token) {

        redisTemplate.delete(Constant.EM_TOKEN_PREFIX + UserType.EMPLOYEE.getCode() + ":" + token);
    }

    /**
     * 清除换机助手登陆缓存
     * @param mobile
     */
    private void removeCacheExchange(String mobile) {
        redisTemplate.delete(Constant.EM_TOKEN_PREFIX + UserType.EXCHANGE_PHONE_TOOL_USER.getCode() + ":" + mobile);
    }

    public List<Map<String, String>> queryTokenByDeptCode(String deptCode) {
        return emDao.queryTokenByDeptCode(deptCode, new Date());
    }


    public void updateDeptCode(List cmpIds, String deptCode, Date updateTime, String updator, Long newCmpId, Integer newCompanyType) {

        emDao.updateDeptCode(cmpIds, deptCode, updateTime, updator, newCmpId, newCompanyType);
    }


    public void updateRedisEm(String deptCode) {
        List<Map<String, String>> maps = queryTokenByDeptCode(deptCode);
        for (Map<String, String> map : maps) {
            String token = map.get("token");
            String newDeptCode = map.get("deptCode");
            String redisKey = Constants.EM_TOKEN_PREFIX + UserType.EMPLOYEE.getCode() + ":" + token;
            String newValue = "";
            try {
                String value = redisTemplate.opsForValue().get(redisKey);
                if (StringUtils.isNotBlank(value)) {
                    LoginUser loginUser = JSONObject.parseObject(value, LoginUser.class);
                    loginUser.setDeptCode(newDeptCode);
                    newValue = JSONObject.toJSONString(loginUser);
                    redisTemplate.opsForValue().set(redisKey, newValue);
                }
                log.error("更新员工缓存成功 token:{}, value:{} ", redisKey, newValue);
            } catch (Exception e) {
                log.error("更新员工缓存失败 token:{}, value:{} ", redisKey, newValue);
            }
        }
    }


    public PageUtils pageList(EmLsReq emLsReq, PageReq pageReq) {

        List<EmLsRps> lsRps = list(emLsReq, pageReq);
        return PageUtils.create(lsRps);
    }

    public List<EmLsRps> list(EmLsReq emLsReq, PageReq pageReq) {

        PageHelper.startPage(pageReq.getPage(), pageReq.getPageSize());
        List<EmLsRps> lsRps = extEmMapper.pageListEm(emLsReq);
        Map<Long, List<EmLsRps>> mapNoDeptManEmListRes = lsRps.stream().
                filter(emRps -> (StringUtils.isBlank(emRps.getMDeptMan()) && (emRps.getDeptId() != null))).
                collect(Collectors.groupingBy(EmLsRps::getDeptId));
        mapNoDeptManEmListRes.entrySet().forEach(
                noDeptManEmListRes -> {
                    Employee employee = getDeptManagerInf(noDeptManEmListRes.getKey());
                    if (employee != null) {
                        noDeptManEmListRes.getValue().forEach(emRps -> {
                            emRps.setMDeptMan(employee.getName());
                            emRps.setMDeptManMobile(employee.getMobileNumber());
                        });
                    }
                }
        );
        return lsRps;
    }

    @Transactional
    public void closeEmployee(EmployeeOperateLog employeeOperateLog) {
        logout(employeeOperateLog.getEmployeeId());
        //更改员工状态
        Employee employee = this.getById(employeeOperateLog.getEmployeeId());
        employee.setStatus(EmStatus.OFFLINE.getCode());
        mapper.updateById(employee);
        //插入员工状态变更日志
        employeeOperateLog.setType(OperateType.CLOSE.getCode());
        employeeOperateLog.setCreator(ShiroUtils.getUser().getName());
        employeeOperateLogService.save(employeeOperateLog);
    }

    @Transactional
    public void cancelEmployee(EmployeeOperateLog employeeOperateLog) {
        logout(employeeOperateLog.getEmployeeId());
        //更改员工状态
        Employee employee = this.getById(employeeOperateLog.getEmployeeId());
        //清除换机助手登陆缓存
        removeCacheExchange(employee.getMobileNumber());
        List<Employee> employeeList = this.mapper.selectList(Wrappers.lambdaQuery(Employee.class).eq(Employee::getStatus,1).likeRight(Employee::getAncestors,employee.getAncestors()));

        if(employeeList.size()>1){
            throw new BaseException(99999,"部门底下有员工不能直接注销");
        }

        employee.setStatus(EmStatus.CANCEL.getCode());
        mapper.updateById(employee);
        //插入员工状态变更日志
        employeeOperateLog.setType(OperateType.CANCEL.getCode());
        employeeOperateLog.setCreator(ShiroUtils.getUser().getName());
        employeeOperateLogService.save(employeeOperateLog);
    }

    @Transactional
    public void online(EmployeeOperateLog employeeOperateLog) {
        //更改员工状态
        Employee employee = this.getById(employeeOperateLog.getEmployeeId());
        employee.setStatus(EmStatus.NORMAL.getCode());
        mapper.updateById(employee);
        //插入员工状态变更日志
        employeeOperateLog.setType(OperateType.ON_LINE.getCode());
        employeeOperateLog.setCreator(ShiroUtils.getUser().getName());
        employeeOperateLogService.save(employeeOperateLog);
    }

    /**
     * 渠道的管理员
     *
     * @param companyId
     * @return
     */
    public Employee getCompanyManage(Long companyId) {
        Company company = companyService.getById(companyId);
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getCompanyId, company.getId());
        queryWrapper.eq(Employee::getCompanyType, CompanyType.STORE.getCode());
        queryWrapper.eq(Employee::getType, EmType.MANGER_MANGER.getCode());
        queryWrapper.eq(Employee::getStatus, EmStatus.NORMAL.getCode());
        Employee employee = mapper.selectOne(queryWrapper);
        return employee;
    }

    public String getSecondDept(Long employeeId) {
        Employee employee = this.getById(employeeId);
        Long currentDeptId = employee.getDeptId();
        Long companyId = employee.getCompanyId();

        Dept currentDept = deptService.getDeptById(currentDeptId);
        // 查询渠道的管理部门
        Dept companyManagerDept = deptService.getCompanyManagerDept(companyId);

        String companyManagerDeptCode = companyManagerDept.getCode();

        String currentDeptCode = currentDept.getCode();

        List<String> companyManagerDeptCodeSplits = splitDeptCode(companyManagerDeptCode);
        List<String> currentDeptCodeSplits = splitDeptCode(currentDeptCode);

        int companyManagerDeptCodeSplitsSize = companyManagerDeptCodeSplits.size();
        int currentDeptCodeSplitsSize = currentDeptCodeSplits.size();

        if ((companyManagerDeptCodeSplitsSize + 1) < currentDeptCodeSplitsSize) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i <= companyManagerDeptCodeSplitsSize; i++) {
                if (i != 0) {
                    sb.append("-");
                }
                sb.append(currentDeptCodeSplits.get(i));
            }
            String secondDeptCode = sb.toString();
            Dept secondDept = deptService.getDeptByCompanyIdAndCode(companyId, secondDeptCode);
            return secondDept.getName();
        }
        return null;

    }


    private List<String> splitDeptCode(String deptCode) {
        String[] items = deptCode.split("-");
        return Arrays.asList(items);
    }

    public List<EmployeeParentInfoDTO> buildAncestorsInfo(Long empId, String ancestors) {
        List<EmployeeParentInfoDTO> list = new ArrayList<>();
        if (StrUtil.isBlank(ancestors)) {
            return list;
        }
        List<String> split = StrUtil.split(ancestors, ",");
        List<Long> ancestoreIdList = new ArrayList<>();
        split.forEach(id -> ancestoreIdList.add(Long.valueOf(id)));
        List<Long> parentIds = ancestoreIdList.stream().filter(o -> !empId.equals(o)).collect(Collectors.toList());
        if (CollUtil.isEmpty(parentIds)) {
            return list;
        }
        List<Employee> parents = mapper.selectList(new LambdaQueryWrapper<Employee>().in(Employee::getId, parentIds));
        if (CollUtil.isEmpty(parents)) {
            return list;
        }
        List<Long> deptIds = parents.stream().map(Employee::getDeptId).collect(Collectors.toList());
        if (CollUtil.isEmpty(deptIds)) {
            return list;
        }
        List<Dept> deptList = deptMapper.selectList(new LambdaQueryWrapper<Dept>().in(Dept::getId, deptIds));
        if (CollUtil.isEmpty(deptList)) {
            return list;
        }
        Map<Long, Employee> parentsMap = parents.stream().collect(Collectors.toMap(Employee::getId, Function.identity()));
        Map<Long, Dept> deptMap = deptList.stream().collect(Collectors.toMap(Dept::getId, Function.identity()));

        for (Long parentId : parentIds) {
            Employee parent = parentsMap.get(parentId);
            String name = parent.getName();
            String mobileNumber = parent.getMobileNumber();
            Dept dept = deptMap.get(parent.getDeptId());
            String deptName = dept.getName();
            list.add(EmployeeParentInfoDTO.builder().name(name).mobile(mobileNumber).deptName(deptName).build());
        }

        return list;
    }

    /**
     * Bd列表
     * @return
     */
    public List<Employee> BdList(){
        List<Employee> result = mapper.selectList(Wrappers.lambdaQuery(Employee.class)
                .eq(Employee::getLevel,1)
                .eq(Employee::getType,3)
                .eq(Employee::getStatus,1));
        return result;
    }

    /**
     * 区域经理列表
     * @return
     */
    public List<Employee> areaList(String bdEmployeeId){
        Employee bdEmployee  = getById(Long.valueOf(bdEmployeeId));
        List<Employee> result = mapper.selectList(Wrappers.lambdaQuery(Employee.class)
                 .likeRight(Employee::getAncestors,bdEmployee.getAncestors())
                .eq(Employee::getLevel,2)
                .eq(Employee::getStatus,1));
        return result;
    }

    /**
     * 下级代理列表
     * @return
     */
    public List<Employee> agentList(String employeeId){
        Employee employee  = getById(Long.valueOf(employeeId));
        List<Employee> employeeList = mapper.selectList(Wrappers.lambdaQuery(Employee.class)
                .eq(Employee::getLevel, employee.getLevel() + 1)
                .likeRight(Employee::getAncestors, employee.getAncestors())
                .eq(Employee::getCompanyType, CompanyType.COMPANY.getCode())
                .eq(Employee::getStatus, EmStatus.NORMAL.getCode()));
        if (CollUtil.isEmpty(employeeList)) {
            return Collections.emptyList();
        }
        return employeeList;
    }





    public Map<Long, Employee> getEmployeeByMap(Collection<Long> ids) {

        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyMap();
        }

        List<Employee> employeeList = mapper.selectList(Wrappers.lambdaQuery(Employee.class)
                .in(Employee::getId,ids)
                .eq(Employee::getStatus,1));

        if (CollUtil.isEmpty(employeeList)) {
            return Collections.emptyMap();
        }
        return employeeList.stream()
                .collect(Collectors.toMap(Employee::getId, Function.identity()));
    }

    public List<Employee> queryChildEmployees(long employeeId) {
        Employee employee = getById(employeeId);
        int level = employee.getLevel();
        boolean isAdmin = 0 == employee.getLevel();
        int childLevel = level + 1;
        String ancestor = employee.getAncestors();
        return mapper.selectList(Wrappers.lambdaQuery(Employee.class)
                .eq(Employee::getLevel, childLevel)
                .in(isAdmin, Employee::getType, Arrays.asList(EmType.MANGER_MANGER.getCode(), EmType.CM_MANGER.getCode()))
                .in(isAdmin, Employee::getCompanyType, CompanyType.COMPANY.getCode())
                .eq(Employee::getStatus, EmStatus.NORMAL.getCode())
                .likeRight(Employee::getAncestors, ancestor));

    }
}