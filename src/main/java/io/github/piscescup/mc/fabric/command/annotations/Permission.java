package io.github.piscescup.mc.fabric.command.annotations;

import io.github.piscescup.mc.fabric.command.permission.PermissionLevel;

/**
 * @author REN YuanTong
 * @since 1.0.0
 */
public @interface Permission {
    PermissionLevel value();
}
