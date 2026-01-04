package com.zxtx.hummer.commission.service;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxtx.hummer.commission.domain.OrderCommissionRule;
import com.zxtx.hummer.commission.dao.mapper.OrderCommissionRuleMapper;
import com.zxtx.hummer.commission.enums.CommissionBizType;
import com.zxtx.hummer.commission.enums.CommissionPackage;
import org.springframework.stereotype.Service;

/**
 * @author peng can
 * @date 2022/12/1
 */
@Service
public class OrderCommissionRuleService extends ServiceImpl<OrderCommissionRuleMapper, OrderCommissionRule> {


    public Boolean checkOrderRuleExists(Long orderId, CommissionBizType typeEnum, CommissionPackage packageEnum) {
        Long count = this.lambdaQuery().eq(OrderCommissionRule::getOrderId, orderId)
                .eq(OrderCommissionRule::getCommissionPackage, packageEnum.getType())
                .eq(OrderCommissionRule::getCommissionType, typeEnum.getType()).count();
        return (count == null || count == 0) ? false : true;
    }


    public OrderCommissionRule getRuleVersionByOrderId(Long orderId,CommissionBizType typeEnum, Long commissionPackageId) {
        return this.lambdaQuery().eq(OrderCommissionRule::getOrderId, orderId)
                .eq(OrderCommissionRule::getCommissionPackage, commissionPackageId)
                .eq(OrderCommissionRule::getCommissionType, typeEnum.getType()).one();
    }


}