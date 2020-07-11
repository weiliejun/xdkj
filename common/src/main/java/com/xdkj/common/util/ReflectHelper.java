//$Id: ReflectHelper.java,v 1.12 2005/08/03 23:23:39 epbernard Exp $
package com.xdkj.common.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/*
 * ===========================================================================
 * Copyright 2007 WEBTRANING Corp. All Rights Reserved.
 * WEBTRANING PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * ===========================================================================
 * @version 1.0, 2007-8-11
 * @author  Jack Chen
 * ===========================================================================
 *
 */

public final class ReflectHelper {

    private static final Log log = LogFactory.getLog(ReflectHelper.class);

    private ReflectHelper() {
    }

    public static Class classForName(String name) throws ClassNotFoundException {
        try {
            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            if (contextClassLoader != null) {
                return contextClassLoader.loadClass(name);
            } else {
                return Class.forName(name);
            }
        } catch (Exception e) {
            return Class.forName(name);
        }
    }

    public static Class classForName(String name, Class caller) throws ClassNotFoundException {
        try {
            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            if (contextClassLoader != null) {
                return contextClassLoader.loadClass(name);
            } else {
                return Class.forName(name, true, caller.getClassLoader());
            }
        } catch (Exception e) {
            return Class.forName(name, true, caller.getClassLoader());
        }
    }

}
