
package io.github.hof2.states;

import com.jme3.app.Application;
import io.github.hof2.states.simple.SimpleAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import io.github.hof2.enums.Materials;
import io.github.hof2.states.simple.SimpleMaterials;

/**
 *
 */
public class SchoolAppState extends SimpleAppState{
    
    private Node school;
    private Vector3f translation;
    private RigidBodyControl control = new RigidBodyControl(0);
    
    /**
     * @param translation the Translation in which the {@code school} should be displayed
     */
    public SchoolAppState(Vector3f translation) {
        this.translation = translation;
    }
    
    /**
     * Initializes the {@code School}s Spatial. sets {@code translation} as set in Constructor
     * @param stateManager
     * @param app 
     */
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        school=(Node) app.getAssetManager().loadModel("Scenes/School/Cube.001.mesh.j3o");
         
//        Spatial s = app.getAssetManager().loadModel("Models/school/school.j3o");
//        System.out.println(s.toString());
//        school.attachChild(s);
        school.setLocalTranslation(translation);
        school.setMaterial(SimpleMaterials.getMaterial(Materials.School));
        school.addControl(control);
        stateManager.getState(BulletAppState.class).getPhysicsSpace().add(control);
        this.app.getRootNode().attachChild(school);
    }

    @Override
    public void update(float tpf) {
    }
    
    /**
     * Detaches the {@code school} again.
     */
    @Override
    public void cleanup() {
        super.cleanup();
        app.getRootNode().detachChild(school);
        school.removeControl(control);
    }

}
