package io.github.hof2.collection;

import io.github.hof2.controls.PlayerControl;
import io.github.hof2.states.MultiplayerAppState;
import io.github.hof2.states.PlayerAppState;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Stores all local {@link PlayerControl PlayerControls}. Gets updated by
 * {@link PlayerAppState} and {@link MultiplayerAppState}
 */
public class PlayerCollection {

    /*
     * Contains the {@link PlayerControl PlayerControls} mapped to their unique ids.
     */
    public static HashMap<String, PlayerControl> players = new HashMap<>();

}
