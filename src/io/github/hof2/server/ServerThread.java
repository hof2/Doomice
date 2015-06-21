package io.github.hof2.server;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A {@link Thread} used by the {@link Server} to communicate with
 * {@link Client Clients}. For every new {@link Client}, a new
 * {@link SessionThread} is started.
 */
public class ServerThread extends Thread {

    private final Server server;
    private final CopyOnWriteArrayList<SessionThread> sessions = new CopyOnWriteArrayList<>();

    /**
     * Sets the {@link Server} parent.
     *
     * @param server The {@link Server}.
     */
    public ServerThread(Server server) {
        this.server = server;
    }

    /**
     * The main loop. For every new {@link Client}, a new {@link SessionThread}
     * is created.
     */
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

    /**
     * Gets all {@link Session Sessions} which are currently open.
     *
     * @return the {@link CopyOnWriteArrayList List} filled with active
     * {@link Session Sessions}.
     */
    public CopyOnWriteArrayList<SessionThread> getSessions() {
        return sessions;
    }
}
