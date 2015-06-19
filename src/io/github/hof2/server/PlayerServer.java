package io.github.hof2.server;

import io.github.hof2.client.PlayerClient;
import io.github.hof2.collection.Player;
import io.github.hof2.enums.Communications;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Implementation of the {@link Client}. Logs {@link Exception Exceptions} to
 * the console. Handles the distribution of all multiplayer data.
 */
public class PlayerServer extends Server {

    private ArrayList<Player> players = new ArrayList<>();

    /**
     * Starts the {@link PlayerServer Server} on specified {@code port}.
     *
     * @param port the port to start the server on.
     */
    public PlayerServer(int port) {
        super(port);
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

    /**
     * Sends the {@link Player} list to {@link PlayerClient Clients} requesting
     * updates, or adds new or changed {@link Player Players} to its
     * {@link Player} list.
     *
     * @param request the sent object.
     * @param origin the {@link SessionThread} from which the object came from.
     * @return
     */
    @Override
    public Serializable performResponse(Serializable request, SessionThread origin) {
        if (request.equals(Communications.UPDATE)) {
            return players;
        } else if (request instanceof Player) {
            players.add((Player) request);
        }
        return Communications.DONT_ANSWER;
    }
}
