package io.github.minixc.controls;

import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;

public class HeadmasterControl extends CharacterControl {

    private boolean left, right, up, down;
    private static final float SPEED_FORWARD = 0.6f;
    private static final float SPEED_LEFT = 0.2f;

    public HeadmasterControl(CollisionShape shape, float stepHeight) {
        super(shape, stepHeight);
    }

    public void updateDirection(Camera cam) {
        Vector3f camForward = cam.getDirection().clone().setY(0).multLocal(SPEED_FORWARD);
        Vector3f camLeft = cam.getLeft().clone().setY(0).multLocal(SPEED_LEFT);
        Vector3f direction = new Vector3f();
        direction.set(0, 0, 0);
        if (left) {
            direction.addLocal(camLeft);
        }
        if (right) {
            direction.addLocal(camLeft.negate());
        }
        if (up) {
            direction.addLocal(camForward);
        }
        if (down) {
            direction.addLocal(camForward.negate());
        }
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
}
