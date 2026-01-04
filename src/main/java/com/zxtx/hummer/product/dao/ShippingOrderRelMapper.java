package com.zxtx.hummer.product.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zxtx.hummer.product.domain.ShippingOrderRel;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/3/20
 * @Copyright
 * @Version 1.0
 */
@Mapper
@Repository
public interface ShippingOrderRelMapper extends BaseMapper<ShippingOrderRel> {
}