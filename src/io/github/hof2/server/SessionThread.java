package io.github.hof2.server;

import io.github.hof2.client.Client;
import io.github.hof2.enums.Communications;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.SocketException;

/**
 * Handles a single connection (session) to a {@link Client}.
 */
public class SessionThread extends Thread {

    private final Socket socket;
    private final Server server;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    /**
     * Starts a new {@link SessionThread Session} using specified {@link Socket}
     * and {@link Server}.
     *
     * @param socket the {@link Socket} to use.
     * @param server the parent {@link Server}.
     */
    public SessionThread(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    /**
     * Reads incoming messages and calls {@code performResponse}, to determine
     * what to answer. Doesn't answer if {@code performResponse} returns
     * {@link Communications}{@code .DONT_ANSWER}. Closes the session if the
     * {@link Client} sends {@link Communications}{@code .STOP}.
     */
    @Override
    public void run() {
        try {
            inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            while (!isInterrupted()) {
                Object message = inputStream.readObject();
                if (message.equals(Communications.STOP)) {
                    interrupt();
                    server.closeSession(this);
                } else {
                    Serializable response = server.performResponse((Serializable) message, this);
                    if (!response.equals(Communications.DONT_ANSWER)) {
                        send(response);
                    }
                }
            }
        } catch (SocketException | EOFException ex) {
            //this has to occur when closing the connection
        } catch (IOException | ClassNotFoundException ex) {
            server.onException(ex);
        } finally {
            try {
                socket.close();
            } catch (IOException ex) {
                server.onException(ex);
            }
        }
    }

    /**
     * Sends an object to the {@link Client}.
     * @param object the object to send.
     */
    public void send(Serializable object) {
        try {
            outputStream.writeUnshared(object);
        } catch (IOException ex) {
            server.closeSession(this);
        }
    }

    /**
     * Gets the used {@link Socket}.
     * @return the {@link Socket}.
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * Closes the {@link SessionThread Session}.
     * @throws IOException if the socket has already been closed.
     */
    public void close() throws IOException {
        send(Communications.STOP);
        socket.close();
    }
}
