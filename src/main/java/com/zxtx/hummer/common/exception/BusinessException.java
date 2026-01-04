package com.zxtx.hummer.common.exception;

import cn.hutool.core.util.StrUtil;

import java.text.MessageFormat;

public class BusinessException extends BaseException {
    public BusinessException(IError error) {
        super(error);
    }

    public BusinessException(String message) {
        super(500, message);
    }

    public BusinessException(IError error, String extMessage) {
        super(error, extMessage);
    }

    /**
     * 消息格式化
     *
     * @param error
     * @param args
     */
    public BusinessException(IError error, Object[] args) {
        super(error.getCode(), MessageFormat.format(error.getMessage(), args));
    }

    public BusinessException(BizError error, String remark, boolean replace) {
        super(error.getCode(), StrUtil.format(error.getMessage(), remark));
    }

}
