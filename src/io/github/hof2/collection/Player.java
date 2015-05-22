package io.github.hof2.collection;

import com.jme3.math.Vector3f;
import io.github.hof2.enums.PlayerTypes;
import java.io.Serializable;

/**
 * This beans class is used to transfer player information over the network for
 * multiplayer.
 */
public class Player implements Serializable {

    private PlayerTypes type;
    private Vector3f position;

    /**
     * Creates a new object with the {@link PlayerTypes PlayerType} and
     * {@link Vector3f position} of the player.
     */
    public Player(PlayerTypes type, Vector3f position) {
        this.type = type;
        this.position = position;
    }

    /**
     * Creates a new {@link Player} object.
     */
    public Player() {
    }

    /**
     * Sets the {@link PlayerTypes PlayerType}.
     * @param type The {@link PlayerTypes PlayerType}. 
     */
    public void setType(PlayerTypes type) {
        this.type = type;
    }
    
    /**
     * Sets the {@link Vector3f position}.
     * @param type The {@link Vector3f position}. 
     */
    public void setPosition(Vector3f position) {
        this.position = position;
    }
}
