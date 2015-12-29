/*
package com.shuruta.sergey.ftpclient.event;

import android.os.Bundle;

import com.shuruta.sergey.ftpclient.Constants;

import de.greenrobot.event.EventBus;

*/
/**
 * Author: Sergey Shuruta
 * Date: 08/15/15
 * Time: 22:11
 *//*

public class EventBusMessenger {

    private final Integer type;
    public final Event event;
    public final Bundle bundle;

    public static final String MSG = "message";

    public static void sendMessage(int type, Event event, Bundle bundle) {
        EventBus.getDefault().post(new EventBusMessenger(type, event, bundle));
    }

    public static void sendMessage(int type, Event event) {
        EventBus.getDefault().post(new EventBusMessenger(type, event, null));
    }

    public static void sendConnectionMessage(Event event, Bundle bundle) {
        EventBus.getDefault().post(new EventBusMessenger(Constants.TYPE_CONNECTION, event, bundle));
    }

    public static void sendConnectionMessage(Event event) {
        EventBus.getDefault().post(new EventBusMessenger(Constants.TYPE_CONNECTION, event, null));
    }

    public static void sendFtpMessage(Event event, Bundle bundle) {
        EventBus.getDefault().post(new EventBusMessenger(Constants.TYPE_FTP, event, bundle));
    }

    public static void sendFtpMessage(Event event) {
        EventBus.getDefault().post(new EventBusMessenger(Constants.TYPE_FTP, event, null));
    }

    public static void sendLocalMessage(Event event, Bundle bundle) {
        EventBus.getDefault().post(new EventBusMessenger(Constants.TYPE_LOCAL, event, bundle));
    }

    public static void sendLocalMessage(Event event) {
        EventBus.getDefault().post(new EventBusMessenger(Constants.TYPE_LOCAL, event, null));
    }

    private EventBusMessenger(int type, Event event, Bundle bundle) {
        this.type = type;
        this.event = event;
        this.bundle = bundle;
    }

    public boolean isValidListType(int type) {
        return this.type.equals(type);
    }

    public int getType() {
        return type;
    }

    public enum ConnectionEvent {
        START,
        ERROR,
        FINISH,
        DISCONNECT_START,
        DISCONNECT_ERROR,
        DISCONNECT_FINISHED,
    }

    public enum FTPEvent {
        START,
        ERROR,
        FINISH,
    }

    public enum DownloadEvent {
        PREPARE_START,
        PREPARE_ERROR,
        PREPARE_FINISH,
        START,
        ERROR,
        FINISH,
    }

    public enum ListEvent {
        SELECT,
        REFRESH,
        BACK,
        DISCONNECT,
    }


    public enum Event {
        START,
        OK,
        ERROR,
        FINISH,
        START_PREDOWNLOAD,
        ERROR_PREDOWNLOAD,
        FINISH_PREDOWNLOAD,
        START_DOWNLOAD,
        ERROR_DOWNLOAD,
        FINISH_DOWNLOAD,
        SELECT,
        REFRESH,
        BACK,
        CLOSE,
        DISCONNECT_FINISHED,
        DISCONNECT_ERROR,
    }
}*/
