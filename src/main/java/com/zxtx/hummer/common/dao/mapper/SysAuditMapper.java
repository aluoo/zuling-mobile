package com.zxtx.hummer.common.dao.mapper;

import com.zxtx.hummer.common.domain.SysAudit;
import com.zxtx.hummer.common.domain.SysAuditExample;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface SysAuditMapper {
    long countByExample(SysAuditExample example);

    int deleteByExample(SysAuditExample example);

    int deleteByPrimaryKey(Long id);

    int insert(SysAudit record);

    int insertSelective(SysAudit record);

    List<SysAudit> selectByExampleWithRowbounds(SysAuditExample example, RowBounds rowBounds);

    List<SysAudit> selectByExample(SysAuditExample example);

    SysAudit selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") SysAudit record, @Param("example") SysAuditExample example);

    int updateByExample(@Param("record") SysAudit record, @Param("example") SysAuditExample example);

    int updateByPrimaryKeySelective(SysAudit record);

    int updateByPrimaryKey(SysAudit record);
}