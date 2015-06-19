package io.github.hof2.server;

import io.github.hof2.enums.Communications;
import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;

abstract public class Server {

    private final int port;

    private ServerThread thread;
    private ServerSocket socket;

    public Server(int port) {
        this.port = port;
    }

    public final void start() throws IOException {
        if (thread == null || !thread.isAlive()) {
            //init socket
            socket = new ServerSocket(port);

            //start new server thread
            thread = new ServerThread(this);
            thread.start();

            onStart();
        }
    }

    public final void stop() throws IOException {
        if (thread != null && thread.isAlive()) {
            onStop();
            broadcast(Communications.STOP);
            for (SessionThread session : thread.getSessions()) {
                session.interrupt();
            }
            thread.interrupt();
        }
    }

    public final void closeSession(SessionThread session) {
        onDisconnect(session);
        session.interrupt();
        thread.getSessions().remove(session);
    }

    public final ServerSocket getSocket() {
        return socket;
    }

    public final void broadcast(Serializable object) {
        for (SessionThread session : thread.getSessions()) {
            session.send(object);
        }
    }

    public final void broadcast(Serializable object, SessionThread origin) {
        for (SessionThread session : thread.getSessions()) {
            if (!session.equals(origin)) {
                session.send(object);
            }
        }
    }

    public void onClose() {
    }

    public void onStart() {
    }

    public void onStop() {
    }

    public void onDisconnect(SessionThread session) {
    }

    public void onConnect(Socket socket) {
    }

    public abstract void onException(Exception ex);

    public abstract Serializable performResponse(Serializable request, SessionThread origin);
}
