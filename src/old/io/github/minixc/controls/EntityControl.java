package old.io.github.minixc.controls;

import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;

public abstract class EntityControl extends CharacterControl {

    private boolean left, right, up, down;
    private static final float SPEED_FORWARD = 1f;
    private static final float SPEED_LEFT = 0.4f;
    private static final float HEIGHT = 0.2f;
    private static final float WIDTH = 0.1f;
    private static final float STEP_HEIGHT = 0.05f;

    public EntityControl() {
        super(new CapsuleCollisionShape(WIDTH, HEIGHT, 1), STEP_HEIGHT);
        init();
    }

    abstract Vector3f getDirection();

    public void updateDirection(Camera cam) {
        Vector3f direction = getDirection().setY(0);
        Vector3f forwardDir = direction.clone().multLocal(SPEED_FORWARD);
        //turn to the left
        Quaternion turn90 = new Quaternion();
        turn90.fromAngleAxis(FastMath.PI / 2, new Vector3f(0, 1, 0));
        Vector3f leftDir = turn90.multLocal(direction.clone()).clone().multLocal(SPEED_LEFT);
        direction.set(0, 0, 0);
        if (left) {
            direction.addLocal(leftDir);
        }
        if (right) {
            direction.addLocal(leftDir.negate());
        }
        if (up) {
            direction.addLocal(forwardDir);
        }
        if (down) {
            direction.addLocal(forwardDir.negate());
        }
        System.out.println(leftDir);
        System.out.println(forwardDir);
        setWalkDirection(direction);
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    private void init() {
        setJumpSpeed(20);
        setFallSpeed(30);
        setGravity(30);
    }
}
