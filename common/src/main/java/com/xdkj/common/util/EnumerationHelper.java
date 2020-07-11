package com.xdkj.common.util;

import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.collections.iterators.EnumerationIterator;

import java.util.Enumeration;
import java.util.List;

/*
 * ===========================================================================
 * Copyright 2007 WEBTRANING Corp. All Rights Reserved.
 * WEBTRANING PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * ===========================================================================
 * @version 1.0, 2007-8-11
 * @author  Jack Chen
 * reference Commons Collections  EnumerationUtils class
 * ===========================================================================
 *
 */

public class EnumerationHelper {

    public EnumerationHelper() {
        super();
        // constructor from parent
    }

    /**
     * Creates a list based on an enumeration.
     *
     * <p>
     * As the enumeration is traversed, an ArrayList of its values is created.
     * The new list is returned.
     * </p>
     *
     * @param enumeration the enumeration to traverse, which should not be
     *                    <code>null</code>.
     * @throws NullPointerException if the enumeration parameter is <code>null</code>.
     */
    public static List toList(Enumeration enumeration) {
        return IteratorUtils.toList(new EnumerationIterator(enumeration));
    }
}
