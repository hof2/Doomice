package io.github.hof2.server;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.CopyOnWriteArrayList;

public class ServerThread extends Thread {

    private final Server server;
    private final CopyOnWriteArrayList<SessionThread> sessions = new CopyOnWriteArrayList<>();

    public ServerThread(Server server) {
        this.server = server;
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                Socket socket = server.getSocket().accept();
                SessionThread session = new SessionThread(socket, server);
                sessions.add(session);
                session.start();
                server.onConnect(socket);
            } catch (SocketException ex) {
                //this exception occurrs once, when the socket is closed by the server.
                interrupt();
            } catch (IOException ex) {
                server.onException(ex);
            }
        }
        try {
            server.getSocket().close();
            server.onClose();
        } catch (IOException ex) {
            server.onException(ex);
        }
    }

    public CopyOnWriteArrayList<SessionThread> getSessions() {
        return sessions;
    }

}
