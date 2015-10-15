package com.shuruta.sergey.ftpclient;

import android.os.Bundle;

/**
 * Author: Sergey Shuruta
 * Date: 08/15/15
 * Time: 22:11
 */
public class EventBusMessenger {

    public final Bundle bandle;
    public final State state;

    public EventBusMessenger(State state) {
        this(null, state);
    }
    public EventBusMessenger(Bundle bandle, State state) {
        this.bandle = bandle;
        this.state = state;
    }

    public enum State {
        CONNECTION_START,
        CONNECTION_OK,
        CONNECTION_ERROR,
        CONNECTION_ERROR_AUTHORIZATION,
        CONNECTION_FINISH,
        READ_FTP_LIST_START,
        READ_FTP_LIST_OK,
        READ_FTP_LIST_ERROR,
        READ_FTP_LIST_FINISH,
        SELECT_FTP,
        SELECT_LOCAL,
        REFRESH,
        BACK,
    }
}