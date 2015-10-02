package com.shuruta.sergey.ftpclient.utils;

import java.text.DecimalFormat;

/**
 * Created by Sergey Shuruta
 * 13.08.2015 at 15:59
 */
public class Utils {

    public static String trim(String str, String cStr) {
        if(null == str || str.length() == 0 || cStr.length() > 1) return str;
        Character c = cStr.charAt(0);
        str = str.trim();
        if (str.charAt(str.length()-1) == c) {
            str = str.substring(0, str.length()-1);
        }
        if (str.charAt(0) == c) {
            str = str.substring(1);
        }
        return str;
    }

    public static String readableFileSize(long size) {
        if(size <= 0) return "0";
        final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
}
