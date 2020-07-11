package com.xdkj.common.components.exception;

public class ValidSignErrorException extends Exception {
    private static final long serialVersionUID = 1L;

    public ValidSignErrorException() {
        super("验证数字签名失败");
    }

    public ValidSignErrorException(String msg) {
        super(msg);
    }

    public ValidSignErrorException(String msg, Throwable t) {
        super(msg, t);
    }
}
