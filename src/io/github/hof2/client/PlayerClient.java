package io.github.hof2.client;

import java.io.IOException;
import java.net.Socket;

/**
 * @author Christoph Minixhofer
 * @since June 15, 2015
 * Implementation of the {@link Client}. Logs {@link Exception Exceptions} to the console.
 */
public class PlayerClient extends Client {

    /**
     * Creates a new {@link Client} and tries to establish a {@link Socket} connection.
     * @param socket the {@link Socket} to connect to.
     * @throws IOException when the connection fails.
     */
    public PlayerClient(Socket socket) throws IOException {
        super(socket);
    }

    /**
     * Logs {@link Exception Exceptions} to the console.
     *
     * @param ex the {@link Exception Exception}
     */
    @Override
    public void onException(Exception ex) {
        System.out.println("Error: " + ex);
    }
}
