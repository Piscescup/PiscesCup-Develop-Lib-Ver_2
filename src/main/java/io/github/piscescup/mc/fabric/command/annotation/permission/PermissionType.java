package io.github.piscescup.mc.fabric.command.annotation.permission;

import java.util.Arrays;

/**
 *
 * @author REN YuanTong
 * @since
 */
public enum PermissionType {
    ALL(0),
    MODERATOR(1),
    GAMEMASTER(2),
    ADMIN(3),
    OWNER(4)
    ;
    public static PermissionType[] VALUES = values();

    private final int level;
    PermissionType(int level) {
        this.level = level;
    }
    public int level() {
        return level;
    }

    public static PermissionType fromLevel(int level) {
        return Arrays.stream(VALUES)
            .filter(e -> e.level() == level)
            .findFirst()
            .orElse(ALL);
    }

    public static boolean isValidPermissionLevel(int level) {
        return ALL.level() <= level && level <= OWNER.level();
    }

}
