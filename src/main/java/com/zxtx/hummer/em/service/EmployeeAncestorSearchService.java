package com.zxtx.hummer.em.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zxtx.hummer.em.dao.mapper.EmployeeMapper;
import com.zxtx.hummer.em.domain.Employee;
import com.zxtx.hummer.em.enums.EmStatus;
import com.zxtx.hummer.em.vo.SimpleEmployeeInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/10/8
 */
@Slf4j
@Service
public class EmployeeAncestorSearchService {
    @Autowired
    private EmployeeMapper employeeMapper;

    public List<SimpleEmployeeInfoVO> searchEmployeeAncestors(String mobile) {
        if (StrUtil.isBlank(mobile)) {
            return new ArrayList<>();
        }
        Employee employee = employeeMapper.selectOne(new LambdaQueryWrapper<Employee>()
                .eq(Employee::getMobileNumber, mobile)
                .ne(Employee::getStatus, EmStatus.CANCEL.getCode()));
        if (employee == null) {
            return new ArrayList<>();
        }

        List<String> ancestors = StrUtil.split(employee.getAncestors(), ",");
        if (CollUtil.isEmpty(ancestors) || ancestors.size() <= 1) {
            String employeeRole = buildEmployeeRole(employee);
            SimpleEmployeeInfoVO first = SimpleEmployeeInfoVO.builder()
                    .id(employee.getId())
                    .name(employee.getName())
                    .mobileNumber(employee.getMobileNumber())
                    .statusStr(EnumUtil.getBy(EmStatus::getCode, employee.getStatus()).getName())
                    .employeeRole(employeeRole)
                    .build();
            return Collections.singletonList(first);
        }

        List<SimpleEmployeeInfoVO> resp = new ArrayList<>();
        List<Long> ancestoreIdList = new ArrayList<>();
        ancestors.forEach(id -> ancestoreIdList.add(Long.valueOf(id)));
        List<Long> parentIds = ancestoreIdList.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        List<Employee> parents = employeeMapper.selectList(new LambdaQueryWrapper<Employee>().in(Employee::getId, parentIds));
        Map<Long, Employee> collect = parents.stream().collect(Collectors.toMap(Employee::getId, Function.identity()));
        for (Long id : parentIds) {
            Employee emp = collect.get(id);
            String employeeRole = buildEmployeeRole(emp);
            SimpleEmployeeInfoVO vo = SimpleEmployeeInfoVO.builder()
                    .id(emp.getId())
                    .name(emp.getName())
                    .mobileNumber(emp.getMobileNumber())
                    .statusStr(EnumUtil.getBy(EmStatus::getCode, emp.getStatus()).getName())
                    .employeeRole(employeeRole)
                    .build();
            resp.add(vo);
        }
        return resp;
    }

    private String buildEmployeeRole(Employee dto) {
        if (dto.getCompanyType().equals(1) && dto.getDeptType().equals(2)) {
            // 管理层级
            if (dto.getLevel().equals(1)) {
                return "合伙人";
            } else if (dto.getLevel().equals(2)) {
                return "区域经理";
            } else {
                return "代理";
            }
        }
        if (dto.getCompanyType().equals(2)) {
            // 门店层级
            if (dto.getType().equals(1) || dto.getType().equals(3)) {
                return "门店负责人";
            }
            if (dto.getType().equals(2) || dto.getType().equals(4)) {
                return "店员";
            }
        }
        if (dto.getCompanyType().equals(3)) {
            // 门店层级
            if (dto.getType().equals(1) || dto.getType().equals(3)) {
                return "连锁店负责人";
            }
            if (dto.getType().equals(2) || dto.getType().equals(4)) {
                return "店员";
            }
        }
        return "-";
    }
}