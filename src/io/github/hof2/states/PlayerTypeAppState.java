/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.hof2.states;

import com.jme3.app.Application;
import com.jme3.app.state.AppState;
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
 * This {@link AppState} displays a {@link NiftyJmeDisplay} with a selection to
 * choose the Playertype (as seen in {@link PlayerTypes}. When Selected it
 * delegates to the {@link PlayerAppState} The corresponding NiftyGui XML-File
 * is {@code "Interface/NiftyGUI/ChoosePlayerType.xml}
 *
 * Also Handles the Buttons Actions over {@code startGame(String type)}
 *
 * @author Matthias
 */
public class PlayerTypeAppState extends SimpleAppState implements ScreenController {

    private PlayerAppState player;
    private MultiplayerAppState multi;
    private NiftyJmeDisplay display;

    /**
     * Initializes the display. Connects {@link NiftyJmeDisplay} to the
     * {@code app.guiViewPort} Gets the layouts xml from {@link SimpleGui} over
     * the {@link Gui}-Enum Displays the Menu Sets the Camera to fixed (drag to
     * rotate)
     *
     * @see SimpleAppState
     * @see AbstractAppState
     * @param stateManager The state manager
     * @param app The application
     */
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        display = new NiftyJmeDisplay(app.getAssetManager(), app.getInputManager(), app.getAudioRenderer(), app.getGuiViewPort());
        Nifty nifty = display.getNifty();
        nifty.fromXml(SimpleGui.getGuiPath(Gui.ChoosePlayer), "Start", this);
        app.getGuiViewPort().addProcessor(display);
        this.app.getFlyByCamera().setDragToRotate(true);
    }

    /**
     * the {@code display} needs to be removed from the screen when this
     * {@link AppState} is detached. This
     *
     */
    @Override
    public void cleanup() {
        super.cleanup();
    }

    /**
     * Method called by the buttons with id {@code Student} and
     * {@code Headmaster} It attaches the {@link PlayerAppState} to the
     * {@code app}
     *
     * @param type
     */
    public void startGame(String type) {
        player = new PlayerAppState(type.equals("Student") ? PlayerTypes.Student : PlayerTypes.Headmaster);
        multi = new MultiplayerAppState();
        stateManager.attach(multi);
        app.getGuiViewPort().removeProcessor(display);
    }

    /**
     * Getter Methods for {@link String Student} called by the Student Button
     *
     * @return The String´
     */
    public String getStudent() {
        return "Student";
    }

    /**
     * Getter Methods for {@link String Headmaster} called by the Headmaster
     * Button
     *
     * @return The String
     */
    public String getHeadmaster() {
        return "Headmaster";
    }

    /**
     * Gets the generated {@link PlayerAppState}.
     *
     * @return the {@link PlayerAppState}.
     */
    public PlayerAppState getPlayerAppState() {
        return player;
    }

    /**
     * Gets the global {@link NiftyJmeDisplay} instance.
     *
     * @return the {@link NiftyJmeDisplay}.
     */
    public NiftyJmeDisplay getDisplay() {
        return display;
    }

    /**
     * nothing happens here
     */
    @Override
    public void bind(Nifty nifty, Screen screen) {
    }

    /**
     * nothing happens here
     */
    @Override
    public void onStartScreen() {
    }

    /**
     * nothing happens here
     */
    @Override
    public void onEndScreen() {
    }
}
