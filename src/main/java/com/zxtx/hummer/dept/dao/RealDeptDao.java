package com.zxtx.hummer.dept.dao;

import com.zxtx.hummer.dept.dao.mapper.ExtDeptMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RealDeptDao {
    @Autowired
    private ExtDeptMapper extDeptMapper;

    public Integer selectMaxCode(Long pdeptId) {
        Integer i = extDeptMapper.selectMaxCode(pdeptId);
        if (i == null) {
            i = 0;
        }
        return i;
    }
}
