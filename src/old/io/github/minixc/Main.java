package old.io.github.minixc;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.ChaseCamera;
import com.jme3.light.AmbientLight;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.heightmap.HillHeightMap;
import old.io.github.minixc.controls.EntityControl;
import old.io.github.minixc.controls.HeadmasterControl;
import old.io.github.minixc.listeners.MoveListener;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends SimpleApplication {

    private BulletAppState bulletAppState;
    private EntityControl player;
    private MoveListener listener;
    private static final int TERRAIN_SIZE = 1025;
    private static final int TERRAIN_PATCH_SIZE = 65;

    @Override
    public void simpleInitApp() {
        //set up physics
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);     
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
        //add listener
        listener = new MoveListener(player, inputManager);
    }

    private float[] getRandomHeightMap() throws Exception {
        HillHeightMap heightmap;
        HillHeightMap.NORMALIZE_RANGE = 100; // optional
        heightmap = new HillHeightMap(TERRAIN_SIZE, 1000, 50, 120, (byte) 3); // byte 3 is a random seed
        return heightmap.getHeightMap();
    }

    @Override
    public void simpleUpdate(float tpf) {
        player.updateDirection(cam);
    }

    private void setUpChaseCam(Node target) {
        ChaseCamera chaseCam = new ChaseCamera(cam, target, inputManager);
        chaseCam.setDragToRotate(false);
        chaseCam.setRotationSpeed(1.5f);
        chaseCam.setSmoothMotion(true);
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
        player = new HeadmasterControl(cam);
        //set the controlled spatial
        headmasterNode.attachChild(headmasterModel);
        player.setSpatial(headmasterModel);
        headmasterNode.addControl(player);
        //let the cam follow
        setUpChaseCam(headmasterNode);
        //add to scene
        rootNode.attachChild(headmasterNode);
        bulletAppState.getPhysicsSpace().add(player);
    }

    public static void main(String[] args) {
        new Main().start();
    }
}
