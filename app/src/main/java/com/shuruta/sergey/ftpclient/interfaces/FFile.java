package com.shuruta.sergey.ftpclient.interfaces;

import android.graphics.drawable.Drawable;

/**
 * Created by Sergey Shuruta
 * 02.10.2015 at 15:31
 */
public interface FFile {
    public String getName();
    public long getSize();
    public String getFormatSize();
    public boolean isDir();
    public boolean isFile();
    public boolean isBackButton();
    public Drawable getIcon();
}
