package com.xdkj.common.components.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * @author cyp
 * @time : 2019-01-17 13:45
 * @description 检查性异常基类
 */


public class BaseCheckedException extends Exception {

    public BaseCheckedException() {
        super();
    }

    public BaseCheckedException(String message) {
        super(message);
    }

    public BaseCheckedException(Throwable cause) {
        super(cause);
    }

    public BaseCheckedException(String message, Throwable cause) {
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
     * print writer.
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
     * Currently just traverses NestedCheckedException causes. Will use the JDK
     * 1.4 exception cause mechanism once Spring requires JDK 1.4.
     *
     * @return the innermost exception, or <code>null</code> if none
     */
    public Throwable getRootCause() {
        Throwable cause = getCause();
        if (cause instanceof BaseCheckedException) {
            return ((BaseCheckedException) cause).getRootCause();
        } else {
            return cause;
        }
    }

}
