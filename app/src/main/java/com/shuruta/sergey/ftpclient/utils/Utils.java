package com.shuruta.sergey.ftpclient.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.shuruta.sergey.ftpclient.Constants;
import com.shuruta.sergey.ftpclient.adapters.FTPFileAdapter;
import com.shuruta.sergey.ftpclient.cache.CacheManager;
import com.shuruta.sergey.ftpclient.interfaces.FFile;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import it.sauronsoftware.ftp4j.FTPFile;

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

    public static void sortFiles(List<FFile> files) {
        Collections.sort(files, new Comparator<FFile>() {
            @Override
            public int compare(final FFile object1, final FFile object2) {
                if (object1.isDir() && object2.isFile())
                    return -1;
                if (object1.isFile() && object2.isDir())
                    return 1;
                return object1.getName().compareTo(object2.getName());
            }
        });
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        }
        return false;
    }
}
