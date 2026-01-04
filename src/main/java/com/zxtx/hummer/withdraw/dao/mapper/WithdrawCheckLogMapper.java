package com.zxtx.hummer.withdraw.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zxtx.hummer.withdraw.domain.WithdrawCheckLog;
import com.zxtx.hummer.withdraw.req.WithdrawApplyCheckReq;
import com.zxtx.hummer.withdraw.vo.WithdrawApplyCheckVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 提现审核记录 Mapper 接口
 * </p>
 *
 * @author chenjian
 * @since 2023/06/02
 */
public interface WithdrawCheckLogMapper extends BaseMapper<WithdrawCheckLog> {
    List<WithdrawApplyCheckVo> selectByParam(@Param("req") WithdrawApplyCheckReq req);

}
