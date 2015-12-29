package com.shuruta.sergey.ftpclient.event;


import com.shuruta.sergey.ftpclient.entity.Connection;

import de.greenrobot.event.EventBus;

import static com.shuruta.sergey.ftpclient.event.ConnectionEvent.State.*;

/**
 * Author: Sergey Shuruta
 * Date: 08/15/15
 * Time: 22:11
 */
public class ConnectionEvent {

    public interface EventListener {
        public void onEventMainThread(ConnectionEvent event);
        public void onConnectionStart(Connection connection);
        public void onConnectionError(Connection connection);
        public void onConnectionFinish(Connection connection);
    }

    public enum State {
        START,
        ERROR,
        FINISH,
    }

    private final State state;
    private final Connection connection;
    private String message;

    public static void send(State state, Connection connection) {
        send(state, connection, null);
    }

    public static void send(State state, Connection connection, String message) {
        EventBus.getDefault().post(new ConnectionEvent(state, connection, message));
    }

    private ConnectionEvent(State state, Connection connection, String message) {
        this.state = state;
        this.connection = connection;
        this.message = message;
    }

    public void setListener(EventListener eventListener) {
        switch (state) {
            case START:  eventListener.onConnectionStart(connection); break;
            case ERROR:  eventListener.onConnectionError(connection);break;
            case FINISH: eventListener.onConnectionFinish(connection); break;
        }
    }
}