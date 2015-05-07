/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.hof2.states;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.input.ChaseCamera;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Node;
import com.jme3.scene.control.CameraControl;
import io.github.hof2.controls.HeadmasterControl;
import io.github.hof2.materials.MaterialManager;
import java.awt.MouseInfo;

/**
 *
 * @author Matthias
 */
public class HeadmasterAppState extends AbstractAppState implements ActionListener, AnalogListener {

    private Node headmaster, rootNode;
    private HeadmasterControl control;
    private BulletAppState physics;
    private InputManager input;
    private CameraNode camNode;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        rootNode = ((SimpleApplication) app).getRootNode();
        input = app.getInputManager();
        initInputs();
        control = new HeadmasterControl(1f, 3f, 1f);
        control.setGravity(new Vector3f(0, 1f, 0));

        headmaster = (Node) app.getAssetManager().loadModel("Models/Headmaster/Headmaster.j3o");
        headmaster.setMaterial(MaterialManager.getMaterial("player"));
        headmaster.addControl(control);

        physics = stateManager.getState(BulletAppState.class);
        physics.getPhysicsSpace().addAll(headmaster);
        physics.getPhysicsSpace().add(control);


        ((SimpleApplication) app).getFlyByCamera().setEnabled(false);
        camNode = new CameraNode("HeadmasterCam", app.getCamera());
        camNode.setControlDir(CameraControl.ControlDirection.SpatialToCamera);
        headmaster.attachChild(camNode);
        camNode.setLocalTranslation(new Vector3f(0, 5, -5));
        camNode.lookAt(headmaster.getLocalTranslation(), Vector3f.UNIT_Y);

        ((SimpleApplication) app).getRootNode().attachChild(headmaster);

        control.warp(new Vector3f(0, 128, 0));
    }

    @Override
    public void update(float tpf) {
    }

    @Override
    public void cleanup() {
        rootNode.detachChild(headmaster);
        super.cleanup();
    }

    private void initInputs() {
        input.addMapping("Left", new KeyTrigger(KeyInput.KEY_A),
                new KeyTrigger(KeyInput.KEY_LEFT));
        input.addMapping("Right", new KeyTrigger(KeyInput.KEY_D),
                new KeyTrigger(KeyInput.KEY_RIGHT));
        input.addMapping("Forward", new KeyTrigger(KeyInput.KEY_W),
                new KeyTrigger(KeyInput.KEY_UP));
        input.addMapping("Backward", new KeyTrigger(KeyInput.KEY_S),
                new KeyTrigger(KeyInput.KEY_DOWN));
        input.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
        input.addMapping("RotateLeft", new MouseAxisTrigger(MouseInput.AXIS_X, false));
        input.addMapping("RotateRight", new MouseAxisTrigger(MouseInput.AXIS_X, true));
        input.addMapping("RotateUp", new MouseAxisTrigger(MouseInput.AXIS_Y, false));
        input.addMapping("RotateDown", new MouseAxisTrigger(MouseInput.AXIS_Y, true));

        input.addListener(this, "Left");
        input.addListener(this, "Right");
        input.addListener(this, "Forward");
        input.addListener(this, "Backward");
        input.addListener(this, "Jump");
        input.addListener(this, "RotateLeft");
        input.addListener(this, "RotateRight");
        input.addListener(this, "RotateUp");
        input.addListener(this, "RotateDown");


    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
    }

    @Override
    public void onAnalog(String name, float value, float tpf) {
        System.out.println("name: " + name);
        Quaternion turn90 = new Quaternion();
        turn90.fromAngleAxis((FastMath.PI / 2) * tpf, new Vector3f(0, tpf*1, 0));
        Vector3f newDir = turn90.multLocal(control.getViewDirection().clone()).clone();

        System.out.println("Walkdir: " + control.getViewDirection());

        switch (name) {
            case "RotateLeft":
                control.setViewDirection(control.getViewDirection().addLocal(newDir));
                break;
            case "RotateRight":
                control.setViewDirection(control.getViewDirection().addLocal(newDir.negate()));
                break;
            case "RotateUp":
                camNode.getLocalTranslation().addLocal(new Vector3f(0, tpf*2*1, tpf*2*1));
                camNode.lookAt(headmaster.getLocalTranslation(), Vector3f.UNIT_Y);
                break;
            case "RotateDown":
                camNode.getLocalTranslation().addLocal(new Vector3f(0, tpf*2*-1, tpf*2*-1));
                camNode.lookAt(headmaster.getLocalTranslation(), Vector3f.UNIT_Y);
                break;
        }
    }
}
