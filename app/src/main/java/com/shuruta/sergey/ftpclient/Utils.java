package com.shuruta.sergey.ftpclient;

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
}
