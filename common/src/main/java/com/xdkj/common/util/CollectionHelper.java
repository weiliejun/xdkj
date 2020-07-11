package com.xdkj.common.util;

import org.springframework.util.ObjectUtils;

import java.lang.reflect.Array;
import java.util.*;

/*
 * ===========================================================================
 * Copyright 2007 WEBTRANING Corp. All Rights Reserved.
 * WEBTRANING PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * ===========================================================================
 * @version 1.0, 2007-8-11
 * @author  Jack Chen
 * reference Commons Collections and spring CollectionUtils class
 * ===========================================================================
 *
 */

public class CollectionHelper {

    /**
     * Constant to avoid repeated object creation
     */
    private static Integer INTEGER_ONE = new Integer(1);

    public CollectionHelper() {
        super();
        // constructor from parent
    }

    /**
     * Returns a {@link Collection} containing the union of the given
     * {@link Collection}s.
     * <p>
     * The cardinality of each element in the returned {@link Collection} will
     * be equal to the maximum of the cardinality of that element in the two
     * given {@link Collection}s.
     *
     * @param a the first collection, must not be null
     * @param b the second collection, must not be null
     * @return the union of the two collections
     * @see Collection#addAll
     */
    public static Collection union(final Collection a, final Collection b) {
        ArrayList list = new ArrayList();
        Map mapa = getCardinalityMap(a);
        Map mapb = getCardinalityMap(b);
        Set elts = new HashSet(a);
        elts.addAll(b);
        Iterator it = elts.iterator();
        while (it.hasNext()) {
            Object obj = it.next();
            for (int i = 0, m = Math.max(getFreq(obj, mapa), getFreq(obj, mapb)); i < m; i++) {
                list.add(obj);
            }
        }
        return list;
    }

    /**
     * Returns a {@link Collection} containing the intersection of the given
     * {@link Collection}s.
     * <p>
     * The cardinality of each element in the returned {@link Collection} will
     * be equal to the minimum of the cardinality of that element in the two
     * given {@link Collection}s.
     *
     * @param a the first collection, must not be null
     * @param b the second collection, must not be null
     * @return the intersection of the two collections
     * @see Collection#retainAll
     * @see #containsAny
     */
    public static Collection intersection(final Collection a, final Collection b) {
        ArrayList list = new ArrayList();
        Map mapa = getCardinalityMap(a);
        Map mapb = getCardinalityMap(b);
        Set elts = new HashSet(a);
        elts.addAll(b);
        Iterator it = elts.iterator();
        while (it.hasNext()) {
            Object obj = it.next();
            for (int i = 0, m = Math.min(getFreq(obj, mapa), getFreq(obj, mapb)); i < m; i++) {
                list.add(obj);
            }
        }
        return list;
    }

    /**
     * Returns a {@link Map} mapping each unique element in the given
     * {@link Collection} to an {@link Integer} representing the number of
     * occurrences of that element in the {@link Collection}.
     * <p>
     * Only those elements present in the collection will appear as keys in the
     * map.
     *
     * @param coll the collection to get the cardinality map for, must not be
     *             null
     * @return the populated cardinality map
     */
    public static Map getCardinalityMap(final Collection coll) {
        Map count = new HashMap();
        for (Iterator it = coll.iterator(); it.hasNext(); ) {
            Object obj = it.next();
            Integer c = (Integer) (count.get(obj));
            if (c == null) {
                count.put(obj, INTEGER_ONE);
            } else {
                count.put(obj, new Integer(c.intValue() + 1));
            }
        }
        return count;
    }

    /**
     * Returns a new {@link Collection} containing <tt><i>a</i> - <i>b</i></tt>.
     * The cardinality of each element <i>e</i> in the returned
     * {@link Collection} will be the cardinality of <i>e</i> in <i>a</i> minus
     * the cardinality of <i>e</i> in <i>b</i>, or zero, whichever is greater.
     *
     * @param a the collection to subtract from, must not be null
     * @param b the collection to subtract, must not be null
     * @return a new collection with the results
     * @see Collection#removeAll
     */
    public static Collection subtract(final Collection a, final Collection b) {
        ArrayList list = new ArrayList(a);
        for (Iterator it = b.iterator(); it.hasNext(); ) {
            list.remove(it.next());
        }
        return list;
    }

