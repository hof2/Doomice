package io.github.hof2;

import com.jme3.app.SimpleApplication;
import io.github.hof2.states.GameAppState;

/**
 *
 * @author christoph
 */
public class Main extends SimpleApplication {

    /**
     * Attaches a new {@link GameAppState} to the {@code stateManager}.
     * This should not be called from user code.
     */
    @Override
    public void simpleInitApp() {
        stateManager.attach(new GameAppState());
    }
    
    /**
     * The main method. Creates a new {@link Main} instance and calls its {@code start} method.
     * @param args
     */
    public static void main(String[] args) {
        new Main().start();
    }
}
