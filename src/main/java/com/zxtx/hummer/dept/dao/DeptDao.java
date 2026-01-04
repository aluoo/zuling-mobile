package com.zxtx.hummer.dept.dao;

import com.zxtx.hummer.dept.dao.mapper.DeptMapper;
import com.zxtx.hummer.dept.dao.mapper.ExtDeptMapper;
import com.zxtx.hummer.dept.domain.Dept;
import com.zxtx.hummer.dept.domain.DeptExample;
import com.zxtx.hummer.dept.enums.DeptStatus;
import com.zxtx.hummer.dept.enums.DeptType;
import com.zxtx.hummer.dept.vo.DeptListRs;
import com.zxtx.hummer.system.shiro.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class DeptDao {

    @Autowired
    private ExtDeptMapper extDeptMapper;
    @Autowired
    private DeptMapper deptMapper;

/*
    public List<DeptListRs> getAllChildDepts(Long companyId, String deptCode) {
        return extDeptMapper.getAllChildDepts(companyId, deptCode);
    }

    public List<DeptListRs> getChildDepts(Long companyId, Long deptId) {
        return extDeptMapper.getChildDepts(companyId, deptId);
    }*/

    public Dept getByName(Long companyId, String name) {
        DeptExample deptExample = new DeptExample();
        DeptExample.Criteria cri = deptExample.createCriteria();
        cri.andNameEqualTo(name).andStatusEqualTo(DeptStatus.NORMAL.getCode());
        cri.andCompanyIdEqualTo(companyId);
        List<Dept> depts = deptMapper.selectByExample(deptExample);
        if (depts.size() > 0) {
            return depts.get(0);
        }
        return null;
    }

    public Integer selectMaxCode(Long pdeptId) {
        Integer i = extDeptMapper.selectMaxCode(pdeptId);
        if (i == null) {
            i = 0;
        }
        return i;
    }

    public List<DeptListRs> getAllChildDepts(Long companyId, String deptCode) {
        return extDeptMapper.getAllChildDepts(companyId, deptCode);
    }

    public List<DeptListRs> getAllStatusChildDepts(Long companyId, String deptCode) {
        return extDeptMapper.getAllStatusChildDepts(companyId, deptCode);
    }

    public List<DeptListRs> getChildDepts(Long companyId, Long deptId) {
        return extDeptMapper.getChildDepts(companyId, deptId);
    }

/*
    public int queryTotalEm(String deptCode) {
        String childDeptCode = deptCode + "-";
        return extDeptMapper.queryTotalEm(deptCode, childDeptCode);
    }*/
/*
    public Dept getManagerDept(Long companyId) {
        return getManagerDept(companyId,DeptStatus.NORMAL.getCode());
    }


    public Dept getManagerDept(Long companyId,List<Integer> lsStatus) {
        DeptExample deptExample = new DeptExample();
        deptExample.createCriteria().andCompanyIdEqualTo(companyId)
                .andStatusIn(status)
                .andTypeEqualTo(DeptType.MANGER.getCode());
        List<Dept> depts = deptMapper.selectByExample(deptExample);
        if (depts.size() > 0){
            return depts.get(0);
        }
        return null;
    }*/

    public Dept getManagerDept(Long companyId, List<Integer> lsStatus) {
        DeptExample deptExample = new DeptExample();
        deptExample.createCriteria().andCompanyIdEqualTo(companyId)
                .andStatusIn(lsStatus)
                .andTypeEqualTo(DeptType.MANGER.getCode());
        List<Dept> depts = deptMapper.selectByExample(deptExample);
        if (depts.size() > 0) {
            return depts.get(0);
        }
        return null;
    }


    public void offlineChannelDept(String code, LoginUser user) {
        Dept dept = new Dept();
        dept.setUpdator(user.getName());
        dept.setUpdateTime(new Date());
        dept.setStatus(DeptStatus.OFFLINE.getCode());
        extDeptMapper.changeChannelDeStatus(code, dept, DeptStatus.NORMAL.getCode());
    }

    public void cancelChannelDept(String code, LoginUser user) {
        Dept dept = new Dept();
        dept.setUpdator(user.getName());
        dept.setUpdateTime(new Date());
        dept.setStatus(DeptStatus.CANCEL.getCode());
        extDeptMapper.changeChannelDeStatus(code, dept, DeptStatus.NORMAL.getCode());
    }


    public void onlineChannelDept(String code, LoginUser user) {
        Dept dept = new Dept();
        dept.setUpdator(user.getName());
        dept.setUpdateTime(new Date());
        dept.setStatus(DeptStatus.NORMAL.getCode());
        extDeptMapper.changeChannelDeStatus(code, dept, DeptStatus.OFFLINE.getCode());
    }


    public List<Long> queryCmpIdByCode(String code) {
        return extDeptMapper.queryCmpIdByCode(code);
    }


    public void updateChildsCode(String newPcode, String oldPcode, List<Long> cmpIds, Date updateTime, String updator, Long newCmpId, Integer newCompanyType) {
        extDeptMapper.updateChildsCode(newPcode, oldPcode, cmpIds, updateTime, updator, newCmpId, newCompanyType);
    }

}
