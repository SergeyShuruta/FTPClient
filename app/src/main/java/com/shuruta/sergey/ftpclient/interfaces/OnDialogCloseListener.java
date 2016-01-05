package com.shuruta.sergey.ftpclient.interfaces;

/**
 * Created by Sergey on 05.01.2016.
 */
public interface OnDialogCloseListener {
    enum State {
        FINISH,
        ERROR,
    };
    public void onCloseDialog(State state);
}
