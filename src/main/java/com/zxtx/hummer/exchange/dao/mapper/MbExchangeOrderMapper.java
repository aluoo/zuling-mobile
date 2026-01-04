package com.zxtx.hummer.exchange.dao.mapper;

import com.zxtx.hummer.exchange.domain.MbExchangeOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zxtx.hummer.exchange.req.ExchangeInstallReq;
import com.zxtx.hummer.exchange.req.ExchangeOrderReq;
import com.zxtx.hummer.exchange.vo.ExchangeOrderSummaryVO;
import com.zxtx.hummer.exchange.vo.ExchangeOrderVO;
import com.zxtx.hummer.exchange.vo.ExchangePhoneVO;
import com.zxtx.hummer.mobileStat.domain.CompanyDataDailyBase;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 换机晒单表 Mapper 接口
 * </p>
 *
 * @author L
 * @since 2024-03-27
 */
public interface MbExchangeOrderMapper extends BaseMapper<MbExchangeOrder> {

    List<ExchangeOrderVO> selectByParam(@Param("req") ExchangeOrderReq req);
    ExchangeOrderSummaryVO orderSummary(@Param("req") ExchangeOrderReq req);

    List<CompanyDataDailyBase> statAllGroupByEmployee(@Param("beginTime") Date beginTime, @Param("endTime") Date endTim);

    List<CompanyDataDailyBase> statPassGroupByEmployee(@Param("beginTime") Date beginTime, @Param("endTime") Date endTim);
}
