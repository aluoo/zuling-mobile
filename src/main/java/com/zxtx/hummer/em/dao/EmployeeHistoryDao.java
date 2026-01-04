package com.zxtx.hummer.em.dao;


import com.zxtx.hummer.em.dao.mapper.EmployeeHistoryMapper;
import com.zxtx.hummer.em.domain.EmployeeHistory;
import com.zxtx.hummer.em.domain.EmployeeHistoryExample;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EmployeeHistoryDao {
    @Autowired
    private EmployeeHistoryMapper mapper;

    public int insert(EmployeeHistory record) {
        return mapper.insertSelective(record);
    }

    public EmployeeHistory getLatest(Long employeeId) {
        EmployeeHistoryExample example = new EmployeeHistoryExample();
        example.createCriteria().andUserIdEqualTo(employeeId);
        example.setOrderByClause(" login_time desc limit 1");
        List<EmployeeHistory> records = mapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(records)) {
            return records.get(0);
        }
        return null;
    }

    public int update(EmployeeHistory record) {
        return mapper.updateByPrimaryKey(record);
    }
}
