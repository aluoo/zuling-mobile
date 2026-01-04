package com.zxtx.hummer.insurance.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zxtx.hummer.insurance.domain.DiInsuranceOrder;
import com.zxtx.hummer.insurance.req.DiInsuranceOrderReq;
import com.zxtx.hummer.insurance.vo.DiInsuranceOrderVO;
import com.zxtx.hummer.mobileStat.domain.CompanyDataDailyBase;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 数保订单表 Mapper 接口
 * </p>
 *
 * @author L
 * @since 2024-06-06
 */
public interface DiInsuranceOrderMapper extends BaseMapper<DiInsuranceOrder> {

    List<DiInsuranceOrderVO> selectByParam(DiInsuranceOrderReq req);

    List<CompanyDataDailyBase> statGroupByEmployee(@Param("beginTime") Date beginTime, @Param("endTime") Date endTim,@Param("insuranceType") String insuranceType);

}
