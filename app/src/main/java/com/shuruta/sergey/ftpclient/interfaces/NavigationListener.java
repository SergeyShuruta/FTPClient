package com.shuruta.sergey.ftpclient.interfaces;

/**
 * Created by Sergey Shuruta
 * 29.12.2015 at 15:21
 */
public interface NavigationListener {
    public void onListRefresh();
    public void onListBack();
    public void onDisconnect();
    public void readList();
}
