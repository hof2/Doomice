package io.github.hof2.server;

import io.github.hof2.enums.Communications;
import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * An abstract server that can connect to multiple {@link Client Clients}
 * running on specific {@link Socket Sockets}. Information is transmitted as
 * {@link Serializable} or {@link Communications Communication}.
 */
abstract public class Server {

    private final int port;
    private ServerThread thread;
    private ServerSocket socket;

    /**
     * Sets the server port.
     *
     * @param port The server port.
     */
    public Server(int port) {
        this.port = port;
    }

    /**
     * Creates a new {@link Socket} and starts the {@link Server}.
     *
     * @throws IOException
     */
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

    /**
     * Stops the {@link Server} and tells all {@link Client Clients} to stop
     * too.
     *
     * @throws IOException
     */
    public final void stop() throws IOException {
        if (thread != null && thread.isAlive()) {
            onStop();
            broadcast(Communications.STOP);
            for (SessionThread session : thread.getSessions()) {
                session.interrupt();
            }
            thread.interrupt();
            socket.close();
        }
    }

    /**
     * Closes the connection to one specific {@link Client}.
     *
     * @param session The session of the {@link Client}.
     */
    public final void closeSession(SessionThread session) {
        onDisconnect(session);
        session.interrupt();
        thread.getSessions().remove(session);
    }

    /**
     * Gets the {@link Socket} of the {@link Server}
     *
     * @return The {@link Socket}.
     */
    public final ServerSocket getSocket() {
        return socket;
    }

    /**
     * Sends an {@link Serializable Object} to all {@link Client Clients}.
     *
     * @param object The {@link Serializable Object} to be sent.
     */
    public final void broadcast(Serializable object) {
        for (SessionThread session : thread.getSessions()) {
            session.send(object);
        }
    }

    /**
     * Sends an {@link Serializable Object} to all {@link Client Clients} except
     * one defined by a {@link SessionThread}.
     *
     * @param object The {@link Serializable Object} to be sent.
     */
    public final void broadcast(Serializable object, SessionThread origin) {
        for (SessionThread session : thread.getSessions()) {
            if (!session.equals(origin)) {
                session.send(object);
            }
        }
    }

    /**
     * Called when the server closes.
     */
    public void onClose() {
    }

    /**
     * Called when the server starts.
     */
    public void onStart() {
    }

    /**
     * Called when the server stops.
     */
    public void onStop() {
    }

    /**
     * Called when a specific {@link Client} is disconnected.
     *
     * @param session The {@link SessionThread} dedicated to the disconnected
     * client.
     */
    public void onDisconnect(SessionThread session) {
    }

    /**
     * Called when the {@link Server} connects to a {@link Client}.
     */
    public void onConnect(Socket socket) {
    }

    /**
     * Called when an {@link Exception} occurs.
     *
     * @param ex The {@link Exception}.
     */
    public abstract void onException(Exception ex);

    /**
     * Called to the determine what to send to a {@link Client} upon request. If
     * this method returns {@link Communications}{@code .DONT_ANSWER}, the
     * {@link Server} doesn't send anything.
     *
     * @param request The {@link Client} request.
     * @param origin The {@link SessionThread} which belongs to the
     * {@link Client}.
     * @return The response to be sent.
     */
    public abstract Serializable performResponse(Serializable request, SessionThread origin);
}
