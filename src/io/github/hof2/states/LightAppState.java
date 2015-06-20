package io.github.hof2.states;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.post.filters.CartoonEdgeFilter;
import com.jme3.post.ssao.SSAOFilter;
import com.jme3.scene.Node;
import com.jme3.shadow.DirectionalLightShadowFilter;
import io.github.hof2.states.simple.SimpleAppState;
import io.github.hof2.states.simple.SimpleColors;

/**
 * Handles the lights in-game.
 */
public class LightAppState extends SimpleAppState {

    private AmbientLight ambient = new AmbientLight();
    private DirectionalLight directional = new DirectionalLight();
    private Node rootNode;
    private static final int SHADOWMAP_SIZE = 4048;
    private static final int SHADOWMAP_NUMBER = 4;
    private FilterPostProcessor fpp;

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
        initShadow();
    }

    /**
     * Adds an {@link AmbientLight} to the scene.
     */
    private void initAmbient() {
        ambient.setColor(SimpleColors.CONCRETE);
        rootNode.addLight(ambient);
    }

    /**
     * Adds a {@link DirectionalLight} to the scene.
     */
    private void initDirectional() {
        directional.setDirection(new Vector3f(-.5f,-.5f,-.5f).normalizeLocal());
        directional.setColor(SimpleColors.FLINT);
        rootNode.addLight(directional);
    }

    /**
     * Creates shadows, ambient occlusion and the cartoon edges.
     */
    private void initShadow() {
        DirectionalLightShadowFilter dlsf = new DirectionalLightShadowFilter(app.getAssetManager(), SHADOWMAP_SIZE, SHADOWMAP_NUMBER);
        dlsf.setLight(directional);
        dlsf.setEnabled(true);
        fpp = new FilterPostProcessor(app.getAssetManager());
        SSAOFilter ssaoFilter = new SSAOFilter();
        BloomFilter bloomFilter = new BloomFilter();
        CartoonEdgeFilter cartoonEdgeFilter = new CartoonEdgeFilter();
        fpp.addFilter(dlsf);
        fpp.addFilter(ssaoFilter);
        fpp.addFilter(bloomFilter);
        fpp.addFilter(cartoonEdgeFilter);
        app.getViewPort().addProcessor(fpp);
    }

    /**
     * Removes the lights and shadows from the scene.
     *
     * @see SimpleAppState
     * @see AbstractAppState
     */
    @Override
    public void cleanup() {
        super.cleanup();
        rootNode.removeLight(ambient);
        rootNode.removeLight(directional);
        app.getViewPort().removeProcessor(fpp);
    }
}
