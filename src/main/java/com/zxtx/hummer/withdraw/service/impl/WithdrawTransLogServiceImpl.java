package com.zxtx.hummer.withdraw.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxtx.hummer.withdraw.dao.mapper.WithdrawTransLogMapper;
import com.zxtx.hummer.withdraw.domain.WithdrawTransLog;
import com.zxtx.hummer.withdraw.service.IWithdrawTransLogService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 打款记录 服务实现类
 * </p>
 *
 * @author chenjian
 * @since 2023/06/02
 */
@Service
public class WithdrawTransLogServiceImpl extends ServiceImpl<WithdrawTransLogMapper, WithdrawTransLog> implements IWithdrawTransLogService {

}
