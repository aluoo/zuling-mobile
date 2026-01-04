package com.zxtx.hummer.hk.service;

import com.zxtx.hummer.common.domain.AbstractBaseEntity;
import com.zxtx.hummer.hk.domain.HkOperator;
import com.zxtx.hummer.hk.domain.HkSupplier;
import com.zxtx.hummer.hk.domain.enums.HkApplyOrderStatusEnum;
import com.zxtx.hummer.hk.domain.response.HkCommonPropertiesVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author WangWJ
 * @Description
 * @Date 2025/8/1
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Service
public class HkCommonService {
    @Autowired
    private HkOperatorRepository hkOperatorRepository;
    @Autowired
    private HkSupplierRepository hkSupplierRepository;

    public HkCommonPropertiesVO commonPropertiesInfo() {
        Map<Integer, String> productStatus = new HashMap<>(2);
        productStatus.put(0, "下架");
        productStatus.put(1, "在售");

        Map<Integer, String> orderStatus = new HashMap<>(32);
        for (HkApplyOrderStatusEnum e : HkApplyOrderStatusEnum.values()) {
            orderStatus.put(e.getCode(), e.getDesc());
        }
        return HkCommonPropertiesVO.builder()
                .supplier(this.getSupplier())
                .operator(this.getOperator())
                .productStatus(productStatus)
                .orderStatus(orderStatus)
                .build();
    }

    public Map<Long, String> getOperator() {
        return hkOperatorRepository.lambdaQuery()
                .eq(AbstractBaseEntity::getDeleted, false)
                .eq(HkOperator::getStatus, 1)
                .orderByDesc(HkOperator::getSort)
                .orderByDesc(HkOperator::getCreateTime)
                .orderByDesc(HkOperator::getUpdateTime)
                .list().stream().collect(Collectors.toMap(HkOperator::getId, HkOperator::getName));
    }

    public Map<Long, String> getSupplier() {
        return hkSupplierRepository.lambdaQuery()
                .eq(AbstractBaseEntity::getDeleted, false)
                .eq(HkSupplier::getStatus, 1)
                .orderByDesc(HkSupplier::getSort)
                .orderByDesc(HkSupplier::getCreateTime)
                .orderByDesc(HkSupplier::getUpdateTime)
                .list().stream().collect(Collectors.toMap(HkSupplier::getId, HkSupplier::getName));
    }
}