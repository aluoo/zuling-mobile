package com.zxtx.hummer.withdraw.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.zxtx.hummer.withdraw.dao.mapper.WithdrawCheckLogMapper;
import com.zxtx.hummer.withdraw.domain.WithdrawCheckLog;
import com.zxtx.hummer.withdraw.req.WithdrawApplyCheckReq;
import com.zxtx.hummer.withdraw.service.IWithdrawCheckLogService;
import com.zxtx.hummer.withdraw.vo.WithdrawApplyCheckVo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 提现审核记录 服务实现类
 * </p>
 *
 * @author chenjian
 * @since 2023/06/02
 * IWithdrawTransLogService
 */
@Service
public class WithdrawCheckLogServiceImpl extends ServiceImpl<WithdrawCheckLogMapper, WithdrawCheckLog> implements IWithdrawCheckLogService {

    @Override
    public List<WithdrawApplyCheckVo> selectPage(WithdrawApplyCheckReq req) {
        PageHelper.startPage(req.getPage(), req.getPageSize());
        return this.getBaseMapper().selectByParam(req);
    }

}
