package com.zxtx.hummer.order.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zxtx.hummer.mobileStat.domain.CompanyDataDailyBase;
import com.zxtx.hummer.mobileStat.domain.RecycleDataDailyBase;
import com.zxtx.hummer.order.domain.MbOrder;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 报价订单表 Mapper 接口
 * </p>
 *
 * @author L
 * @since 2024-03-07
 */
public interface MbOrderMapper extends BaseMapper<MbOrder> {

    /**
     * 服务商数据看板 每日交易数量交易额统计
     *
     * @param beginTime
     * @param endTime
     * @return
     */
    List<RecycleDataDailyBase> tranStatGroupByEmployee(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    /**
     * 服务商数据看板 每日确认收货统计
     *
     * @param beginTime
     * @param endTime
     * @return
     */
    List<RecycleDataDailyBase> receiptStatGroupByEmployee(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    /**
     * 服务商数据看板 每日退款交易额统计
     *
     * @param beginTime
     * @param endTime
     * @return
     */
    List<RecycleDataDailyBase> refundGroupByEmployee(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime);


    /**
     * 门店数据看板 每日回收数量
     *
     * @param beginTime
     * @param endTime
     * @return
     */
    List<CompanyDataDailyBase> companyTransStatGroupByEmployee(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    /**
     * 门店数据看板 每日询价数量
     *
     * @param beginTime
     * @param endTime
     * @return
     */
    List<CompanyDataDailyBase> companyOrderStatGroupByEmployee(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    /**
     * 门店数据看板 每日报价数量
     *
     * @param beginTime
     * @param endTime
     * @return
     */
    List<CompanyDataDailyBase> priceOrderStatGroupByEmployee(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    /**
     * 门店数据看板 每日取消数量
     *
     * @param beginTime
     * @param endTime
     * @return
     */
    List<CompanyDataDailyBase> cancelStatGroupByEmployee(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    /**
     * 门店数据看板 每日作废数量
     *
     * @param beginTime
     * @param endTime
     * @return
     */
    List<CompanyDataDailyBase> overTimeStatGroupByEmployee(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime);


}
