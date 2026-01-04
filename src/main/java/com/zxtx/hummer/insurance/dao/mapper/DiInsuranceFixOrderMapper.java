package com.zxtx.hummer.insurance.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zxtx.hummer.insurance.domain.DiInsuranceFixOrder;
import com.zxtx.hummer.insurance.domain.dto.DiFixOrderCountDTO;
import com.zxtx.hummer.insurance.domain.dto.DiFixOrderInfoDTO;
import com.zxtx.hummer.insurance.req.DiInsuranceFixReq;
import com.zxtx.hummer.insurance.vo.DiInsuranceFixOrderVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 数保报修订单 Mapper 接口
 * </p>
 *
 * @author L
 * @since 2024-06-06
 */
public interface DiInsuranceFixOrderMapper extends BaseMapper<DiInsuranceFixOrder> {

    List<DiInsuranceFixOrderVO> selectByParam(DiInsuranceFixReq req);

    List<DiFixOrderInfoDTO> listFixOrderInfo(@Param("req") DiInsuranceFixReq req);

    List<DiFixOrderCountDTO> getFixOrderCount(@Param("req") DiFixOrderCountDTO req);
}