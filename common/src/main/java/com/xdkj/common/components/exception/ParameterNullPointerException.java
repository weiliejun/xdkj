package com.xdkj.common.components.exception;

/**
 * @author cyp
 * @time : 2019-01-17 13:49
 * @description 元素空指针异常
 */


public class ParameterNullPointerException extends BaseRuntimeException {

    private static final long serialVersionUID = -7042921772535826170L;

    public ParameterNullPointerException(String paramName) {
        super("参数" + paramName + "不能为空！");
    }
}
