package com.zxtx.hummer.insurance.dao.mapper;

import com.zxtx.hummer.insurance.domain.DiInsurance;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zxtx.hummer.insurance.req.DiInsuranceReq;
import com.zxtx.hummer.insurance.req.DiPackageReq;
import com.zxtx.hummer.insurance.vo.DiInsuranceVO;
import com.zxtx.hummer.insurance.vo.DiPackageVO;

import java.util.List;

/**
 * <p>
 * 保险产品表 Mapper 接口
 * </p>
 *
 * @author aycx
 * @since 2024-05-21
 */
public interface DiInsuranceMapper extends BaseMapper<DiInsurance> {

    List<DiInsuranceVO> selectByParam(DiInsuranceReq req);

}
