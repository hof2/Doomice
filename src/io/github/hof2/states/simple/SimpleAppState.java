package io.github.hof2.states.simple;

import io.github.hof2.enums.Mappings;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;

/**
 * A simple implementation of {@link AbstractAppState} which assumes a
 * {@link SimpleApplication} is used.
 */
public abstract class SimpleAppState extends AbstractAppState implements ActionListener, AnalogListener {

    /**
     * The {@link SimpleApplication}.
     */
    public SimpleApplication app;
    /**
     * The corresponding {@link AppStateManager}
     */
    public AppStateManager stateManager;
    /**
     * An instance of the {@link SimpleMappings} helper class.
     */
    public SimpleMappings mappings = SimpleMappings.getInstance();

    /**
     * Casts the {@link Application} to a {@link SimpleApplication} if possible
     * and writes it to the {@code app} variable. Also initializes the 
     * {@code stateManager} variable
     *
     * @param stateManager The state manager
     * @param app The application
     */
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        if (app instanceof SimpleApplication) {
            this.app = (SimpleApplication) app;
        }
        this.stateManager = stateManager;
    }

    /**
     * Converts the {@code name} parameter to a {@link Mapping} and calls
     * {@code onMappingAction}.
     *
     * @param name The name of the mapping that was invoked.
     * @param isPressed True if the action is "pressed", false otherwise.
     * @param tpf The time per frame value.
     */
    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        onMappingAction(Mappings.valueOf(name), isPressed, tpf);
    }

    /**
     * Can be overridden in subclasses to handle user input.
     *
     * @param mapping The {@link Mapping} that was invoked.
     * @param isPressed True if the action is "pressed", false otherwise.
     * @param tpf The time per frame value.
     */
    public void onMappingAction(Mappings mapping, boolean isPressed, float tpf) {
    }

    /**
     * Converts the {@code name} parameter to a {@link Mapping} and calls
     * {@code onMappingAnalog}.
     *
     * @param name The name of the mapping that was invoked.
     * @param isPressed True if the action is "pressed", false otherwise.
     * @param tpf The time per frame value.
     */
    @Override
    public void onAnalog(String name, float value, float tpf) {
        onMappingAnalog(Mappings.valueOf(name), value, tpf);
    }

    /**
     * Can be overridden in subclasses to handle user input.
     *
     * @param mapping The {@link Mapping} that was invoked.
     * @param isPressed True if the action is "pressed", false otherwise.
     * @param tpf The time per frame value.
     */
    public void onMappingAnalog(Mappings mapping, float value, float tpf) {
    }
}
