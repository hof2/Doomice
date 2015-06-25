package io.github.hof2.client;

import io.github.hof2.enums.Communications;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.SocketException;

/**
 * @author Christoph Minixhofer
 * @since May 22, 2015
 * A {@link Thread} used by {@link Client} to communicate with the
 * {@link Server}. Requested objects can be retrieved with
 * {@code getRequestedObject}, while unrequested object will lead to a call of
 * {@link Client}{@code .receive}.
 */
public class ClientThread extends Thread {

    private final Client client;
    private final ObjectInputStream inputStream;
    private boolean request;
    private Object requestedObject;

    /**
     * Initializes the client by setting a parent {@link Client} and its
     * {@link ObjectInputStream}.
     *
     * @param client The {@link Client} this {@link Thread} belongs to.
     * @throws IOException
     */
    public ClientThread(Client client) throws IOException {
        this.client = client;
        inputStream = client.getInputStream();
    }

    /**
     * The main loop of the thread. Uses locking to differentiate between
     * answers to requests and unrequest messages. Closes the {@link Client} if
     * the {@link Server} sends a {@link Communications}{@code .STOP} object.
     */
    @Override
    public void run() {
        try {
            while (!isInterrupted()) {
                Object message = inputStream.readObject();
                if (message.equals(Communications.STOP)) {
                    client.close();
                } else {
                    if (request) {
                        synchronized (client) {
                            requestedObject = message;
                            request = false;
                            client.notifyAll();
                        }
                    } else {
                        client.receive(message);
                    }
                }
            }
        } catch (SocketException ex) {
        } catch (IOException | ClassNotFoundException ex) {
            client.onException(ex);
        } finally {
            try {
                client.getSocket().close();
            } catch (IOException ex) {
                client.onException(ex);
            }
        }
    }

    /**
     * Tells the {@link ClientThread} to wait for a server response for the
     * latest request and not call {@link Client}{@code .receive}.
     */
    public void requestObject() {
        request = true;
    }

    /**
     * Used to retrieve a response to a certain request sent to the
     * {@link Server}.
     *
     * @return The requested object.
     */
    public Object getRequestedObject() {
        return requestedObject;
    }
}
