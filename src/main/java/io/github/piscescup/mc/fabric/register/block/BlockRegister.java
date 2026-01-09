package io.github.piscescup.mc.fabric.register.block;

import io.github.piscescup.mc.fabric.register.Register;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.Objects;
import java.util.function.Function;

/**
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
        super(id);

    }

    public static BlockPreRegistrable createFor(@NotNull Identifier id) {
        Objects.requireNonNull(id);
        return new BlockRegister(id);
    }

    public static BlockPreRegistrable createFor(@NotNull String namespace, @NotNull String path) {
        Objects.requireNonNull(namespace);
        Objects.requireNonNull(path);
        return new BlockRegister(Identifier.of(namespace, path));
    }

    @Override
    public BlockPreRegistrable settings(AbstractBlock.@NonNull Settings settings) {
        Objects.requireNonNull(settings);
        this.settings = settings;
        return this;
    }

    @Override
    public BlockPreRegistrable factory(@NonNull Function<AbstractBlock.Settings, Block> factory) {
        Objects.requireNonNull(factory);
        this.factory = factory;
        return this;
    }

    @Override
    public BlockPostRegistrable register() {
        this.registryKey = RegistryKey.of(
            RegistryKeys.BLOCK,
            this.id
        );

        Block block = this.factory.apply(
            this.settings.registryKey(this.registryKey)
        );

        this.thing = Registry.register(
            Registries.BLOCK,
            this.id,
            block
        );

        return this;
    }

}
