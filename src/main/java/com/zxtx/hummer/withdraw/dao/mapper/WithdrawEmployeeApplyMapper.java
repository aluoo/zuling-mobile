package com.zxtx.hummer.withdraw.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zxtx.hummer.withdraw.domain.WithdrawEmployeeApply;
import com.zxtx.hummer.withdraw.req.WithdrawApplyReq;
import com.zxtx.hummer.withdraw.vo.WithdrawApplyVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 提现申请表 Mapper 接口
 * </p>
 *
 * @author chenjian
 * @since 2023/06/02
 */
public interface WithdrawEmployeeApplyMapper extends BaseMapper<WithdrawEmployeeApply> {

    List<WithdrawApplyVo> selectByParam(@Param("req") WithdrawApplyReq req);

}
