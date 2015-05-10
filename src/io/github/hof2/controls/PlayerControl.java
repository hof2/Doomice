package io.github.hof2.controls;

import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import io.github.hof2.states.GameAppState;
import io.github.hof2.states.simple.Mapping;
import io.github.hof2.states.simple.SimpleQuaternions;
import java.util.HashMap;

/**
 * This is where the player is controlled. The player moves based on its
 * {@code direction}.
 */
public class PlayerControl extends BetterCharacterControl {
    
    private Camera cam;
    private static final float RADIUS = 1;
    private static final float HEIGHT = 3;
    private static final float MASS = 3;
    private static final float GROUND_SPEED = 50;
    private static final float AIR_SPEED = 30;
    private HashMap<Mapping, Float> directions = new HashMap<>();

    /**
     * Creates a new character with the given {@link Camera} to continously set
     * the {@code viewDirection}. {@code RADIUS}, {@code HEIGHT} and
     * {@code MASS} are set based on static variables. The gravity is set to
     * static variable {@code GRAVITY} in {@link GameAppState}.
     *
     * @param cam used to update the {@code viewDirection}
     */
    public PlayerControl(Camera cam) {
        super(RADIUS, HEIGHT, MASS);
        this.cam = cam;
        setGravity(GameAppState.GRAVITY);
    }
    
    @Override
    public void update(float tpf) {
        super.update(tpf);
        
        Vector3f newViewDirection = cam.getDirection().setY(0);
        Vector3f newWalkDirection = newViewDirection.clone();
        
        if (!directions.isEmpty()) {
            Vector3f commonDirection = new Vector3f(0, 0, 0);
            float directionNumber = 0;
            if (directions.containsKey(Mapping.Left)) {
                commonDirection.addLocal(SimpleQuaternions.YAW090.mult(newWalkDirection)
                        .mult(directions.get(Mapping.Left)));
                directionNumber++;
            }
            if (directions.containsKey(Mapping.Right)) {
                commonDirection.addLocal(SimpleQuaternions.YAW090.mult(newWalkDirection).negate()
                        .mult(directions.get(Mapping.Right)));
                directionNumber++;
            }
            if (directions.containsKey(Mapping.Forward)) {
                commonDirection.addLocal(newWalkDirection
                        .mult(directions.get(Mapping.Forward)));
                directionNumber++;
            }
            if (directions.containsKey(Mapping.Backward)) {
                commonDirection.addLocal(newWalkDirection.negate()
                        .mult(directions.get(Mapping.Backward)));
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
    public void addDirection(Mapping direction, float speed) {
        directions.put(direction, speed);
    }

    /**
     * Removes a {@link Mapping direction} from the player. When there are no
     * directions left, the player stops moving.
     *
     * @param direction The direction to be removed.
     */
    public void removeDirection(Mapping direction) {
        directions.remove(direction);
    }
}
