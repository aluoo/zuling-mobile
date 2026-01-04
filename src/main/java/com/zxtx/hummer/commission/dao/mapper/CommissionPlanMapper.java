package com.zxtx.hummer.commission.dao.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zxtx.hummer.commission.domain.CommissionPlan;
import com.zxtx.hummer.commission.dto.EmployeeCommissionPlanInfoDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 佣金方案表 Mapper 接口
 * </p>
 */
public interface CommissionPlanMapper extends BaseMapper<CommissionPlan> {

    List<EmployeeCommissionPlanInfoDTO> getEmployeeCommissionPlanInfo(@Param("employeeId") Long employeeId);

}
