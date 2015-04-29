/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.hof2.states;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetKey;
import com.jme3.bullet.BulletAppState;
import com.jme3.material.Material;
import com.jme3.scene.Node;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.heightmap.HillHeightMap;
import io.github.hof2.Main;
import io.github.hof2.materials.MaterialManager;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Matthias
 */
public class TerrainAppState extends AbstractAppState {

    private static final int PATCH_SIZE = 65;
    private static final int TOTAL_SIZE = 1025;
    private AppStateManager stateManager;
    private BulletAppState physics;
    private Node rootNode;
    private TerrainQuad terrain;
    private Material material;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);

        this.stateManager = stateManager;
        this.physics = this.stateManager.getState(BulletAppState.class);
        this.rootNode = ((SimpleApplication) app).getRootNode();
        try {
            this.terrain = new TerrainQuad("Floor", PATCH_SIZE, TOTAL_SIZE, createHeightMap());
            
            setMaterial(MaterialManager.getMaterial("floor"));
            terrain.setLocalTranslation(0, -512, 0);
            rootNode.attachChild(terrain);
        } catch (Exception ex) {
            System.out.println("TerrainAppState : HeightMap creation failed");
            ex.printStackTrace();
        }
    }

    @Override
    public void update(float tpf) {
    }

    @Override
    public void cleanup() {
        super.cleanup();
        rootNode.detachChild(terrain);
    }

    private float[] createHeightMap() throws Exception {
        HillHeightMap map = new HillHeightMap(TOTAL_SIZE, 512, 50, 120);
        return map.getHeightMap();
    }

    public void setMaterial(Material material) {
        this.material = material;
        terrain.setMaterial(material);
    }
    
    
}
