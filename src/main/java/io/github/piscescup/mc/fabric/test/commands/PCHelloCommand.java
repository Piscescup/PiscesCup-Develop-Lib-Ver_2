package io.github.piscescup.mc.fabric.test.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.piscescup.mc.fabric.command.annotation.CommandExecutable;
import io.github.piscescup.mc.fabric.command.annotation.RootCommand;
import io.github.piscescup.mc.fabric.command.annotation.argument.Argument;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
@RootCommand("pc-hello")
public class PCHelloCommand {

    @CommandExecutable
    public int hello(
        CommandContext<ServerCommandSource> ctx,
        @Argument(name = "targets", type = EntityArgumentType.class)
        EntityArgumentType targets
    ) {
        ctx.getSource().sendFeedback(
            () -> Text.literal("Hello, " + EntityArgumentType.players()),
            false
        );
        return Command.SINGLE_SUCCESS;
    }
}
