package com.xdkj.common.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/*
 * ===========================================================================
 * Copyright 2007 WEBTRANING Corp. All Rights Reserved.
 * WEBTRANING PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * ===========================================================================
 * @version 1.0, 2007-8-11
 * @author  Jack Chen
 * reference Commons Collections  ListUtils class
 * ===========================================================================
 *
 */

public class ListHelper {

    public ListHelper() {
        super();
        // constructor from parent
    }
    // -----------------------------------------------------------------------

    /**
     * Returns a new list containing all elements that are contained in both
     * given lists.
     *
     * @param list1 the first list
     * @param list2 the second list
     * @return the intersection of those two lists
     * @throws NullPointerException if either list is null
     */
    public static List intersection(final List list1, final List list2) {
        final ArrayList result = new ArrayList();
        final Iterator iterator = list2.iterator();

        while (iterator.hasNext()) {
            final Object o = iterator.next();

            if (list1.contains(o)) {
                result.add(o);
            }
        }

        return result;
    }

    /**
     * Subtracts all elements in the second list from the first list, placing
     * the results in a new list.
     * <p>
     * This differs from {@link List#removeAll(Collection)} in that cardinality
     * is respected; if <Code>list1</Code> contains two occurrences of
     * <Code>null</Code> and <Code>list2</Code> only contains one occurrence,
     * then the returned list will still contain one occurrence.
     *
     * @param list1 the list to subtract from
     * @param list2 the list to subtract
     * @return a new list containing the results
     * @throws NullPointerException if either list is null
     */
    public static List subtract(final List list1, final List list2) {
        final ArrayList result = new ArrayList(list1);
        final Iterator iterator = list2.iterator();

        while (iterator.hasNext()) {
            result.remove(iterator.next());
        }

        return result;
    }

    /**
     * Returns the sum of the given lists. This is their intersection subtracted
     * from their union.
     *
     * @param list1 the first list
     * @param list2 the second list
     * @return a new list containing the sum of those lists
     * @throws NullPointerException if either list is null
     */
    public static List sum(final List list1, final List list2) {
        return subtract(union(list1, list2), intersection(list1, list2));
    }

    /**
     * Returns a new list containing the second list appended to the first list.
     * The {@link List#addAll(Collection)} operation is used to append the two
     * given lists into a new list.
     *
     * @param list1 the first list
     * @param list2 the second list
     * @return a new list containing the union of those lists
     * @throws NullPointerException if either list is null
     */
    public static List union(final List list1, final List list2) {
        final ArrayList result = new ArrayList(list1);
        result.addAll(list2);
        return result;
    }

    /**
     * Tests two lists for value-equality as per the equality contract in
     * {@link List#equals(Object)}.
     * <p>
     * This method is useful for implementing <code>List</code> when you cannot
     * extend AbstractList. The method takes Collection instances to enable
     * other collection types to use the List implementation algorithm.
     * <p>
     * The relevant text (slightly paraphrased as this is a static method) is:
     * <blockquote> Compares the two list objects for equality. Returns
     * <tt>true</tt> if and only if both lists have the same size, and all
     * corresponding pairs of elements in the two lists are <i>equal</i>. (Two
     * elements <tt>e1</tt> and <tt>e2</tt> are <i>equal</i> if
     * <tt>(e1==null ? e2==null :
     * e1.equals(e2))</tt>.) In other words, two lists are defined to be equal
     * if they contain the same elements in the same order. This definition
     * ensures that the equals method works properly across different
     * implementations of the <tt>List</tt> interface. </blockquote>
     *
     * <b>Note:</b> The behaviour of this method is undefined if the lists are
     * modified during the equals comparison.
     *
     * @param list1 the first list, may be null
     * @param list2 the second list, may be null
     * @return whether the lists are equal by value comparison
     * @see List
     */
    public static boolean isEqualList(final Collection list1, final Collection list2) {
        if (list1 == list2) {
            return true;
        }
        if (list1 == null || list2 == null || list1.size() != list2.size()) {
            return false;
        }

        Iterator it1 = list1.iterator();
        Iterator it2 = list2.iterator();
        Object obj1 = null;
        Object obj2 = null;

        while (it1.hasNext() && it2.hasNext()) {
            obj1 = it1.next();
            obj2 = it2.next();

            if (!(obj1 == null ? obj2 == null : obj1.equals(obj2))) {
                return false;
            }
        }

        return !(it1.hasNext() || it2.hasNext());
    }

}
