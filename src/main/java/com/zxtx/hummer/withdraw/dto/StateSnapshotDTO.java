package com.zxtx.hummer.withdraw.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * 描述
 * </p>
 *
 * @author shenbh
 * @since 2023/4/2
 */
@Data
public class StateSnapshotDTO {

    private Integer status;

    private String remark;

    private LocalDateTime reachTime;
}
