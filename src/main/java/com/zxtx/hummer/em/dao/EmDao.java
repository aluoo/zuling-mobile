package com.zxtx.hummer.em.dao;

import com.zxtx.hummer.em.dao.mapper.EmployeeMapper;
import com.zxtx.hummer.em.dao.mapper.ExtEmMapper;
import com.zxtx.hummer.em.domain.Employee;
import com.zxtx.hummer.em.domain.EmployeeExample;
import com.zxtx.hummer.em.enums.EmStatus;
import com.zxtx.hummer.system.shiro.LoginUser;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public class EmDao {

    @Autowired
    private EmployeeMapper mapper;

    @Autowired
    private ExtEmMapper extEmMapper;

    public Employee getByMobile(String mobile) {
        return getByMobile(mobile, EmStatus.NORMAL.getCode());
    }


    public Employee getByMobile(String mobile, byte status) {
        return getByMobile(mobile, Collections.singletonList(status));
    }


    public Employee getByMobile(String mobile, List<Byte> lsStatus) {

        EmployeeExample example = new EmployeeExample();
        example.createCriteria().andMobileNumberEqualTo(mobile).andStatusIn(lsStatus);
        List<Employee> records = mapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(records)) {
            return records.get(0);
        }
        return null;
    }


    public Employee getManager(Long deptId, int emType) {
        EmployeeExample exp = new EmployeeExample();
        exp.createCriteria().andDeptIdEqualTo(deptId).andTypeEqualTo(emType).andStatusEqualTo(EmStatus.NORMAL.getCode());
        exp.setOrderByClause("id asc limit 1");
        List<Employee> employees = mapper.selectByExample(exp);
        if (CollectionUtils.isNotEmpty(employees)) {
            return employees.get(0);
        } else {
            return null;
        }
    }

    public void offlineChannelEms(String code, LoginUser user) {
        Employee employee = new Employee();
        employee.setStatus(EmStatus.OFFLINE.getCode());
        employee.setUpdator(user.getName());
        employee.setUpdateTime(new Date());
        extEmMapper.changeChannelEmStatus(code, employee, EmStatus.NORMAL.getCode());
    }

    public void cancelChannelEms(String code, LoginUser user) {
        Employee employee = new Employee();
        employee.setStatus(EmStatus.CANCEL.getCode());
        employee.setUpdator(user.getName());
        employee.setUpdateTime(new Date());
        extEmMapper.changeChannelEmStatus(code, employee, EmStatus.NORMAL.getCode());
    }


    public void onlineChannelEms(String code, LoginUser user) {
        Employee employee = new Employee();
        employee.setStatus(EmStatus.NORMAL.getCode());
        employee.setUpdator(user.getName());
        employee.setUpdateTime(new Date());
        extEmMapper.changeChannelEmStatus(code, employee, EmStatus.OFFLINE.getCode());
    }

    public Employee getById(long id) {
        return mapper.selectByPrimaryKey(id);
    }


    public void updateDeptCode(List cmpIds, String deptCode, Date updateTime, String updator, Long newCmpId, Integer newCompanyType) {
        extEmMapper.updateDeptCode(cmpIds, deptCode, updateTime, updator, newCmpId, newCompanyType);
    }

    public List<Map<String, String>> queryTokenByDeptCode(String deptCode, Date currDate) {
        return extEmMapper.queryTokenByDeptCode(deptCode, currDate);
    }

}
