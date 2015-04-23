/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.hof2.states;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.BulletAppState;

/**
 *
 * @author Matthias
 */
public class GameAppState extends AbstractAppState {

    private AppStateManager stateManager;
    private BulletAppState physics = new BulletAppState();
    private TerrainAppState terrain = new TerrainAppState();

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.stateManager = stateManager;
        
        this.stateManager.attach(physics);
        this.stateManager.attach(terrain);
    }

    @Override
    public void update(float tpf) {
    }

    @Override
    public void cleanup() {
        super.cleanup();
        stateManager.detach(physics);
        stateManager.detach(terrain);

    }
}
