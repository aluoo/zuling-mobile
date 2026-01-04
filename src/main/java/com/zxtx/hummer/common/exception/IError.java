package com.zxtx.hummer.common.exception;

public interface IError {
    /**
     * 编码
     *
     * @return
     */
    int getCode();

    /**
     * 消息
     *
     * @return
     */
    String getMessage();
}
