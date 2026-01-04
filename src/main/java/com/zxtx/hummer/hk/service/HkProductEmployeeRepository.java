package com.zxtx.hummer.hk.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxtx.hummer.common.domain.AbstractBaseEntity;
import com.zxtx.hummer.hk.dao.HkProductEmployeeMapper;
import com.zxtx.hummer.hk.domain.HkProductEmployee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author WangWJ
 * @Description
 * @Date 2025/8/7
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Repository
public class HkProductEmployeeRepository extends ServiceImpl<HkProductEmployeeMapper, HkProductEmployee> {

    public HkProductEmployee getByEmployeeId(Long employeeId, Long productId) {
        return this.lambdaQuery()
                .eq(HkProductEmployee::getEmployeeId, employeeId)
                .eq(HkProductEmployee::getProductId, productId)
                .one();
    }

    public Long countByProductId(Long productId) {
        return Optional.ofNullable(this.lambdaQuery()
                .eq(HkProductEmployee::getProductId, productId)
                .eq(AbstractBaseEntity::getDeleted, false)
                .count()).orElse(0L);
    }

    public List<HkProductEmployee> listByProductId(Long productId) {
        return this.lambdaQuery()
                .eq(HkProductEmployee::getProductId, productId)
                .list();
    }

    public List<HkProductEmployee> listByCompanyId(Long companyId) {
        return this.lambdaQuery()
                .eq(HkProductEmployee::getCompanyId, companyId)
                .list();
    }
}