package com.shuruta.sergey.ftpclient.event;


import com.shuruta.sergey.ftpclient.entity.Connection;
import com.shuruta.sergey.ftpclient.entity.DFile;

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
        public void onDisconnectionFinish(boolean isCloseSession);
    }

    public interface ListReadEventListener {
        public void onEventMainThread(CommunicationEvent event);
        public void onListReadStart(int listTpe);
        public void onListReadError(int listTpe, String message);
        public void onListReadFinish(int listTpe);
        public void onListSelect(int listTpe);
    }

    public interface PreDownloadEventListener {
        public void onEventMainThread(CommunicationEvent event);
        public void onPreDownloadStart();
        public void onPreDownloadError(String message);
        public void onPreDownloadFinish();
    }

    public interface DownloadEventListener {
        public void onEventMainThread(CommunicationEvent event);
        public void onDownloadStart();
        public void onDownloadError(String message);
        public void onDownloadFinish();
    }

    public interface FileDownloadEventListener {
        public void onEventMainThread(CommunicationEvent event);
        public void onFileDownloadStart(DFile dFile);
        public void onFileDownloadProgress(DFile dFile);
        public void onFileDownloadAborted(DFile dFile);
        public void onFileDownloadError(DFile dFile, String message);
        public void onFileDownloadFinish(DFile dFile);
    }


    public enum Type {
        CONNECTION,
        DISCONNECTION,
        LIST,
        PREDOWNLOAD,
        DOWNLOAD,
        FILE_DOWNLOAD,
    }

    public enum State {
        START,
        ERROR,
        FINISH,
        SELECT,
        PROGRESS,
        ABORTED,
    }

    public final Type type;
    public final State state;
    private String message;
    private Object object;
    private int constant;

    public static void send(Type type, State state) {
        send(type, state, null);
    }

    public static void send(Type type, State state, String message) {
        EventBus.getDefault().post(new CommunicationEvent(type, state, message, null, 0));
    }

    public static void sendDisconnection(State state, boolean isCloseSession) {
        sendDisconnection(state, null, isCloseSession);
    }

    public static void sendDisconnection(State state, String message, boolean isCloseSession) {
        EventBus.getDefault().post(new CommunicationEvent(Type.DISCONNECTION, state, null, message, isCloseSession ? 1 : 0));
    }

    public static void sendConnection(State state, Connection item) {
        sendConnection(state, item, null);
    }

    public static void sendConnection(State state, Connection item, String message) {
        EventBus.getDefault().post(new CommunicationEvent(Type.CONNECTION, state, item, message, 0));
    }

    public static void sendReadList(State state, int constant) {
        sendReadList(state, constant, null);
    }

    public static void sendReadList(State state, int constant, String message) {
        EventBus.getDefault().post(new CommunicationEvent(Type.LIST, state, null, message, constant));
    }

    public static void selectFilesPanel(int listType) {
        EventBus.getDefault().post(new CommunicationEvent(Type.LIST, State.SELECT, null, null, listType));
    }

    public static void sendPreDownload(State state) {
        sendPreDownload(state, null);
    }

    public static void sendPreDownload(State state, String message) {
        EventBus.getDefault().post(new CommunicationEvent(Type.PREDOWNLOAD, state, null, message, 0));
    }

    public static void sendDownload(State state) {
        sendDownload(state, null);
    }

    public static void sendDownload(State state, String message) {
        EventBus.getDefault().post(new CommunicationEvent(Type.DOWNLOAD, state, null, message, 0));
    }

    public static void sendFileDownload(State state, DFile dFile) {
        sendFileDownload(state, dFile, null, 0);
    }

    public static void sendFileDownload(State state, DFile dFile, String message) {
        sendFileDownload(state, dFile, message, 0);
    }

    public static void sendFileDownload(State state, DFile dFile, int progress) {
        sendFileDownload(state, dFile, null, progress);
    }

    public static void sendFileDownload(State state, DFile dFile, String message, int progress) {
        EventBus.getDefault().post(new CommunicationEvent(Type.FILE_DOWNLOAD, state, dFile, message, progress));
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
        if(!type.equals(Type.DISCONNECTION)) return;
        switch (state) {
            case START:  eventListener.onDisconnectionStart(); break;
            case ERROR:  eventListener.onDisconnectionError(message); break;
            case FINISH: eventListener.onDisconnectionFinish(constant > 0); break;
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

    public void setListener(PreDownloadEventListener eventListener) {
        if(!type.equals(Type.PREDOWNLOAD)) return;
        Log.d("TEST", "State: " + state);
        switch (state) {
            case START:  eventListener.onPreDownloadStart(); break;
            case ERROR:  eventListener.onPreDownloadError(message); break;
            case FINISH: eventListener.onPreDownloadFinish(); break;
        }
    }

    public void setListener(DownloadEventListener eventListener) {
        if(!type.equals(Type.DOWNLOAD)) return;
        switch (state) {
            case START:  eventListener.onDownloadStart(); break;
            case ERROR:  eventListener.onDownloadError(message); break;
            case FINISH: eventListener.onDownloadFinish(); break;
        }
    }

    public void setListener(FileDownloadEventListener eventListener) {
        if(!type.equals(Type.FILE_DOWNLOAD)) return;
        switch (state) {
            case START:    eventListener.onFileDownloadStart((DFile) object); break;
            case PROGRESS: eventListener.onFileDownloadProgress((DFile) object); break;
            case ERROR:    eventListener.onFileDownloadError((DFile) object, message); break;
            case PRESENT:  eventListener.onFileDownloadPresent((DFile) object); break;
            case FINISH:   eventListener.onFileDownloadFinish((DFile) object); break;
        }
    }
}