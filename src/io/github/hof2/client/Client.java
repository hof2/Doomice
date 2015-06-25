package io.github.hof2.client;

import io.github.hof2.enums.Communications;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

/**
 * @author Christoph Minixhofer
 * @since May 22,2015
 * An abstract client that can connect to a {@link Server} running on a specific
 * {@link Socket}. Information is transmitted as {@link Serializable} or
 * {@link Communications Communication}.
 */
public abstract class Client {

    private final Socket socket;
    private final ObjectOutputStream outputStream;
    private final ObjectInputStream inputStream;
    private ClientThread thread;
    private boolean closed = true;

    /**
     * Initializes a connection to specified socket.
     *
     * @param socket The {@link Socket} to connect to.
     * @throws IOException If the {@link Socket} doesn't exist.
     */
    public Client(Socket socket) throws IOException {
        this.socket = socket;
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        inputStream = new ObjectInputStream(socket.getInputStream());
    }

    /**
     * Sends a {@link Serializable Object} to the Server and returns the
     * response from the {@link Server}. Calls {@code send} to send the object
     * and {@code getNextObject} to return the response.
     *
     * @param object The {@link Serializable Object} to be sent.
     * @return The {@link Object Answer} from the {@link Server}.
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws InterruptedException
     */
    public final Object performRequest(Serializable object) throws IOException, ClassNotFoundException, InterruptedException {
        send(object);
        return getNextObject();
    }

    /**
     * Returns the next thing the {@link Server} sends to the {@link Client}.
     *
     * @return The server message.
     * @throws InterruptedException
     */
    public final synchronized Object getNextObject() throws InterruptedException {
        thread.requestObject();
        wait();
        return thread.getRequestedObject();
    }

    /**
     * Sends an {@link Serializable Object} to the {@link Server}.
     *
     * @param object The {@link Serializable Object} to be sent.
     * @throws IOException
     */
    public final void send(Serializable object) throws IOException {
        outputStream.writeUnshared(object);
    }

    /**
     * Gets the {@link Socket} used to communicate.
     *
     * @return The {@link Socket}.
     */
    public final Socket getSocket() {
        return socket;
    }

    /**
     * Starts the {@link ClientThread}.
     *
     * @throws IOException
     */
    public final void start() throws IOException {
        if (thread == null || !thread.isAlive()) {
            thread = new ClientThread(this);
            thread.start();
            closed = false;
        }
    }

    /**
     * Stops the {@link ClientThread} and sends a
     * {@link Communications}{@code .STOP} message to its {@link Server}.
     *
     * @throws IOException
     */
    public final void stop() throws IOException {
        if (thread != null && thread.isAlive()) {
            send(Communications.STOP);
            close();
        }
    }

    /**
     * Stops the {@link ClientThread} and closes the {@link Socket}. Called by
     * {@code stop} to close the {@link ClientThread} and the {@link Socket}.
     *
     * @throws IOException
     */
    public final void close() throws IOException {
        onClose();
        thread.interrupt();
        socket.close();
        closed = true;
    }

    /**
     * Gets the {@link ObjectOutputStream} used to communicate.
     *
     * @return The {@link ObjectOutputStream}.
     */
    public final ObjectOutputStream getOutputStream() {
        return outputStream;
    }

    /**
     * Gets the {@link ObjectInputStream} used to communicate.
     *
     * @return The {@link ObjectInputStream}.
     */
    public final ObjectInputStream getInputStream() {
        return inputStream;
    }

    /**
     * Called when an {@link Exception} occurs.
     *
     * @param ex The {@link Exception}.
     */
    public abstract void onException(Exception ex);

    /**
     * Called when the client closes.
     */
    public void onClose() {
    }

    /**
     * Used to verify if the client is currently closed.
     *
     * @return True when the client is closed, False when it's running.
     */
    public final boolean isClosed() {
        return closed;
    }

    /**
     * Sets the closed-status of the client. Mainly called by the
     * {@link ClientThread} when it stops.
     *
     * @param closed the closed-status.
     */
    public final void setClose(boolean closed) {
        this.closed = closed;
    }

    /**
     * Called when receiving an {@link Object} from the {@link Server}. Not
     * called when the server answers to {@code performRequest}.
     *
     * @param object The received {@link Object}.
     */
    public void receive(Object object) {
    }
}
