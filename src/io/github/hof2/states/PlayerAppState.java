package io.github.hof2.states;

import io.github.hof2.enums.PlayerTypes;
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
import io.github.hof2.collection.PlayerCollection;
import io.github.hof2.controls.HeadmasterControl;
import io.github.hof2.controls.PlayerControl;
import io.github.hof2.controls.StudentControl;
import static io.github.hof2.enums.PlayerTypes.Headmaster;
import static io.github.hof2.enums.PlayerTypes.Student;
import io.github.hof2.states.simple.SimpleMaterials;
import io.github.hof2.enums.Mappings;
import io.github.hof2.enums.Materials;
import io.github.hof2.states.simple.SimpleAppState;

/**
 * Adds a player to the game, which can be either a
 * {@link StudentControl student} or {@link HeadmasterControl headmaster}.
 */
public class PlayerAppState extends SimpleAppState {

    private Node player;
    private PlayerControl playerControl;
    private StudentControl studentControl;
    private HeadmasterControl headmasterControl;
    private InputManager inputManager;
    private Node rootNode;
    private PhysicsSpace physicsSpace;
    private PlayerTypes playerType;
    private PlayerCollection players;
    
    /**
     * Sets the {@code playerType} to either student or headmaster.
     *
     * @param type the {@link PlayerTypes PlayerType}, can be either student or
     * headmaster.
     */
    public PlayerAppState(PlayerTypes type) {
        this.playerType = type;
    }

    /**
     * Sets the {@code inputManager}, {@code rootNode} and {@code physicsSpace}.
     * Calls {@code initNode}, {@code initCamera}, {@code initKeybindings}.
     * Also detaches the {@link PlayerTypeAppState} from the Application
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
        players = PlayerCollection.getInstance();
        initNode();
        initCamera();
        try {
            initKeybindings();
        } catch (Exception ex) {
            System.out.println("PlayerAppState : Failed to bind keys");
        }
        stateManager.detach(stateManager.getState(PlayerTypeAppState.class));
    }

    /**
     * Detaches the player from the {@code rootNode} and removes the mappings
     * for player movement and jumping. This also re-enables the default
     * {@link FlyByCamera}.
     *
     * @see SimpleAppState
     * @see AbstractAppState
     */
    @Override
    public void cleanup() {
        super.cleanup();
        players.removePlayer(playerControl.getPlayer());
        rootNode.detachChild(player);
        mappings.removeMappings(this, inputManager, Mappings.Left, Mappings.Right, Mappings.Forward, Mappings.Backward, Mappings.Jump);
        app.getFlyByCamera().setEnabled(true);
    }

    /**
     * Adds keybindings for left, right, forward and backward movement and
     * jumping.
     *
     * @throws Exception is thrown when a keybinding already exists
     */
    private void initKeybindings() throws Exception {
        mappings.addMapping(this, inputManager, Mappings.Left, KeyInput.KEY_A, KeyInput.KEY_LEFT);
        mappings.addMapping(this, inputManager, Mappings.Right, KeyInput.KEY_D, KeyInput.KEY_RIGHT);
        mappings.addMapping(this, inputManager, Mappings.Forward, KeyInput.KEY_W, KeyInput.KEY_UP);
        mappings.addMapping(this, inputManager, Mappings.Backward, KeyInput.KEY_S, KeyInput.KEY_DOWN);
        mappings.addMapping(this, inputManager, Mappings.Jump, KeyInput.KEY_SPACE);
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
        cam.setInvertVerticalAxis(true);
    }

    /**
     * Attaches a player node to the {@code rootNode} and adds a
     * {@link PlayerControl} to it. Also adds a {@link HeadmasterControl} or
     * {@link StudentControl} based on the {@link PlayerTypes} to the node.
     */
    private void initNode() {
        playerControl = new PlayerControl(app.getCamera());
        studentControl = new StudentControl();
        headmasterControl = new HeadmasterControl();
        player = (Node) app.getAssetManager().loadModel("Models/" + playerType + "/" + playerType + ".j3o");
        player.setMaterial(SimpleMaterials.getMaterial(Materials.Player));
        player.rotateUpTo(new Vector3f(0, FastMath.PI / 2, 0));
        player.addControl(playerControl);
        switch (playerType) {
            case Student:
                player.addControl(studentControl);
                break;
            case Headmaster:
                player.addControl(headmasterControl);
                break;
        }
        physicsSpace.addAll(player);
        physicsSpace.add(playerControl);
        rootNode.attachChild(player);
        players.addPlayer(playerControl.getPlayer());
    }

    /**
     * Sets the direction of the {@link PlayerControl PlayerControl} to move the
     * player or jump.
     *
     * @see SimpleAppState
     * @param mapping The mapping.
     * @param isPressed If the key was pressed or released.
     * @param tpf The time per frame value.
     */
    @Override
    public void onMappingAnalog(Mappings mapping, float value, float tpf) {
        if (mapping != Mappings.Jump) {
            if (value > 0) {
                playerControl.addDirection(mapping, value);
            } else {
                playerControl.removeDirection(mapping);
            }
        } else {
            playerControl.jump(value);
        }
    }
}
