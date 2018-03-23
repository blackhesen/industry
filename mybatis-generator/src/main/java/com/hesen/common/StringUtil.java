package com.hesen.common;

import java.util.StringTokenizer;

public class StringUtil {


    public static String nvl(String value) {
        if (value == null) {
            return "";
        } else {
            return value.trim();
        }
    }


    public static String zeroToSpace(String s) {
        boolean allZero = true;

        if (s == null) {
            return "";
        }
        s = s.trim();
        if ("".equals(s)) {
            return "";
        }


        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '0') {
                continue;
            } else {
                allZero = false;
                break;
            }
        }

        // result deal
        if (allZero == true) {
            return "";
        } else {
            return s;
        }
    }


    private StringUtil() {}


    public static String[] str2Array(String str) {
        return str2Array(str, ",");
    }


    public static String[] str2Array(String str, String sep) {
        StringTokenizer token = null;
        String[] array = null;

        // check
        if (str == null || sep == null) {
            return null;
        }

        // get string array
        token = new StringTokenizer(str, sep);
        array = new String[token.countTokens()];
        for (int i = 0; token.hasMoreTokens(); i++) {
            array[i] = token.nextToken();
        }

        return array;
    }

    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }



}
