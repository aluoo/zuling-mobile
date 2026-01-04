package com.zxtx.hummer.insurance.dao.mapper;

import com.zxtx.hummer.exchange.req.ExchangeInstallReq;
import com.zxtx.hummer.exchange.vo.ExchangeInstallVO;
import com.zxtx.hummer.insurance.domain.DiType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zxtx.hummer.insurance.req.DiTypeReq;
import com.zxtx.hummer.insurance.vo.DiTypeVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 险种类型表 Mapper 接口
 * </p>
 *
 * @author aycx
 * @since 2024-05-21
 */
public interface DiTypeMapper extends BaseMapper<DiType> {

    List<DiTypeVO> selectByParam(DiTypeReq req);
}
