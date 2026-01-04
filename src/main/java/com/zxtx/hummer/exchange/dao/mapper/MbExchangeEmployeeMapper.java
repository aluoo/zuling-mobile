package com.zxtx.hummer.exchange.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zxtx.hummer.exchange.domain.MbExchangeEmployee;
import com.zxtx.hummer.exchange.req.ExchangeEmployeeInfoReq;
import com.zxtx.hummer.exchange.vo.AgencyCalVO;
import com.zxtx.hummer.exchange.vo.ExchangeEmployeeInfoVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 合伙人换机包 Mapper 接口
 * </p>
 *
 * @author L
 * @since 2024-03-27
 */
public interface MbExchangeEmployeeMapper extends BaseMapper<MbExchangeEmployee> {


    List<ExchangeEmployeeInfoVO> selectByParam(@Param("req") ExchangeEmployeeInfoReq req);

    AgencyCalVO getByAncestor(String ancestors);

}
