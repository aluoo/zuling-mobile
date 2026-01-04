package com.zxtx.hummer.commission.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zxtx.hummer.commission.domain.CommissionSettleCheck;
import com.zxtx.hummer.commission.dto.CommissionSettleCheckSumVO;
import com.zxtx.hummer.commission.dto.CommissionSettleCheckVO;
import com.zxtx.hummer.commission.req.CommissionSettleCheckReq;

import java.util.List;

/**
 * <p>
 * 系统结算单 Mapper 接口
 * </p>
 *
 * @author shenbh
 * @since 2023-03-06
 */
public interface CommissionSettleCheckMapper extends BaseMapper<CommissionSettleCheck> {

    List<CommissionSettleCheckVO> selectByParam(CommissionSettleCheckReq req);

    CommissionSettleCheckSumVO SumByParam(CommissionSettleCheckReq req);

    List<CommissionSettleCheckVO> fixByParam(CommissionSettleCheckReq req);

}