    /**
     * Adds all elements in the iteration to the given collection.
     *
     * @param collection the collection to add to, must not be null
     * @param iterator   the iterator of elements to add, must not be null
     * @throws NullPointerException if the collection or iterator is null
     */
    public static void addAll(Collection collection, Iterator iterator) {
        while (iterator.hasNext()) {
            collection.add(iterator.next());
        }
    }

    /**
     * Adds all elements in the enumeration to the given collection.
     *
     * @param collection  the collection to add to, must not be null
     * @param enumeration the enumeration of elements to add, must not be null
     * @throws NullPointerException if the collection or enumeration is null
     */
    public static void addAll(Collection collection, Enumeration enumeration) {
        while (enumeration.hasMoreElements()) {
            collection.add(enumeration.nextElement());
        }
    }

    /**
     * Adds all elements in the array to the given collection.
     *
     * @param collection the collection to add to, must not be null
     * @param elements   the array of elements to add, must not be null
     * @throws NullPointerException if the collection or array is null
     */
    public static void addAll(Collection collection, Object[] elements) {
        for (int i = 0, size = elements.length; i < size; i++) {
            collection.add(elements[i]);
        }
    }

    /**
     * Given an Object, and an index, returns the nth value in the object.
     * <ul>
     * <li>If obj is a Map, returns the nth value from the <b>keySet</b>
     * iterator, unless the Map contains an Integer key with integer value =
     * idx, in which case the corresponding map entry value is returned. If idx
     * exceeds the number of entries in the map, an empty Iterator is returned.
     * <li>If obj is a List or an array, returns the nth value, throwing
     * IndexOutOfBoundsException, ArrayIndexOutOfBoundsException, resp. if the
     * nth value does not exist.
     * <li>If obj is an iterator, enumeration or Collection, returns the nth
     * value from the iterator, returning an empty Iterator (resp. Enumeration)
     * if the nth value does not exist.
     * <li>Returns the original obj if it is null or not a Collection or
     * Iterator.
     * </ul>
     *
     * @param obj the object to get an index of, may be null
     * @param idx the index to get
     * @throws IndexOutOfBoundsException
     * @throws ArrayIndexOutOfBoundsException
     * @deprecated use {@link #get(Object, int)} instead. Will be removed in
     * v4.0
     */
    public static Object index(Object obj, int idx) {
        return index(obj, new Integer(idx));
    }

    /**
     * Given an Object, and a key (index), returns the value associated with
     * that key in the Object. The following checks are made:
     * <ul>
     * <li>If obj is a Map, use the index as a key to get a value. If no match
     * continue.
     * <li>Check key is an Integer. If not, return the object passed in.
     * <li>If obj is a Map, get the nth value from the <b>keySet</b> iterator.
     * If the Map has fewer than n entries, return an empty Iterator.
     * <li>If obj is a List or an array, get the nth value, throwing
     * IndexOutOfBoundsException, ArrayIndexOutOfBoundsException, resp. if the
     * nth value does not exist.
     * <li>If obj is an iterator, enumeration or Collection, get the nth value
     * from the iterator, returning an empty Iterator (resp. Enumeration) if the
     * nth value does not exist.
     * <li>Return the original obj.
     * </ul>
     *
     * @param obj   the object to get an index of
     * @param index the index to get
     * @return the object at the specified index
     * @throws IndexOutOfBoundsException
     * @throws ArrayIndexOutOfBoundsException
     * @deprecated use {@link #get(Object, int)} instead. Will be removed in
     * v4.0
     */
    public static Object index(Object obj, Object index) {
        if (obj instanceof Map) {
            Map map = (Map) obj;
            if (map.containsKey(index)) {
                return map.get(index);
            }
        }
        int idx = -1;
        if (index instanceof Integer) {
            idx = ((Integer) index).intValue();
        }
        if (idx < 0) {
            return obj;
        } else if (obj instanceof Map) {
            Map map = (Map) obj;
            Iterator iterator = map.keySet().iterator();
            return index(iterator, idx);
        } else if (obj instanceof List) {
            return ((List) obj).get(idx);
        } else if (obj instanceof Object[]) {
            return ((Object[]) obj)[idx];
        } else if (obj instanceof Enumeration) {
            Enumeration it = (Enumeration) obj;
            while (it.hasMoreElements()) {
                idx--;
                if (idx == -1) {
                    return it.nextElement();
                } else {
                    it.nextElement();
                }
            }
        } else if (obj instanceof Iterator) {
            return index((Iterator) obj, idx);
        } else if (obj instanceof Collection) {
            Iterator iterator = ((Collection) obj).iterator();
            return index(iterator, idx);
        }
        return obj;
    }

