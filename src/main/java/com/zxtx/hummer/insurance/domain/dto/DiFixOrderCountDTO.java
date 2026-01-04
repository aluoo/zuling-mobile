package com.zxtx.hummer.insurance.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/9/23
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiFixOrderCountDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long orderId;
    private Integer count;

    private Collection<Long> orderIds;
}