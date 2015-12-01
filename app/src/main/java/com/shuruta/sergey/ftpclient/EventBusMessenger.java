package com.shuruta.sergey.ftpclient;

import android.os.Bundle;

import de.greenrobot.event.EventBus;

/**
 * Author: Sergey Shuruta
 * Date: 08/15/15
 * Time: 22:11
 */
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

    public enum Event {
        START,
        OK,
        ERROR,
        FINISH,
        SELECT,
        REFRESH,
        BACK,
        CLOSE,
        DISCONNECTED,
        DISCONNECT_ERROR,
    }
}