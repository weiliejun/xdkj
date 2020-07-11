package com.xdkj.common.components.rsa.exception;

public class AddSignErrorException extends Exception {
    private static final long serialVersionUID = 1L;

    public AddSignErrorException() {
        super("生成数字签名失败");
    }

    public AddSignErrorException(String msg) {
        super(msg);
    }

    public AddSignErrorException(String msg, Throwable t) {
        super(msg, t);
    }
}