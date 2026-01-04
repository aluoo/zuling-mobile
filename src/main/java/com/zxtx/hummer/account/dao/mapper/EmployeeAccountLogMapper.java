package com.zxtx.hummer.account.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zxtx.hummer.account.domain.EmployeeAccountLog;
import com.zxtx.hummer.account.dto.EmployeeAccountLogDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 个人账户变动明细表 Mapper 接口
 * </p>
 *
 * @author shenbh
 * @since 2023-03-02
 */
public interface EmployeeAccountLogMapper extends BaseMapper<EmployeeAccountLog> {

    List<EmployeeAccountLogDTO> selectByEmployeeId(@Param("employeeId") Long employeeId);
}
