package com.zxtx.hummer.em.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zxtx.hummer.em.domain.EmployeeOperateLog;
import com.zxtx.hummer.em.vo.OperateLogVo;
import com.zxtx.hummer.em.vo.OperateQueryReq;

import java.util.List;

/**
 * <p>
 * 员工上下线操作日志表 Mapper 接口
 * </p>
 *
 * @author L
 * @since 2023-09-12
 */
public interface EmployeeOperateLogMapper extends BaseMapper<EmployeeOperateLog> {

    List<OperateLogVo> selectPage(OperateQueryReq req);

}
