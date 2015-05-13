package io.github.hof2.states.simple;

import io.github.hof2.enums.Mappings;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.InputListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.Trigger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * A helper class which effectively replaces Strings with
 * {@link Mapping Mapppings} for {@link InputManager} mappings.
 *
 * @author christoph
 */
public class SimpleMappings {

    private HashMap<Mappings, ArrayList<KeyTrigger>> map = new HashMap<>();

    private SimpleMappings() {
    }

    /**
     * Gets a {@link SimpleMappings} object.
     *
     * @return an instance of {@link SimpleMappings}.
     */
    public static SimpleMappings getInstance() {
        return SimpleMappingsHolder.INSTANCE;
    }

    private static class SimpleMappingsHolder {

        private static final SimpleMappings INSTANCE = new SimpleMappings();
    }

    /**
     * Adds a {@link Mapping} with one or more {@link KeyTrigger KeyTriggers} to
     * a given {@link InputManager} and {@link InputListener}.
     *
     * @param listener The {@link InputListener}.
     * @param input The {@link InputManager}.
     * @param mapping The {@link Mapping} to be added.
     * @param triggers Any amount of {@link KeyTrigger KeyTriggers}.
     * @throws Exception is thrown when mapping or trigger is already in use.
     */
    public void addMapping(InputListener listener, InputManager input, Mappings mapping, KeyTrigger... triggers) throws Exception {
        for (KeyTrigger oldKeyTrigger : getAllTriggers()) {
            for (KeyTrigger newKeyTrigger : triggers) {
                if (oldKeyTrigger.equals(newKeyTrigger)) {
                    throw new Exception("trigger already used");
                }
            }
        }
        if (!map.containsKey(mapping)) {
            input.addMapping(mapping.toString(), triggers);
            input.addListener(listener, mapping.toString());
            map.put(mapping, new ArrayList<>(Arrays.asList(triggers)));
        } else {
            throw new Exception("mapping already used");
        }
    }

    /**
     * Adds a {@link Mapping} with one or more {@link KeyInput KeyCodes} to a
     * given {@link InputManager} and {@link InputListener}.
     *
     * @param listener The {@link InputListener}.
     * @param input The {@link InputManager}.
     * @param mapping The {@link Mapping} to be added.
     * @param triggers Any amount of {@link KeyInput KeyCodes}.
     * @throws Exception is thrown when mapping or trigger is already in use.
     */
    public void addMapping(InputListener listener, InputManager input, Mappings mapping, int... keys) throws Exception {
        ArrayList<KeyTrigger> triggers = new ArrayList<>();
        for (int keyCode : keys) {
            triggers.add(new KeyTrigger(keyCode));
        }
        for (KeyTrigger keyTrigger : getAllTriggers()) {
            for (int key : keys) {
                if (key == keyTrigger.getKeyCode()) {
                    throw new Exception("trigger already used");
                }
            }
        }
        if (!map.containsKey(mapping)) {
            Trigger[] arrTriggers = new Trigger[triggers.size()];
            input.addMapping(mapping.toString(), triggers.toArray(arrTriggers));
            input.addListener(listener, mapping.toString());
            map.put(mapping, new ArrayList<>(triggers));
        } else {
            throw new Exception("mapping already used");
        }
    }

    /**
     * Removes the given {@link Mapping Mappings} and their
     * {@link KeyTrigger KeyTriggers} from the given {@link InputManager} and
     * {@link InputListener}.
     *
     * @param listener The {@link InputListener}.
     * @param input The {@link InputManager}.
     * @param mapping The {@link Mapping} to be removed.
     */
    public void removeMappings(InputListener listener, InputManager input, Mappings... mappings) {
        for (Mappings mapping : mappings) {
            input.deleteMapping(mapping.toString());
            map.remove(mapping);
        }
        input.removeListener(listener);
    }

    /**
     * Used to get all {@link KeyTrigger KeyTriggers} that are currently in use.
     * @return All {@link KeyTrigger KeyTriggers} currently mapped.
     */
    private ArrayList<KeyTrigger> getAllTriggers() {
        ArrayList<KeyTrigger> triggers = new ArrayList<>();
        for (ArrayList<KeyTrigger> list : map.values()) {
            for (KeyTrigger keyTrigger : list) {
                triggers.add(keyTrigger);
            }
        }
        return triggers;
    }
}
