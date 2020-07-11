package com.xdkj.common.components.exception;

/**
 * @author cyp
 * @time : 2019-01-17 13:46
 * @description 消息模板消息发送类型未定义抛出异常
 */


public class MessageBusiTypeTmplInexistenceException extends BaseRuntimeException {

    private static final long serialVersionUID = 8773423080531229172L;

    public MessageBusiTypeTmplInexistenceException(String busiType) {
        super("业务类型模板：" + busiType + "未定义！");
    }
}