    private static Object index(Iterator iterator, int idx) {
        while (iterator.hasNext()) {
            idx--;
            if (idx == -1) {
                return iterator.next();
            } else {
                iterator.next();
            }
        }
        return iterator;
    }

    /**
     * Returns the <code>index</code>-th value in <code>object</code>, throwing
     * <code>IndexOutOfBoundsException</code> if there is no such element or
     * <code>IllegalArgumentException</code> if <code>object</code> is not an
     * instance of one of the supported types.
     * <p>
     * The supported types, and associated semantics are:
     * <ul>
     * <li>Map -- the value returned is the <code>Map.Entry</code> in position
     * <code>index</code> in the map's <code>entrySet</code> iterator, if there
     * is such an entry.</li>
     * <li>List -- this method is equivalent to the list's get method.</li>
     * <li>Array -- the <code>index</code>-th array entry is returned, if there
     * is such an entry; otherwise an <code>IndexOutOfBoundsException</code> is
     * thrown.</li>
     * <li>Collection -- the value returned is the <code>index</code>-th object
     * returned by the collection's default iterator, if there is such an
     * element.</li>
     * <li>Iterator or Enumeration -- the value returned is the
     * <code>index</code>-th object in the Iterator/Enumeration, if there is
     * such an element. The Iterator/Enumeration is advanced to
     * <code>index</code> (or to the end, if <code>index</code> exceeds the
     * number of entries) as a side effect of this method.</li>
     * </ul>
     *
     * @param object the object to get a value from
     * @param index  the index to get
     * @return the object at the specified index
     * @throws IndexOutOfBoundsException if the index is invalid
     * @throws IllegalArgumentException  if the object type is invalid
     */
    public static Object get(Object object, int index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("Index cannot be negative: " + index);
        }
        if (object instanceof Map) {
            Map map = (Map) object;
            Iterator iterator = map.entrySet().iterator();
            return get(iterator, index);
        } else if (object instanceof List) {
            return ((List) object).get(index);
        } else if (object instanceof Object[]) {
            return ((Object[]) object)[index];
        } else if (object instanceof Iterator) {
            Iterator it = (Iterator) object;
            while (it.hasNext()) {
                index--;
                if (index == -1) {
                    return it.next();
                } else {
                    it.next();
                }
            }
            throw new IndexOutOfBoundsException("Entry does not exist: " + index);
        } else if (object instanceof Collection) {
            Iterator iterator = ((Collection) object).iterator();
            return get(iterator, index);
        } else if (object instanceof Enumeration) {
            Enumeration it = (Enumeration) object;
            while (it.hasMoreElements()) {
                index--;
                if (index == -1) {
                    return it.nextElement();
                } else {
                    it.nextElement();
                }
            }
            throw new IndexOutOfBoundsException("Entry does not exist: " + index);
        } else if (object == null) {
            throw new IllegalArgumentException("Unsupported object type: null");
        } else {
            try {
                return Array.get(object, index);
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException("Unsupported object type: " + object.getClass().getName());
            }
        }
    }

    /**
     * Gets the size of the collection/iterator specified.
     * <p>
     * This method can handles objects as follows
     * <ul>
     * <li>Collection - the collection size
     * <li>Map - the map size
     * <li>Array - the array size
     * <li>Iterator - the number of elements remaining in the iterator
     * <li>Enumeration - the number of elements remaining in the enumeration
     * </ul>
     *
     * @param object the object to get the size of
     * @return the size of the specified collection
     * @throws IllegalArgumentException thrown if object is not recognised or null
     * @since Commons Collections 3.1
     */
    public static int size(Object object) {
        int total = 0;
        if (object instanceof Map) {
            total = ((Map) object).size();
        } else if (object instanceof Collection) {
            total = ((Collection) object).size();
        } else if (object instanceof Object[]) {
            total = ((Object[]) object).length;
        } else if (object instanceof Iterator) {
            Iterator it = (Iterator) object;
            while (it.hasNext()) {
                total++;
                it.next();
            }
        } else if (object instanceof Enumeration) {
            Enumeration it = (Enumeration) object;
            while (it.hasMoreElements()) {
                total++;
                it.nextElement();
            }
        } else if (object == null) {
            throw new IllegalArgumentException("Unsupported object type: null");
        } else {
            try {
                total = Array.getLength(object);
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException("Unsupported object type: " + object.getClass().getName());
            }
        }
        return total;
    }

    /**
     * Checks if the specified collection/array/iterator is empty.
     * <p>
     * This method can handles objects as follows
     * <ul>
     * <li>Collection - via collection isEmpty
     * <li>Map - via map isEmpty
     * <li>Array - using array size
     * <li>Iterator - via hasNext
     * <li>Enumeration - via hasMoreElements
     * </ul>
     * <p>
     * Note: This method is named to avoid clashing with
     * {@link #isEmpty(Collection)}.
     *
     * @param object the object to get the size of, not null
     * @return true if empty
     * @throws IllegalArgumentException thrown if object is not recognised or null
     * @since Commons Collections 3.2
     */
    public static boolean sizeIsEmpty(Object object) {
        if (object instanceof Collection) {
            return ((Collection) object).isEmpty();
        } else if (object instanceof Map) {
            return ((Map) object).isEmpty();
        } else if (object instanceof Object[]) {
            return ((Object[]) object).length == 0;
        } else if (object instanceof Iterator) {
            return ((Iterator) object).hasNext() == false;
        } else if (object instanceof Enumeration) {
            return ((Enumeration) object).hasMoreElements() == false;
        } else if (object == null) {
            throw new IllegalArgumentException("Unsupported object type: null");
        } else {
            try {
                return Array.getLength(object) == 0;
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException("Unsupported object type: " + object.getClass().getName());
            }
        }
    }

    // -----------------------------------------------------------------------

    /**
     * Null-safe check if the specified collection is empty.
     * <p>
     * Null returns true.
     *
     * @param coll the collection to check, may be null
     * @return true if empty or null
     * @since Commons Collections 3.2
     */
    public static boolean isEmpty(Collection coll) {
        return (coll == null || coll.isEmpty());
    }

    /**
     * Null-safe check if the specified collection is not empty.
     * <p>
     * Null returns false.
     *
     * @param coll the collection to check, may be null
     * @return true if non-null and non-empty
     * @since Commons Collections 3.2
     */
    public static boolean isNotEmpty(Collection coll) {
        return !CollectionHelper.isEmpty(coll);
    }

    private static final int getFreq(final Object obj, final Map freqMap) {
        Integer count = (Integer) freqMap.get(obj);
        if (count != null) {
            return count.intValue();
        }
        return 0;
    }

    /**
     * Return <code>true</code> if the supplied <code>Map</code> is null or
     * empty. Otherwise, return <code>false</code>.
     *
     * @param map the <code>Map</code> to check
     */
    public static boolean isEmpty(Map map) {
        return (map == null || map.isEmpty());
    }

    /**
     * Check whether the given Iterator contains the given element.
     *
     * @param iterator the Iterator to check
     * @param element  the element to look for
     * @return <code>true</code> if found, <code>false</code> else
     */
    public static boolean contains(Iterator iterator, Object element) {
        if (iterator != null) {
            while (iterator.hasNext()) {
                Object candidate = iterator.next();
                if (ObjectUtils.nullSafeEquals(candidate, element)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Check whether the given Enumeration contains the given element.
     *
     * @param enumeration the Enumeration to check
     * @param element     the element to look for
     * @return <code>true</code> if found, <code>false</code> else
     */
    public static boolean contains(Enumeration enumeration, Object element) {
        if (enumeration != null) {
            while (enumeration.hasMoreElements()) {
                Object candidate = enumeration.nextElement();
                if (ObjectUtils.nullSafeEquals(candidate, element)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Determine whether the given collection only contains a single unique
     * object.
     *
     * @param collection the collection to check
     * @return <code>true</code> if the collection contains a single reference
     * or multiple references to the same instance, <code>false</code>
     * else
     */
    public static boolean hasUniqueObject(Collection collection) {
        if (isEmpty(collection)) {
            return false;
        }
        boolean hasCandidate = false;
        Object candidate = null;
        for (Iterator it = collection.iterator(); it.hasNext(); ) {
            Object elem = it.next();
            if (!hasCandidate) {
                hasCandidate = true;
                candidate = elem;
            } else if (candidate != elem) {
                return false;
            }
        }
        return true;
    }

    /**
     * Find a value of the given type in the given collection.
     *
     * @param collection the collection to search
     * @param type       the type to look for
     * @return a value of the given type found, or <code>null</code> if none
     * @throws IllegalArgumentException if more than one value of the given type found
     */
    public static Object findValueOfType(Collection collection, Class type) throws IllegalArgumentException {
        if (isEmpty(collection)) {
            return null;
        }
        Class typeToUse = (type != null ? type : Object.class);
        Object value = null;
        for (Iterator it = collection.iterator(); it.hasNext(); ) {
            Object obj = it.next();
            if (typeToUse.isInstance(obj)) {
                if (value != null) {
                    throw new IllegalArgumentException("More than one value of type [" + typeToUse.getName() + "] found");
                }
                value = obj;
            }
        }
        return value;
    }

    /**
     * Find a value of one of the given types in the given collection: searching
     * the collection for a value of the first type, then searching for a value
     * of the second type, etc.
     *
     * @param collection the collection to search
     * @param types      the types to look for, in prioritized order
     * @return a of one of the given types found, or <code>null</code> if none
     * @throws IllegalArgumentException if more than one value of the given type found
     */
    public static Object findValueOfType(Collection collection, Class[] types) throws IllegalArgumentException {
        if (isEmpty(collection) || ObjectUtils.isEmpty(types)) {
            return null;
        }
        for (int i = 0; i < types.length; i++) {
            Object value = findValueOfType(collection, types[i]);
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    /**
     * Converts the supplied array into a List. Primitive arrays are correctly
     * converted into Lists of the appropriate wrapper type.
     *
     * @param source the original array
     * @return the converted List result
     */
    public static List arrayToList(Object source) {
        if (source == null || !source.getClass().isArray()) {
            throw new IllegalArgumentException("Source is not an array: " + source);
        }
        boolean primitive = source.getClass().getComponentType().isPrimitive();
        Object[] array = (primitive ? ObjectUtils.toObjectArray(source) : (Object[]) source);
        return Arrays.asList(array);
    }

    /**
     * Merge the given Properties instance into the given Map, copying all
     * properties (key-value pairs) over.
     * <p>
     * Uses <code>Properties.propertyNames()</code> to even catch default
     * properties linked into the original Properties instance.
     *
     * @param props the Properties instance to merge (may be <code>null</code>)
     * @param map   the target Map to merge the properties into
     */
    public static void mergePropertiesIntoMap(Properties props, Map map) {
        if (map == null) {
            throw new IllegalArgumentException("Map must not be null");
        }
        if (props != null) {
            for (Enumeration en = props.propertyNames(); en.hasMoreElements(); ) {
                String key = (String) en.nextElement();
                map.put(key, props.getProperty(key));
            }
        }
    }

    /**
     * Return <code>true</code> if any element in '<code>candidates</code>' is
     * contained in '<code>source</code>'; otherwise returns <code>false</code>.
     */
    public static boolean containsAny(Collection source, Collection candidates) {
        if (isEmpty(source) || isEmpty(candidates)) {
            return false;
        }
        for (Iterator iterator = candidates.iterator(); iterator.hasNext(); ) {
            if (source.contains(iterator.next())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Return the first element in '<code>candidates</code>' that is contained
     * in '<code>source</code>'. If no element in '<code>candidates</code>' is
     * present in '<code>source</code>' returns <code>null</code>. Iteration
     * order is {@link Collection} implementation specific.
     */
    public static Object findFirstMatch(Collection source, Collection candidates) {
        if (isEmpty(source) || isEmpty(candidates)) {
            return null;
        }
        for (Iterator iterator = candidates.iterator(); iterator.hasNext(); ) {
            Object candidate = iterator.next();
            if (source.contains(candidate)) {
                return candidate;
            }
        }
        return null;
    }

}
