package com.zxtx.hummer.company.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zxtx.hummer.company.domain.Company;
import com.zxtx.hummer.company.vo.CompanyChainInfoDTO;
import com.zxtx.hummer.em.dao.mapper.EmployeeMapper;
import com.zxtx.hummer.em.domain.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author WangWJ
 * @Description
 * @Date 2025/8/8
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Service
public class CompanyChainInfoService {
    @Autowired
    private EmployeeMapper employeeMapper;

    public Map<Long, CompanyChainInfoDTO> buildCompanyChainInfo(List<Company> companyList) {
        // 门店负责人员工信息
        Set<Long> companyContactEmpIds = companyList.stream().map(Company::getEmployeeId).collect(Collectors.toSet());
        List<Employee> companyContactEmpList = employeeMapper.selectList(new LambdaQueryWrapper<Employee>()
                .in(Employee::getId, companyContactEmpIds));
        List<Long> chainEmployeeIds = new ArrayList<>();
        List<CompanyChainInfoDTO> chainInfoDTOList = new ArrayList<>();
        for (Employee e : companyContactEmpList) {
            chainEmployeeIds.add(e.getId());
            String[] levels = e.getAncestors().split(",");
            Long bdId = levels.length > 1 ? Long.valueOf(levels[1]) : null;
            Long areaId = levels.length > 2 ? Long.valueOf(levels[2]) : null;
            if (bdId != null) {
                chainEmployeeIds.add(bdId);
            }
            if (areaId != null) {
                chainEmployeeIds.add(areaId);
            }
            CompanyChainInfoDTO info = CompanyChainInfoDTO.builder()
                    .companyId(e.getCompanyId())
                    .employeeId(e.getId())
                    .agentId(e.getId())
                    .agentName(e.getName())
                    .areaId(areaId)
                    .bdId(bdId)
                    .build();
            chainInfoDTOList.add(info);
        }
        List<Employee> chainEmployeeList = employeeMapper.selectList(new LambdaQueryWrapper<Employee>()
                .in(Employee::getId, chainEmployeeIds));
        Map<Long, Employee> collect = chainEmployeeList.stream().collect(Collectors.toMap(Employee::getId, Function.identity()));
        chainInfoDTOList.forEach(o -> {
            o.setAreaName(o.getAreaId() != null ? Optional.ofNullable(collect.get(o.getAreaId())).map(Employee::getName).orElse(null) : null);
            o.setBdName(o.getBdId() != null ? Optional.ofNullable(collect.get(o.getBdId())).map(Employee::getName).orElse(null) : null);
        });

        return chainInfoDTOList.stream().collect(Collectors.toMap(CompanyChainInfoDTO::getEmployeeId, Function.identity()));
    }
}