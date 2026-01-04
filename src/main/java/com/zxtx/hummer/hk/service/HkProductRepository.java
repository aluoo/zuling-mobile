package com.zxtx.hummer.hk.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxtx.hummer.hk.dao.HkProductMapper;
import com.zxtx.hummer.hk.domain.HkProduct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * @author WangWJ
 * @Description
 * @Date 2025/8/1
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Repository
public class HkProductRepository extends ServiceImpl<HkProductMapper, HkProduct> {
}