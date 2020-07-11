package com.xdkj.common.components.exception;


import com.xdkj.common.components.rsa.exception.BaseRuntimeException;

public class ParamErrorException extends BaseRuntimeException {

    private static final long serialVersionUID = 1L;

    //错误编码
    private String code;
    //错误描述信息
    private String msg;

    public ParamErrorException() {

    }

    public ParamErrorException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public ParamErrorException(String code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


}
