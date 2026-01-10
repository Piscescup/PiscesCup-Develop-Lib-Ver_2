package io.github.piscescup.mc.fabric.register.block;

import io.github.piscescup.mc.fabric.register.Register;
import io.github.piscescup.mc.fabric.utils.CheckUtils;
import io.github.piscescup.mc.fabric.utils.CheckUtils.NullCheck;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * <h2>Description</h2>
 *
 * <h2>Usages</h2>
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public class BlockRegister
    extends Register<Block, BlockPostRegistrable, BlockRegister>
    implements BlockPreRegistrable, BlockPostRegistrable
{
    private AbstractBlock.Settings settings = AbstractBlock.Settings.create();
    private Function<AbstractBlock.Settings, Block> factory = Block::new;

    private BlockRegister(@NotNull Identifier id) {
        NullCheck.requireNonNull(id);
        super(RegistryKeys.BLOCK, id);
    }

    @Contract("_, _ -> new")
    public static @NotNull BlockPreRegistrable createFor(
        @NotNull String namespace,
        @NotNull String path
    ) {
        NullCheck.requireNonNull(namespace);
        NullCheck.requireNonNull(path);
        return new BlockRegister(Identifier.of(namespace, path));
    }

    @Contract("_ -> new")
    public static @NotNull BlockPreRegistrable createFor(
        @NotNull Identifier id
    ) {
        NullCheck.requireNonNull(id);
        return new BlockRegister(id);
    }

    @Override
    public BlockPostRegistrable register() {
        Block block = this.factory.apply(this.settings.registryKey(this.registryKey));

        this.thing = Registry.register(
            Registries.BLOCK,
            this.id,
            block
        );

        return this;
    }

    @Override
    public BlockPreRegistrable settings(@NotNull AbstractBlock.Settings settings) {
        NullCheck.requireNonNull(settings);
        this.settings = settings;
        return this;
    }

    @Override
    public BlockPreRegistrable factory(@NotNull Function<AbstractBlock.Settings, net.minecraft.block.Block> factory) {
        NullCheck.requireNonNull(factory);
        this.factory = factory;
        return this;
    }
}
