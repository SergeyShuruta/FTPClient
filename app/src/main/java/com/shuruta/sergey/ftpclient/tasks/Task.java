package com.shuruta.sergey.ftpclient.tasks;

import android.content.Context;

/**
 * Author: Sergey Shuruta
 * Date: 10/17/13
 * Time: 3:04 PM
 */
public abstract class Task implements Runnable {

    private Context mContext;

    protected Task(Context context) {
        mContext = context;
    }

}