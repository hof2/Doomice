package io.github.hof2.enums;

import com.jme3.terrain.geomipmap.TerrainQuad;
import io.github.hof2.states.SchoolAppState;

/**
 * @author Matthias Hofreiter
 * @since May 13, 2015
 * Defines all {@link Materials} for {@link SimpleMaterials}.
 */
public enum Materials {
    /**
     * The material of the default {@link TerrainQuad}.
     */
    Floor,
    /**
     * The material the player node consist of.
     */
    Player,
    /**
     * The material of the {@link SchoolAppState school} 
     */
    School
}
