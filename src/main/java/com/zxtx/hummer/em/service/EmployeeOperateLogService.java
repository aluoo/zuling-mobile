package com.zxtx.hummer.em.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.zxtx.hummer.em.dao.mapper.EmployeeOperateLogMapper;
import com.zxtx.hummer.em.domain.EmployeeOperateLog;
import com.zxtx.hummer.em.vo.OperateLogVo;
import com.zxtx.hummer.em.vo.OperateQueryReq;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 员工上下线操作日志表 服务类
 * </p>
 *
 * @author L
 * @since 2023-09-12
 */
@Service
public class EmployeeOperateLogService extends ServiceImpl<EmployeeOperateLogMapper, EmployeeOperateLog> {

    public List<OperateLogVo> selectPage(OperateQueryReq req) {
        PageHelper.startPage(req.getPage(), req.getPageSize());
        List<OperateLogVo> resultList = this.getBaseMapper().selectPage(req);
        return resultList;
    }

}
