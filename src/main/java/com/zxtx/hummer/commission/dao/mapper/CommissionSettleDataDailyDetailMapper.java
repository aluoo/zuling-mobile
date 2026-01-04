package com.zxtx.hummer.commission.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zxtx.hummer.commission.domain.CommissionSettleDataDailyDetail;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/6/13
 */
@Repository
@Mapper
public interface CommissionSettleDataDailyDetailMapper extends BaseMapper<CommissionSettleDataDailyDetail> {
}