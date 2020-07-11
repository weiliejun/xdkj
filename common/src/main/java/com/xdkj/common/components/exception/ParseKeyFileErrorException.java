package com.xdkj.common.components.exception;

public class ParseKeyFileErrorException extends Exception {
    private static final long serialVersionUID = 1L;

    public ParseKeyFileErrorException() {
        super("解析密钥文件失败");
    }

    public ParseKeyFileErrorException(String msg) {
        super(msg);
    }

    public ParseKeyFileErrorException(String msg, Throwable t) {
        super(msg, t);
    }
}