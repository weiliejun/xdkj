package com.xdkj.common.components.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * @author cyp
 * @version V1.0.0
 * @time : 2019-01-17 13:45
 * @description 运行时异乎寻基类
 */


public class BaseRuntimeException extends RuntimeException {

    public BaseRuntimeException() {
        super();
    }

    public BaseRuntimeException(String message) {
        super(message);
    }

    public BaseRuntimeException(Throwable cause) {
        super(cause);
    }

    public BaseRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Print the composite message and the embedded stack trace to the specified
     * stream.
     *
     * @param ps the print stream
     */
    public void printStackTrace(PrintStream ps) {
        if (getCause() == null) {
            super.printStackTrace(ps);
        } else {
            ps.println(this);
            ps.print("Caused by: ");
            getCause().printStackTrace(ps);
        }
    }

    /**
     * Print the composite message and the embedded stack trace to the specified
     * writer.
     *
     * @param pw the print writer
     */
    public void printStackTrace(PrintWriter pw) {
        if (getCause() == null) {
            super.printStackTrace(pw);
        } else {
            pw.println(this);
            pw.print("Caused by: ");
            getCause().printStackTrace(pw);
        }
    }

    /**
     * Retrieve the innermost cause of this exception, if any.
     * <p>
     * Currently just traverses NestedRuntimeException causes. Will use the JDK
     * 1.4 exception cause mechanism once Spring requires JDK 1.4.
     *
     * @return the innermost exception, or <code>null</code> if none
     */
    public Throwable getRootCause() {
        Throwable cause = getCause();
        if (cause instanceof BaseRuntimeException) {
            return ((BaseRuntimeException) cause).getRootCause();
        } else {
            return cause;
        }
    }
}
