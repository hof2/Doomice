package io.github.hof2.enums;

import io.github.hof2.client.Client;
import io.github.hof2.server.Server;

/**
 * @author Christoph Minixhofer
 * @since May 22, 2015
 * Used for communication between {@link Server} and {@link Client}.
 */
public enum Communications {
    /**
     * Stop the connection.
     */
    STOP, 
    /**
     * Don't answer a request.
     */
    DONT_ANSWER,
    /**
     * Requests an update.
     */
    UPDATE
}
