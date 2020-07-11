package com.xdkj.common.components.exception;

import com.xdkj.common.components.rsa.exception.BaseRuntimeException;

/*
 * ===========================================================================
 * Copyright 2007 WEBTRANING Corp. All Rights Reserved.
 * WEBTRANING PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * ===========================================================================
 * @version 1.0, 2007-8-12
 * @author  Jack Chen
 * ===========================================================================
 *
 */

public class IORuntimeException extends BaseRuntimeException {

    public IORuntimeException() {
        super();
        // constructor from parent
    }

    public IORuntimeException(String message) {
        super(message);
        // constructor from parent
    }

    public IORuntimeException(Throwable cause) {
        super(cause);
        // constructor from parent
    }

    public IORuntimeException(String message, Throwable cause) {
        super(message, cause);
        // constructor from parent
    }

}
