package io.github.hof2.collection;

import com.jme3.math.Vector3f;
import io.github.hof2.enums.PlayerTypes;
import io.github.hof2.states.MultiplayerAppState;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author Christoph Minixhofer
 * @since June 21,2015
 * Data class which is used for sending {@link MultiplayerAppState Multiplayer}
 * information.
 */
public class Player implements Serializable {

    private Vector3f position;
    private Vector3f viewDirection;
    private Vector3f walkDirection;
    private PlayerTypes type;
    private String id;

    /**
     * Creates a new {@link Player} object.
     *
     * @param position the {@link Vector3f Position}.
     * @param type the {@link PlayerTypes PlayerType}.
     * @param viewDirection the {@link Vector3f ViewDirection}.
     * @param walkDirection the {@link Vector3f WalkDirection}.
     * @param id the unique player id.
     */
    public Player(Vector3f position, Vector3f viewDirection, Vector3f walkDirection, PlayerTypes type, String id) {
        this.position = position;
        this.viewDirection = viewDirection;
        this.walkDirection = walkDirection;
        this.type = type;
        this.id = id;
    }

    /**
     * Gets the current {@link Vector3f Position}.
     *
     * @return the {@link Vector3f Position}.
     */
    public Vector3f getPosition() {
        return position;
    }

    /**
     * Gets the {@link PlayerTypes PlayerType}.
     *
     * @return the {@link PlayerTypes PlayerType}.
     */
    public PlayerTypes getType() {
        return type;
    }

    /**
     * Gets the current {@link Vector3f ViewDirection}.
     *
     * @return the {@link Vector3f ViewDirection}.
     */
    public Vector3f getViewDirection() {
        return viewDirection;
    }

    /**
     * Gets the current {@link Vector3f WalkDirection}.
     *
     * @return the {@link Vector3f WalkDirection}.
     */
    public Vector3f getWalkDirection() {
        return walkDirection;
    }

    /**
     * Used to check if two {@link Player Players} are equal.
     *
     * @param obj The {@link Player} to compare to.
     * @return {@code true} if both {@link Player Players} are equal,
     * {@code false} if unequal.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Player other = (Player) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    /**
     * Gets the unique {@link Player} id.
     *
     * @return the {@link Player} id.
     */
    public String getId() {
        return id;
    }
}
