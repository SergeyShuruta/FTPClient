package com.shuruta.sergey.ftpclient.event;


import com.shuruta.sergey.ftpclient.entity.Connection;
import de.greenrobot.event.EventBus;

/**
 * Author: Sergey Shuruta
 * Date: 08/15/15
 * Time: 22:11
 */
public class CommunicationEvent {

    public interface EventListener {
        public void onEventMainThread(CommunicationEvent event);
        public void onStart(Type type);
        public void onError(Type type, String message);
        public void onFinish(Type type);
    }

    public interface ConnectionEventListener {
        public void onEventMainThread(CommunicationEvent event);
        public void onConnectionStart(Connection connection);
        public void onConnectionError(Connection connection, String message);
        public void onConnectionFinish(Connection connection);
    }

    public interface DisconnectionEventListener {
        public void onEventMainThread(CommunicationEvent event);
        public void onDisconnectionStart();
        public void onDisconnectionError(String message);
        public void onDisconnectionFinish();
    }

    public interface ListReadEventListener {
        public void onEventMainThread(CommunicationEvent event);
        public void onListReadStart(int listTpe);
        public void onListReadError(int listTpe, String message);
        public void onListReadFinish(int listTpe);
        public void onListSelect(int listTpe);
    }

    public enum Type {
        CONNECTION,
        DISCONNECTION,
        LIST,
        DOWNLOAD,
    }

    public enum State {
        START,
        ERROR,
        FINISH,
        SELECT,
    }

    private final Type type;
    private final State state;
    private String message;
    private Object object;
    private int constant;

    public static void send(Type type, State state) {
        send(type, state, null);
    }

    public static void send(Type type, State state, String message) {
        EventBus.getDefault().post(new CommunicationEvent(type, state, message, null, 0));
    }

    public static void send(State state, Connection item) {
        send(state, item, null);
    }

    public static void send(State state, Connection item, String message) {
        EventBus.getDefault().post(new CommunicationEvent(Type.CONNECTION, state, item, message, 0));
    }

    public static void send(State state, int constant) {
        send(state, constant, null);
    }

    public static void send(State state, int constant, String message) {
        EventBus.getDefault().post(new CommunicationEvent(Type.LIST, state, null, message, constant));
    }

    public static void selectFilesPanel(int listType) {
        EventBus.getDefault().post(new CommunicationEvent(Type.LIST, State.SELECT, null, null, listType));
    }

    private CommunicationEvent(Type type, State state, Object object, String message, int constant) {
        this.type = type;
        this.state = state;
        this.object = object;
        this.message = message;
        this.constant = constant;
    }

    public int getConstant() {
        return constant;
    }

    public void setListener(EventListener eventListener) {
        switch (state) {
            case START:  eventListener.onStart(type); break;
            case ERROR:  eventListener.onError(type, message); break;
            case FINISH: eventListener.onFinish(type); break;
        }
    }

    public void setListener(ConnectionEventListener eventListener) {
        if(!type.equals(Type.CONNECTION)) return;
        switch (state) {
            case START:  eventListener.onConnectionStart((Connection) object); break;
            case ERROR:  eventListener.onConnectionError((Connection) object, message); break;
            case FINISH: eventListener.onConnectionFinish((Connection) object); break;
        }
    }

    public void setListener(DisconnectionEventListener eventListener) {
        if(!type.equals(Type.CONNECTION)) return;
        switch (state) {
            case START:  eventListener.onDisconnectionStart(); break;
            case ERROR:  eventListener.onDisconnectionError(message); break;
            case FINISH: eventListener.onDisconnectionFinish(); break;
        }
    }

    public void setListener(ListReadEventListener eventListener) {
        if(!type.equals(Type.LIST)) return;
        switch (state) {
            case SELECT: eventListener.onListSelect(constant); break;
            case START:  eventListener.onListReadStart(constant); break;
            case ERROR:  eventListener.onListReadError(constant, message); break;
            case FINISH: eventListener.onListReadFinish(constant); break;
        }
    }
}