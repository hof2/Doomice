package io.github.hof2.states;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.scene.Node;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import io.github.hof2.client.PlayerClient;
import io.github.hof2.collection.Player;
import io.github.hof2.collection.PlayerCollection;
import static io.github.hof2.collection.PlayerCollection.players;
import io.github.hof2.controls.PlayerControl;
import io.github.hof2.enums.Communications;
import io.github.hof2.enums.Gui;
import io.github.hof2.server.PlayerServer;
import io.github.hof2.states.simple.SimpleAppState;
import io.github.hof2.states.simple.SimpleGui;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Handles multiplayer functinality: Starts a {@link PlayerClient Client} and
 * {@link PlayerServer Server} if there isn't already a
 * {@link PlayerServer Server} running on the localhost.
 */
public class MultiplayerAppState extends SimpleAppState implements ScreenController {

    private PlayerClient client;
    private PlayerServer server;
    private float time;
    private PlayerAppState playerAppState;
    private static final boolean ENABLE_HORDE = false;
    private NiftyJmeDisplay display;
    private ArrayList<Player> response = new ArrayList<>();
    private boolean newResponse;
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
        initGui();
    }

    /**
     * Sets up the NiftyGui.
     */
    private void initGui() {
        display = stateManager.getState(PlayerTypeAppState.class).getDisplay();
        Nifty nifty = display.getNifty();
        nifty.fromXml(SimpleGui.getGuiPath(Gui.HandleMultiplayer), "Start", this);
        app.getGuiViewPort().addProcessor(display);
    }

    /**
     * Called when the user tries to connect to a certain server ip.
     */
    public void connectTo() {
        try {
            client = new PlayerClient(new Socket(getTextById("ipText"), PORT));
            client.start();
            startGame();
        } catch (IOException ex) {
            System.out.println("Error: " + ex);
        }
    }

    /**
     * Gets the current text from any currently existing nifty element based on
     * its id.
     *
     * @param id the id of th element.
     * @return the text of the element.
     */
    private String getTextById(String id) {
        return display.getNifty().getCurrentScreen().findNiftyControl(id, TextField.class).getDisplayedText();
    }

    /**
     * Called when the user decides to host a server.
     */
    public void startServer() {
        try {
            server = new PlayerServer(PORT);
            server.start();
            client = new PlayerClient(new Socket(InetAddress.getLocalHost(), PORT));
            client.start();
            startGame();
        } catch (IOException ex) {
            System.out.println("Error: " + ex);
        }
    }

    /**
     * Removes the multiplayer gui and starts the game.
     */
    private void startGame() {
        app.getGuiViewPort().removeProcessor(display);
        stateManager.attach(stateManager.getState(PlayerTypeAppState.class).getPlayerAppState());
    }

    /**
     * Clears the {@link PlayerCollection PlayerCollection's} list and stops
     * {@link PlayerClient Client} and {@link PlayerServer Server}.
     */
    @Override
    public void cleanup() {
        super.cleanup();
        try {
            if (server != null) {
                server.stop();
            }
            client.close();
            players.clear();
        } catch (IOException ex) {
            System.out.println("Error: " + ex);
        } catch (NullPointerException ex) {
            //this happens when the user exists before connecting to a server
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
        PlayerControl control = new PlayerControl(player.getViewDirection(), player.getWalkDirection(), player.getType());
        playerAppState.addNode(control, false);
        return control;
    }

    /**
     * Sets the response {@link ArrayList}.
     *
     * @param response the {@link ArrayList}
     */
    private synchronized void setResponse(ArrayList<Player> response) {
        this.response = response;
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
            if ((time += tpf) >= 0.25) {
                time = 0;
                if (playerAppState != null && client != null && !client.isClosed()) {
                    PlayerControl control = playerAppState.getPlayerControl();
                    client.send(new Player(control.getLocation().clone(), control.getViewDirection().clone(), control.getWalkDirection().clone(), control.getType(), control.getName()));
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                setResponse((ArrayList<Player>) client.performRequest(Communications.UPDATE));
                                newResponse = true;
                            } catch (IOException | ClassNotFoundException | InterruptedException ex) {
                                System.out.println("Error: " + ex);
                            }
                        }
                    }.start();
                    if (newResponse) {
                        for (Player player : response) {
                            String id = ENABLE_HORDE ? control.getName() : player.getId();
                            if (!players.containsKey(id)) {
                                players.put(id, createPlayer(player));
                            } else if (!players.get(id).isLocal()) {
                                control = players.get(id);
                                control.warp(player.getPosition());
                                control.setViewDirection(player.getViewDirection());
                                control.setWalkDirection(player.getWalkDirection());
                            }
                        }
                        newResponse = false;
                    }
                } else {
                    playerAppState = stateManager.getState(PlayerAppState.class);
                }
            }
        } catch (IOException ex) {
            System.out.println("Error: " + ex);
        }
    }

    /**
     * nothing happens here
     */
    @Override
    public void bind(Nifty nifty, Screen screen) {
    }

    /**
     * nothing happens here
     */
    @Override
    public void onStartScreen() {
    }

    /**
     * nothing happens here
     */
    @Override
    public void onEndScreen() {
    }
}
