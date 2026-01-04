package com.zxtx.hummer.exchange.dao.mapper;

import com.zxtx.hummer.exchange.domain.MbExchangePhone;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zxtx.hummer.exchange.req.ExchangeInstallReq;
import com.zxtx.hummer.exchange.vo.ExchangeInstallVO;
import com.zxtx.hummer.exchange.vo.ExchangePhoneVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 拉新换机包 Mapper 接口
 * </p>
 *
 * @author L
 * @since 2024-03-27
 */
public interface MbExchangePhoneMapper extends BaseMapper<MbExchangePhone> {

    List<ExchangePhoneVO> selectByParam(@Param("req") ExchangeInstallReq req);

}
