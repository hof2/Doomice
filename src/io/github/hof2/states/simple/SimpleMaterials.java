package io.github.hof2.states.simple;

import io.github.hof2.enums.Materials;
import com.jme3.material.Material;
import java.util.HashMap;

/**
 * @author Christoph Minixhofer
 * @since May 10, 2015
 * A collection for storing and retrieving {@link Material Materials}.
 */
public class SimpleMaterials {

    private static HashMap<Materials, Material> map = new HashMap<>();

    /**
     * Returns the {@link Material} to which the specified key is mapped, or {@code null} if this map contains no {@link Material} for the key.
     * @param key the key whose associated {@link Material} is to be returned
     * @return the {@link Material} to which the specified key is mapped, or null if this map contains no mapping for the key
     */
    public static Material getMaterial(Materials key) {
        return map.get(key);
    }

    /**
     * Associates the specified {@link Material} with the specified key in this map. If the map previously contained a {@link Material} for the key, the old value is replaced.
     * @param key key with which the specified {@link Material} is to be associated
     * @param value {@link Material} to be associated with the specified key
     */
    public static void putMaterial(Materials key, Material value) {
        map.put(key, value);
    }
}
