package io.github.hof2.states;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import io.github.hof2.enums.Gui;
import io.github.hof2.enums.Materials;
import io.github.hof2.states.simple.SimpleMaterials;
import io.github.hof2.states.simple.SimpleAppState;
import io.github.hof2.states.simple.SimpleGui;

/**
 * @author Matthias Hofreiter
 * @since April 23, 2015
 * Stores common assets and handles the game overall, attaching other
 * {@link SimpleAppState SimpleAppStates} whenever needed.
 */
public class GameAppState extends SimpleAppState {

    /* AppStates */
    private BulletAppState physics = new BulletAppState();
    private TerrainAppState terrain = new TerrainAppState();
    private LightAppState lighting = new LightAppState();
    private PlayerTypeAppState player = new PlayerTypeAppState();
    /* Global Parameters */
    /**
     * Defines the gravity for all in-game objects.
     */
    public static final Vector3f GRAVITY = new Vector3f(0, 1f, 0);

    /**
     * Calls {@code initMaterials} and {@code initAppStates}.
     *
     * @see SimpleAppState
     * @see AbstractAppState
     * @param stateManager The state manager
     * @param app The application
     */
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);

        initMaterials(app);
        initGuiPaths();
        initAppStates();
    }

    /**
     * Detaches all default {@link AppState AppStates} from the
     * {@code stateManager}.
     *
     * @see SimpleAppState
     * @see AbstractAppState
     */
    @Override
    public void cleanup() {
        super.cleanup();
        stateManager.detach(terrain);
        stateManager.detach(lighting);
        stateManager.detach(player);
        stateManager.detach(physics);
    }

    /**
     * Attaches all default {@link AppState AppStates} to the
     * {@code stateManager}.
     */
    private void initAppStates() {
        stateManager.attach(physics);
        stateManager.attach(terrain);
        stateManager.attach(lighting);
        stateManager.attach(player);
    }

    /**
     * Initializes the {@link MaterialManager}.
     */
    private void initMaterials(Application app) {
        SimpleMaterials.putMaterial(Materials.Floor, new Material(app.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md"));
        SimpleMaterials.putMaterial(Materials.Player, new Material(app.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md"));
        SimpleMaterials.putMaterial(Materials.School, new Material(app.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md"));
    }

    private void initGuiPaths() {
        SimpleGui.putGuiPath(Gui.ChoosePlayer, "Interface/NiftyGUI/ChoosePlayerType.xml");
        SimpleGui.putGuiPath(Gui.HandleMultiplayer, "Interface/NiftyGUI/HandleMultiplayer.xml");
    }
}
