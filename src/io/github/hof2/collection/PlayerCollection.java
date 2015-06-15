package io.github.hof2.collection;

import io.github.hof2.controls.PlayerControl;
import java.util.HashMap;

/**
 * <<<<<<< HEAD Stores all local {@link PlayerControl PlayerControls}. Gets
 * updated by {@link PlayerAppState} and {@link MultiplayerAppState} =======
 * Contains all other players, and is used in {@link MultiplayerAppState}.
 * >>>>>>> multiplayer
 */
public class PlayerCollection {

    /*
     * Contains the {@link PlayerControl PlayerControls} mapped to their unique ids.
     */
    public static HashMap<String, PlayerControl> players = new HashMap<>();
}
