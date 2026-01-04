package com.zxtx.hummer.company.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.zxtx.hummer.commission.service.CommissionPlanService;
import com.zxtx.hummer.common.Constants;
import com.zxtx.hummer.common.exception.BizError;
import com.zxtx.hummer.common.exception.BusinessException;
import com.zxtx.hummer.common.utils.PageUtils;
import com.zxtx.hummer.common.utils.ShiroUtils;
import com.zxtx.hummer.company.dao.CompanyDao;
import com.zxtx.hummer.company.dao.mapper.CompanyMapper;
import com.zxtx.hummer.company.dao.mapper.ExtCompanyMapper;
import com.zxtx.hummer.company.domain.Company;
import com.zxtx.hummer.company.enums.CompanyStatus;
import com.zxtx.hummer.company.enums.ExchangeType;
import com.zxtx.hummer.company.req.CompanyBaseInfoEditReq;
import com.zxtx.hummer.company.vo.BankVo;
import com.zxtx.hummer.company.vo.ComListRes;
import com.zxtx.hummer.company.vo.CompanyListReq;
import com.zxtx.hummer.company.vo.CompanyReq;
import com.zxtx.hummer.dept.domain.Dept;
import com.zxtx.hummer.dept.enums.DeptStatus;
import com.zxtx.hummer.dept.enums.DeptType;
import com.zxtx.hummer.dept.service.DeptService;
import com.zxtx.hummer.em.dao.mapper.EmployeeMapper;
import com.zxtx.hummer.em.domain.Employee;
import com.zxtx.hummer.em.enums.CompanyType;
import com.zxtx.hummer.em.enums.EmStatus;
import com.zxtx.hummer.em.enums.EmType;
import com.zxtx.hummer.em.service.EmService;
import com.zxtx.hummer.packageInfo.service.PackageInfoService;
import com.zxtx.hummer.system.domain.UserDO;
import com.zxtx.hummer.system.service.UserService;
import com.zxtx.hummer.system.shiro.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CompanyService extends ServiceImpl<CompanyMapper, Company> {
    @Autowired
    private CompanyDao companyDao;
    @Autowired
    private CompanyMapper companyMapper;
    @Autowired
    private EmService emService;
    @Value("${spring.profiles.active}")
    private String env;
    //    @Autowired
//    private SmsSender smsSender;
    @Autowired
    private DeptService deptService;
    @Autowired
    UserService userService;
    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private PackageInfoService packageInfoService;
    @Autowired
    private CommissionPlanService commissionPlanService;
    @Autowired
    private ExtCompanyMapper extCompanyMapper;

    public List<Company> list() {
        return companyDao.selectAll();
    }

    @Transactional(rollbackFor = Exception.class)
    public void editCompanyBaseInfo(CompanyBaseInfoEditReq req) {
        Company company = BeanUtil.copyProperties(req, Company.class);
        company.setUpdator(ShiroUtils.getUser().getUserName());
        company.setUpdateTime(new Date());
        this.updateById(company);
    }

    public PageUtils listByCondition(CompanyListReq req) {
        PageHelper.startPage(req.getPage(), req.getPageSize());
        List<ComListRes> companies = companyDao.listByCondition(req);
        if (CollUtil.isEmpty(companies)) {
            return PageUtils.create(companies);
        }
        for (ComListRes res : companies) {
            res.setStatusName(CompanyStatus.getNameByCode(res.getStatus().byteValue()));
            res.setTypeName(CompanyType.getNameByCode(res.getType()));
            res.setExchangeTypeName(ExchangeType.getNameByCode(res.getExchangeType()));
            //门店管理员
            if (ObjectUtil.isNotNull(res.getEmployeeId())) {
                Employee employee = employeeMapper.selectById(res.getEmployeeId());
                Employee bdEmployee = employeeMapper.selectBdByAncestors(employee.getAncestors());
                res.setBdName(ObjectUtil.isNull(bdEmployee) ? "" : bdEmployee.getName());
            }
        }
        return PageUtils.create(companies);
    }


    public Company getCompanyById(Long companyId) {
        return companyMapper.selectByPrimaryKey(companyId);
    }


    @Transactional
    public void agree(Long id) {
        Company company = companyMapper.selectByPrimaryKey(id);
        Employee em = emService.getByMobile(company.getContactMobile(),
                Arrays.asList(EmStatus.OFFLINE.getCode(), EmStatus.NORMAL.getCode()));
        if (em != null) {
            throw new BusinessException(BizError.EM_EXIST);
        }
        company = doUpdate(company, CompanyStatus.NORMAL);
        Employee employee = emService.getById(company.getAplId());
        // 部门创建
        Dept dept = deptService.createDeptOfChannel(company, employee);
        // 管理员创建
        Dept parentDept = deptService.getById(dept.getpDeptId());
        Employee manager = emService.getManager(parentDept);
        emService.createEm(dept, employee, company, manager);

        //默认平台服务费压价补贴插入
        packageInfoService.saveDefault(company.getId());

        //初始化店长的压价分佣方案
        if (company.getPId().equals(Constants.LAN_HAI_CMP_ID)) {
            commissionPlanService.create(company.getEmployeeId());
            commissionPlanService.insuranceCreate(company.getEmployeeId());
        }

        // 短信
//        SmsMessage smsMessage = SmsMessage.create().mobile(emService.getById(company.getAplId()).getMobileNumber())
//                .templateCode("SMS_167963308")
//                .addParam("name", company.getName());
//        smsSender.send(smsMessage);

    }


    public void refuse(Long id) {
        Company company = companyMapper.selectByPrimaryKey(id);
        company = doUpdate(company, CompanyStatus.FAILED);
        // 短信
//        SmsMessage smsMessage = SmsMessage.create().mobile(emService.getById(company.getAplId()).getMobileNumber())
//                .templateCode("SMS_167973252")
//                .addParam("name", company.getName());
//        smsSender.send(smsMessage);

    }

    private Company doUpdate(Company company, CompanyStatus status) {

        if (company == null) {
            throw new BusinessException(BizError.Company_not_exist);
        }
        String updator = ShiroUtils.getUser().getUserName();
        int i = companyDao.updateStatus(company, updator, status);
        if (i <= 0) {
            throw new BusinessException(BizError.COMPANY_STATUS_CHANGE);
        }
        return company;
    }

    public void updateDeposit(Long companyId, Integer deposit, String name) {
        Company company = companyMapper.selectByPrimaryKey(companyId);
        company.setUpdateTime(new Date());
        company.setUpdator(name);
        companyMapper.updateById(company);
    }

    public void updateInvoceAble(Company oldcompany, boolean invoiceAble) {
        companyMapper.updateInvoiceAble(oldcompany.getId(), invoiceAble);
    }


    public Company getByName(String name, boolean hadOffline) {
        return companyDao.getByName(name, hadOffline);
    }

    public Company getById(Long channelId) {
        return companyMapper.selectByPrimaryKey(channelId);
    }

    public void updateCompany(Company company) {
        companyMapper.updateById(company);
    }


    private boolean doUpdateChannelName(CompanyReq req, Company oldCompany, String updator) {
        if (!oldCompany.getName().equals(req.getName())) { //判断公司
            Company data = getByName(req.getName(), true);
            if (data != null) {
                throw new BusinessException(BizError.COMPANY_EXIST);
            }
            oldCompany.setName(req.getName());
            // 更新管理部名称
            Dept managerDept = deptService.getManagerDept(req.getId(), Arrays.asList(new Integer[]{DeptStatus.NORMAL.getCode(), DeptStatus.OFFLINE.getCode()}));
            managerDept.setName(oldCompany.getName() + "-管理部");
            deptService.update(managerDept, updator);
            return true;
        }
        return false;
    }


    private boolean doUpdateChannelSn(CompanyReq req, Company oldCompany) {
        return false;
    }


    private boolean doUpdateChannelOther(CompanyReq req, Company oldCompany) {
        return true;
    }


    private boolean doUpdateChannelManager(CompanyReq req, Company oldCompany, LoginUser user, boolean isOffLine) {

        byte emStatus = EmStatus.NORMAL.getCode();
        if (isOffLine) {
            emStatus = EmStatus.OFFLINE.getCode();
        }
        List<Byte> lsEmStatus = Arrays.asList(new Byte[]{EmStatus.NORMAL.getCode(), EmStatus.OFFLINE.getCode()});
        List<Integer> lsDeptStats = Arrays.asList(new Integer[]{DeptStatus.OFFLINE.getCode(), DeptStatus.NORMAL.getCode()});
        //这里与新增逻辑一样，不能设置已存在的员工作为渠道管理员，需要员工先注销账号，然后可以设置为管理员
        Employee employee = emService.getManManager(oldCompany.getId(), lsEmStatus);
        if (!employee.getMobileNumber().equals(req.getContactMobile())) { //判断手机号
            Employee emByMobile = emService.getByMobile(req.getContactMobile(), lsEmStatus);
            if (emByMobile != null) {
                throw new BusinessException(BizError.EM_CANT_CANNEL);
            }
            Dept dept = deptService.getManagerDept(oldCompany.getId(), lsDeptStats);
            // 修改员工角色
            doChangeEmRole(dept, user, EmType.MANGER_EM, employee);
            //要求重新登录
            emService.logout(employee.getId());
            oldCompany.setContactMobile(req.getContactMobile());
            oldCompany.setContact(req.getContact());

            Dept parentDept = deptService.getById(dept.getpDeptId());
            Employee manager = emService.getManager(parentDept);
            // 创建渠道管理员
            emService.createManEm(req.getContactMobile(), req.getContact(), dept, EmType.MANGER_MANGER.getCode(), emStatus, manager);
            return true;
        } else {
            /*if (!employee.getName().equals(req.getContact())) {
                throw new BusinessException(BizError.USER_EXIST);
            }*/
        }
        return false;
    }


    @Transactional
    public void updateChannel(CompanyReq req) {

        Company company = getById(req.getId());
        if (company == null) {
            throw new BusinessException(BizError.COMPANY_NOT_EXIST);
        }
        byte companyStatus = company.getStatus();
        boolean isOffLine = (CompanyStatus.OFFLINE.getCode() == companyStatus);
        LoginUser user = ShiroUtils.getUser();
        String updator = user.getUserName();
        boolean updateCompany = doUpdateChannelName(req, company, updator);
        boolean updateSn = doUpdateChannelSn(req, company);
        boolean updateOthe = doUpdateChannelOther(req, company);
        boolean updateManger = doUpdateChannelManager(req, company, user, isOffLine);
        if (updateCompany || updateSn || updateOthe || updateManger) {
            company.setUpdateTime(new Date());
            company.setUpdator(user.getUserName());
            updateCompany(company);
        }

        List<Long> packageIds = req.getPackageIds();
    }


    private void doChangeEmRole(Dept dept, LoginUser user, EmType type, Employee employee) {
        if (employee == null) {
            return;
        }
        employee.setDeptId(dept.getId());
        employee.setUpdateTime(new Date());
        employee.setUpdator(user.getName());
        employee.setType(type.getCode());
        employee.setDeptType(dept.getType());
        employee.setDeptCode(dept.getCode());
        emService.updateEm(employee);
    }


    private void doOfflineChannel(String code, String updator) {
        companyDao.offlineChannel(code, updator);
    }

    private void cancelChannel(String code, String updator) {
        companyDao.cancelChannel(code, updator);
    }


    private void doOnlineChannel(String code, String updator) {
        companyDao.onlineChannel(code, updator);
    }


    @Transactional
    public void offlineChannel(Long companyId) {
        LoginUser user = ShiroUtils.getUser();
        Company company = getById(companyId);
        if (company == null) {
            throw new BusinessException(BizError.COMPANY_NOT_EXIST);
        }
        Dept dept = deptService.getManagerDept(companyId,
                Arrays.asList(new Integer[]{DeptStatus.OFFLINE.getCode(), DeptStatus.NORMAL.getCode()}));
        emService.offlineChannelEms(dept.getCode(), user); //下线渠道人员
        deptService.offlineChannelDept(dept.getCode(), user); //下线渠道部门
        doOfflineChannel(company.getCode(), user.getName()); //下线渠道
        //下线所有渠道的员工
        emService.logoutEmpsByDeptCode(dept.getCode());
    }


    @Transactional
    public void onlineChannel(Long companyId) {
        LoginUser user = ShiroUtils.getUser();
        Company company = getById(companyId);
        if (company == null) {
            throw new BusinessException(BizError.COMPANY_NOT_EXIST);
        }
        Dept dept = deptService.getManagerDept(companyId,
                Arrays.asList(new Integer[]{DeptStatus.OFFLINE.getCode(), DeptStatus.NORMAL.getCode()}));
        emService.onlineChannelEms(dept.getCode(), user); //上线渠道人员
        deptService.onlineChannelDept(dept.getCode(), user); //上线渠道部门
        doOnlineChannel(company.getCode(), user.getName()); //上线渠道
        //TODO 通知
    }


    public List<BankVo> getBanks() {
        List<BankVo> lsBanks = new ArrayList<>();
        lsBanks.add(new BankVo(1, "交通银行"));
        lsBanks.add(new BankVo(2, "工商银行"));
        return lsBanks;
    }

    @Transactional
    public void changeInvoice(Long id) {

        Company company = companyMapper.selectByPrimaryKey(id);

        if (company.getStatus() != CompanyStatus.TO_AUDIT.getCode()) {
            throw new BusinessException(BizError.COMPANY_STATUS_HAVING_AUDIT);
        }

        updateInvoceAble(company, !company.getInvoiceAble());
    }

    @Transactional
    public void addUser(Long id) {
        Company company = this.getById(id);
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getCompanyId, company.getId());
        queryWrapper.eq(Employee::getCompanyType, Integer.valueOf(2));
        queryWrapper.eq(Employee::getType, Integer.valueOf(1));
        Employee employee = employeeMapper.selectOne(queryWrapper);
        if (ObjectUtil.isNull(employee)) {
            throw new BusinessException("员工信息不存在");
        }
        UserDO user = new UserDO();
        user.setName(company.getName());
        user.setMobile(company.getContactMobile());
        user.setPassword("aycx" + company.getContactMobile().substring(company.getContactMobile().length() - 4));
        user.setChannel("lanhai");
        user.setEmployeeId(employee.getId());
        user.setDeptId(employee.getDeptId());
        //暂时先设置客服角色
        user.setRoleIds(Arrays.asList(102L));
        userService.save(user);
        company.setLoginAble(true);
        companyMapper.updateById(company);
    }

    public List<Long> childCompanyIds() {
        //登录用户绑定的员工
        Long employeeId = ShiroUtils.getUser().getEmployeeId();
        if (ObjectUtil.isNull(employeeId)) return Collections.emptyList();
        Employee employee = emService.getById(employeeId);
        LambdaQueryWrapper<Company> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Company::getStatus, Arrays.asList(1, 2));
        queryWrapper.eq(Company::getPDeptId, employee.getDeptId());
        List<Company> companyList = companyMapper.selectList(queryWrapper);
        if (CollUtil.isEmpty(companyList)) return Collections.emptyList();
        return companyList.stream().map(Company::getId).collect(Collectors.toList());
    }

    @Transactional
    public void changeDeposit(Long id) {
        Company company = companyMapper.selectByPrimaryKey(id);
        if (company.getStatus() != CompanyStatus.TO_AUDIT.getCode()) {
            throw new BusinessException(BizError.COMPANY_STATUS_HAVING_AUDIT);
        }
        companyMapper.updateById(company);
    }

    @Transactional
    public void changeCommission(Long id) {
        Company company = companyMapper.selectByPrimaryKey(id);
        if (company.getStatus() != CompanyStatus.TO_AUDIT.getCode()) {
            throw new BusinessException(BizError.COMPANY_STATUS_HAVING_AUDIT);
        }
        company.setCommissionAble(true);
        companyMapper.updateById(company);
    }

    @Transactional
    public void cancelCompany(Long companyId) {
        LoginUser user = ShiroUtils.getUser();
        Company company = getById(companyId);
        if (company == null) {
            throw new BusinessException(BizError.COMPANY_NOT_EXIST);
        }
        Dept dept = deptService.getManagerDept(companyId,
                Arrays.asList(new Integer[]{DeptStatus.OFFLINE.getCode(), DeptStatus.NORMAL.getCode()}));
        emService.cancelChannelEms(dept.getCode(), user); //注销渠道人员
        deptService.cancelChannelDept(dept.getCode(), user); //下线渠道部门
        cancelChannel(company.getCode(), user.getName()); //下线渠道
        //下线所有渠道的员工
        emService.logoutEmpsByDeptCode(dept.getCode());
    }

    /**
     * 根据连锁总店名称获取门店
     * @return
     */
    public Set<Long> getByChainName(String companyChainName) {
        //登录用户绑定的员工
        LambdaQueryWrapper<Company> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Company::getStatus, 2);
        queryWrapper.eq(Company::getType, 3);
        queryWrapper.eq(Company::getName, companyChainName);
        List<Company> companyList = companyMapper.selectList(queryWrapper);
        if (CollUtil.isEmpty(companyList)) return Collections.emptySet();

        Set<Long> pId = companyList.stream().map(Company::getId).collect(Collectors.toSet());

        Set<Long> cpmId = this.lambdaQuery().in(Company::getPId,pId)
                .eq(Company::getStatus,2).list().stream().map(Company::getId).collect(Collectors.toSet());

        Set<Long> ids = Stream.concat(pId.stream(), cpmId.stream()).collect(Collectors.toSet());
        return ids;
    }

    public Set<Long> getByChainId(Long chainCompanyId) {

        if (chainCompanyId == null) {
            return Collections.emptySet();
        }

        Set<Long> pId = Collections.singleton(chainCompanyId);

        Set<Long> cpmId = this.lambdaQuery()
                .eq(Company::getPId,chainCompanyId)
                .eq(Company::getStatus,2)
                .list().stream().map(Company::getId).collect(Collectors.toSet());

        return Stream.concat(pId.stream(), cpmId.stream()).collect(Collectors.toSet());
    }

    public List<Company> getChainCompanyByBdEmployeeId(Long employeeId) {
        if (employeeId == null) {
            return new ArrayList<>();
        }
        Employee employee = employeeMapper.selectById(employeeId);
        if (employee == null) {
            return new ArrayList<>();
        }
        List<Employee> subChainEmployees = employeeMapper.selectList(new LambdaQueryWrapper<Employee>()
                .eq(Employee::getCompanyType, CompanyType.CHAIN.getCode())
                .eq(Employee::getDeptType, DeptType.MANGER.getCode())
                .eq(Employee::getType, EmType.MANGER_MANGER.getCode())
                .eq(Employee::getStatus, EmStatus.NORMAL.getCode())
                .likeRight(Employee::getAncestors, employee.getAncestors()));
        if (CollUtil.isEmpty(subChainEmployees)) {
            return new ArrayList<>();
        }
        Set<Long> companyIds = subChainEmployees.stream().map(Employee::getCompanyId).filter(Objects::nonNull).collect(Collectors.toSet());
        return this.lambdaQuery()
                .eq(Company::getType, CompanyType.CHAIN.getCode())
                .eq(Company::getStatus, CompanyStatus.NORMAL.getCode())
                .in(Company::getId, companyIds)
                .list();
    }

    public Map<Long, Company> getCompanyInfoMap(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        List<Company> list = this.lambdaQuery().in(Company::getId, ids).list();
        return CollUtil.isEmpty(list)
                ? Collections.emptyMap()
                : list.stream().collect(Collectors.toMap(Company::getId, Function.identity()));
    }
}