package com.zxtx.hummer.exchange.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.zxtx.hummer.account.domain.EmployeeAccount;
import com.zxtx.hummer.account.service.IEmployeeAccountService;
import com.zxtx.hummer.common.utils.StringUtils;
import com.zxtx.hummer.em.dao.mapper.EmployeeMapper;
import com.zxtx.hummer.em.domain.Employee;
import com.zxtx.hummer.em.enums.EmStatus;
import com.zxtx.hummer.exchange.dao.mapper.MbExchangeEmployeeMapper;
import com.zxtx.hummer.exchange.domain.MbExchangeEmployee;
import com.zxtx.hummer.exchange.domain.MbExchangeEmployeeInfo;
import com.zxtx.hummer.exchange.domain.MbExchangeVerifyEmployee;
import com.zxtx.hummer.exchange.domain.dto.ExchangeEmployeePackageDTO;
import com.zxtx.hummer.exchange.domain.dto.ExchangeEmployeeUpdateDTO;
import com.zxtx.hummer.exchange.domain.dto.ExchangeMemberAddDTO;
import com.zxtx.hummer.exchange.req.ExchangeEmployeeInfoReq;
import com.zxtx.hummer.exchange.vo.AgencyCalVO;
import com.zxtx.hummer.exchange.vo.ExchangeEmployeeInfoVO;
import com.zxtx.hummer.exchange.vo.ExchangeEmployeeViewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>
 * 合伙人换机包 服务实现类
 * </p>
 *
 * @author L
 * @since 2024-03-27
 */
@Service
public class MbExchangeEmployeeService extends ServiceImpl<MbExchangeEmployeeMapper, MbExchangeEmployee>{

    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private MbExchangeEmployeeInfoService mbExchangeEmployeeInfoService;
    @Autowired
    IEmployeeAccountService employeeAccountService;
    @Autowired
    private MbExchangePhoneService mbExchangePhoneService;


    public List<ExchangeEmployeeInfoVO> selectPage(ExchangeEmployeeInfoReq req) {

        if(StrUtil.isNotBlank(req.getBdPhone())){
            Employee bdEmployee = employeeMapper.selectOne(Wrappers.lambdaQuery(Employee.class).eq(Employee::getStatus,1)
                    .eq(Employee::getMobileNumber,req.getBdPhone()));
            req.setBdAncestor(Optional.ofNullable(bdEmployee).map(Employee::getAncestors).orElse(null));
        }

        PageHelper.startPage(req.getPage(), req.getPageSize());
        List<ExchangeEmployeeInfoVO> resultList = this.getBaseMapper().selectByParam(req);
        if (CollUtil.isNotEmpty(resultList)) {
            for (ExchangeEmployeeInfoVO applyVo : resultList) {
                //bd相关信息
                Employee bd = employeeMapper.selectBdByAncestors(applyVo.getAncestors());
                applyVo.setBdId(Optional.ofNullable(bd).map(Employee::getId).orElse(null));
                applyVo.setBdName(Optional.ofNullable(bd).map(Employee::getName).orElse(null));
                applyVo.setBdPhone(Optional.ofNullable(bd).map(Employee::getMobileNumber).orElse(null));
                applyVo.setStatusStr(EmStatus.getNameByCode(applyVo.getStatus().byteValue()));
                applyVo.setAbleBalanceStr(applyVo.getAbleBalance()!=null?StringUtils.fenToYuan(applyVo.getAbleBalance()):"0");
                applyVo.setLevelStr(buildLevel(applyVo.getLevel()));
            }
        }
        return resultList;
    }

