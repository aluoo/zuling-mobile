package com.zxtx.hummer.company.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import com.zxtx.hummer.account.domain.EmployeeAccount;
import com.zxtx.hummer.account.service.IEmployeeAccountService;
import com.zxtx.hummer.common.exception.BaseException;
import com.zxtx.hummer.common.lock.RedisLockService;
import com.zxtx.hummer.common.utils.ExcelImportUtil;
import com.zxtx.hummer.common.utils.PasswordUtil;
import com.zxtx.hummer.common.utils.SnowflakeIdWorker;
import com.zxtx.hummer.common.utils.ValidatorUtil;
import com.zxtx.hummer.company.dao.mapper.ExtCompanyMapper;
import com.zxtx.hummer.company.domain.Company;
import com.zxtx.hummer.company.enums.CompanyStatus;
import com.zxtx.hummer.company.req.CompanyCreateDTO;
import com.zxtx.hummer.company.req.EmployeeCreateDTO;
import com.zxtx.hummer.dept.domain.Dept;
import com.zxtx.hummer.dept.enums.DeptType;
import com.zxtx.hummer.dept.service.DeptService;
import com.zxtx.hummer.em.domain.Employee;
import com.zxtx.hummer.em.enums.EmStatus;
import com.zxtx.hummer.em.enums.EmType;
import com.zxtx.hummer.em.service.EmService;
import com.zxtx.hummer.em.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/10/28
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Service
public class CompanyBatchCreateService {
    @Autowired
    private CompanyService companyService;
    @Autowired
    private ExtCompanyMapper extCompanyMapper;
    @Autowired
    private EmService emService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private IEmployeeAccountService employeeAccountService;
    @Autowired
    private DeptService deptService;
    @Autowired
    private RedisLockService redisLockService;

    @Transactional(rollbackFor = Exception.class)
    public void batchCreateEmployeeFromImport(MultipartFile file) {
        redisLockService.redisLock(StrUtil.format("pc_import_batch_create_employee_lock_{}",file.getOriginalFilename()), 20L, TimeUnit.SECONDS);
        List<EmployeeCreateDTO> list;
        try {
            list = ExcelImportUtil.importFromExcel(file.getInputStream(), EmployeeCreateDTO.class);
        } catch (Exception e) {
            log.error(ExceptionUtil.getMessage(e));
            throw new BaseException(-1, "导入失败");
        }
        if (CollUtil.isEmpty(list)) {
            throw new BaseException(-1, "导入的文件为空");
        }
        list.forEach(ValidatorUtil::validateBean);
        this.checkDuplicateEmployeeMobile(list);
        Date createTime = new Date();
        List<Employee> createEmployeeList = new ArrayList<>();
        List<Employee> updateEmployeeList = new ArrayList<>();
        List<EmployeeAccount> createEmployeeAccountList = new ArrayList<>();
        for (EmployeeCreateDTO dto : list) {
            if (!Validator.isMobile(dto.getEmployeeMobile())) {
                throw new BaseException(-1, StrUtil.format("手机号{}格式错误,请输入正确的手机号", dto.getEmployeeName()));
            }
            Employee checkEmp = emService.getByMobile(dto.getEmployeeMobile(), Arrays.asList(EmStatus.NORMAL.getCode(), EmStatus.OFFLINE.getCode()));
            if (checkEmp != null) {
                throw new BaseException(-1, StrUtil.format("{}该手机号已经注册", dto.getEmployeeMobile()));
            }
            Employee aplEmp = emService.getByMobile(dto.getDeptManagerMobile());
            if (aplEmp == null) {
                throw new BaseException(-1, StrUtil.format("邀请人{}不存在", dto.getDeptManagerMobile()));
            }
            Dept dept = deptService.getDeptById(aplEmp.getDeptId());
            int type = dept.getType() == DeptType.MANGER.getCode() ? EmType.MANGER_EM.getCode() : EmType.CM_EM.getCode();

            Employee emp = new Employee();
            emp.setId(SnowflakeIdWorker.nextID());
            emp.setCompanyId(aplEmp.getCompanyId());
            emp.setCompanyType(aplEmp.getCompanyType());
            emp.setCreateTime(createTime);
            emp.setCreator(aplEmp.getName());
            emp.setDeptCode(aplEmp.getDeptCode());
            emp.setDeptType(aplEmp.getDeptType());
            emp.setType(type);
            emp.setUpdator(aplEmp.getName());
            emp.setStatus(EmStatus.NORMAL.getCode());
            emp.setUpdateTime(createTime);
            emp.setMobileNumber(dto.getEmployeeMobile());
            emp.setName(dto.getEmployeeName());
            emp.setDeptId(aplEmp.getDeptId());
            // 设置人员组织关系
            emp.setAncestors(aplEmp.getAncestors() + "," + emp.getId());
            emp.setIsLeaf(Boolean.TRUE);
            emp.setLevel(aplEmp.getLevel() + 1);
            emp.setPassword(PasswordUtil.buildDefaultPassword(emp.getMobileNumber()));
            createEmployeeList.add(emp);

            aplEmp.setIsLeaf(Boolean.FALSE);
            aplEmp.setUpdateTime(createTime);
            updateEmployeeList.add(aplEmp);

            EmployeeAccount employeeAccount = new EmployeeAccount();
            employeeAccount.setId(SnowflakeIdWorker.nextID());
            employeeAccount.setEmployeeId(emp.getId());
            employeeAccount.setAncestors(emp.getAncestors());
            createEmployeeAccountList.add(employeeAccount);
        }

        employeeService.saveBatch(createEmployeeList);
        employeeService.updateBatchById(updateEmployeeList);
        employeeAccountService.saveBatch(createEmployeeAccountList);
    }

