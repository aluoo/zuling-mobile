package com.zxtx.hummer.withdraw.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxtx.hummer.withdraw.dao.mapper.WithdrawEmployeeCardMapper;
import com.zxtx.hummer.withdraw.domain.WithdrawEmployeeCard;
import com.zxtx.hummer.withdraw.service.IWithdrawEmployeeCardService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 账户提现方式绑定 服务实现类
 * </p>
 *
 * @author chenjian
 * @since 2023/06/02
 */
@Service
public class WithdrawEmployeeCardServiceImpl extends ServiceImpl<WithdrawEmployeeCardMapper, WithdrawEmployeeCard> implements IWithdrawEmployeeCardService {
}
