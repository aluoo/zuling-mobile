package com.zxtx.hummer.company.dao.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zxtx.hummer.company.domain.RecycleRechargeLog;
import com.zxtx.hummer.company.req.RecycleRechargeReq;
import com.zxtx.hummer.company.vo.RecycleRechargeVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RecycleRechargeLogMapper extends BaseMapper<RecycleRechargeLog> {

    List<RecycleRechargeVo> selectByParam(@Param("req") RecycleRechargeReq req);

}