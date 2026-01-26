package io.github.piscescup.mc.fabric.command.permission;


/**
 * Defines the permission levels mapping directly to Minecraft's
 * internal Operator (OP) levels.
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public enum PermissionLevel {
    /**
     * <b>Standard player</b>.
     * <p>No special permissions.</p>
     */
    LEVEL_ALL(0),

    /**
     * <b>Moderator</b>. <p>The player can bypass spawn protection.</p>
     */
    LEVEL_MODERATOR(1),

    /**
     * <b>Game Master.</b>
     *
     * <ul>
     *     <li>More commands are available.</li>
     *     <li>The player can use command blocks.</li>
     *     <li>The player can copy the server-side NBT data of an entity or a block entity when pressing the {@code F3+I} debug hotkey, and copy the client-side NBT data when pressing {@code Shift+F3+I}.</li>
     *     <li>The player can use {@code F3+F4} (game mode switcher) and {@code F3+N} debug hotkey (toggle between Spectator and the previous game mode).</li>
     *     <li>The player can change or lock difficulty in Options screen. Note that the player in a singleplayer world or the owner of a LAN world can change or lock difficulty without a permission level of {@code LEVEL_GAMEMASTER}.</li>
     *     <li>With "Operator Items Tab" option turned on, the player can find operator items and an "Operator Utilities" tab in the creative inventory.</li>
     *     <li>Target selectors can be used in commands like /tell and raw JSON texts.</li>
     * </ul>
     *
     */
    LEVEL_GAMEMASTER(2),

    /**
     * <b>Administrator.</b>
     * <p>
     *     Commands related to multiplayer management are available.
     * </p>
     */
    LEVEL_ADMIN(3),

    /**
     * <b>Owner.</b>
     * <p>
     *     All commands are available, including commands related to server management.
     * </p>
     */
    LEVEL_OWNER(4);

    private final int level;

    PermissionLevel(int level) {
        this.level = level;
    }

    /**
     * Get the permission level.
     *
     * @return The integer value of the permission level.
     */
    public int getLevelValue() {
        return level;
    }

    /**
     * Checks if this level meets or exceeds the required level.
     * @param required The level to compare against.
     * @return {@code true} if authorized.
     */
    public boolean isAtLeast(PermissionLevel required) {
        return this.level >= required.level;
    }

    public static PermissionLevel fromLevel(int level) {
        for (PermissionLevel permission : values()) {
            if ( level == permission.level )
                return permission;
        }

        return LEVEL_ALL;
    }

    public static boolean isValidLevel(int level) {
        return level >= 0 && level < values().length;
    }

}