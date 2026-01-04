package com.zxtx.hummer.insurance.dao.mapper;

import com.zxtx.hummer.insurance.domain.DiCompanyAccount;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zxtx.hummer.insurance.domain.DiCompanyAccountLog;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 数保门店账户表 Mapper 接口
 * </p>
 *
 * @author aycx
 * @since 2024-05-21
 */
public interface DiCompanyAccountMapper extends BaseMapper<DiCompanyAccount> {

    int changeAccountBalance(@Param("employeeAccountLog") DiCompanyAccountLog employeeAccountLog);

}
