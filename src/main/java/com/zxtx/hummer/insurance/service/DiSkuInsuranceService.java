package com.zxtx.hummer.insurance.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.zxtx.hummer.insurance.dao.mapper.DiSkuInsuranceMapper;
import com.zxtx.hummer.insurance.dao.mapper.DiTypeMapper;
import com.zxtx.hummer.insurance.domain.DiSkuInsurance;
import com.zxtx.hummer.insurance.domain.DiType;
import com.zxtx.hummer.insurance.req.DiTypeDTO;
import com.zxtx.hummer.insurance.req.DiTypeReq;
import com.zxtx.hummer.insurance.vo.DiTypeVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 数保SKU产品价格配置表
 * </p>
 *
 * @author aycx
 * @since 2024-05-21
 */
@Service
public class DiSkuInsuranceService extends ServiceImpl<DiSkuInsuranceMapper, DiSkuInsurance> {

}
