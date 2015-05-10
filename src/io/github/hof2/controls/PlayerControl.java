package io.github.hof2.controls;

import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import io.github.hof2.states.GameAppState;
import io.github.hof2.states.simple.Mapping;
import io.github.hof2.states.simple.SimpleQuaternions;

/**
 * This is where the headmaster is controlled. The headmaster moves based on its
 * {@code direction}.
 */
public class PlayerControl extends BetterCharacterControl {

    private Camera cam;
    private static final float RADIUS = 1;
    private static final float HEIGHT = 3;
    private static final float MASS = 3;
    private static final float SPEED = 100;
    private Mapping direction;

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

        if (direction != null) {
            switch (direction) {
                case Left:
                    SimpleQuaternions.YAW090.multLocal(newWalkDirection).multLocal(SPEED);
                    break;
                case Right:
                    SimpleQuaternions.YAW090.multLocal(newWalkDirection).negateLocal().multLocal(SPEED);
                    break;
                case Forward:
                    newWalkDirection.multLocal(SPEED);
                    break;
                case Backward:
                    newWalkDirection.negateLocal().multLocal(SPEED);
                    break;
                case Jump:
                    jump();
                    break;
                default:
                    newWalkDirection.set(Vector3f.ZERO);
            }
        }

        setViewDirection(viewDirection.interpolate(newViewDirection, tpf));
        setWalkDirection(walkDirection.interpolate(newWalkDirection, tpf));
    }

    /**
     * Sets the direction of the headmaster. The character will move in the
     * direction relative to the {@code viewDirection} and stops when the
     * direction is set to {@code null}.
     *
     * @param direction
     */
    public void setDirection(Mapping direction) {
        this.direction = direction;
    }
}
