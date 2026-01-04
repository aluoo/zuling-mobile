package com.zxtx.hummer.commission.req;
import com.zxtx.hummer.common.domain.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>
 * 佣金方案表
 * </p>
 *
 * @author shenbh
 * @since 2023-03-06
 */
@Data
public class CommissionTypePackageReq extends BaseEntity {

    private static final long serialVersionUID = 1L;


    private Long id;


    private Long typeId;


    private Integer type;


    private String name;


    private BigDecimal maxCommissionFee;


}
