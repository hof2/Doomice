package io.github.hof2.states.simple;

import io.github.hof2.enums.Gui;
import de.lessvoid.nifty.Nifty;
import java.util.HashMap;

/**
 * A collection for storing and retrieving paths to {@link Gui Guis}. This class
 * is only used for {@link Nifty NiftyGui}.
 */
public class SimpleGui {

    private static HashMap<Gui, String> map = new HashMap<>();

    /**
     * Returns the {@link String path} to which the specified key is mapped, or
     * {@code null} if this map contains no {@link String path} for the key.
     *
     * @param key the key whose associated {@link String path} is to be returned
     * @return the {@link String path} to which the specified key is mapped, or
     * null if this map contains no mapping for the key
     */
    public static String getGuiPath(Gui key) {
        return map.get(key);
    }

    /**
     * Associates the specified {@link String path} with the specified key in
     * this map. If the map previously contained a {@link String path} for the
     * key, the old value is replaced.
     *
     * @param key key with which the specified {@link String path} is to be
     * associated
     * @param value {@link String path} to be associated with the specified key
     */
    public static void putGuiPath(Gui key, String value) {
        map.put(key, value);
    }
}
