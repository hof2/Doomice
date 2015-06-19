package io.github.hof2.states;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.scene.Node;
import io.github.hof2.client.PlayerClient;
import io.github.hof2.collection.Player;
import io.github.hof2.collection.PlayerCollection;
import static io.github.hof2.collection.PlayerCollection.players;
import io.github.hof2.controls.PlayerControl;
import io.github.hof2.enums.Communications;
import io.github.hof2.server.PlayerServer;
import io.github.hof2.states.simple.SimpleAppState;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Handles multiplayer functinality: Starts a {@link PlayerClient Client} and
 * {@link PlayerServer Server} if there isn't already a
 * {@link PlayerServer Server} running on the localhost.
 */
public class MultiplayerAppState extends SimpleAppState {

    private PlayerClient client;
    private PlayerServer server;
    private float time;
    private PlayerAppState playerAppState;
    /**
     * The port used for all communications
     */
    public static final int PORT = 4242;

    /**
     * Creates a new {@link PlayerClient Client} and tries to connect to a
     * server at localhost using the {@link MultiplayerAppState}{@code .PORT}
     * number.
     *
     * @param stateManager The state manager
     * @param app The application
     */
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        try {
            client = new PlayerClient(new Socket(InetAddress.getLocalHost(), PORT));
            client.start();
        } catch (Exception e) {
            System.out.println("No server running yet, starting a new one...");
            try {
                server = new PlayerServer(4242);
                server.start();
                client = new PlayerClient(new Socket(InetAddress.getLocalHost(), PORT));
                client.start();
            } catch (IOException ex) {
                System.out.println("Error: " + ex);
            }
        }
    }

    /**
     * Clears the {@link PlayerCollection PlayerCollection's} list and stops
     * {@link PlayerClient Client} and {@link PlayerServer Server}.
     */
    @Override
    public void cleanup() {
        super.cleanup();
        players.clear();
        try {
            client.stop();
            if (server != null) {
                server.stop();
            }
        } catch (IOException ex) {
            System.out.println("Error: " + ex);
        }
    }

    /**
     * Creates a {@link Node} and {@link PlayerControl} for a new
     * {@link Player}.
     *
     * @param player the {@link Player} object which defines the {@link Node}
     * and {@link PlayerControl}.
     * @return the created {@link PlayerControl}.
     */
    private PlayerControl createPlayer(Player player) {
        PlayerControl control = new PlayerControl(player.getViewDirection(), player.getType());
        playerAppState.addNode(control);
        return control;
    }

    /**
     * Sends the own player, and updates the game based on the list of players
     * received.
     *
     * @param tpf the time per frame value.
     */
    @Override
    public void update(float tpf) {
        try {
            if ((time += tpf) >= 1) {
                time = 0;
                if (playerAppState != null) {
                    PlayerControl control = playerAppState.getPlayerControl();
                    client.send(new Player(control.getLocation().clone(), control.getType(), control.getViewDirection().clone(), control.getName()));
                    ArrayList<Player> response = (ArrayList<Player>) client.performRequest(Communications.UPDATE);
                    String id = control.getName();
                    for (Player player : response) {
                        if (!players.containsKey(player.getId())) {
                            players.put(id, createPlayer(player));
                        } else if (!players.get(player.getId()).isLocal()) {
                            control = players.get(id);
                            control.warp(player.getPosition());
                            control.setViewDirection(player.getViewDirection());
                        }
                    }
                } else {
                    playerAppState = stateManager.getState(PlayerAppState.class);
                }
            }
        } catch (IOException | ClassNotFoundException | InterruptedException ex) {
            System.out.println("Error: " + ex);
        }
    }
}
