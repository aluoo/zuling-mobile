package com.zxtx.hummer.order.dao.mapper;

import com.zxtx.hummer.mobileStat.domain.RecycleDataDailyBase;
import com.zxtx.hummer.order.domain.MbOrderQuotePriceLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 回收商报价记录表 Mapper 接口
 * </p>
 *
 * @author L
 * @since 2024-03-07
 */
public interface MbOrderQuotePriceLogMapper extends BaseMapper<MbOrderQuotePriceLog> {

    /**
     * 统计时间段内报价时长和次数
     *
     * @param beginTime
     * @param endTime
     * @return
     */
    List<RecycleDataDailyBase> quoteCountGroupByEmployee(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

}
