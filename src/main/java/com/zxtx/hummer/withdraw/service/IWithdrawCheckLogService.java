package com.zxtx.hummer.withdraw.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zxtx.hummer.withdraw.domain.WithdrawCheckLog;
import com.zxtx.hummer.withdraw.req.WithdrawApplyCheckReq;
import com.zxtx.hummer.withdraw.vo.WithdrawApplyCheckVo;

import java.util.List;

/**
 * <p>
 * 提现审核记录 服务类
 * </p>
 *
 * @author chenjian
 * @since 2023/06/02
 */
public interface IWithdrawCheckLogService extends IService<WithdrawCheckLog> {

    /**
     * @author chenjian
     * @create 2023/6/5 14:52
     * @desc 后台提现审核记录分页列表
     **/
    List<WithdrawApplyCheckVo> selectPage(WithdrawApplyCheckReq req);


}
