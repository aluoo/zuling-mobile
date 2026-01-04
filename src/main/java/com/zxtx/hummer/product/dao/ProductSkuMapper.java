package com.zxtx.hummer.product.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zxtx.hummer.product.domain.ProductSku;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/6/4
 * @Copyright
 * @Version 1.0
 */
@Mapper
@Repository
public interface ProductSkuMapper extends BaseMapper<ProductSku> {
}