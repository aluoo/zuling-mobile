package com.zxtx.hummer.exchange.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/9/5
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExchangeOrderCommissionBackDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private String remark;
}