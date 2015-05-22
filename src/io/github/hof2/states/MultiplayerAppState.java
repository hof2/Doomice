package io.github.hof2.states;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import io.github.hof2.states.simple.SimpleAppState;

/**
 * Sends information about the player and retrieves information of other players
 * and places them in the game.
 */
public class MultiplayerAppState extends SimpleAppState {

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
    }

    @Override
    public void update(float tpf) {
        
    }

    @Override
    public void cleanup() {
        super.cleanup();
    }
}
