package io.github.hof2.collection;

import java.util.ArrayList;

public class PlayerCollection {

    private ArrayList<Player> players = new ArrayList<>();

    private PlayerCollection() {
    }

    /**
     * Gets a {@link PlayerCollection} object.
     *
     * @return an instance of {@link PlayerCollection}.
     */
    public static PlayerCollection getInstance() {
        return PlayerCollectionHolder.INSTANCE;
    }

    private static class PlayerCollectionHolder {

        private static final PlayerCollection INSTANCE = new PlayerCollection();
    }

    /**
     * Adds the {@link Player} to {@link ArrayList players}.
     *
     * @param player The {@link Player} to be added.
     */
    public void addPlayer(Player player) {
        if (!players.contains(player)) {
            players.add(player);
            onChange(player);
        }
    }
    
    /**
     * Removes the {@link Player} from {@link ArrayList players}.
     *
     * @param player The {@link Player} to be removed.
     */
    public void removePlayer(Player player) {
        if (players.contains(player)) {
            players.remove(player);
            onRemove(player);
        }
    }

    /**
     * Tells the collection that the given {@link Player} has been updated.
     *
     * @param player The player object that has been updated.
     */
    public void updatePlayer(Player player) {
        onChange(player);
    }

    /**
     *
     * Not implemented yet. Called when a {@link Player} is added or changes in
     * any way. Sends the new information to other clients.
     *
     * @param player The {@link Player} which is added or changed.
     */
    private void onChange(Player player) {
    }
    
    /**
     *
     * Not implemented yet. Called when a {@link Player} is removed. Sends this new information to other clients.
     *
     * @param player The {@link Player} which is removed.
     */
    private void onRemove(Player player) {
    }
}
