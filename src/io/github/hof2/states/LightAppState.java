package io.github.hof2.states;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import io.github.hof2.states.simple.SimpleAppState;
import io.github.hof2.states.simple.SimpleColors;

/**
 * Handles the lights in-game.
 */
public class LightAppState extends SimpleAppState {

    private AmbientLight ambient = new AmbientLight();
    private DirectionalLight directional = new DirectionalLight();
    private Node rootNode;

    /**
     * Calls {@code initAmbient} and {@code initDirectional}.
     *
     * @see SimpleAppState
     * @see AbstractAppState
     * @param stateManager The state manager
     * @param app The application
     */
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        rootNode = this.app.getRootNode();
        
        initAmbient();
        initDirectional();
    }
    
    /**
     * Adds an {@link AmbientLight} to the scene.
     */
    private void initAmbient() {
        ambient.setColor(ColorRGBA.White);
        rootNode.addLight(ambient);
    }
    
    /**
     * Adds a {@link DirectionalLight} to the scene.
     */
    private void initDirectional() {
        directional.setDirection(new Vector3f(0, -1f, 0));
        directional.setColor(SimpleColors.FLINT);
        rootNode.addLight(directional);
    }

    /**
     * Removes the lights from the scene.
     * @see SimpleAppState
     * @see AbstractAppState
     */
    @Override
    public void cleanup() {
        super.cleanup();
        rootNode.removeLight(ambient);
        rootNode.removeLight(directional);
    }
}
