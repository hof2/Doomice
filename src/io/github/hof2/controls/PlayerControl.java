package io.github.hof2.controls;

import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Spatial;
import io.github.hof2.states.GameAppState;
import io.github.hof2.enums.Mappings;
import io.github.hof2.enums.PlayerTypes;
import io.github.hof2.states.simple.SimpleQuaternions;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;

/**
 * This is where the player is controlled. The player moves based on its
 * {@code direction}.
 */
public class PlayerControl extends BetterCharacterControl implements Serializable {

    private Camera cam;
    private static final float RADIUS = 1;
    private static final float HEIGHT = 3;
    private static final float MASS = 100;
    private static final float GROUND_SPEED = 50;
    private static final float AIR_SPEED = 30;
    private static final float JUMP_FORCE = 40000;
    private boolean doJump;
    private boolean multiplayer;
    private Vector3f nextView;
    private Vector3f nextWalk;
    private String name;
    private PlayerTypes type;
    private boolean local;
    private HashMap<Mappings, Float> directions = new HashMap<>();

    {
        name = new BigInteger(130, new SecureRandom()).toString(32);
    }

    /**
     * Creates a new character with the given {@link Camera} to continously set
     * the {@code viewDirection}. {@code RADIUS}, {@code HEIGHT} and
     * {@code MASS} are set based on static variables. The gravity is set to
     * static variable {@code GRAVITY} in {@link GameAppState}.
     *
     * @param cam used to update the {@code viewDirection}
     */
    public PlayerControl(Camera cam, PlayerTypes type) {
        super(RADIUS, HEIGHT, MASS);
        this.cam = cam;
        setGravity(GameAppState.GRAVITY);
        this.type = type;
    }

    /**
     * Creates a new character with the next {@code viewDirection} and
     * {@code walkDirection}. {@code RADIUS}, {@code HEIGHT} and {@code MASS}
     * are set based on static variables. The gravity is set to static variable
     * {@code GRAVITY} in {@link GameAppState}.
     *
     * @param nextView the next {@code viewDirection}
     * @param nextWalk the next {@code walkDirection}
     * @param type the {@link PlayerTypes PlayerType}
     */
    public PlayerControl(Vector3f nextView, Vector3f nextWalk, PlayerTypes type) {
        super(RADIUS, HEIGHT, MASS);
        setGravity(GameAppState.GRAVITY);
        multiplayer = true;
        this.nextView = nextView;
        this.nextWalk = nextWalk;
        this.type = type;
    }

    /**
     * Updates the control by calling {@code jump()} if {@code jump} is set to
     * {@code true}}, moving it into the average of all {@code directions} and
     * setting the new {@code viewDirection} equal to the camera direction. Uses
     * interpolation for smooth motion.
     *
     * @param tpf Time per frame.
     */
    @Override
    public void update(float tpf) {
        super.update(tpf);

        if (doJump) {
            jump();
            doJump = false;
        }

        Vector3f newViewDirection = multiplayer ? nextView : cam.getDirection().setY(0);
        Vector3f newWalkDirection = multiplayer ? nextWalk : newViewDirection.clone();

        if (!directions.isEmpty()) {
            Vector3f commonDirection = new Vector3f(0, 0, 0);
            float directionNumber = 0;
            if (directions.containsKey(Mappings.Left)) {
                commonDirection.addLocal(SimpleQuaternions.YAW090.mult(newWalkDirection)
                        .mult(directions.get(Mappings.Left)));
                directionNumber++;
            }
            if (directions.containsKey(Mappings.Right)) {
                commonDirection.addLocal(SimpleQuaternions.YAW090.mult(newWalkDirection).negate()
                        .mult(directions.get(Mappings.Right)));
                directionNumber++;
            }
            if (directions.containsKey(Mappings.Forward)) {
                commonDirection.addLocal(newWalkDirection
                        .mult(directions.get(Mappings.Forward)));
                directionNumber++;
            }
            if (directions.containsKey(Mappings.Backward)) {
                commonDirection.addLocal(newWalkDirection.negate()
                        .mult(directions.get(Mappings.Backward)));
                directionNumber++;
            }
            newWalkDirection = commonDirection.divideLocal(directionNumber)
                    .normalizeLocal().multLocal(isOnGround() ? GROUND_SPEED : AIR_SPEED);
        } else {
            newWalkDirection.multLocal(0);
        }

        setViewDirection(viewDirection.interpolate(newViewDirection, tpf));
        setWalkDirection(viewDirection.interpolate(newWalkDirection, tpf));

        directions.clear();
    }

    /**
     * Adds a {@link Mapping direction} and a corresponding {@code speed} to the
     * the player. The character will move into the direction of the average of
     * all added directions relative to the {@code viewDirection} and stops when
     * there are no directions. The given {@code speed} will also be considered
     * in the calculation.
     *
     * @param direction The direction to be added.
     */
    public void addDirection(Mappings direction, float speed) {
        directions.put(direction, speed);
    }

    /**
     * Removes a {@link Mapping direction} from the player. When there are no
     * directions left, the player stops moving.
     *
     * @param direction The direction to be removed.
     */
    public void removeDirection(Mappings direction) {
        directions.remove(direction);
    }

    /**
     * Makes the player jump with a certain force (based on {@code JUMP_FORCE}.
     *
     * @param force The jump force.
     */
    public void jump(float force) {
        setJumpForce(new Vector3f(0, force * JUMP_FORCE, 0));
        doJump = true;
    }

    public String getName() {
        return name;
    }

    public PlayerTypes getType() {
        return type;
    }

    public Vector3f getLocation() {
        return location;
    }

    public Spatial getSpatial() {
        return spatial;
    }

    public boolean isLocal() {
        return local;
    }

    public void setLocal(boolean local) {
        this.local = local;
    }
}
