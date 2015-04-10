package io.github.minixc;

import com.jme3.app.SimpleApplication;
import com.jme3.input.ChaseCamera;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import java.util.Random;

public class Main extends SimpleApplication {

    Geometry surface;
    Spatial headmaster;
    Node headmasternode;
    //pauses the game if set to false
    Boolean isRunning = true;

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");

        Box p = new Box(100, 0, 100);
        surface = new Geometry("Plane", p);
        surface.setMaterial(mat);
        rootNode.attachChild(surface);

        headmaster = assetManager.loadModel("Models/Headmaster/Headmaster001.j3o");
        headmaster.setMaterial(mat);

        headmasternode = new Node();
        headmasternode.attachChild(headmaster);
        rootNode.attachChild(headmasternode);

        PointLight sun = new PointLight();
        sun.setPosition(new Vector3f(1, 1, 1));
        rootNode.addLight(sun);

        AmbientLight ambient = new AmbientLight();
        rootNode.addLight(ambient);


        // Disable the default flyby cam
        flyCam.setEnabled(false);

        // Enable a chase cam for this target (typically the player).
        ChaseCamera chaseCam = new ChaseCamera(cam, headmasternode, inputManager);

        //chaseCam.setSmoothMotion(true);
        chaseCam.setDefaultHorizontalRotation(-90 * FastMath.DEG_TO_RAD);

        // You can map one or several inputs to one named action
        inputManager.addMapping("Pause", new KeyTrigger(KeyInput.KEY_P));
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Forward", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Backward", new KeyTrigger(KeyInput.KEY_S));

        // Add the names to the action listener.
        inputManager.addListener(actionListener, "Pause");
        inputManager.addListener(analogListener, "Left", "Right", "Forward", "Backward");

    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
    float x = 0, y = 0, z = 0;

    @Override
    public void simpleUpdate(float tpf) {
        double cap = (2 * FastMath.DEG_TO_RAD);
        x += (new Random().nextInt(3) - 1) * tpf / 100;
        y += (new Random().nextInt(3) - 1) * tpf / 100;
        z += (new Random().nextInt(3) - 1) * tpf / 100;
        if (headmasternode.getLocalRotation().getX() > cap || headmasternode.getLocalRotation().getX() < -cap) {
            x *= -1;
        }
        if (headmasternode.getLocalRotation().getY() > cap || headmasternode.getLocalRotation().getY() < -cap) {
            y *= -1;
        }
        if (headmasternode.getLocalRotation().getZ() > cap || headmasternode.getLocalRotation().getZ() < -cap) {
            z *= -1;
        }

        headmasternode.rotate(x, y, z);
    }
    private ActionListener actionListener = new ActionListener() {
        public void onAction(String name, boolean keyPressed, float tpf) {
            if (name.equals("Pause") && !keyPressed) {
                isRunning = !isRunning;
            }
        }
    };
    private AnalogListener analogListener = new AnalogListener() {
        public void onAnalog(String name, float value, float tpf) {
            if (isRunning) {
                if (name.equals("Right")) {
                    Vector3f v = headmasternode.getLocalTranslation();
                    headmasternode.setLocalTranslation(v.x - value * speed * 4, v.y, v.z);
                }
                if (name.equals("Left")) {
                    Vector3f v = headmasternode.getLocalTranslation();
                    headmasternode.setLocalTranslation(v.x + value * speed * 4, v.y, v.z);
                }
                if (name.equals("Forward")) {
                    Vector3f v = headmasternode.getLocalTranslation();
                    headmasternode.setLocalTranslation(v.x, v.y, v.z + value * speed * 4);
                }
                if (name.equals("Backward")) {
                    Vector3f v = headmasternode.getLocalTranslation();
                    headmasternode.setLocalTranslation(v.x, v.y, v.z - value * speed * 4);
                }
            } else {
                System.out.println("Press P to unpause.");
            }
        }
    };
}
