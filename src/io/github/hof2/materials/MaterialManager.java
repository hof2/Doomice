/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.hof2.materials;

import com.jme3.material.Material;
import java.util.HashMap;

/**
 *
 * @author Matthias
 */
public class MaterialManager {

    private static HashMap<String, Material> map = new HashMap<>();

    public static Material getMaterial(String key) {
        return map.get(key);
    }

    public static void putMaterial(String key, Material value) {
        map.put(key, value);
    }
}
