package com.zxtx.hummer.dept.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zxtx.hummer.dept.domain.Dept;
import com.zxtx.hummer.dept.domain.DeptExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface DeptMapper extends BaseMapper<Dept> {
    long countByExample(DeptExample example);

    int deleteByExample(DeptExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Dept record);

    int insertSelective(Dept record);

    List<Dept> selectByExampleWithRowbounds(DeptExample example, RowBounds rowBounds);

    List<Dept> selectByExample(DeptExample example);

    Dept selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Dept record, @Param("example") DeptExample example);

    int updateByExample(@Param("record") Dept record, @Param("example") DeptExample example);

    int updateByPrimaryKeySelective(Dept record);

    int updateByPrimaryKey(Dept record);
}