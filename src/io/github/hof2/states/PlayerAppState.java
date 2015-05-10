package io.github.hof2.states;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.input.ChaseCamera;
import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import io.github.hof2.controls.PlayerControl;
import io.github.hof2.states.simple.SimpleMaterials;
import io.github.hof2.states.simple.Mapping;
import io.github.hof2.states.simple.Materials;
import io.github.hof2.states.simple.SimpleAppState;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Adds a player to the game, which can be either a
 * {@link StudentControl student} or {@link HeadmasterControl headmaster}.
 */
public class PlayerAppState extends SimpleAppState {

    private Node player;
    private PlayerControl control;
    private InputManager inputManager;
    private Node rootNode;
    private PhysicsSpace physicsSpace;
    private PlayerTypes type;

    /**
     * Set
     *
     * @param type the {@link PlayerTypes PlayerType}, can be either student or
     * headmaster.
     */
    public PlayerAppState(PlayerTypes type) {
        this.type = type;
    }

    /**
     * Sets the {@code inputManager}, {@code rootNode} and {@code physicsSpace}.
     * Calls {@code initNode}, {@code initCamera}, {@code initKeybindings}.
     *
     * @see SimpleAppState
     * @see AbstractAppState
     * @param stateManager The state manager
     * @param app The application
     */
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        inputManager = this.app.getInputManager();
        rootNode = this.app.getRootNode();
        physicsSpace = stateManager.getState(BulletAppState.class).getPhysicsSpace();
        initNode();
        initCamera();
        try {
            initKeybindings();
        } catch (Exception ex) {
            System.out.println("PlayerAppState : Failed to bind keys");
        }
    }

    /**
     * Detaches the player from the {@code rootNode} and removes the mappings
     * for player movement. This also re-enables the default
     * {@link FlyByCamera}.
     *
     * @see SimpleAppState
     * @see AbstractAppState
     */
    @Override
    public void cleanup() {
        super.cleanup();
        rootNode.detachChild(player);
        mappings.removeMappings(this, inputManager, Mapping.Left, Mapping.Right, Mapping.Forward, Mapping.Backward);
        app.getFlyByCamera().setEnabled(true);
    }

    /**
     * Adds keybindings for left, right, forward and backward movement.
     *
     * @throws Exception is thrown when a keybinding already exists
     */
    private void initKeybindings() throws Exception {
        mappings.addMapping(this, inputManager, Mapping.Left, KeyInput.KEY_A, KeyInput.KEY_LEFT);
        mappings.addMapping(this, inputManager, Mapping.Right, KeyInput.KEY_D, KeyInput.KEY_RIGHT);
        mappings.addMapping(this, inputManager, Mapping.Forward, KeyInput.KEY_W, KeyInput.KEY_UP);
        mappings.addMapping(this, inputManager, Mapping.Backward, KeyInput.KEY_S, KeyInput.KEY_DOWN);
    }

    /**
     * Disables the default {@link FlyByCamera} and attaches a
     * {@link ChaseCamera} to the player node.
     */
    private void initCamera() {
        this.app.getFlyByCamera().setEnabled(false);
        ChaseCamera cam = new ChaseCamera(app.getCamera(), player, inputManager);
        cam.setDragToRotate(false);
        cam.setTrailingEnabled(false);
        cam.setLookAtOffset(new Vector3f(0, 2.5f, 0));
        cam.setZoomSensitivity(3f);
    }

    /**
     * Attaches a player node to the {@code rootNode} and adds a
     * {@link PlayerControl} to it. TODO: Add a {@link HeadmasterControl} or
     * {@link StudentControl} based on the {@link PlayerTypes} to the node.
     */
    private void initNode() {
        control = new PlayerControl(app.getCamera());
        player = (Node) app.getAssetManager().loadModel("Models/Headmaster/" + type + ".j3o");
        player.setMaterial(SimpleMaterials.getMaterial(Materials.Player));
        player.rotateUpTo(new Vector3f(0, FastMath.PI / 2, 0));
        player.addControl(control);
        physicsSpace.addAll(player);
        physicsSpace.add(control);
        rootNode.attachChild(player);
    }

    /**
     * Sets the direction of the {@link PlayerControl PlayerControl} to move the
     * player.
     *
     * @see SimpleAppState
     * @param mapping The new direction.
     * @param isPressed If the key was pressed or released.
     * @param tpf The time per frame value.
     */
    @Override
    public void onMappingAction(Mapping mapping, boolean isPressed, float tpf) {
        if (isPressed) {
            control.setDirection(mapping);
        } else {
            control.setDirection(null);
        }
    }
}