    private void buildAndSaveCompanyList(List<CompanyCreateDTO> list) {
        if (CollUtil.isEmpty(list)) {
            return;
        }
        List<Company> companyList = new ArrayList<>();
        for (CompanyCreateDTO dto : list) {
            // 查找邀请人，异常则抛出
            Employee aplEmp = emService.getByMobile(dto.getAplMobile());
            if (aplEmp == null) {
                throw new BaseException(-1, StrUtil.format("邀请代理{}不存在", dto.getAplMobile()));
            }
            // 查找邀请人公司，不存在则抛出
            Company aplCompany = companyService.getById(aplEmp.getCompanyId());
            if (aplCompany == null) {
                throw new BaseException(-1, StrUtil.format("邀请代理{}对应的渠道公司不存在", dto.getAplMobile()));
            }
            // 查找门店负责人，存在则抛出
            Employee contactEmp = emService.getByMobile(dto.getContactMobile(), Arrays.asList(EmStatus.NORMAL.getCode(), EmStatus.OFFLINE.getCode()));
            if (contactEmp != null) {
                throw new BaseException(-1, StrUtil.format("{}该手机号已经注册，不能成为门店管理员。请更换手机号", dto.getContactMobile()));
            }
            // 查找门店负责人公司，存在则抛出
            Company contactCmp = this.getByMobile(dto.getContactMobile());
            if (contactCmp != null) {
                throw new BaseException(-1, StrUtil.format("{}该手机号已经注册，不能成为门店管理员。请更换手机号", dto.getContactMobile()));
            }
            Company cmp = new Company();
            BeanUtil.copyProperties(dto, cmp);
            cmp.setType(dto.getType());
            cmp.setExchangeType(dto.getExchangeType());
            cmp.setAplId(aplEmp.getId());
            cmp.setPId(aplEmp.getCompanyId());
            cmp.setPDeptId(aplEmp.getDeptId());
            cmp.setStatus(CompanyStatus.TO_AUDIT.getCode());
            cmp.setCode(this.generateCompanyCode(aplEmp.getCompanyId(), aplCompany.getCode()));
            companyList.add(cmp);
            boolean success = companyService.save(cmp);
            if (!success) {
                throw new BaseException(-1, "门店创建失败");
            }
        }

        if (CollUtil.isNotEmpty(companyList)) {
            // 审核通过并创建部门&管理员
            companyList.forEach(o -> companyService.agree(o.getId()));
        }
    }

