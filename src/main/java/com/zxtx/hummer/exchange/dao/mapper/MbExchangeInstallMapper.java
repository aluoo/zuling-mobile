package com.zxtx.hummer.exchange.dao.mapper;

import com.zxtx.hummer.exchange.domain.MbExchangeInstall;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zxtx.hummer.exchange.req.ExchangeEmployeeInfoReq;
import com.zxtx.hummer.exchange.req.ExchangeInstallReq;
import com.zxtx.hummer.exchange.vo.ExchangeEmployeeInfoVO;
import com.zxtx.hummer.exchange.vo.ExchangeInstallVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 拉新换机安装包关联表 Mapper 接口
 * </p>
 *
 * @author L
 * @since 2024-03-27
 */
public interface MbExchangeInstallMapper extends BaseMapper<MbExchangeInstall> {

    List<ExchangeInstallVO> selectByParam(@Param("req") ExchangeInstallReq req);

}
