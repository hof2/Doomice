/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.hof2.states;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Node;
import io.github.hof2.controls.HeadmasterControl;
import io.github.hof2.controls.PhysicsChaseCamera;
import io.github.hof2.materials.MaterialManager;

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
        control = new HeadmasterControl(1f, 3f, 1f,app.getCamera());
        control.setGravity(new Vector3f(0, 1f, 0));

        headmaster = (Node) app.getAssetManager().loadModel("Models/Headmaster/Headmaster.j3o");
        headmaster.setMaterial(MaterialManager.getMaterial("player"));
        headmaster.rotateUpTo(new Vector3f(0, FastMath.PI/2, 0));
        headmaster.addControl(control);

        physics = stateManager.getState(BulletAppState.class);
        physics.getPhysicsSpace().addAll(headmaster);
        physics.getPhysicsSpace().add(control);
        


        ((SimpleApplication) app).getFlyByCamera().setEnabled(false);
        PhysicsChaseCamera cc = new PhysicsChaseCamera(app.getCamera(),headmaster,input,physics.getPhysicsSpace());
        cc.setDragToRotate(false);
        cc.setTrailingEnabled(false);
        cc.setLookAtOffset(new Vector3f(0, 2.5f, 0));
        cc.setZoomSensitivity(3f);
//        cc.setRotationSensitivity(1);
//        cc.setZoomSpeed(0.1f);
        
//        cc.setDownRotateOnCloseViewOnly(true);
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

        input.addListener(this, "Left");
        input.addListener(this, "Right");
        input.addListener(this, "Forward");
        input.addListener(this, "Backward");
        input.addListener(this, "Jump");
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
    }

    @Override
    public void onAnalog(String name, float value, float tpf) {
        
    }
}
