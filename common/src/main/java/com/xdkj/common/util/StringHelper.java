package com.xdkj.common.util;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.util.ObjectUtils;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

public class StringHelper {
    private static final String FOLDER_SEPARATOR = "/";
    private static final String WINDOWS_FOLDER_SEPARATOR = "\\";
    private static final String TOP_PATH = "..";
    private static final String CURRENT_PATH = ".";
    private static final char EXTENSION_SEPARATOR = '.';
    private static Logger logger = Logger.getLogger(StringHelper.class);

    public StringHelper() {
        super();
        // constructor from parent
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static boolean isNotEmpty(String str) {
        return str != null && str.length() > 0;
    }

    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    public static boolean isNotBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return false;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }

        return false;
    }

    /**
     * 空值转化,默认将null转换成""
     */
    public static String nvl(String srcStr) {
        if (srcStr == null) {
            return "";
        } else {
            return srcStr;
        }
    }

    /**
     * 空值转化
     */
    public static String nvl(String srcStr, String replaceStr) {
        if (srcStr == null) {
            return replaceStr != null ? replaceStr : "";
        } else {
            return srcStr;
        }
    }

    /**
     * @param obj
     * @return
     * @description 对象空值转换
     */
    public static String nvl(Object obj) {
        return obj == null ? "" : obj.toString();
    }

    public static boolean equals(String str1, String str2) {
        return str1 != null ? str1.equals(str2) : str2 == null;
    }

    public static boolean equalsIgnoreCase(String str1, String str2) {
        return str1 != null ? str1.equalsIgnoreCase(str2) : str2 == null;
    }

    public static int indexOf(String str, char searchChar) {
        if (str == null || str.length() == 0) {
            return -1;
        } else {
            return str.indexOf(searchChar);
        }
    }

    public static int indexOf(String str, char searchChar, int startPos) {
        if (str == null || str.length() == 0) {
            return -1;
        } else {
            return str.indexOf(searchChar, startPos);
        }
    }

    public static int indexOf(String str, String searchStr) {
        if (str == null || searchStr == null) {
            return -1;
        } else {
            return str.indexOf(searchStr);
        }
    }

    public static int indexOf(String str, String searchStr, int startPos) {
        if (str == null || searchStr == null) {
            return -1;
        }
        if (searchStr.length() == 0 && startPos >= str.length()) {
            return str.length();
        } else {
            return str.indexOf(searchStr, startPos);
        }
    }

    public static int lastIndexOf(String str, char searchChar) {
        if (str == null || str.length() == 0) {
            return -1;
        } else {
            return str.lastIndexOf(searchChar);
        }
    }

    public static int lastIndexOf(String str, char searchChar, int startPos) {
        if (str == null || str.length() == 0) {
            return -1;
        } else {
            return str.lastIndexOf(searchChar, startPos);
        }
    }

    public static int lastIndexOf(String str, String searchStr) {
        if (str == null || searchStr == null) {
            return -1;
        } else {
            return str.lastIndexOf(searchStr);
        }
    }

    public static int lastIndexOf(String str, String searchStr, int startPos) {
        if (str == null || searchStr == null) {
            return -1;
        } else {
            return str.lastIndexOf(searchStr, startPos);
        }
    }

    public static boolean contains(String str, char searchChar) {
        if (str == null || str.length() == 0) {
            return false;
        } else {
            return str.indexOf(searchChar) >= 0;
        }
    }

    public static boolean contains(String str, String searchStr) {
        if (str == null || searchStr == null) {
            return false;
        } else {
            return str.indexOf(searchStr) >= 0;
        }
    }

    public static String substring(String str, int start) {
        if (str == null) {
            return null;
        }
        if (start < 0) {
            start = str.length() + start;
        }
        if (start < 0) {
            start = 0;
        }
        if (start > str.length()) {
            return "";
        } else {
            return str.substring(start);
        }
    }

    public static String substring(String str, int start, int end) {
        if (str == null) {
            return null;
        }
        if (end < 0) {
            end = str.length() + end;
        }
        if (start < 0) {
            start = str.length() + start;
        }
        if (end > str.length()) {
            end = str.length();
        }
        if (start > end) {
            return "";
        }
        if (start < 0) {
            start = 0;
        }
        if (end < 0) {
            end = 0;
        }
        return str.substring(start, end);
    }

    public static String[] split(String str, char separatorChar) {
        if (str == null) {
            return null;
        }
        int len = str.length();
        if (len == 0) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        List list = new ArrayList();
        int i = 0;
        int start = 0;
        boolean match = false;
        while (i < len) {
            if (str.charAt(i) == separatorChar) {
                if (match) {
                    list.add(str.substring(start, i));
                    match = false;
                }
                start = ++i;
            } else {
                match = true;
                i++;
            }
        }
        if (match) {
            list.add(str.substring(start, i));
        }
        return (String[]) list.toArray(new String[list.size()]);
    }

    public static String[] split(String str, String separatorChars) {
        return split(str, separatorChars, -1);
    }

    public static String[] split(String str, String separatorChars, int max) {
        if (str == null) {
            return null;
        }
        int len = str.length();
        if (len == 0) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        List list = new ArrayList();
        int sizePlus1 = 1;
        int i = 0;
        int start = 0;
        boolean match = false;
        if (separatorChars == null) {
            while (i < len) {
                if (Character.isWhitespace(str.charAt(i))) {
                    if (match) {
                        if (sizePlus1++ == max) {
                            i = len;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                    }
                    start = ++i;
                } else {
                    match = true;
                    i++;
                }
            }
        } else if (separatorChars.length() == 1) {
            char sep = separatorChars.charAt(0);
            while (i < len) {
                if (str.charAt(i) == sep) {
                    if (match) {
                        if (sizePlus1++ == max) {
                            i = len;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                    }
                    start = ++i;
                } else {
                    match = true;
                    i++;
                }
            }
        } else {
            while (i < len) {
                if (separatorChars.indexOf(str.charAt(i)) >= 0) {
                    if (match) {
                        if (sizePlus1++ == max) {
                            i = len;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                    }
                    start = ++i;
                } else {
                    match = true;
                    i++;
                }
            }
        }
        if (match) {
            list.add(str.substring(start, i));
        }
        return (String[]) list.toArray(new String[list.size()]);
    }

    public static String replaceOnce(String text, String repl, String with) {
        return replace(text, repl, with, 1);
    }

    public static String replace(String text, String repl, String with) {
        return replace(text, repl, with, -1);
    }

    public static String replace(String text, String repl, String with, int max) {
        if (text == null || repl == null || with == null || repl.length() == 0 || max == 0) {
            return text;
        }
        StringBuffer buf = new StringBuffer(text.length());
        int start = 0;
        int end = 0;
        do {
            if ((end = text.indexOf(repl, start)) == -1) {
                break;
            }
            buf.append(text.substring(start, end)).append(with);
            start = end + repl.length();
        } while (--max != 0);
        buf.append(text.substring(start));
        return buf.toString();
    }

    public static String replaceChars(String str, char searchChar, char replaceChar) {
        if (str == null) {
            return null;
        } else {
            return str.replace(searchChar, replaceChar);
        }
    }

    public static String replaceChars(String str, String searchChars, String replaceChars) {
        if (str == null || str.length() == 0 || searchChars == null || searchChars.length() == 0) {
            return str;
        }
        char[] chars = str.toCharArray();
        int len = chars.length;
        boolean modified = false;
        int i = 0;
        for (int isize = searchChars.length(); i < isize; i++) {
            char searchChar = searchChars.charAt(i);
            if (replaceChars == null || i >= replaceChars.length()) {
                int pos = 0;
                for (int j = 0; j < len; j++) {
                    if (chars[j] != searchChar) {
                        chars[pos++] = chars[j];
                    } else {
                        modified = true;
                    }
                }

                len = pos;
                continue;
            }
            for (int j = 0; j < len; j++) {
                if (chars[j] == searchChar) {
                    chars[j] = replaceChars.charAt(i);
                    modified = true;
                }
            }

        }

        if (!modified) {
            return str;
        } else {
            return new String(chars, 0, len);
        }
    }

    /**
     * Append the given String to the given String array, returning a new array
     * consisting of the input array contents plus the given String.
     *
     * @param array the array to append to (can be <code>null</code>)
     * @param str   the String to append
     * @return the new array (never <code>null</code>)
     */
    public static String[] addStringToArray(String[] array, String str) {
        if (ObjectUtils.isEmpty(array)) {
            return new String[]{str};
        }
        String[] newArr = new String[array.length + 1];
        System.arraycopy(array, 0, newArr, 0, array.length);
        newArr[array.length] = str;
        return newArr;
    }

    /**
     * Copy the given Collection into a String array. The Collection must
     * contain String elements only.
     *
     * @param collection the Collection to copy
     * @return the String array (<code>null</code> if the Collection was
     * <code>null</code> as well)
     */
    public static String[] toStringArray(Collection collection) {
        if (collection == null) {
            return null;
        }
        return (String[]) collection.toArray(new String[collection.size()]);
    }

    /**
     * Tokenize the given String into a String array via a StringTokenizer.
     * Trims tokens and omits empty tokens.
     * <p>
     * The given delimiters string is supposed to consist of any number of
     * delimiter characters. Each of those characters can be used to separate
     * tokens. A delimiter is always a single character; for multi-character
     * delimiters, consider using <code>delimitedListToStringArray</code>
     *
     * @param str        the String to tokenize
     * @param delimiters the delimiter characters, assembled as String (each of those
     *                   characters is individually considered as delimiter).
     * @return an array of the tokens
     * @see StringTokenizer
     * @see String#trim
     * @see #
     */
    public static String[] tokenizeToStringArray(String str, String delimiters) {
        return tokenizeToStringArray(str, delimiters, true, true);
    }

    /**
     * Tokenize the given String into a String array via a StringTokenizer.
     * <p>
     * The given delimiters string is supposed to consist of any number of
     * delimiter characters. Each of those characters can be used to separate
     * tokens. A delimiter is always a single character; for multi-character
     * delimiters, consider using <code>delimitedListToStringArray</code>
     *
     * @param str               the String to tokenize
     * @param delimiters        the delimiter characters, assembled as String (each of those
     *                          characters is individually considered as delimiter)
     * @param trimTokens        trim the tokens via String's <code>trim</code>
     * @param ignoreEmptyTokens omit empty tokens from the result array (only applies to
     *                          tokens that are empty after trimming; StringTokenizer will not
     *                          consider subsequent delimiters as token in the first place).
     * @return an array of the tokens
     * @see StringTokenizer
     * @see String#trim
     * @see #
     */
    public static String[] tokenizeToStringArray(String str, String delimiters, boolean trimTokens, boolean ignoreEmptyTokens) {

        StringTokenizer st = new StringTokenizer(str, delimiters);
        List tokens = new ArrayList();
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (trimTokens) {
                token = token.trim();
            }
            if (!ignoreEmptyTokens || token.length() > 0) {
                tokens.add(token);
            }
        }
        return toStringArray(tokens);
    }

    public static int lastIndexOfLetter(String string) {
        for (int i = 0; i < string.length(); i++) {
            char character = string.charAt(i);
            if (!Character.isLetter(character) /* && !('_'==character) */)
                return i - 1;
        }
        return string.length() - 1;
    }

    public static String join(String seperator, String[] strings) {
        int length = strings.length;
        if (length == 0)
            return "";
        StringBuffer buf = new StringBuffer(length * strings[0].length()).append(strings[0]);
        for (int i = 1; i < length; i++) {
            buf.append(seperator).append(strings[i]);
        }
        return buf.toString();
    }

    public static String join(String seperator, Iterator objects) {
        StringBuffer buf = new StringBuffer();
        if (objects.hasNext())
            buf.append(objects.next());
        while (objects.hasNext()) {
            buf.append(seperator).append(objects.next());
        }
        return buf.toString();
    }

    public static String[] add(String[] x, String sep, String[] y) {
        String[] result = new String[x.length];
        for (int i = 0; i < x.length; i++) {
            result[i] = x[i] + sep + y[i];
        }
        return result;
    }

    public static String repeat(String string, int times) {
        StringBuffer buf = new StringBuffer(string.length() * times);
        for (int i = 0; i < times; i++)
            buf.append(string);
        return buf.toString();
    }

    public static String replace(String template, String placeholder, String replacement, boolean wholeWords) {
        int loc = template == null ? -1 : template.indexOf(placeholder);
        if (loc < 0) {
            return template;
        } else {
            final boolean actuallyReplace = !wholeWords || loc + placeholder.length() == template.length() || !Character.isJavaIdentifierPart(template.charAt(loc + placeholder.length()));
            String actualReplacement = actuallyReplace ? replacement : placeholder;
            return new StringBuffer(template.substring(0, loc)).append(actualReplacement).append(replace(template.substring(loc + placeholder.length()), placeholder, replacement, wholeWords)).toString();
        }
    }

    public static String[] split(String seperators, String list, boolean include) {
        StringTokenizer tokens = new StringTokenizer(list, seperators, include);
        String[] result = new String[tokens.countTokens()];
        int i = 0;
        while (tokens.hasMoreTokens()) {
            result[i++] = tokens.nextToken();
        }
        return result;
    }

    public static String unqualify(String qualifiedName) {
        int loc = qualifiedName.toLowerCase().lastIndexOf(".");
        return (loc < 0) ? qualifiedName : qualifiedName.substring(qualifiedName.lastIndexOf(".") + 1);
    }

    public static String qualifier(String qualifiedName) {
        int loc = qualifiedName.lastIndexOf(".");
        return (loc < 0) ? "" : qualifiedName.substring(0, loc);
    }

    public static String[] suffix(String[] columns, String suffix) {
        if (suffix == null)
            return columns;
        String[] qualified = new String[columns.length];
        for (int i = 0; i < columns.length; i++) {
            qualified[i] = suffix(columns[i], suffix);
        }
        return qualified;
    }

    private static String suffix(String name, String suffix) {
        return (suffix == null) ? name : name + suffix;
    }

    public static String root(String qualifiedName) {
        int loc = qualifiedName.indexOf(".");
        return (loc < 0) ? qualifiedName : qualifiedName.substring(0, loc);
    }

    public static String unroot(String qualifiedName) {
        int loc = qualifiedName.indexOf(".");
        return (loc < 0) ? qualifiedName : qualifiedName.substring(loc + 1, qualifiedName.length());
    }

    public static boolean booleanValue(String tfString) {
        String trimmed = tfString.trim().toLowerCase();
        return trimmed.equals("true") || trimmed.equals("t");
    }

    public static String toString(Object[] array) {
        int len = array.length;
        if (len == 0)
            return "";
        StringBuffer buf = new StringBuffer(len * 12);
        for (int i = 0; i < len - 1; i++) {
            buf.append(array[i]).append(", ");
        }
        return buf.append(array[len - 1]).toString();
    }

    public static String toString(Object[] array, String separator) {
        int len = array.length;
        if (len == 0)
            return "";
        StringBuffer buf = new StringBuffer(len * 12);
        for (int i = 0; i < len - 1; i++) {
            buf.append(array[i]).append(separator);
        }
        return buf.append(array[len - 1]).toString();
    }

    public static int countUnquoted(String string, char character) {
        if ('\'' == character) {
            throw new IllegalArgumentException("Unquoted count of quotes is invalid");
        }
        if (string == null)
            return 0;
        // Impl note: takes advantage of the fact that an escpaed single quote
        // embedded within a quote-block can really be handled as two seperate
        // quote-blocks for the purposes of this method...
        int count = 0;
        int stringLength = string.length();
        boolean inQuote = false;
        for (int indx = 0; indx < stringLength; indx++) {
            char c = string.charAt(indx);
            if (inQuote) {
                if ('\'' == c) {
                    inQuote = false;
                }
            } else if ('\'' == c) {
                inQuote = true;
            } else if (c == character) {
                count++;
            }
        }
        return count;
    }

    public static int[] locateUnquoted(String string, char character) {
        if ('\'' == character) {
            throw new IllegalArgumentException("Unquoted count of quotes is invalid");
        }
        if (string == null) {
            return new int[0];
        }

        ArrayList locations = new ArrayList(20);

        // Impl note: takes advantage of the fact that an escpaed single quote
        // embedded within a quote-block can really be handled as two seperate
        // quote-blocks for the purposes of this method...
        int stringLength = string.length();
        boolean inQuote = false;
        for (int indx = 0; indx < stringLength; indx++) {
            char c = string.charAt(indx);
            if (inQuote) {
                if ('\'' == c) {
                    inQuote = false;
                }
            } else if ('\'' == c) {
                inQuote = true;
            } else if (c == character) {
                locations.add(new Integer(indx));
            }
        }
        return ArrayHelper.toIntArray(locations);
    }

    public static String qualify(String prefix, String name) {
        if (name == null || prefix == null) {
            throw new NullPointerException();
        }
        return new StringBuffer(prefix.length() + name.length() + 1).append(prefix).append('.').append(name).toString();
    }

    public static String[] qualify(String prefix, String[] names) {
        if (prefix == null)
            return names;
        int len = names.length;
        String[] qualified = new String[len];
        for (int i = 0; i < len; i++) {
            qualified[i] = qualify(prefix, names[i]);
        }
        return qualified;
    }

    public static int firstIndexOfChar(String sqlString, String string, int startindex) {
        int matchAt = -1;
        for (int i = 0; i < string.length(); i++) {
            int curMatch = sqlString.indexOf(string.charAt(i), startindex);
            if (curMatch >= 0) {
                if (matchAt == -1) { // first time we find match!
                    matchAt = curMatch;
                } else {
                    matchAt = Math.min(matchAt, curMatch);
                }
            }
        }
        return matchAt;
    }

    public static String truncate(String string, int length) {
        if (string.length() <= length) {
            return string;
        } else {
            return string.substring(0, length);
        }
    }

    public static String truncateByEllipsis(String string, int length) {
        if (string.length() <= length) {
            return string;
        } else {
            return string.substring(0, length) + "...";
        }
    }

    public static String unqualifyEntityName(String entityName) {
        String result = unqualify(entityName);
        int slashPos = result.indexOf('/');
        if (slashPos > 0) {
            result = result.substring(0, slashPos - 1);
        }
        return result;
    }

    public static String toUpperCase(String str) {
        return str == null ? null : str.toUpperCase();
    }

    public static String toLowerCase(String str) {
        return str == null ? null : str.toLowerCase();
    }

    public static String moveAndToBeginning(String filter) {
        if (filter.trim().length() > 0) {
            filter += " and ";
            if (filter.startsWith(" and "))
                filter = filter.substring(4);
        }
        return filter;
    }

    public static String genRandomCode(int length) {
        Random r = new Random();
        int i = 0;
        String str = "";
        String s = null;
        while (i < length) {
            switch (r.nextInt(58)) {
                // case (0) :
                // s = "0";
                // break;
                // case (1) :
                // s = "1";
                // break;
                case (0):
                    s = "2";
                    break;
                case (1):
                    s = "3";
                    break;
                case (2):
                    s = "4";
                    break;
                case (3):
                    s = "5";
                    break;
                case (4):
                    s = "6";
                    break;
                case (5):
                    s = "7";
                    break;
                case (6):
                    s = "8";
                    break;
                case (7):
                    s = "9";
                    break;
                case (8):
                    s = "a";
                    break;
                case (9):
                    s = "b";
                    break;
                case (10):
                    s = "c";
                    break;
                case (11):
                    s = "d";
                    break;
                case (12):
                    s = "e";
                    break;
                case (13):
                    s = "f";
                    break;
                case (14):
                    s = "g";
                    break;
                case (15):
                    s = "h";
                    break;
                case (16):
                    s = "i";
                    break;
                case (17):
                    s = "j";
                    break;
                case (18):
                    s = "k";
                    break;
                case (19):
                    s = "m";
                    break;
                case (20):
                    s = "n";
                    break;
                // case (21) :
                // s = "o";
                // break;
                case (21):
                    s = "p";
                    break;
                case (22):
                    s = "q";
                    break;
                case (23):
                    s = "r";
                    break;
                case (24):
                    s = "s";
                    break;
                case (25):
                    s = "t";
                    break;
                case (26):
                    s = "u";
                    break;
                case (27):
                    s = "v";
                    break;
                case (28):
                    s = "w";
                    break;
                // case (32) :
                // s = "l";
                // break;
                case (29):
                    s = "x";
                    break;
                case (30):
                    s = "y";
                    break;
                case (31):
                    s = "z";
                    break;
                case (32):
                    s = "A";
                    break;
                case (33):
                    s = "B";
                    break;
                case (34):
                    s = "C";
                    break;
                case (35):
                    s = "D";
                    break;
                case (36):
                    s = "E";
                    break;
                case (37):
                    s = "F";
                    break;
                case (38):
                    s = "G";
                    break;
                case (39):
                    s = "H";
                    break;
                case (40):
                    s = "I";
                    break;
                case (41):
                    s = "L";
                    break;
                case (42):
                    s = "J";
                    break;
                case (43):
                    s = "K";
                    break;
                case (44):
                    s = "M";
                    break;
                case (45):
                    s = "N";
                    break;
                case (46):
                    s = "O";
                    break;
                case (47):
                    s = "P";
                    break;
                case (48):
                    s = "Q";
                    break;
                case (49):
                    s = "R";
                    break;
                case (50):
                    s = "S";
                    break;
                case (51):
                    s = "T";
                    break;
                case (52):
                    s = "U";
                    break;
                case (53):
                    s = "V";
                    break;
                case (54):
                    s = "W";
                    break;
                case (55):
                    s = "X";
                    break;
                case (56):
                    s = "Y";
                    break;
                case (57):
                    s = "Z";
                    break;
            }
            i++;
            str = s + str;
        }
        // logger.info(str);
        return str;
    }

    public static String genRecommendCode(int length) {
        Random r = new Random();
        int i = 0;
        String str = "";
        String s = "";
        while (i < length) {
            switch (r.nextInt(32)) {
                // case (0) :
                // s = "0";
                // break;
                // case (1) :
                // s = "1";
                // break;
                case (0):
                    s = "2";
                    break;
                case (1):
                    s = "3";
                    break;
                case (2):
                    s = "4";
                    break;
                case (3):
                    s = "5";
                    break;
                case (4):
                    s = "6";
                    break;
                case (5):
                    s = "7";
                    break;
                case (6):
                    s = "8";
                    break;
                case (7):
                    s = "9";
                    break;
                case (8):
                    s = "a";
                    break;
                case (9):
                    s = "b";
                    break;
                case (10):
                    s = "c";
                    break;
                case (11):
                    s = "d";
                    break;
                case (12):
                    s = "e";
                    break;
                case (13):
                    s = "f";
                    break;
                case (14):
                    s = "g";
                    break;
                case (15):
                    s = "h";
                    break;
                case (16):
                    s = "i";
                    break;
                case (17):
                    s = "j";
                    break;
                case (18):
                    s = "k";
                    break;
                case (19):
                    s = "m";
                    break;
                case (20):
                    s = "n";
                    break;
                // case (21) :
                // s = "o";
                // break;
                case (21):
                    s = "p";
                    break;
                case (22):
                    s = "q";
                    break;
                case (23):
                    s = "r";
                    break;
                case (24):
                    s = "s";
                    break;
                case (25):
                    s = "t";
                    break;
                case (26):
                    s = "u";
                    break;
                case (27):
                    s = "v";
                    break;
                case (28):
                    s = "w";
                    break;
                // case (29) :
                // s = "l";
                // break;
                case (29):
                    s = "x";
                    break;
                case (30):
                    s = "y";
                    break;
                case (31):
                    s = "z";
                    break;
            }
            i++;
            str = s + str;
        }
        // logger.info(str);
        return str.toUpperCase();
    }

    public static String createNoFourNum(String str) {
        int num = 0;
        String s1 = str.substring(0, 1);
        String s2 = str.substring(1, 2);
        String s3 = str.substring(2, 3);
        String s4 = str.substring(3, 4);

        logger.info(s1 + s2 + s3 + s4);

        if (!s1.equals("0")) {
            num = Integer.parseInt(str) + 1;
            logger.info(num);
        } else {
            if (!s2.equals("0")) {
                num = Integer.parseInt(s2 + s3 + s4) + 1;
                logger.info(num);
            } else {
                if (!s3.equals("0")) {
                    num = Integer.parseInt(s3 + s4) + 1;
                    logger.info(num);
                } else {
                    if (!s4.equals("0")) {
                        num = Integer.parseInt(s4) + 1;
                        logger.info(num);
                    }
                }
            }
        }

        String newstr = String.valueOf(num);
        String result = "";
        if (newstr.length() == 4) {
            String a1 = newstr.substring(0, 1);
            if (a1.equals("4")) {
                a1 = "5";
            }
            String a2 = newstr.substring(1, 2);
            if (a2.equals("4")) {
                a2 = "5";
            }
            String a3 = newstr.substring(2, 3);
            if (a3.equals("4")) {
                a3 = "5";
            }
            String a4 = newstr.substring(3, 4);
            if (a4.equals("4")) {
                a4 = "5";
            }
            result = a1 + a2 + a3 + a4;
        }

        if (newstr.length() == 3) {
            String a1 = "0";
            String a2 = newstr.substring(0, 1);
            if (a2.equals("4")) {
                a2 = "5";
            }
            String a3 = newstr.substring(1, 2);
            if (a3.equals("4")) {
                a3 = "5";
            }
            String a4 = newstr.substring(2, 3);
            if (a4.equals("4")) {
                a4 = "5";
            }
            result = a1 + a2 + a3 + a4;
        }
        if (newstr.length() == 2) {
            String a1 = "0";
            String a2 = "0";
            String a3 = newstr.substring(0, 1);

            if (a3.equals("4")) {
                a3 = "5";
            }
            String a4 = newstr.substring(1, 2);

            if (a4.equals("4")) {
                a4 = "5";
            }
            result = a1 + a2 + a3 + a4;
        }

        if (newstr.length() == 1) {
            String a1 = "0";
            String a2 = "0";
            String a3 = "0";
            String a4 = newstr.substring(0, 1);
            if (a4.equals("4")) {
                a4 = "5";
            }

            result = a1 + a2 + a3 + a4;
        }

        return result;

    }

    /**
     * 得到一个字符串的长度,显示的长度,一个汉字或日韩文长度为2,英文字符长度为1
     *
     * @param s s 需要得到长度的字符串
     * @return int 得到的字符串长度
     */
    public static int length(String s) {
        if (s == null)
            return 0;
        char[] c = s.toCharArray();
        int len = 0;
        for (int i = 0; i < c.length; i++) {
            len++;
            if (!isLetter(c[i])) {
                len++;
            }
        }
        return len;
    }

    public static boolean isLetter(char c) {
        int k = 0x80;
        return c / k == 0 ? true : false;
    }

    public static String format(String srcStr, Map<String, String> params) {
        StringBuffer result = new StringBuffer();
        if (srcStr == null || "".equals(srcStr)) {
            throw new RuntimeException("格式化字符串时元字符串为空");
        }
        Pattern pattern = Pattern.compile("\\$\\{\\s*([a-z]{1}[a-zA-Z_$0-9]*)\\s*\\}");
        Matcher matcher = pattern.matcher(srcStr);
        while (matcher.find()) {
            String key = matcher.group(1);
            if (StringUtils.isEmpty(key)) {
                throw new RuntimeException("格式化字符串模板参数未定义");
            }
            String val = params.get(key);
            if (StringUtils.isEmpty(val)) {
                throw new RuntimeException("格式化字符串时参数为空");
            }
            matcher.appendReplacement(result, val);
        }
        matcher.appendTail(result);
        return result.toString();
    }

    public static String format(String srcStr, String... params) {
        StringBuffer result = new StringBuffer();
        if (srcStr == null || "".equals(srcStr)) {
            throw new RuntimeException("格式化字符串时元字符串为空");
        }
        Pattern pattern = Pattern.compile("\\$\\{\\s*([0-9]+)\\s*\\}");
        Matcher matcher = pattern.matcher(srcStr);
        while (matcher.find()) {
            String key = matcher.group(1);
            if (StringUtils.isEmpty(key)) {
                throw new RuntimeException("格式化字符串模板参数未定义");
            }
            String val = params[Integer.valueOf(key)];
            if (StringUtils.isEmpty(val)) {
                throw new RuntimeException("格式化字符串时参数为空");
            }
            matcher.appendReplacement(result, val);
        }
        matcher.appendTail(result);
        return result.toString();
    }

    /**
     * @param srcStr 源字符串
     * @param padStr 填充的字符串
     * @param len    填充后的长度
     * @return
     * @description 字符串左填充
     * @version 3.0
     * @author cyp
     * @update 2017年3月21日 下午5:50:36
     */
    public static String lpad(String srcStr, String padStr, int len) {
        StringBuilder joinStr = new StringBuilder();
        if (isEmpty(srcStr)) {
            return "";
        }
        if (srcStr.length() >= len) {
            return srcStr;
        }
        int length = len - srcStr.length();
        for (int i = 0; i < length; i++) {
            joinStr.append(padStr);
        }
        return joinStr + srcStr;
    }

    /**
     * @param srcStr 源字符串
     * @param padStr 填充的字符串
     * @param len    填充后的长度
     * @return
     * @description 字符串右填充
     * @version 3.0
     * @author cyp
     * @update 2017年3月21日 下午5:50:36
     */
    public static String rpad(String srcStr, String padStr, int len) {
        StringBuilder joinStr = new StringBuilder();
        if (isEmpty(srcStr)) {
            return "";
        }
        if (srcStr.length() >= len) {
            return srcStr;
        }
        int length = len - srcStr.length();
        for (int i = 0; i < length; i++) {
            joinStr.append(padStr);
        }
        return srcStr + joinStr;
    }

    /**
     * @param text
     * @param with
     * @param start
     * @param end
     * @return
     * @description 字符串固定位置用字符替换
     */
    public static String replaceWithStr(String text, String with, int start, int end) {
        if (StringHelper.isEmpty(text) || StringHelper.isEmpty(with)) {
            return text;
        }
        if (start < 0 || start >= text.length()) {
            return text;
        }
        if (end < start) {
            return text;
        }
        int size = end - start + 1;
        StringBuilder join = new StringBuilder(size);
        for (int i = 0; i < size; i++) {
            join.append(with);
        }
        return text.substring(0, start) + join + text.substring(end + 1);
    }

    /**
     * @param name
     * @return
     * @description 首字母大写
     */
    public static String captureName(String name) {
        // name = name.substring(0, 1).toUpperCase() + name.substring(1);
        // return name;
        char[] cs = name.toCharArray();
        if (cs[0] >= 97 && cs[0] <= 122) {
            cs[0] -= 32;
        }
        return String.valueOf(cs);

    }

    /**
     * @param str
     * @return
     * @description 去除字符串中的回车
     */
    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\n|\r\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    /**
     * @param str
     * @return
     * @description 去除字符串中的空格、回车、换行符、制表符
     */
    public static String replaceBlankAll(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }


    /**
     * 生成不重复随机字符串包括字母数字
     *
     * @param len
     * @return
     */
    public static String generateRandomStr(int len) {
        //字符源，可以根据需要删减
        String generateSource = "0123456789abcdefghigklmnopqrstuvwxyz";
        if (len > generateSource.length()) {
            throw new RuntimeException("生成不重复随机字符串产生异常");
        }
        String rtnStr = "";
        for (int i = 0; i < len; i++) {
            //循环随机获得当次字符，并移走选出的字符
            String nowStr = String.valueOf(generateSource.charAt((int) Math.floor(Math.random() * generateSource.length())));
            rtnStr += nowStr;
            generateSource = generateSource.replaceAll(nowStr, "");
        }
        return rtnStr;
    }

    /**
     * 字符串加密，隐藏其他位数，隐藏用*代替，例：123456789 要加密成 123****89，则pre传3，end传2
     *
     * @param str 要加密的字符串
     * @param pre 前面要保留的位数
     * @param end 后面要保留的位数
     * @return
     */
    public static String formatNameOrMobile(String str, int pre, int end) {
        //字符源，可以根据需要删减
        String encryptName = "";
        if (str != null || str != "") {
            // 1、加密
            encryptName = str.substring(0, pre);
            int len = str.length();
            for (int i = 0; i < len - pre - end; i++) {
                encryptName += "*";
            }
            encryptName += str.substring(len - end, len);
        }
        return encryptName;
    }


    /**
     * 获取UTF-8编码的字节长度
     *
     * @param value
     * @return
     */
    public static int getLengthByte(String value, String charsetName) {
        if (value == null || "".equals(value)) {
            return 0;
        }
        int length = 0;
        try {
            length = value.getBytes(charsetName).length;
        } catch (UnsupportedEncodingException e) {
            logger.error("获取编码的字节长度异常：编码" + charsetName + "不支持");
            e.printStackTrace();
        }
        return length;
    }

    /**
     * 获取UTF-8编码的字节长度
     *
     * @param value
     * @return
     */
    public static int getLengthByteOfUTF8(String value) {
        return getLengthByte(value, "UTF-8");
    }

    /**
     * 按字节截取字符串 ，指定截取起始字节位置与截取字节长度
     *
     * @param orignal 要截取的原字符串
     * @param start   截取的字节起始位置，从0开始，如果为负数，表示从倒数第几位开始
     * @param count   截取的字节长度
     * @return 截取的字符串
     */
    public static String substringByte(String orignal, int start, int count, String charsetName) {
        //如果目标字符串为空，则直接返回，不进入截取逻辑；
        if (orignal == null || "".equals(orignal)) return orignal;
        //截取Byte长度必须>0
        if (count <= 0) throw new RuntimeException("按字节截取字符串参数count非法");

        int lenAll = getLengthByte(orignal, charsetName);
        //起始字节位置必须小于最大值
        if (start >= lenAll || start < -lenAll) throw new RuntimeException("按字节截取字符串参数start非法");

        if (start < 0) {
            start = lenAll + start;
        }

        //目标char Pull buff缓存区间；
        StringBuffer buff = new StringBuffer();
        //遍历字节起始位置
        int byteStart = 0;
        //遍历字节结束位置
        int byteEnd = 0;

        int end = start + count;
        if (end > lenAll) {
            end = lenAll;
        }
        char c;
        try {
            //遍历String的每一个Char字符，计算当前总长度
            //如果到当前Char的的字节长度大于要截取的字符总长度，则跳出循环返回截取的字符串。
            for (int i = 0; i < orignal.toCharArray().length; i++) {
                c = orignal.charAt(i);
                byteEnd = byteStart + String.valueOf(c).getBytes(charsetName).length;
                if (byteStart < start) {
                    byteStart = byteEnd;
                    continue;
                }
                if (byteEnd > end) break;
                if (byteStart >= start && byteEnd <= end) {
                    buff.append(c);
                    byteStart = byteEnd;
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        return buff.toString();
    }


    public static String substringByteOfUTF8(String orignal, int start, int count) {
        return substringByte(orignal, start, count, "UTF-8");
    }

    /**
     * 数据库长度超限，截取处理
     *
     * @param value
     * @param maxLen
     * @return
     */
    public static String moreLengthOfDBProcess(String value, int maxLen) {
        if (value == null || "".equals(value)) {
            return "";
        }
        if (getLengthByteOfUTF8(value) > maxLen) {
            value = substringByteOfUTF8(value, 0, maxLen / 2 - 10) + "..." + substringByteOfUTF8(value, -maxLen / 2, maxLen / 2);
        }
        return value;
    }

    /**
     * @param target      要处理的字符串
     * @param replaceMent 需要替换后的字符,传空字符串代表去除
     * @Description: 将首尾空格、空行替换为replaceMent,
     * @Author: 徐赛平
     * @UpdateDate: 2018/12/19 17:37
     * @Version: 1.0
     */
    public static String replaceHeadAndEndTab(String target, String replaceMent) {
        Pattern pt = Pattern.compile("^\\s*|\\s*$");
        Matcher mt = pt.matcher(target);
        target = mt.replaceAll(replaceMent);
        return target;

    }


    public static void main(String[] args) {
        String textTemplate = "<xml>" + "<ToUserName><![CDATA[${0}]]></ToUserName>" + "<FromUserName><![CDATA[${1}]]></FromUserName>" + "<CreateTime>${2}</CreateTime>" + "<MsgType><![CDATA[text]]></MsgType>" + "<Content><![CDATA[${3}]]></Content>" + "</xml>";
        String str = StringHelper.format(textTemplate, "aaa", "bb", "cc", "dd");
        String encryptMobile = "";
        String name = "123456789";

        String id1 = formatNameOrMobile(name, 0, 4);
        System.out.println("id===" + id1);

        int len = name.length();
        //encryptMobile = name.substring(0,(len-4));
        for (int i = 0; i < len; i++) {
            encryptMobile += "*";
        }
        encryptMobile += name.substring(len - 4, len);


        System.out.println("0encryptMobile===" + encryptMobile);
        logger.info(lpad("2", "0", 4));
        logger.info(lpad("22", "0", 4));
        logger.info(lpad("222", "0", 4));
        logger.info(lpad("2222", "0", 4));
        logger.info(str);

    }
}
