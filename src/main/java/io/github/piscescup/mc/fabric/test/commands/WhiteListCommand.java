package io.github.piscescup.mc.fabric.test.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import io.github.piscescup.mc.fabric.command.annotations.CommandExecutable;
import io.github.piscescup.mc.fabric.command.annotations.LiteralCommand;
import io.github.piscescup.mc.fabric.command.annotations.Permission;
import io.github.piscescup.mc.fabric.command.annotations.RootCommand;
import io.github.piscescup.mc.fabric.command.permission.PermissionLevel;
import net.minecraft.server.command.ServerCommandSource;



/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
@RootCommand("pc-whitelist")
public final class WhiteListCommand {

    @LiteralCommand("add")
    @Permission(PermissionLevel.LEVEL_ALL)
    public static
    class AddCommand {

        @CommandExecutable
        public int add(
            CommandContext<ServerCommandSource> source
        ) {

            return Command.SINGLE_SUCCESS;
        }
    }

    @LiteralCommand("delete")
    @Permission(PermissionLevel.LEVEL_ALL)
    public static
    class DeleteCommand {

        @CommandExecutable
        public int delete(
            CommandContext<ServerCommandSource> source
        ) {
            return Command.SINGLE_SUCCESS;
        }
    }
}
