package io.github.hof2.states.simple;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

/**
 * @author Christoph Minixhofer
 * @since May 10, 2015
 * Provides some static variables, each of them a {@link Quaternion}, which help altering vectors.
 */
public class SimpleQuaternions {
    /**
     * Used to yaw a spatial by 90 degrees.
     */
    public static final Quaternion YAW090 = new Quaternion().fromAngleAxis(FastMath.PI / 2, new Vector3f(0, 1, 0));
}
