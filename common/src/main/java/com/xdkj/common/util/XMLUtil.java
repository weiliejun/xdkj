package com.xdkj.common.util;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class XMLUtil {

    private final static Logger logger = LoggerFactory.getLogger(XMLUtil.class);

    /**
     * 解析XMl字符串数据，转化成对象，并返回
     *
     * @param xml          xml字符串数据
     * @param xpathPattern xpath的匹配模式；具体语法请查XPath的语法
     * @param classType    要转化对象的类型
     * @return 返回用xml数据实例化之后的对象
     */
    public static <T> List<T> parseXmlStr(String xml, String xpathPattern, Class<T> classType) {
        try {
            Document doc = DocumentHelper.parseText(xml);

            List<Node> nodes = doc.selectNodes(xpathPattern);

            List<T> objs = new ArrayList<T>();
            for (Node node : nodes) {
                Map<String, String> map = traverse(node);
                if (classType == String.class) {
                    objs.add((T) map.values().toArray()[0]);
                } else {
                    objs.add(nodeToBean(map, classType));
                }
            }
            return objs;
        } catch (Exception e) {
            logger.error("parse string xml error , xpathPattern = " + xpathPattern + " class is " + classType.getName(), e);
            return new ArrayList<T>();
        }
    }

    /**
     * 解析XMl字符串数据，转化成对象，并返回
     *
     * @param xml          xml字符串数据
     * @param xpathPattern xpath的匹配模式；具体语法请查XPath的语法
     * @param classType    要转化对象的类型
     * @return 返回用xml数据实例化之后的对象
     */
    public static <T> List<T> parseXml2Str(String xml, String xpathPattern, Class<T> classType) {
        try {
            Document doc = DocumentHelper.parseText(xml);

            List<Node> nodes = doc.selectNodes(xpathPattern);

            List<T> objs = new ArrayList<T>();
            for (Node node : nodes) {
                Map<String, String> map = traverse2(node);
                if (classType == String.class) {
                    objs.add((T) map.values().toArray()[0]);
                } else {
                    objs.add(nodeToBean(map, classType));
                }
            }
            return objs;
        } catch (Exception e) {
            logger.error("parse string xml error , xpathPattern = " + xpathPattern + " class is " + classType.getName(), e);
            return new ArrayList<T>();
        }
    }

    /**
     * 解析XMl文件，转化成对象，并返回
     *
     * @param xmlPath      :xml文件路径(绝对路径)
     * @param xpathPattern xpath的匹配模式；具体语法请查XPath的语法
     * @param classType    要转化对象的类型
     * @return 返回用xml数据实例化之后的对象
     */
    public static <T> List<T> parseXmlFile(String xmlPath, String xpathPattern, Class<T> classType) {
        try {
            SAXReader reader = new SAXReader();
            Document doc = reader.read(new File(xmlPath));
            List<Node> nodes = doc.selectNodes(xpathPattern);
            List<T> objs = new ArrayList<T>();
            for (Node node : nodes) {
                Map<String, String> map = traverse(node);
                if (classType == String.class) {
                    objs.add((T) map.values().toArray()[0]);
                } else {
                    objs.add(nodeToBean(map, classType));
                }
            }
            return objs;
        } catch (Exception e) {
            logger.error("parse File xml error , xpathPattern = " + xpathPattern + " class is " + classType.getName(), e);
            return new ArrayList<T>();
        }
    }

    /**
     * 从找出的Node节点开始，遍历所有子节点，获取子节点的数据
     *
     * @param node :Node节点
     * @return 返回节点的值
     */
    private static Map<String, String> traverse(Node node) {
        Map<String, String> map = new HashMap<String, String>();
        if (node instanceof Element) {
            map = treeWalk((Element) node, map);
        } else {
            map.put(node.getName(), node.getText());
        }
        return map;
    }

    /**
     * 从找出的Node节点开始，遍历所有子节点，获取子节点的数据
     *
     * @param node :Node节点
     * @return 返回节点的值
     */
    private static Map<String, String> traverse2(Node node) {
        Map<String, String> map = new HashMap<String, String>();
        if (node instanceof Element) {
            map = tree2Walk((Element) node, map);
        } else {
            map.put(node.getName(), node.getText());
        }
        return map;
    }

    /**
     * 递归法提取节点的值
     *
     * @param element : 节点
     * @param map     : 保存节点的值
     */
    private static Map<String, String> treeWalk(Element element, Map<String, String> map) {
        // 遍历该元素自身的属性
        List<Element> elements = element.elements();
        for (Element ele : elements) {
            String name = ele.attribute("name").getStringValue();
            String value = ele.attribute("value").getStringValue();
            map.put(name, value);
        }

        return map;
    }

    /**
     * 递归法提取节点的值
     *
     * @param element : 节点
     * @param map     : 保存节点的值
     */
    private static Map<String, String> tree2Walk(Element element, Map<String, String> map) {
        // 遍历该元素自身的属性
        List<Element> elements = element.elements();
        for (Element ele : elements) {
            String name = ele.getName();
            String value = ele.getTextTrim();
            map.put(name, value);
        }

        return map;
    }

    private static <T> T nodeToBean(Map<String, String> map, Class<T> classType) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        Field[] fields = classType.getDeclaredFields();// 返回该类型的所有子属性（包含他继承的类和接口的）
        T obj = classType.getConstructor().newInstance();// 创建该类型的实例
        for (Field field : fields) {
            // 属性名
            String fieldName = field.getName();
            if (!map.containsKey(fieldName)) {
                continue;
            }
            // 因为解析出来都是String类型的，所以创建一个String类型转换成field.getType类型的构造器
            Constructor<?> con = field.getType().getConstructor(String.class);//
            Object param = con.newInstance(map.get(fieldName));// 通过构造器把字符串转换成field.getType类型的实例
            fieldName = StringUtils.capitalize(fieldName);// 首字母大写

            // 与下面的得到method设值一个功能
            field.set(obj, param);

            // 根据方法的命名规范，获取该属性的set方法
            // String methodName = "set" + fieldName;
            // Method method = classType.getMethod(methodName,
            // field.getType());//获取setter方法把值set到对象实例中
            // method.invoke(obj, param);
        }
        return obj;
    }

}
