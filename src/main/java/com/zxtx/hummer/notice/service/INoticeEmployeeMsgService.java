package com.zxtx.hummer.notice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zxtx.hummer.notice.domain.NoticeEmployeeMsg;
import com.zxtx.hummer.notice.enums.MsgBizTypeEnum;

/**
 * <p>
 * 通知_员工消息 服务类
 * </p>
 *
 * @author shenbh
 * @since 2023-03-23
 */
public interface INoticeEmployeeMsgService extends IService<NoticeEmployeeMsg> {

    /**
     * 插入一条消息
     *
     * @param employeeId
     * @param type
     * @param title
     * @param content
     * @param bizId
     */
    void addMessage(Long employeeId, MsgBizTypeEnum type, String title, String content, Long bizId);

}
