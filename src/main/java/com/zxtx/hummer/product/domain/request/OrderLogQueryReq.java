package com.zxtx.hummer.product.domain.request;

import com.zxtx.hummer.common.domain.AbstractBaseQueryEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/10/15
 * @Copyright
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class OrderLogQueryReq extends AbstractBaseQueryEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long orderId;

}