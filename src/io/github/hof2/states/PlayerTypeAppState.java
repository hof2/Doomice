/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.hof2.states;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.niftygui.NiftyJmeDisplay;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import io.github.hof2.enums.Gui;
import io.github.hof2.enums.PlayerTypes;
import io.github.hof2.states.simple.SimpleAppState;
import io.github.hof2.states.simple.SimpleGui;

/**
 *
 * @author Matthias
 */
public class PlayerTypeAppState extends SimpleAppState implements ScreenController{
    private PlayerAppState player;
    private NiftyJmeDisplay display;
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        display = new NiftyJmeDisplay(app.getAssetManager(), app.getInputManager(), app.getAudioRenderer(), app.getGuiViewPort());
        Nifty nifty = display.getNifty();
        nifty.fromXml(SimpleGui.getGuiPath(Gui.ChoosePlayer), "Start",this);
        app.getGuiViewPort().addProcessor(display);
        this.app.getFlyByCamera().setDragToRotate(true);
        
    
    }
    
    @Override
    public void update(float tpf) {
        //TODO: implement behavior during runtime
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
        app.getGuiViewPort().removeProcessor(display);
    }

    @Override
    public void bind(Nifty nifty, Screen screen) {
    }

    @Override
    public void onStartScreen() {
    }

    @Override
    public void onEndScreen() {
    }
    public void startGame(String type){
        System.out.println("Hallo");
        player = new PlayerAppState(type.equals("Student")?PlayerTypes.Student:PlayerTypes.Headmaster);
        stateManager.attach(player);
    }
    public String getStudent(){
        return "Student"; 
   }
    public String getHeadmaster(){
        return "Headmaster"; 
   }
}
