package io.github.minixc;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.ChaseCamera;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.heightmap.HillHeightMap;
import io.github.minixc.controls.HeadmasterControl;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends SimpleApplication implements ActionListener {

    private BulletAppState bulletAppState;
    private HeadmasterControl headmaster;
    private static final int TERRAIN_SIZE = 1025;
    private static final int TERRAIN_PATCH_SIZE = 65;

    @Override
    public void simpleInitApp() {
        //set up physics
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);

        //set up keys
        setUpKeys();

        //set up light
        setUpLight();

        //create default material
        Material defaultMaterial = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");

        //create terrain with random height map
        try {
            setUpTerrain(defaultMaterial);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        //create headmaster
        setUpHeadmaster(defaultMaterial);
    }

    private float[] getRandomHeightMap() throws Exception {
        HillHeightMap heightmap;
        HillHeightMap.NORMALIZE_RANGE = 100; // optional
        heightmap = new HillHeightMap(TERRAIN_SIZE, 1000, 50, 120, (byte) 3); // byte 3 is a random seed
        return heightmap.getHeightMap();
    }

    @Override
    public void simpleUpdate(float tpf) {
        headmaster.updateDirection(cam);
    }

    //https://github.com/MiniXC/Doomice/issues/4 - Make FollowCamera class?
    private void setUpChaseCam(Node target) {
        //disable the default cam
        flyCam.setEnabled(false);
        //create the chaseCam
        ChaseCamera chaseCam = new ChaseCamera(cam, target, inputManager);
        chaseCam.setDragToRotate(false);
        chaseCam.setRotationSpeed(3);
        //Attach the chaseCam to the target
        target.addControl(chaseCam);
    }

    private void setUpLight() {
        //init lights
        PointLight sun = new PointLight();
        sun.setPosition(new Vector3f(0, 1, 0));
        AmbientLight ambient = new AmbientLight();
        rootNode.addLight(sun);
        rootNode.addLight(ambient);
    }

    private void setUpKeys() {
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addListener(this, "Left");
        inputManager.addListener(this, "Right");
        inputManager.addListener(this, "Up");
        inputManager.addListener(this, "Down");
        inputManager.addListener(this, "Jump");
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        switch (name) {
            case "Left":
                headmaster.setLeft(isPressed);
                break;
            case "Right":
                headmaster.setRight(isPressed);
                break;
            case "Up":
                headmaster.setUp(isPressed);
                break;
            case "Down":
                headmaster.setDown(isPressed);
                break;
            case "Jump":
                headmaster.jump();
                break;
        }
    }

    private void setUpTerrain(Material material) throws Exception {
        TerrainQuad terrain;
        terrain = new TerrainQuad("Floor", TERRAIN_PATCH_SIZE, TERRAIN_SIZE, getRandomHeightMap());
        //give the terrain its material, position & scale it, and attach it
        terrain.setMaterial(material);
        terrain.setLocalTranslation(new Vector3f(0, -100, 0));
        terrain.setLocalScale(2f, 1f, 2f);
        //render terrain far away with less detail
        TerrainLodControl control = new TerrainLodControl(terrain, getCamera());
        terrain.addControl(control);
        //add physics
        terrain.addControl(new RigidBodyControl(0));
        //add to scene
        rootNode.attachChild(terrain);
        bulletAppState.getPhysicsSpace().add(terrain);
    }

    private void setUpHeadmaster(Material material) {
        Node headmasterNode = new Node("headmaster");
        Spatial headmasterModel = assetManager.loadModel("Models/Headmaster/Headmaster.j3o");
        //give the headmaster its material
        headmasterModel.setMaterial(material);
        //init headmaster
        headmaster = new HeadmasterControl(new CapsuleCollisionShape(0.1f, 0.1f, 1), 0.5f);
        headmaster.setJumpSpeed(20);
        headmaster.setFallSpeed(30);
        headmaster.setGravity(30);
        //set the controlled spatial
        headmasterNode.attachChild(headmasterModel);
        headmaster.setSpatial(headmasterModel);
        headmasterNode.addControl(headmaster);
        //let the cam follow
        setUpChaseCam(headmasterNode);
        //add to scene
        rootNode.attachChild(headmasterNode);
        bulletAppState.getPhysicsSpace().add(headmaster);
    }

    public static void main(String[] args) {
        new Main().start();
    }
}
