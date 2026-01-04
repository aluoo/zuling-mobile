package com.zxtx.hummer.em.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zxtx.hummer.em.domain.Employee;
import com.zxtx.hummer.em.domain.EmployeeExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface EmployeeMapper extends BaseMapper<Employee> {
    long countByExample(EmployeeExample example);

    int deleteByExample(EmployeeExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Employee record);

    int insertSelective(Employee record);

    List<Employee> selectByExampleWithRowbounds(EmployeeExample example, RowBounds rowBounds);

    List<Employee> selectByExample(EmployeeExample example);

    Employee selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Employee record, @Param("example") EmployeeExample example);

    int updateByExample(@Param("record") Employee record, @Param("example") EmployeeExample example);

    int updateByPrimaryKeySelective(Employee record);

    int updateByPrimaryKey(Employee record);

    void updateIsLeaf(@Param("id") Long id, @Param("isLeaf") Boolean isLeaf);

    /**
     * 根据员工层级,找对应的区域经理BD角色
     *
     * @param ancestors
     * @return
     */
    Employee selectBdByAncestors(String ancestors);
}