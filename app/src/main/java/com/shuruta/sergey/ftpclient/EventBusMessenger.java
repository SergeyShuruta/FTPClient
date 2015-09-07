package com.shuruta.sergey.ftpclient;

/**
 * Author: Sergey Shuruta
 * Date: 08/15/15
 * Time: 22:11
 */
public class EventBusMessenger {

    public final long conId;
    public final State state;

    public EventBusMessenger(long conId, State state) {
        this.conId = conId;
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
    }
}