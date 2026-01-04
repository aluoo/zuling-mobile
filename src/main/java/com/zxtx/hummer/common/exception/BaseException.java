package com.zxtx.hummer.common.exception;

public class BaseException extends RuntimeException {
    private IError error;

    private String extMessage;

    public BaseException(IError error, String extMessage) {
        this(error);
        this.extMessage = extMessage;
    }

    public BaseException(int code, String message) {
        this.error = new IError() {
            @Override
            public int getCode() {
                return code;
            }

            @Override
            public String getMessage() {
                return message;
            }
        };
    }


    public BaseException(IError error) {
        super(error.getMessage());
        this.error = error;
    }

    public IError getError() {
        return error;
    }

    public String getExtMessage() {
        return extMessage;
    }
}
