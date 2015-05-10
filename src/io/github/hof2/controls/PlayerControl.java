package io.github.hof2.controls;

import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import io.github.hof2.states.GameAppState;
import io.github.hof2.states.simple.Mapping;
import io.github.hof2.states.simple.SimpleQuaternions;
import java.util.HashSet;

/**
 * This is where the player is controlled. The player moves based on its
 * {@code direction}.
 */
public class PlayerControl extends BetterCharacterControl {

    private Camera cam;
    private static final float RADIUS = 1;
    private static final float HEIGHT = 3;
    private static final float MASS = 3;
    private static final float SPEED = 100;
    private HashSet<Mapping> directions = new HashSet<>();

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
            if (directions.contains(Mapping.Left)) {
                commonDirection.addLocal(SimpleQuaternions.YAW090.mult(newWalkDirection));
                directionNumber++;
            }
            if (directions.contains(Mapping.Right)) {
                commonDirection.addLocal(SimpleQuaternions.YAW090.mult(newWalkDirection).negate());
                directionNumber++;
            }
            if (directions.contains(Mapping.Forward)) {
                commonDirection.addLocal(newWalkDirection);
                directionNumber++;
            }
            if (directions.contains(Mapping.Backward)) {
                commonDirection.addLocal(newWalkDirection.negate());
                directionNumber++;
            }
            newWalkDirection.set(commonDirection.divideLocal(directionNumber).normalizeLocal().multLocal(SPEED));
        } else {
            newWalkDirection.multLocal(0);
        }

        setViewDirection(viewDirection.interpolate(newViewDirection, tpf));
        setWalkDirection(viewDirection.interpolate(newWalkDirection, tpf));
        
        directions.clear();
    }

    /**
     * Adds a {@link Mapping direction} to the the player. The character will
     * move into the direction of the average of all added directions relative
     * to the {@code viewDirection} and stops when there are no directions.
     *
     * @param direction The direction to be added.
     */
    public void addDirection(Mapping direction) {
        directions.add(direction);
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
