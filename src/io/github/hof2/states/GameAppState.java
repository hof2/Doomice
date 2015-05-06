/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.hof2.states;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.material.Material;
import io.github.hof2.materials.MaterialManager;

/**
 *
 * @author Matthias
 */
public class GameAppState extends AbstractAppState {

    private AppStateManager stateManager;
    private BulletAppState physics = new BulletAppState();
    private TerrainAppState terrain = new TerrainAppState();
    private LightAppState lighting = new LightAppState();
    private HeadmasterAppState headmaster = new HeadmasterAppState();

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.stateManager = stateManager;
        initializeMaterials(app);
        physics.setDebugEnabled(true);
        this.stateManager.attach(physics);
        this.stateManager.attach(terrain);
        this.stateManager.attach(lighting);
        this.stateManager.attach(headmaster);
    }

    @Override
    public void update(float tpf) {
    }

    @Override
    public void cleanup() {
        super.cleanup();
        stateManager.detach(terrain);
        stateManager.detach(lighting);
        stateManager.detach(headmaster);
    }

    private void initializeMaterials(Application app) {
        MaterialManager.putMaterial("floor", new Material(app.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md"));
        MaterialManager.putMaterial("player", new Material(app.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md"));
    }
}
