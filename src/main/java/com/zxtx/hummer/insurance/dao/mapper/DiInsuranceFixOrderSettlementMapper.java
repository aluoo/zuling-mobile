package com.zxtx.hummer.insurance.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zxtx.hummer.insurance.domain.DiInsuranceFixOrderSettlement;
import com.zxtx.hummer.insurance.domain.dto.DiInsuranceFixOrderSettlementDTO;
import com.zxtx.hummer.insurance.req.DiInsuranceFixOrderSettlementQueryReq;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/10/17
 * @Copyright
 * @Version 1.0
 */
@Mapper
@Repository
public interface DiInsuranceFixOrderSettlementMapper extends BaseMapper<DiInsuranceFixOrderSettlement> {

    List<DiInsuranceFixOrderSettlementDTO> listSettlement(@Param("req") DiInsuranceFixOrderSettlementQueryReq req);
}