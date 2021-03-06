package io.github.hof2.collection;

import io.github.hof2.controls.PlayerControl;
import java.util.HashMap;

/**
 * @author Christoph Minixhofer
 * @since May 22, 2015
 * Contains all other players, and is used in {@link MultiplayerAppState}.
 */
public class PlayerCollection {

    /*
     * Contains the {@link PlayerControl PlayerControls} mapped to their unique ids.
     */
    public static HashMap<String, PlayerControl> players = new HashMap<>();
}