    private List<Company> buildCompanyList(List<CompanyCreateDTO> list) {
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyList();
        }
        List<Company> companyList = new ArrayList<>();
        for (CompanyCreateDTO dto : list) {
            // 查找邀请人，异常则抛出
            Employee aplEmp = emService.getByMobile(dto.getAplMobile());
            if (aplEmp == null) {
                throw new BaseException(-1, StrUtil.format("邀请代理{}不存在", dto.getAplMobile()));
            }
            // 查找邀请人公司，不存在则抛出
            Company aplCompany = companyService.getById(aplEmp.getCompanyId());
            if (aplCompany == null) {
                throw new BaseException(-1, StrUtil.format("邀请代理{}对应的渠道公司不存在", dto.getAplMobile()));
            }
            // 查找门店负责人，存在则抛出
            Employee contactEmp = emService.getByMobile(dto.getContactMobile(), Arrays.asList(EmStatus.NORMAL.getCode(), EmStatus.OFFLINE.getCode()));
            if (contactEmp != null) {
                throw new BaseException(-1, StrUtil.format("{}该手机号已经注册，不能成为门店管理员。请更换手机号", dto.getContactMobile()));
            }
            // 查找门店负责人公司，存在则抛出
            Company contactCmp = this.getByMobile(dto.getContactMobile());
            if (contactCmp != null) {
                throw new BaseException(-1, StrUtil.format("{}该手机号已经注册，不能成为门店管理员。请更换手机号", dto.getContactMobile()));
            }
            Company cmp = new Company();
            BeanUtil.copyProperties(dto, cmp);
            cmp.setType(dto.getType());
            cmp.setExchangeType(dto.getExchangeType());
            cmp.setAplId(aplEmp.getId());
            cmp.setPId(aplEmp.getCompanyId());
            cmp.setPDeptId(aplEmp.getDeptId());
            cmp.setStatus(CompanyStatus.TO_AUDIT.getCode());
            cmp.setCode(this.generateCompanyCode(aplEmp.getCompanyId(), aplCompany.getCode()));
            companyList.add(cmp);
        }
        return companyList;
    }

    private void createCompanyAndAgree(List<Company> list) {
        if (CollUtil.isNotEmpty(list)) {
            boolean success = companyService.saveBatch(list);
            if (!success) {
                throw new BaseException(-1, "门店创建失败");
            }
            // 审核通过并创建部门&管理员
            list.forEach(o -> companyService.agree(o.getId()));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchCreateCompanyFromImport(MultipartFile file) {
        redisLockService.redisLock(StrUtil.format("pc_import_batch_create_company_lock_{}",file.getOriginalFilename()), 20L, TimeUnit.SECONDS);
        List<CompanyCreateDTO> list;
        try {
            list = ExcelImportUtil.importFromExcel(file.getInputStream(), CompanyCreateDTO.class);
        } catch (Exception e) {
            log.error(ExceptionUtil.getMessage(e));
            throw new BaseException(-1, "导入失败");
        }
        if (CollUtil.isEmpty(list)) {
            throw new BaseException(-1, "导入的文件为空");
        }
        list.forEach(ValidatorUtil::validateBean);
        this.checkDuplicateContactMobile(list);

        list.forEach(o -> {
            Integer type = this.convertCompanyType(o.getTypeStr());
            Integer exchangeType = this.convertCompanyExchangeType(o.getExchangeTypeStr());
            o.setType(type);
            o.setExchangeType(exchangeType);
        });

        List<CompanyCreateDTO> chain = list.stream().filter(o -> o.getType().equals(3)).collect(Collectors.toList());
        List<CompanyCreateDTO> single = list.stream().filter(o -> o.getType().equals(2)).collect(Collectors.toList());

        if (CollUtil.isNotEmpty(chain)) {
            /*List<Company> chainCompanyList = this.buildCompanyList(chain);
            this.createCompanyAndAgree(chainCompanyList);*/
            this.buildAndSaveCompanyList(chain);
        }

        if (CollUtil.isNotEmpty(single)) {
            /*List<Company> singleCompanyList = this.buildCompanyList(single);
            this.createCompanyAndAgree(singleCompanyList);*/
            this.buildAndSaveCompanyList(single);
        }
    }

    private String generateCompanyCode(Long pId, String pCode) {
        Integer maxCode = extCompanyMapper.selectMaxCode(pId);
        if (maxCode == null) {
            maxCode = 0;
        }
        return pCode + "-" + (++maxCode);
    }

    private Integer convertCompanyType(String type) {
        switch (type) {
            case "门店": {
                return 2;
                // break;
            }
            case "连锁": {
                return 3;
                // break;
            }
            default: {
                throw new BaseException(-1, "门店类型错误，请填写【门店】或【连锁】");
            }
        }
    }

    private Integer convertCompanyExchangeType(String exchangeType) {
        switch (exchangeType) {
            case "换机模式": {
                return 3;
                // break;
            }
            case "一键更新": {
                return 4;
                // break;
            }
            default: {
                throw new BaseException(-1, "拉新模式错误，请填写【换机模式】或【一键更新】");
            }
        }
    }

    private void checkDuplicateContactMobile(List<CompanyCreateDTO> list) {
        Set<String> mobiles = new HashSet<>();
        for (CompanyCreateDTO o : list) {
            if (!mobiles.add(o.getContactMobile())) {
                throw new BaseException(-1, StrUtil.format("存在相同的门店负责人手机号: {}, 请检查导入的文件", o.getContactMobile()));
            }
        }
    }

    private void checkDuplicateEmployeeMobile(List<EmployeeCreateDTO> list) {
        Set<String> mobiles = new HashSet<>();
        for (EmployeeCreateDTO o : list) {
            if (!mobiles.add(o.getEmployeeMobile())) {
                throw new BaseException(-1, StrUtil.format("存在相同的员工手机号: {}, 请检查导入的文件", o.getEmployeeMobile()));
            }
        }
    }

    public Company getByMobile(String mobile) {
        List<Company> list = companyService.lambdaQuery()
                .eq(Company::getContactMobile, mobile)
                .in(Company::getStatus, Arrays.asList(CompanyStatus.TO_AUDIT.getCode(), CompanyStatus.NORMAL.getCode(), CompanyStatus.OFFLINE.getCode()))
                .list();
        return CollUtil.isEmpty(list) ? null : list.get(0);
    }
}