    public ExchangeEmployeeViewVO view(Long employeeId){
        ExchangeEmployeeViewVO resultVo = new ExchangeEmployeeViewVO();
        Employee employee = employeeMapper.selectById(employeeId);
        String[] employees = employee.getAncestors().split(",");
        AgencyCalVO agencyCalVO = this.baseMapper.getByAncestor(employee.getAncestors());
        MbExchangeEmployeeInfo employeeInfo = mbExchangeEmployeeInfoService.getByEmployeeId(employeeId);
        EmployeeAccount employeeAccount = employeeAccountService.getByEmployeeId(employeeId);

        List<MbExchangeEmployee> packageList = this.list(Wrappers.lambdaQuery(MbExchangeEmployee.class).eq(MbExchangeEmployee::getEmployeeId,employeeId));

        if(CollUtil.isNotEmpty(packageList)){
            List<Long> ids = packageList.stream().map(MbExchangeEmployee::getExchangePhoneId).collect(Collectors.toList());
            resultVo.setPackageList(mbExchangePhoneService.listByIds(ids));
        }

        resultVo.setEmployeeId(employee.getId());
        resultVo.setEmployeeName(employee.getName());
        resultVo.setEmployeePhone(employee.getMobileNumber());
        resultVo.setCompanyNum(agencyCalVO.getCompanyNum());
        resultVo.setStaffNum(agencyCalVO.getStaffNum());
        resultVo.setAbleBalance(Optional.ofNullable(employeeAccount).map(EmployeeAccount::getAbleBalance).orElse(null));
        resultVo.setBdName(employeeMapper.selectById(employees[1]).getName());
        resultVo.setIdCard(Optional.ofNullable(employeeInfo).map(MbExchangeEmployeeInfo::getIdCard).orElse(null));
        resultVo.setLevel(employee.getLevel());
        resultVo.setAddress(Optional.ofNullable(employeeInfo).map(MbExchangeEmployeeInfo::getAddress).orElse(null));
        resultVo.setBusiness(Optional.ofNullable(employeeInfo).map(MbExchangeEmployeeInfo::getBusiness).orElse(null));
        resultVo.setPlatCheck(Optional.ofNullable(employeeInfo).map(MbExchangeEmployeeInfo::getPlatCheck).orElse(null));

        return resultVo;
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(ExchangeEmployeeUpdateDTO dto){
        Employee employee = employeeMapper.selectById(dto.getEmployeeId());
        employee.setName(dto.getEmployeeName());
        employeeMapper.updateById(employee);
        MbExchangeEmployeeInfo employeeInfo = mbExchangeEmployeeInfoService.getByEmployeeId(dto.getEmployeeId());
        if(ObjectUtil.isNull(employeeInfo)){
            employeeInfo = new MbExchangeEmployeeInfo();
            employeeInfo.setEmployeeId(dto.getEmployeeId());
            employeeInfo.setIdCard(dto.getIdCard());
            employeeInfo.setAddress(dto.getAddress());
            employeeInfo.setBusiness(dto.getBusiness());
            employeeInfo.setPlatCheck(dto.getPlatCheck());
            mbExchangeEmployeeInfoService.save(employeeInfo);
            return;
        }
        employeeInfo.setIdCard(dto.getIdCard());
        employeeInfo.setAddress(dto.getAddress());
        employeeInfo.setBusiness(dto.getBusiness());
        employeeInfo.setPlatCheck(dto.getPlatCheck());
        mbExchangeEmployeeInfoService.updateById(employeeInfo);

    }

    @Transactional(rollbackFor = Exception.class)
    public void updatePackage(ExchangeEmployeePackageDTO dto){
        //删除旧的换机包
        this.remove(Wrappers.lambdaQuery(MbExchangeEmployee.class).eq(MbExchangeEmployee::getEmployeeId,dto.getEmployeeId()));
        List<MbExchangeEmployee> addList = new ArrayList<>();
        dto.getExchangePhoneIds().stream().forEach(e -> {
            MbExchangeEmployee mbExchangeEmployee = new MbExchangeEmployee();
            mbExchangeEmployee.setEmployeeId(dto.getEmployeeId());
            mbExchangeEmployee.setExchangePhoneId(e);
            addList.add(mbExchangeEmployee);
        });
        this.saveBatch(addList);
    }

    @Transactional(rollbackFor = Exception.class)
    public void addEmployee(ExchangeMemberAddDTO dto){
        //删除旧的换机包
        this.remove(Wrappers.lambdaQuery(MbExchangeEmployee.class).eq(MbExchangeEmployee::getExchangePhoneId,dto.getId()));
        List<MbExchangeEmployee> addList = new ArrayList<>();
        dto.getEmployeeIds().stream().forEach(e -> {
            MbExchangeEmployee mbExchangeEmployee = new MbExchangeEmployee();
            mbExchangeEmployee.setEmployeeId(e);
            mbExchangeEmployee.setExchangePhoneId(dto.getId());
            addList.add(mbExchangeEmployee);
        });
        this.saveBatch(addList);
    }

    private String buildLevel(Integer level){
        if(level == 1){
            return "合伙人";
        }else if(level == 2){
            return "区域经理";
        }else{
            return "代理";
        }
    }

    public MbExchangeEmployee getByEmployeeId(Long employeeId){
        return this.lambdaQuery()
                .eq(MbExchangeEmployee::getEmployeeId,employeeId).one();
    }

    public List<MbExchangeEmployee> getByInstallId(Long installId){
        return this.lambdaQuery()
                .eq(MbExchangeEmployee::getExchangePhoneId,installId).list();
    }


}
