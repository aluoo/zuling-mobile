package com.zxtx.hummer.commission.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zxtx.hummer.commission.domain.CommissionSettleDataDailyTotal;
import com.zxtx.hummer.mobileStat.domain.CompanyDataDailyBase;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/6/2
 */
@Repository
@Mapper
public interface CommissionSettleDataDailyTotalMapper extends BaseMapper<CommissionSettleDataDailyTotal> {

    List<CompanyDataDailyBase> commissionStatGroupByEmployee(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime);
}