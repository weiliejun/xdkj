package com.xdkj.common.util;

import org.springframework.util.Assert;
import org.w3c.dom.CharacterData;
import org.w3c.dom.*;

import java.util.ArrayList;
import java.util.List;

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

public class DomHelper {

    public DomHelper() {
        super();
        // constructor from parent
    }

    /**
     * Retrieve all child elements of the given DOM element that match the given
     * element name. Only look at the direct child level of the given element;
     * do not go into further depth (in contrast to the DOM API's
     * <code>getElementsByTagName</code> method).
     *
     * @param ele          the DOM element to analyze
     * @param childEleName the child element name to look for
     * @return a List of child <code>org.w3c.dom.Element</code> instances
     * @see Element
     * @see Element#getElementsByTagName
     */
    public static List getChildElementsByTagName(Element ele, String childEleName) {
        NodeList nl = ele.getChildNodes();
        List childEles = new ArrayList();
        for (int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);
            if (node instanceof Element && nodeNameEquals(node, childEleName)) {
                childEles.add(node);
            }
        }
        return childEles;
    }

    /**
     * Utility method that returns the first child element identified by its
     * name.
     *
     * @param ele          the DOM element to analyze
     * @param childEleName the child element name to look for
     * @return the <code>org.w3c.dom.Element</code> instance, or
     * <code>null</code> if none found
     */
    public static Element getChildElementByTagName(Element ele, String childEleName) {
        NodeList nl = ele.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);
            if (node instanceof Element && nodeNameEquals(node, childEleName)) {
                return (Element) node;
            }
        }
        return null;
    }

    /**
     * Utility method that returns the first child element value identified by
     * its name.
     *
     * @param ele          the DOM element to analyze
     * @param childEleName the child element name to look for
     * @return the extracted text value, or <code>null</code> if no child
     * element found
     */
    public static String getChildElementValueByTagName(Element ele, String childEleName) {
        Element child = getChildElementByTagName(ele, childEleName);
        return (child != null ? getTextValue(child) : null);
    }

    /**
     * Namespace-aware equals comparison. Returns <code>true</code> if either
     * {@link Node#getLocalName} or {@link Node#getNodeName} equals
     * <code>desiredName</code>, otherwise returns <code>false</code>.
     */
    public static boolean nodeNameEquals(Node node, String desiredName) {
        Assert.notNull(node, "Node must not be null");
        Assert.notNull(desiredName, "Desired name must not be null");
        return desiredName.equals(node.getNodeName()) || desiredName.equals(node.getLocalName());
    }

    /**
     * Extract the text value from the given DOM element, ignoring XML comments.
     * <p>
     * Appends all CharacterData nodes and EntityReference nodes into a single
     * String value, excluding Comment nodes.
     *
     * @see CharacterData
     * @see EntityReference
     * @see Comment
     */
    public static String getTextValue(Element valueEle) {
        StringBuffer value = new StringBuffer();
        NodeList nl = valueEle.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node item = nl.item(i);
            if ((item instanceof CharacterData && !(item instanceof Comment)) || item instanceof EntityReference) {
                value.append(item.getNodeValue());
            }
        }
        return value.toString();
    }

}
