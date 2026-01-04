package com.zxtx.hummer.insurance.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zxtx.hummer.insurance.domain.DiProductInsurancePrice;
import com.zxtx.hummer.insurance.dao.mapper.DiProductInsurancePriceMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxtx.hummer.insurance.vo.ProductInsurancePriceVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 数保产品售价价格区间设置 服务实现类
 * </p>
 *
 * @author L
 * @since 2024-06-04
 */
@Service
public class DiProductInsurancePriceService extends ServiceImpl<DiProductInsurancePriceMapper, DiProductInsurancePrice> {

    public List<DiProductInsurancePrice> ListByInsurance(Long insuranceId){
        return this.list(Wrappers.lambdaQuery(DiProductInsurancePrice.class)
                .eq(DiProductInsurancePrice::getInsuranceId,insuranceId));
    }
}
