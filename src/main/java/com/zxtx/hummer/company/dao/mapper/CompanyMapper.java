package com.zxtx.hummer.company.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zxtx.hummer.company.domain.Company;
import com.zxtx.hummer.company.domain.CompanyExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface CompanyMapper extends BaseMapper<Company> {
    long countByExample(CompanyExample example);

    int deleteByExample(CompanyExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Company record);

    List<Company> selectByExampleWithRowbounds(CompanyExample example, RowBounds rowBounds);

    List<Company> selectByExample(CompanyExample example);

    Company selectByPrimaryKey(Long id);

    void updateInvoiceAble(@Param("id") Long id, @Param("invoiceAble") boolean invoiceAble);
}