package com.zxtx.hummer.insurance.dao.mapper;

import com.zxtx.hummer.company.req.RecycleRechargeReq;
import com.zxtx.hummer.company.vo.RecycleRechargeVo;
import com.zxtx.hummer.insurance.domain.DiCompanyRechargeLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 数保门店账户充值记录表 Mapper 接口
 * </p>
 *
 * @author aycx
 * @since 2024-05-21
 */
public interface DiCompanyRechargeLogMapper extends BaseMapper<DiCompanyRechargeLog> {

    List<RecycleRechargeVo> selectByParam(@Param("req") RecycleRechargeReq req);

}
