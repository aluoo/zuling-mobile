package com.zxtx.hummer.insurance.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zxtx.hummer.insurance.domain.DiPackage;
import com.zxtx.hummer.insurance.req.DiPackageReq;
import com.zxtx.hummer.insurance.vo.DiPackageVO;

import java.util.List;

/**
 * <p>
 * 保险套餐表 Mapper 接口
 * </p>
 *
 * @author aycx
 * @since 2024-05-21
 */
public interface DiPackageMapper extends BaseMapper<DiPackage> {

    List<DiPackageVO> selectByParam(DiPackageReq req);

}
