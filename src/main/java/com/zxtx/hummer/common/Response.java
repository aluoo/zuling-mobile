package com.zxtx.hummer.common;

import com.zxtx.hummer.common.exception.IError;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@ApiModel("返回值")
public class Response<T> {
    public static int SUCCESS_CODE = 200;

    @ApiModelProperty("返回值CODE, (成功=200)")
    private int code = SUCCESS_CODE;

    @ApiModelProperty("业务异常消息")
    private String message = "success";

    @ApiModelProperty("异常堆栈信息")
    private String extMessage;

    @ApiModelProperty("数据")
    private T data;

    @ApiModelProperty("总条数,暂时未用")
    private Integer count;

    private static final Map emptyMap = Collections.emptyMap();

    private static final List emtypList = Collections.emptyList();

    public static <T> Response<T> ok() {
        return new Response(emptyMap);
    }

    public static <T> Response<T> ok(T data) {
//        if(data instanceof PageInfo) {
//            return ok((PageInfo)data);
//        }
//        if(data instanceof Page) {
//            return ok((Page)data);
//        }
        if (data == null) {
            return new Response(emptyMap);
        }
        return new Response<T>(data);
    }


    public static <T> Response<T> failed(IError error, String extMessage) {
        return new Response(error.getCode(), error.getMessage(), extMessage, emptyMap);
    }

    public static <T> Response<T> failed(String extMessage) {
        return new Response(99999, extMessage, extMessage, emptyMap);
    }

    public static <T> Response<T> failed(IError error) {
        return failed(error, null);
    }

    public Response() {
    }

    public Response(T data) {
        this.data = data;
    }

    public Response(int code, String message, String extMessage, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.extMessage = extMessage;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public String getExtMessage() {
        return extMessage;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
