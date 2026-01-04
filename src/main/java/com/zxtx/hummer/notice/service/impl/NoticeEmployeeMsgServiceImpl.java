package com.zxtx.hummer.notice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxtx.hummer.notice.dao.mapper.NoticeEmployeeMsgMapper;
import com.zxtx.hummer.notice.domain.NoticeEmployeeMsg;
import com.zxtx.hummer.notice.enums.MsgBizTypeEnum;
import com.zxtx.hummer.notice.service.INoticeEmployeeMsgService;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 通知_员工消息 服务实现类
 * </p>
 *
 * @author shenbh
 * @since 2023-03-23
 */
@Service
public class NoticeEmployeeMsgServiceImpl extends ServiceImpl<NoticeEmployeeMsgMapper, NoticeEmployeeMsg> implements INoticeEmployeeMsgService {

    @Override
    public void addMessage(Long employeeId, MsgBizTypeEnum type, String title, String content, Long bizId) {
        NoticeEmployeeMsg msg = new NoticeEmployeeMsg();
        msg.setEmployeeId(employeeId);
        msg.setBizType(type.getCode());
        msg.setTitle(title);
        msg.setDigest(content);
        msg.setContent(content);
        msg.setHasRead(0);
        msg.setBizId(bizId);
        msg.setCreateTime(new Date());
        this.save(msg);
    }
}
